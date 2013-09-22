package controllers;

import play.mvc.Controller;
import play.mvc.Http.RawBuffer;
import play.mvc.Http.Request;
import play.mvc.Http.RequestBody;
import play.mvc.Result;

public class Application extends Controller {
  
    public static Result index() {
        return ok("Your new application is ready.");
    }
  
    public static Result echo() {
    	Request request = request();
    	RequestBody body = request.body();
    	RawBuffer raw = body.asRaw();
    	return ok(raw.asBytes());
    }
    
}
