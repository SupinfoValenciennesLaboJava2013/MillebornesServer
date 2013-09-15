package util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.Game;
import models.User;

import commands.AbstractCommand;

import exceptions.NotInAGameException;

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
	
	public static List<AbstractCommand> getCommands(User user) throws NotInAGameException {
		if (!user.inGame()) {
			throw new NotInAGameException();
		}
		return getGame(user.getGame()).getCommandsForUser(user);
	}
	
	public static void removeGame(Game game) {
		games.remove(game.getId());
	}
	
	private GameCommands() {
		this.commands = new HashMap<Long, List<AbstractCommand>>();
	}
	
	private Map<Long, List<AbstractCommand>> commands;
	
	public void boardcast(AbstractCommand command) {
		for (List<AbstractCommand> entry: commands.values()) {
			entry.add(command);
		}
	}
	
	public void send(AbstractCommand command, User user) {
		List<AbstractCommand> cm = commands.get(user.getId());
		if (cm == null) {
			cm = new LinkedList<AbstractCommand>();
			commands.put(user.getId(), cm);
		}
		cm.add(command);
	}
	
	public List<AbstractCommand> getCommandsForUser(User user) throws NotInAGameException {
		if (!user.inGame()) {
			throw new NotInAGameException();
		}
		List<AbstractCommand> result = commands.get(user.getId());
		if (result == null) {
			result = new LinkedList<AbstractCommand>();
			commands.put(user.getId(), result);
		}
		commands.put(user.getId(), new LinkedList<AbstractCommand>());
		return result;
	}
	
}
