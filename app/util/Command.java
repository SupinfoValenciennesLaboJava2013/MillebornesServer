package util;

public class Command {
	protected String type;
	protected Object args;
	
	public Command() {}
	
	public Command(String type) {
		setType(type);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getArgs() {
		return args;
	}
	public void setArgs(Object args) {
		this.args = args;
	}
	
}
