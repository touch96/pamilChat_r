package pc.wsapi.biz;

import java.util.List;
import java.util.Map;

import javapns.Push;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import pc.wsapi.dbs.Users;
import pc.wsapi.utils.JsonUtil;
import play.Logger;
import play.libs.Json;
import play.libs.F.Callback;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class PushSetBiz extends AbstractBiz {

	@Override
	public void execute(In<JsonNode> in, Out<JsonNode> out) {
		Logger.debug("PushSetBiz.invoke");
		in.onMessage(new BizCallback(out));
		
	}
	
	public static class BizCallback implements Callback<JsonNode> {
		private Out<JsonNode> out;
		
		public BizCallback (Out<JsonNode> out) {
			this.out = out;
		}
		
		@Override
		public void invoke(JsonNode ns) throws Throwable {
			Logger.debug("invoke");
			ObjectNode echo = Json.newObject();
			
			try {
				String token = ns.get("token").asText();
				
				Users users = new Users();
				boolean isExistsUser = Users.find.equals(users);
				
				if (!isExistsUser) {
					users.save();
				}

				
			} catch (Exception e) {
				e.printStackTrace();
				echo.put(rtn, JsonUtil.setRtn(error, null));
			} finally {
	            out.write(echo);
			}
		}
		
	}

	@Override
	public JsonNode execute(Map<String, String[]> js) {
		// TODO Auto-generated method stub
		return null;
	}

}
