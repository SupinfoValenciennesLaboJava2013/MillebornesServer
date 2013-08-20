package controllers;

import static akka.pattern.Patterns.ask;
import models.Game;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import actors.GameStreamActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import exceptions.AlreadyInGameException;
import exceptions.TooManyPlayersException;
import forms.Gameinfo;


@Transactional
@Authenticated(Secured.class)
public class Games extends SuperController {
	
	public static final ActorRef myActor = Akka.system().actorOf(new Props(GameStreamActor.class));
	
	public static Result stream() {
		return async(
			Akka.asPromise(ask(myActor, GameStreamActor.Command.subscribe, 5000))
				.map(
					new Function<Object,Result>() {
						public Result apply(Object response) {
							return ok(jsonInfo("quelque chose de nouveau !"));
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
		return async(
			Akka.asPromise(ask(myActor, GameStreamActor.Command.send, 1000)).map(
				new Function<Object,Result>() {
					public Result apply(Object response) {
						return ok(jsonInfo("ok"));
					}
				}
			)
		);
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
		User currentUser = currentUser();
		try {
			game.addPlayer(currentUser);
		} catch (AlreadyInGameException ex) {
			return badRequest(jsonError("Player already in a game"));
		} catch (TooManyPlayersException e) {
			return badRequest(jsonError("This game is full"));
		}
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
