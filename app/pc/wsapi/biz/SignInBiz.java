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
		HashMap<String, Object> params = new HashMap<>();
		
		if (form.get("code") == null) {
			params.put(msg, "code is empty");
			result = JsonUtil.setRtn(ng, params);
		}
		
		if (form.get("password") == null) {
			params.put(msg, "password is empty");
			result = JsonUtil.setRtn(ng, params);
		}
		
		if (form.get("token") == null) {
			params.put(msg, "token is empty");
			result = JsonUtil.setRtn(ng, params);
		}
		
		if (result.size() > 0) {
			return result;
		}
		
		String code = form.get("code")[0];
		String password = form.get("password")[0];
		String token = form.get("token")[0];
		
		//save
		Users users = new Users();
		users.code  = code;
		users.password = password;
		users.token = token;
		
		boolean isExistsUser = Users.find.equals(users);
		if (!isExistsUser) {
			users.password = password;
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
