package controllers;

import static akka.pattern.Patterns.ask;
import models.Game;
import models.User;

import org.codehaus.jackson.node.ObjectNode;

import actors.GameStreamActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Akka;
import play.libs.Json;
import play.libs.F.Function;
import play.mvc.Controller;
import play.mvc.Security.Authenticated;
import util.Command;
import util.GameCommands;

public class SuperController extends Controller {

	public static final ActorRef myActor = Akka.system().actorOf(new Props(GameStreamActor.class));
	
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
	
	public static void sendCommandToGame(Game game, Command command) {
		GameCommands.getGame(game).boardcast(command);
	}
	
	public static void sendCommandToUser(Game game, User user, Command command) {
		GameCommands.getGame(game).send(command, user);
	}
	
	public static void sendFinalize() {
		Akka.asPromise(ask(myActor, GameStreamActor.Command.send, 1000)).map(
			new Function<Object, Void>() {
				public Void apply(Object response) {
					return null;
				}
			}
		);
	}
}
