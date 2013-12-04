package pc.wsapi.controllers;

import java.util.Map;

import pc.common.controllers.AbstractController;
import pc.wsapi.biz.MsgPushBiz;
import play.mvc.Result;

public class SendImageCtr extends AbstractController {

	public static Result _index() {
		return ok(pc.wsapi.views.html.upload.render());
	}
	
	public static Result pushMsg () {
		MsgPushBiz biz = new MsgPushBiz();
		Map<String, String[]> form = request().body().asFormUrlEncoded();
		return ok(biz.execute(form));
	}


}
