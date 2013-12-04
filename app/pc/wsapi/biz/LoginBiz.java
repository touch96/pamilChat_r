package pc.wsapi.biz;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;


import pc.wsapi.dbs.Users;
import pc.wsapi.utils.JsonUtil;
import play.db.ebean.Model.Finder;
import play.libs.Json;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

import com.avaje.ebean.Query;

public class LoginBiz extends AbstractBiz {

	@Override
	public void execute(In<JsonNode> in, Out<JsonNode> out) {
//		in.onMessage(new BizCallback(out));

	}

	@Override
	public JsonNode execute(Map<String, String[]> js) {
		HashMap<String, Object> params = new HashMap<>();
		JsonNode result = Json.newObject();
		
		String code = js.get("code")[0];
		String password = js.get("password")[0];
		String token = js.get("token")[0];
		
		Finder<Long, Users> finder = new Finder<Long, Users>(Long.class, Users.class);
		Query<Users> query = finder.where("code='"+code+"' and password='"+password+"'");
		Users users = query.findUnique();
		
		if (users != null) {
			if (token.equals(users.getToken())) {
				params.put(msg, "ok");
				result = JsonUtil.setRtn(ok, params);
			} else {
				params.put(msg, "invaild token");
				result = JsonUtil.setRtn(ng, params);
			}
		} else {
			params.put(msg, "invaild user");
			result = JsonUtil.setRtn(ng, params);
		}
		
		return result;
	}
	

}
