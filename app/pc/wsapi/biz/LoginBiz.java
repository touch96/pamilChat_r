package pc.wsapi.biz;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;


import pc.wsapi.dbs.Users;
import pc.wsapi.utils.JsonUtil;
import play.Logger;
import play.db.ebean.Model.Finder;
import play.libs.Json;
import play.libs.F.Callback;
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
		
		String id = js.get("id")[0];
		String pw = js.get("pw")[0];
		String m_id = js.get("device")[0];
		
		Finder<Long, Users> finder = new Finder<Long, Users>(Long.class, Users.class);
		Query<Users> query = finder.where("id='"+id+"' and pw='"+pw+"'");
		Users users = query.findUnique();
		
		if (users != null) {
			if (m_id.equals(users.getDevice())) {
				params.put(msg, "ok");
				result = JsonUtil.setRtn(ok, params);
			} else {
				params.put(msg, "invaild m_id");
				result = JsonUtil.setRtn(ng, params);
			}
		} else {
			params.put(msg, "invaild user");
			result = JsonUtil.setRtn(ng, params);
		}
		
		return result;
	}
	

}
