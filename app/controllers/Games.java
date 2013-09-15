package controllers;

import static akka.pattern.Patterns.ask;

import java.util.List;

import models.Game;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import util.Command;
import util.GameCommands;
import actors.GameStreamActor;
import exceptions.AlreadyInGameException;
import exceptions.NotInAGameException;
import exceptions.TooManyPlayersException;
import forms.Gameinfo;


@Transactional
@Authenticated(Secured.class)
public class Games extends SuperController {
	
	public static Result stream() {
		final User currentUser = currentUser();
		List<Command> commands;
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
	
	public static Result send() {
		final User currentUser = currentUser();
		sendCommandToGame(currentUser.getGame(), new Command(){{
			type = "card.receive";
			args = "100km";
		}});
		sendCommandToUser(currentUser.getGame(), currentUser, new Command(){{
			type = "player.new";
			args = new Object() {
				@SuppressWarnings("unused") public String name = "toto";
			};
		}});
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
		}
		sendOneCommandToGame(currentUser.getGame(), new Command("player.join"){{
			args = new Object(){
				@SuppressWarnings("unused")
				public String name = currentUser.getUsername();
			};
		}});
		return ok(jsonInfo("joined game"));
	}
	
	public static Result list() {
		return ok(Json.toJson(Game.findAll()));
	}
	
	public static Result quit() {
		User currentUser = currentUser();
		if (currentUser.getGame() == null) {
			return badRequest(jsonError("player does not have a game"));
		}
		Game game = currentUser.getGame();
		game.removePlayer(currentUser);
		return ok(jsonInfo("quit game"));
	}
	
	public static Result info() {
		User currentUser = currentUser();
		if (currentUser.inGame()) {
			return ok(Json.toJson(currentUser.getGame()));
		}
		return badRequest(jsonError("Not in a game"));
	}
	
}
