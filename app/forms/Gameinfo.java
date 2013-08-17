package forms;

import play.data.validation.Constraints.Required;

public class Gameinfo {
	
	@Required
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
