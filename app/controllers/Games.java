package controllers;

import static akka.pattern.Patterns.ask;

import java.util.List;

import middlewares.InGame;
import models.Game;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.With;
import util.GameCommands;
import actors.GameStreamActor;

import commands.AbstractCommand;
import commands.CardReceiveCommand;
import commands.PlayerJoinCommand;
import commands.PlayerQuitCommand;

import exceptions.AlreadyInGameException;
import exceptions.GameAlreadyStartedException;
import exceptions.NotInAGameException;
import exceptions.TooManyPlayersException;
import forms.Gameinfo;


@Transactional
@Authenticated(Secured.class)
public class Games extends SuperController {

	@Transactional
	@Authenticated(Secured.class)
	@With(InGame.class)
	public static Result stream() {
		final User currentUser = currentUser();
		List<AbstractCommand> commands;
		try {
			commands = GameCommands.getCommands(currentUser);
		} catch (NotInAGameException e) {
			return badRequest(jsonError("not in a game"));
		}
		if (commands.size() != 0) {
			return ok(Json.toJson(commands));
		}
		return async(
			Akka.asPromise(ask(myActor, GameStreamActor.Command.subscribe, 5000))
				.map(
					new Function<Object,Result>() {
						public Result apply(Object response) {
							try {
								return ok(Json.toJson(GameCommands.getCommands(currentUser)));
							} catch (NotInAGameException e) {
								return badRequest(jsonError("not in a game"));
							}
						}
					}
				).recover(new Function<Throwable, Result>() {
					@Override
					public Result apply(Throwable arg0) throws Throwable {
						// TODO Supprimer le listener en cas de timeout ?
						return ok(jsonError("Rien de nouveau, il faut relancer une requete"));
					}
				})
		);
	}

	@Transactional
	@Authenticated(Secured.class)
	@With(InGame.class)
	public static Result send() {
		final User currentUser = currentUser();
		sendCommandToGame(currentUser.getGame(), new CardReceiveCommand("100km"));
		sendCommandToUser(currentUser.getGame(), currentUser, new PlayerJoinCommand("admin"));
		sendFinalize();
		return ok(jsonInfo("ok"));
	}
	
	public static Result create() {
		Form<Gameinfo> gameinfoForm = Form.form(Gameinfo.class).bindFromRequest();
		if (gameinfoForm.hasErrors()) {
			return badRequest(jsonError("Wrong parameters for gameinfo"));
		}
		User currentUser = currentUser();
		if (currentUser.inGame()) {
			return badRequest(jsonError("Player already in a game"));
		}
		Game game = new Game();
		game.setName(gameinfoForm.get().getName());
		try {
			game.addFirstPlayer(currentUser);
		} catch (AlreadyInGameException ex) {
			return badRequest(jsonError("Player already in a game"));
		}
		Game.save(game);
		return ok(jsonInfo("game created"));
	}
	
	public static Result join(long gameId) {
		Game game = Game.findById(gameId);
		final User currentUser = currentUser();
		try {
			game.addPlayer(currentUser);
		} catch (AlreadyInGameException ex) {
			return badRequest(jsonError("Player already in a game"));
		} catch (TooManyPlayersException e) {
			return badRequest(jsonError("This game is full"));
		} catch (GameAlreadyStartedException e) {
			return badRequest(jsonError("The game has already started"));
		}
		sendOneCommandToGame(currentUser.getGame(), new PlayerJoinCommand(currentUser.getUsername()));
		return ok(jsonInfo("joined game"));
	}
	
	public static Result list() {
		return ok(Json.toJson(Game.findAll()));
	}

	@Transactional
	@Authenticated(Secured.class)
	@With(InGame.class)
	public static Result quit() {
		final User currentUser = currentUser();
		Game game = currentUser.getGame();
		game.removePlayer(currentUser);
		sendOneCommandToGame(game, new PlayerQuitCommand(currentUser.getUsername()));
		return ok(jsonInfo("quit game"));
	}
	
	@Transactional
	@Authenticated(Secured.class)
	@With(InGame.class)
	public static Result info() {
		return ok(Json.toJson(currentUser().getGame()));
	}
	
}
