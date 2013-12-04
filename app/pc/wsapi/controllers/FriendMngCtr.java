package pc.wsapi.controllers;

import java.util.Map;

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
	public static Result addFriend () {
		FriendMngBiz biz = new FriendMngBiz();
		Map<String, String[]> param = getReq_params();
		
		return ok(biz.add(param));
	}
	
	public static Result searchFriend () {
		FriendMngBiz biz = new FriendMngBiz();
		Map<String, String[]> param = request().body().asFormUrlEncoded();
		
		return ok(biz.search(param));
	}
	
	public static Result removeFriend () {
		return null;
	}
	
	public static Result getFriendList () {
		return null;
	}

}
