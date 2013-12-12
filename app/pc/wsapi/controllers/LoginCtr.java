package pc.wsapi.controllers;

import java.util.Map;

import org.codehaus.jackson.JsonNode;

import pc.common.controllers.AbstractController;
import pc.wsapi.biz.LoginBiz;
import play.Logger;
import play.mvc.Result;
import play.mvc.WebSocket;

public class LoginCtr extends AbstractController {
	
	public static Result login () {
		LoginBiz biz = new LoginBiz();
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		Logger.debug("form : " + form);
		return ok_res(biz.execute(form));
	}

	
	
	
	public static WebSocket<JsonNode> autholization () {
		return new WebSocket<JsonNode> () {

			@Override
			public void onReady(play.mvc.WebSocket.In<JsonNode> in,
					play.mvc.WebSocket.Out<JsonNode> out) {
				
				LoginBiz biz = new LoginBiz();
				biz.execute(in, out);
			}
		};
	}


	public static Result _index() {
		return ok_res(pc.wsapi.views.html.login.render());
	}
}
