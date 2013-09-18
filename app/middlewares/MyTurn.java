package middlewares;

import models.User;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.Result;


public class MyTurn extends Action.Simple {

	@Override
	public Result call(Context ctx) throws Throwable {
		User currentUser = (User) ctx.args.get("currentUser");
		if (!currentUser.getGame().isCurrentPlayer(currentUser)) {
			// TODO Return JSON
			return badRequest("It is not your turn to play...");
		} else {
			return delegate.call(ctx);
		}
	}

}
