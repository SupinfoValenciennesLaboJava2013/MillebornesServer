package controllers;

import models.Game;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import exceptions.AlreadyInGameException;
import exceptions.TooManyPlayersException;
import forms.Gameinfo;

@Transactional
@Authenticated(Secured.class)
public class Games extends SuperController {

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
