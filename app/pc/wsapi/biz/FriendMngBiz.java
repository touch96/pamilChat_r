package pc.wsapi.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;

import pc.wsapi.dbs.Friendrequest;
import pc.wsapi.dbs.Friends;
import pc.wsapi.dbs.Users;
import pc.wsapi.dbs.sqlbean.FriendsSQL;
import pc.wsapi.dbs.sqlbean.UsersSQL;
import pc.wsapi.utils.JsonUtil;
import pc.wsapi.utils.PushUtil;
import play.Logger;
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
		
		String code =  req_form.get("code")[0];
		String f_code =  req_form.get("f_code")[0];
		int badge = 0;
		
		try {
			Query<Friendrequest> query = Friendrequest.find.where("f_code = '"+f_code+"' and code = '"+code+"'");
			Friendrequest temp_friendRequest = 
					Friendrequest.find.where().
						eq("f_code", f_code).
						eq("code", code).findUnique();
//			Logger.debug("f_code : "+_temp_friendRequest.friends.f_code);
			
			if (temp_friendRequest != null) {
				if ( "00".equals(temp_friendRequest.status) ) {
					params.put(ng, "already request");
					result = JsonUtil.setRtn(ng, params);
					return result;
				} else {
					temp_friendRequest.isnew = true;
					temp_friendRequest.status = "00";
					
					temp_friendRequest.update();
				}
			} else {
				friendRequest.code = code;
				friendRequest.f_code = f_code;
				friendRequest.isnew = true;
				friendRequest.status = "00";
				
				friendRequest.save();
			}
			
			//友達に知らせる
			Query<Users> queryUr = Users.find.where("code='"+f_code+"'");
			Users users = queryUr.findUnique();
			
			query = Friendrequest.find.where("f_code='"+f_code+"'");
			List<Friendrequest> friendrequest = query.findList();
			
			if (friendrequest != null && friendrequest.size() > 0) {
				badge = friendrequest.size() + 1;
			}
			
			PushUtil.push(users.token, "you have friend request from : " + code,badge);
			
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
//				friendrequest.code = frq.code;
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
		String code = req_form.get("code")[0];
		String f_code = req_form.get("f_code")[0];
		String status = req_form.get("status")[0];
		 
		//update isnew
		Query<Friendrequest> query = Friendrequest.find.where("f_code='"+f_code+"' and code='"+code+"' and status='00'");
		
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
			fr.f_code = req_form.get("code")[0];
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
		String code = req_form.get("code")[0];
		String f_code = req_form.get("f_code")[0];
		
//		Query<Users> query = Users.find.where("code like '"+f_code+"%' and code not in (select f_code from friends where code = '"+code+"')");
//		List<Users> users = query.findList();
//		
		RawSql rsql = 
				RawSqlBuilder.
				unparsed(
						"select u.code " +
						"from users u " +
						"where " +
						"u.code like ? and " +
						"u.code not in (select f_code from friends where code = ?)")
				.columnMapping("u.code", "code")
				.create();
		
		Query<UsersSQL> aa = Ebean.find(UsersSQL.class);
		aa.setRawSql(rsql);
		aa.setParameter(1, "%"+f_code+"%");
		aa.setParameter(2, code);
		
		List<UsersSQL> users = aa.findList();
		if (users != null && users.size() > 0) {
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
		
		RawSql rsql = 
				RawSqlBuilder.
				unparsed("select code, f_code, createdt from friends where code = ?")
				.columnMapping("code", "code")
				.columnMapping("f_code", "f_code")
				.columnMapping("createdt", "createdt").create();
		
		Query<FriendsSQL> query = Ebean.find(FriendsSQL.class);
		query.setRawSql(rsql);
		query.setParameter(1, code);
		
		List<FriendsSQL> frList = query.findList();
		
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
		friends.code = req_form.get("code")[0];
		friends.f_code = req_form.get("f_code")[0];
		return Friends.find.equals(friends);
	}



}
