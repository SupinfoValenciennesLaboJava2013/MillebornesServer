package controllers;

import models.Game;
import models.User;
import play.data.Form;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import exceptions.AlreadyInGameException;
import forms.Gameinfo;

@Authenticated(Secured.class)
public class Games extends SuperController {

	public static Result create() {
		Form<Gameinfo> gameinfoForm = Form.form(Gameinfo.class).bindFromRequest();
		if (gameinfoForm.hasErrors()) {
			return badRequest(jsonError("Wrong parameters for gameinfo"));
		}
		Game game = new Game();
		game.setName(gameinfoForm.get().getName());
		User currentUser = currentUser();
		try {
			game.addPlayer(currentUser);
		} catch (AlreadyInGameException ex) {
			return badRequest(jsonError("Player already in a game"));
		}
		Game.save(game);
//		currentUser.save();
		session("game", String.valueOf(game.getId()));
		return ok(jsonInfo("game created"));
	}
	
	public static Result join(long gameId) {
		Game game = Game.findById(gameId);
		User currentUser = currentUser();
		try {
			game.addPlayer(currentUser);
		} catch (AlreadyInGameException ex) {
			return badRequest(jsonError("Player already in a game"));
		}
//		game.save();
//		currentUser.save();
		session("game", String.valueOf(game.getId()));
		return ok(jsonInfo("joined game"));
	}
	
	public static Result quit() {
		User currentUser = currentUser();
		if (currentUser.getGame() == null) {
			return badRequest("player does not have a game");
		}
		Game game = currentUser.getGame();
		game.removePlayer(currentUser);
//		game.save();
//		currentUser.save();
		session().remove("game");
		return ok(jsonInfo("quit game"));
	}
	
}
