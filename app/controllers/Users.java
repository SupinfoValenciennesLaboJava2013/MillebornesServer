package controllers;

import java.util.Map;

import models.Game;
import models.User;

import org.codehaus.jackson.node.ObjectNode;

import play.data.Form;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import forms.Credentials;

public class Users extends SuperController {
	
	@Transactional
	public static Result register() {
		Form<Credentials> credentialsForm = Form.form(Credentials.class).bindFromRequest();
		Request request = request();
		String d = "";
		for (Map.Entry<String, String[]> a: request.headers().entrySet()) {
			d += a.getKey();
			d += ": [";
			for (String b: a.getValue()) {
				d += b;
				d += ", ";
			}
			d += "], ";
		}
		if (credentialsForm.hasErrors()) {
			return badRequest(d);
			//return badRequest(jsonError("Not enough parameters"));
		}
		Credentials cred = credentialsForm.get();
		User user = new User();
		if (User.findByUsername(cred.username) != null) {
			return badRequest(jsonError("User already exists"));
		}
		user.setUsername(cred.username);
		user.setPassword(cred.password);
		User.save(user);
		ObjectNode result = Json.newObject();
		result.put("message", cred.username);
		return ok(result);
	}
	
	@Transactional
	public static Result login() {
		Form<Credentials> credentialsForm = Form.form(Credentials.class).bindFromRequest();
		if (credentialsForm.hasErrors()) {
			return badRequest(jsonError());
		}
		Credentials credentials = credentialsForm.get();
		User user = User.findByUsernameAndPassword(credentials.username, credentials.password); 
		if (user == null) {
			return notFound(jsonError("User not found with such credentials"));
		}
		session("user",  String.valueOf(user.getId()));
		return ok(jsonInfo("User successfully logged in"));
	}
	
	@Transactional
	@Authenticated(Secured.class)
	public static Result whoami() {
		return ok(jsonInfo(currentUser().getUsername()));
	}

	@Transactional
	@Authenticated(Secured.class)
	public static Result logout() {
		User currentUser = currentUser();
		if (currentUser.getGame() != null) {
			Game game = currentUser.getGame();
			game.removePlayer(currentUser);
			Game.save(game);
			User.save(currentUser);
		}
		session().remove("user");
		return ok(jsonInfo("Logout success"));
	}
	
	@Transactional
	@Authenticated(Secured.class)
	public static Result info() {
		return ok(Json.toJson(currentUser()));
	}
}
