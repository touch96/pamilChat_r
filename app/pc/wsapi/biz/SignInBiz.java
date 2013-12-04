package pc.wsapi.biz;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;


import pc.wsapi.dbs.Users;
import pc.wsapi.utils.JsonUtil;
import play.Logger;
import play.libs.Json;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class SignInBiz extends AbstractBiz {
		
	@Override
	public void execute(In<JsonNode> in, Out<JsonNode> out) {
		
	}
	
	@Override
	public JsonNode execute(Map<String, String[]> form) {
		Logger.debug("SignInBiz.execute");
		JsonNode result = Json.newObject();
		
		String id = form.get("id")[0];
		String pw = form.get("pw")[0];
		String m_id = form.get("device")[0];
		
		//save
		Users users = new Users();
		users.id  = id;
		users.pw = pw;
		users.device = m_id;
		
		boolean isExistsUser = Users.find.equals(users);
		HashMap<String, Object> params = new HashMap<>();
		if (!isExistsUser) {
			users.pw = pw;
			users.save();
			
			params.put(msg, "user regist ok");
			result = JsonUtil.setRtn(ok, params);
		} else {
			params.put(msg, "exists user");
			result = JsonUtil.setRtn(ng, params);
			
		}
		
		
		
		return result;
	}
}
