package commands;

public abstract class AbstractCommand {
	protected String type;
	
	public AbstractCommand() {}
	
	public AbstractCommand(String type) {
		setType(type);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
