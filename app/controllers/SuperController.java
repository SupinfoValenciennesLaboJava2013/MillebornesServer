package controllers;

import static akka.pattern.Patterns.ask;
import models.Game;
import models.User;

import org.codehaus.jackson.node.ObjectNode;

import play.libs.Akka;
import play.libs.F.Function;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Security.Authenticated;
import util.GameCommands;
import actors.GameStreamActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import commands.AbstractCommand;

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
	
	public static void sendCommandToGame(Game game, AbstractCommand command) {
		GameCommands.getGame(game).boardcast(command);
	}
	
	public static void sendOneCommandToGame(Game game, AbstractCommand command) {
		GameCommands.getGame(game).boardcast(command);
		sendFinalize();
	}
	
	public static void sendCommandToUser(Game game, User user, AbstractCommand command) {
		GameCommands.getGame(game).send(command, user);
	}
	
	public static void sendOneCommandToUser(Game game, User user, AbstractCommand command) {
		GameCommands.getGame(game).send(command, user);
		sendFinalize();
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
