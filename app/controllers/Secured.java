package controllers;

import models.User;
import play.mvc.Http.Context;
import play.mvc.Security.Authenticator;

public class Secured extends Authenticator {
	
    @Override
    public String getUsername(Context ctx) {
    	String userIdString = ctx.session().get("user");
    	if (userIdString == null) {
    		ctx.session().remove("user");
        	ctx.args.remove("currentUser");
    		return null;
    	}
    	Long userId = Long.parseLong(userIdString);
    	User user = User.findById(userId);
    	if (user == null) {
    		ctx.session().remove("user");
        	ctx.args.remove("currentUser");
    		return null;
    	}
    	ctx.args.put("currentUser", user);
        return user.getUsername();
    }
}
