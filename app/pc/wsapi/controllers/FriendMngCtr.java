package pc.wsapi.controllers;


import pc.common.controllers.AbstractController;
import pc.wsapi.biz.FriendMngBiz;
import play.mvc.Result;

public class FriendMngCtr extends AbstractController {
	
	public static Result _index() {
		return ok(pc.wsapi.views.html.friendMng.render());
//		return null;
	}
	
	/**
	 * 
	 * @return
	 */
//	public static Result add () {
//		FriendMngBiz biz = new FriendMngBiz();
//		return ok(biz.add(getReq_params()));
//	}
	
	public static Result friend_request () {
		FriendMngBiz biz = new FriendMngBiz();
		return ok(biz.friend_request(getReq_params()));
	}
	
	public static Result friend_request_confirm () {
		FriendMngBiz biz = new FriendMngBiz();
		return ok(biz.friend_request_confirm(getReq_params()));
	}
	
	public static Result friend_request_list () {
		FriendMngBiz biz = new FriendMngBiz();
		return ok(biz.friend_request_list(getReq_params()));
		
	}

	public static Result search () {
		FriendMngBiz biz = new FriendMngBiz();
		return ok(biz.search(getReq_params()));
	}
	
	public static Result remove () {
		FriendMngBiz biz = new FriendMngBiz();
		return ok(biz.remove(getReq_params()));
	}
	
	public static Result selectList () {
		FriendMngBiz biz = new FriendMngBiz();
		return ok(biz.selectList(getReq_params()));
	}

}
