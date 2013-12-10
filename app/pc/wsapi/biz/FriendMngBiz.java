package pc.wsapi.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.avaje.ebean.Query;

import pc.wsapi.dbs.Friendrequest;
import pc.wsapi.dbs.Friends;
import pc.wsapi.dbs.Users;
import pc.wsapi.utils.JsonUtil;
import pc.wsapi.utils.PushUtil;
import play.libs.Json;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class FriendMngBiz extends AbstractBiz {

	@Override
	public void execute(In<JsonNode> in, Out<JsonNode> out) {

	}

	@Override
	public JsonNode execute(Map<String, String[]> req_form) {
		return null;
	}
	
	public JsonNode friend_request (Map<String, String[]> req_form) {
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		Friendrequest friendRequest = new Friendrequest();
		
		String s_code =  req_form.get("s_code")[0];
		String f_code =  req_form.get("f_code")[0];
		int badge = 0;
		
		try {
			friendRequest.s_code = s_code;
			friendRequest.f_code = f_code;
			friendRequest.isnew = true;
			friendRequest.status = "00";
			
			friendRequest.save();
			
			//友達に知らせる
			Query<Users> query = Users.find.where("code='"+f_code+"'");
			Users users = query.findUnique();
			
			Query<Friendrequest> queryfr = Friendrequest.find.where("f_code='"+f_code+"'");
			List<Friendrequest> friendrequest = queryfr.findList();
			
			if (friendrequest != null && friendrequest.size() > 0) {
				badge = friendrequest.size() + 1;
			}
			
			PushUtil.push(users.token, "you have friend request from : " + s_code,badge);
			
			params.put(msg, "request success");
			result = JsonUtil.setRtn(ok, params);
			
		} catch (Exception e) {
			e.printStackTrace();
			result = JsonUtil.setRtn(error, params);
		}
		
		return result;
	}
	
	public JsonNode friend_request_list (Map<String, String[]> req_form) {
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		String code = req_form.get("code")[0];
		 
		Query<Friendrequest> query = Friendrequest.find.where("f_code = '"+code+"' and status='00'");
		
		List <Friendrequest> list = query.findList();
		
		if (list != null) {
			params.put(msg, "have request");
			params.put("requestlist", list);
			result = JsonUtil.setRtn(ok, params);
			
			
			//update isnew
//			for (Friendrequest frq : list) {
//				Friendrequest friendrequest = new Friendrequest();
//				friendrequest.s_code = frq.s_code;
//				friendrequest.f_code = frq.f_code;
//				friendrequest.isnew = false;
//				
//				friendrequest.update();
//			}
		} else {
			params.put(msg, "no request");
			result = JsonUtil.setRtn(ng, params);
		}
		
		return result;

	}
	
	/**
	 * 
	 * @param req_form
	 * @return
	 */
	public JsonNode friend_request_confirm (Map<String, String[]> req_form) {
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		String s_code = req_form.get("s_code")[0];
		String f_code = req_form.get("f_code")[0];
		String status = req_form.get("status")[0];
		 
		//update isnew
		Query<Friendrequest> query = Friendrequest.find.where("f_code='"+f_code+"' and s_code='"+s_code+"' and status='00'");
		
		Friendrequest friendrequest = query.findUnique();
		if (friendrequest != null) {
			friendrequest.status = status;
			
			friendrequest.update();
			
			if ("01".equals(status)) {
				add(req_form);
			}
			
			params.put(msg, "OK!!");
			result = JsonUtil.setRtn(ok, params);
		} else {
			params.put(msg, "no request");
			result = JsonUtil.setRtn(ng, params);
			
		}
		
		return result;
		
	}
	
	/**
	 * 
	 * @param req_form
	 * @return
	 */
	private boolean add (Map<String, String[]> req_form) {
		Friends fr = new Friends();
		if (!chkFriend(fr, req_form)) {
			fr.save();
			
			fr = new Friends();
			fr.code = req_form.get("f_code")[0];
			fr.f_code = req_form.get("s_code")[0];
			fr.save();
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param req_form
	 * @return
	 */
	public JsonNode search (Map<String, String[]> req_form) {
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		String f_code = req_form.get("f_code")[0];
		Query<Users> query = Users.find.where("code like '"+f_code+"%'");
		List<Users> users = query.findList();
		
		if (users != null) {
			params.put(msg, "user is exists");
			params.put("users", users);
			result = JsonUtil.setRtn(ok, params);
		} else {
			params.put(msg, "user is not exists");
			result = JsonUtil.setRtn(ng, params);
		}

		return result;
	}
	
	/**
	 * 
	 * @param req_form
	 * @return
	 */
	public JsonNode selectList (Map<String, String[]> req_form) {
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		String code = req_form.get("code")[0];
		Query<Friends> query = Friends.find.where("code = '"+code+"'");
		List<Friends> frList = query.findList();
		
		if (frList != null && frList.size() > 0) {
			params.put(msg, "have friends");
			params.put(friendsList, frList);
			
			result = JsonUtil.setRtn(ok, params);
		} else {
			params.put(msg, "no friends");
			result = JsonUtil.setRtn(ng, params);
		}

		return result;
	}
	
	/**
	 * 
	 * @param req_form
	 * @return
	 */
	public JsonNode remove (Map<String, String[]> req_form) {
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		
		Friends friends = new Friends();
		
		if (chkFriend(friends,req_form)) {
			friends.delete();
			params.put(msg, "delete success");
			result = JsonUtil.setRtn(ok, params);
		} else {
			params.put(msg, "no friends");
			result = JsonUtil.setRtn(ng, params);
		}
		
		return result;
	}
	
	private boolean chkFriend (Friends friends, Map<String, String[]> req_form) {
		
		//追加対象のfriendが、既に登録されたかを確認
		friends.code = req_form.get("s_code")[0];
		friends.f_code = req_form.get("f_code")[0];
		return Friends.find.equals(friends);
	}



}
