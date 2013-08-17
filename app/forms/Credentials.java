package forms;

import play.data.validation.Constraints.Required;

public class Credentials {
	
	@Required
	public String username;
	
	@Required
	public String password;
}
