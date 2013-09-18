package middlewares;

import models.User;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;


public class InGame extends Action.Simple {

	@Override
	public Result call(Context ctx) throws Throwable {
		User currentUser = (User) ctx.args.get("currentUser");
		if (currentUser.getGame() == null) {
			// TODO Return JSON
			return badRequest("Must be in a game !");
		} else {
			return delegate.call(ctx);
		}
	}

}
