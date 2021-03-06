package pc.wsapi.controllers;

import java.util.Map;

import play.Logger;
import pc.common.controllers.AbstractController;
import pc.wsapi.biz.SignInBiz;
import play.mvc.Result;

public class SignInCtr extends AbstractController{

	public static Result _index() {
		
		return ok(pc.wsapi.views.html.signIn.render());
	}
	
	public static Result signIn () {
		SignInBiz biz = new SignInBiz();
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		Logger.debug("form : " + form);
		return ok_res(biz.execute(form));
	}
	
	
//	public static WebSocket<JsonNode> signIn () {
//		return new WebSocket<JsonNode> () {
//
//			@Override
//			public void onReady(play.mvc.WebSocket.In<JsonNode> in,
//					play.mvc.WebSocket.Out<JsonNode> out) {
//				
//				SignInBiz sBiz = new SignInBiz();
//				sBiz.execute(in, out);
//			}
//		};
//	}
}
