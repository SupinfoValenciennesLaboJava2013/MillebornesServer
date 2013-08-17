package controllers;

import models.User;

import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Security.Authenticated;

public class SuperController extends Controller {

	@Authenticated(Secured.class)
	protected static User currentUser() {
		User user = User.findById(Long.parseLong(session("user")));
		return user;
	}

	public static ObjectNode jsonError() {
		return jsonError("undefined error");
	}
	
	public static ObjectNode jsonError(String message) {
		ObjectNode node = Json.newObject();
		node.put("error", message);
		return node;
	}
	
	public static ObjectNode jsonInfo() {
		return jsonInfo("undefined information");
	}
	
	public static ObjectNode jsonInfo(String message) {
		ObjectNode node = Json.newObject();
		node.put("info", message);
		return node;
	}
}
