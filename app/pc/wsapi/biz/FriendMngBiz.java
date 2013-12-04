package pc.wsapi.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.avaje.ebean.Query;

import pc.wsapi.dbs.Friends;
import pc.wsapi.dbs.Users;
import pc.wsapi.utils.JsonUtil;
import play.db.ebean.Model.Finder;
import play.libs.Json;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class FriendMngBiz extends AbstractBiz {

	@Override
	public void execute(In<JsonNode> in, Out<JsonNode> out) {
		// TODO Auto-generated method stub

	}

	@Override
	public JsonNode execute(Map<String, String[]> req_form) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public JsonNode add (Map<String, String[]> req_form) {
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		Friends fr = new Friends();
		if (!chkFriend(fr, req_form)) {
			fr.save();
			params.put(msg, "friend regist ok");
			result = JsonUtil.setRtn(ok, params);
		} else {
			params.put(msg, "exists friend");
			result = JsonUtil.setRtn(ng, params);
		}
		return result;
	}
	
	public JsonNode search (Map<String, String[]> req_form) {
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		
		Finder<Long, Users> finder = new Finder<Long, Users>(Long.class, Users.class);
		Query<Users> query = finder.where("code like '"+req_form.get("f_code")+"%'");
		List<Users> users = query.findList();
		
		if (users != null) {
			params.put(msg, "user is exists");
			params.put("users", users);
			result = JsonUtil.setRtn(ok, params);
		} else {
			params.put(msg, "user is not exists");
			result = JsonUtil.setRtn(ng, params);
		}
		
//		Friends fr = new Friends();
//		if (chkFriend(fr, req_form)) {
//			Map <String, String> frMap = new HashMap<>();
//			frMap.put("f_id", fr.f_id);
//			params.put(msg, "friend regist ok");
//			result = JsonUtil.setRtn(ok, params);
//		} else {
//			params.put(msg, "no friend");
//			result = JsonUtil.setRtn(ng, params);
//		}
		return result;
	}
	
	public JsonNode selectList (Map<String, String[]> req_form) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public JsonNode remove (Map<String, String[]> req_form) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean chkFriend (Friends friends, Map<String, String[]> req_form) {
		
		//追加対象のfriendが、既に登録されたかを確認
		friends.code = req_form.get("code")[0];
		friends.f_code = req_form.get("f_code")[0];
		return Friends.find.equals(friends);
	}



}
