package pc.wsapi.controllers;


import java.io.File;

import pc.common.controllers.AbstractController;

import pc.wsapi.biz.MsgPushBiz;
import play.mvc.Result;

import play.mvc.BodyParser;

public class MsgPushCtr extends AbstractController {

	public static Result _index() {
		return ok(pc.wsapi.views.html.msgPush.render());
//		return null;
	}
	
	
	@BodyParser.Of(BodyParser.MultipartFormData.class)
	public static Result sendMessage () {
		MsgPushBiz biz = new MsgPushBiz();
		return ok(biz.sendMessage(currentUrl(), getMultipartFormData()));
	}
	
	
	public static Result getMessage () {
		MsgPushBiz biz = new MsgPushBiz();
		
		File img = biz.getMessage(getReq_params());
		
		response().setContentType("application/x-download");  
		response().setHeader("Content-disposition","attachment; filename=" + img.getName()); 
		
		
		return ok(img);
	}
	
	public static Result getNoreadMsgList () {
		MsgPushBiz biz = new MsgPushBiz();
		return ok(biz.getNoreadMsgList(getReq_params()));
		
	}
	
	public static Result updateReadStatus () {
		MsgPushBiz biz = new MsgPushBiz();
		return ok(biz.updateReadStatus(getReq_params()));
	}
	
	
	public static Result testPush () throws Exception {
		MsgPushBiz biz = new MsgPushBiz();
		return ok(biz.testPush(getReq_params()));
	}

}
