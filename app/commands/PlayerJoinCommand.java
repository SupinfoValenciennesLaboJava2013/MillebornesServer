package commands;

public class PlayerJoinCommand extends AbstractCommand {

	private String username;
	
	public PlayerJoinCommand(String username) {
		this.setUsername(username);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	{
		this.setType("player.join");
	}
}
