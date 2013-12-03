package pc.wsapi.biz;

import java.util.HashMap;
import java.util.Map;

import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

import org.codehaus.jackson.JsonNode;
import com.avaje.ebean.Query;

import pc.wsapi.dbs.Users;
import pc.wsapi.utils.JsonUtil;
import play.Logger;
import play.db.ebean.Model.Finder;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class MsgPushBiz extends AbstractBiz {

	@Override
	public void execute(In<JsonNode> in, Out<JsonNode> out) {
		

	}
	


	@Override
	public JsonNode execute(Map<String, String[]> form) {
		Logger.debug ("MsgPushBiz - execute");
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		
		String msg = form.get("msg")[0];
		String m_id = form.get("device")[0];
		String id = form.get("id")[0];
		
		
		String key = Messages.get("pamil.apns.key.path");
		String pw = Messages.get("pamil.apns.key.pw");
		
		try {
			Logger.info ("id : " + id);
			Logger.info ("m_id : " + m_id);
			//user存在チェック
			Finder<Long, Users> finder = new Finder<Long, Users>(Long.class, Users.class);
			Query<Users> query = finder.where("id='"+id+"' and m_id='"+m_id+"'");
			Users users = query.findUnique();
			
//			if (users != null && users.id.equals(id)) {
				PushNotificationManager pushManager = new PushNotificationManager();
				PushNotificationPayload payload = PushNotificationPayload.complex();
		        payload.addAlert(msg);
		        payload.addBadge(1);
		        payload.addSound("default");
		        payload.addCustomDictionary("id", "1");
		        
		        BasicDevice device = new BasicDevice(m_id);
		        device.setDeviceId("iPhone");
		        
		        //push messages
		        Logger.debug (payload.toString());
		        
		        AppleNotificationServer appleNoti = new AppleNotificationServerBasicImpl(key, pw,true);
		        pushManager.initializeConnection(appleNoti);
		        
		        //send push
		        PushedNotification pushed = pushManager.sendNotification(device, payload);
		        
		        ResponsePacket res = pushed.getResponse();
		        
		        if (res != null) {
		        	Logger.debug("isErrorResponsePacket : " + res.isErrorResponsePacket());
		        	Logger.debug("isValidErrorMessage   : " + res.isValidErrorMessage());
		        	Logger.debug("getMessage            : " + res.getMessage());
		        	
		        	params.put(msg, "ok");
		        	result = JsonUtil.setRtn(ok, params);
		        } else {
		        	params.put(msg, "no response");
		        	result = JsonUtil.setRtn(ng, params);
		        }
		        
//		        List<PushedNotification> NOTIFICATIONS = 
//		        		Push.payload(payload, Messages.get("pamil.apns.key.path"), Messages.get("pamil.apns.key.pw"), true, device);
//		        
//		        for (PushedNotification NOTIFICATION : NOTIFICATIONS) {
//		            if (NOTIFICATION.isSuccessful()) {
//	                    /* APPLE ACCEPTED THE NOTIFICATION AND SHOULD DELIVER IT */  
//	                    /* STILL NEED TO QUERY THE FEEDBACK SERVICE REGULARLY */
//						params.put(msg, "ok");
//						result = JsonUtil.setRtn(ok, params);
//		            } 
//		            else {
//	                    String INVALIDTOKEN = NOTIFICATION.getDevice().getToken();
//	                    /* ADD CODE HERE TO REMOVE INVALIDTOKEN FROM YOUR DATABASE */  
//
//	                    /* FIND OUT MORE ABOUT WHAT THE PROBLEM WAS */  
//	                    Exception THEPROBLEM = NOTIFICATION.getException();
//	                    THEPROBLEM.printStackTrace();
//
//	                    /* IF THE PROBLEM WAS AN ERROR-RESPONSE PACKET RETURNED BY APPLE, GET IT */  
//	                    ResponsePacket THEERRORRESPONSE = NOTIFICATION.getResponse();
//	                    if (THEERRORRESPONSE != null) {
//	                    	Logger.debug(THEERRORRESPONSE.getMessage());
//	                    }
//						params.put(msg, THEERRORRESPONSE.getMessage());
//						result = JsonUtil.setRtn(ng, params);
//		            }
//		        }
//			}

		} catch (Exception e) {
			e.printStackTrace();
			result = JsonUtil.setRtn(error, params);
		}
		
		return result;
	}
}
