package pc.wsapi.controllers;

import org.codehaus.jackson.JsonNode;

import pc.common.controllers.AbstractController;
import pc.wsapi.biz.MsgPushBiz;
import pc.wsapi.biz.PushSetBiz;
import play.mvc.Result;
import play.mvc.WebSocket;

public class MsgPushCtr extends AbstractController {

	public static Result _index() {
		return ok(pc.wsapi.views.html.msgPush.render());
//		return null;
	}
	
	
	public static WebSocket<JsonNode> pushSet () {
		return new WebSocket<JsonNode> () {

			@Override
			public void onReady(play.mvc.WebSocket.In<JsonNode> in,
					play.mvc.WebSocket.Out<JsonNode> out) {
				
				PushSetBiz biz = new PushSetBiz();
				biz.execute(in, out);
			}
		};
		
	}
	
	public static WebSocket<JsonNode> pushMsg () {
		return new WebSocket<JsonNode> () {

			@Override
			public void onReady(play.mvc.WebSocket.In<JsonNode> in,
					play.mvc.WebSocket.Out<JsonNode> out) {
				
				MsgPushBiz biz = new MsgPushBiz();
				biz.execute(in, out);
			}
		};
	}


}
