package util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import exceptions.NotInAGameException;
import models.Game;
import models.User;

public class GameCommands {

	private static Map<Long, GameCommands> games;
	
	static {
		games = new HashMap<Long, GameCommands>();
	}
	
	public static GameCommands getGame(Game game) {
		GameCommands gc = games.get(game.getId());
		if (gc == null) {
			gc = new GameCommands();
			games.put(game.getId(), gc);
		}
		return gc;
	}
	
	public static List<Command> getCommands(User user) throws NotInAGameException {
		if (!user.inGame()) {
			throw new NotInAGameException();
		}
		return getGame(user.getGame()).getCommandsForUser(user);
	}
	
	public static void removeGame(Game game) {
		games.remove(game.getId());
	}
	
	private GameCommands() {
		this.commands = new HashMap<Long, List<Command>>();
	}
	
	private Map<Long, List<Command>> commands;
	
	public void boardcast(Command command) {
		for (List<Command> entry: commands.values()) {
			entry.add(command);
		}
	}
	
	public void send(Command command, User user) {
		List<Command> cm = commands.get(user.getId());
		if (cm == null) {
			cm = new LinkedList<Command>();
			commands.put(user.getId(), cm);
		}
		cm.add(command);
	}
	
	public List<Command> getCommandsForUser(User user) throws NotInAGameException {
		if (!user.inGame()) {
			throw new NotInAGameException();
		}
		List<Command> result = commands.get(user.getId());
		if (result == null) {
			result = new LinkedList<Command>();
			commands.put(user.getId(), result);
		}
		commands.put(user.getId(), new LinkedList<Command>());
		return result;
	}
	
}
