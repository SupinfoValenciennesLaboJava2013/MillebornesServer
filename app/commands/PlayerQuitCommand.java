package commands;

public class PlayerQuitCommand extends AbstractCommand {

	private String username;
	
	public PlayerQuitCommand(String username) {
		this.setUsername(username);
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	{
		this.setType("player.quit");
	}
}
