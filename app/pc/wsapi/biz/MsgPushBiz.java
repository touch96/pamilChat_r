package pc.wsapi.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonNode;
import com.avaje.ebean.Query;

import pc.wsapi.constants.Constants;
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
		
		if (form == null) {
			params.put("msg", "no parameter");
			result = JsonUtil.setRtn(ng, params);
		}
		
		if (form.get("msg") == null) {
			params.put("msg", "no message");
			result = JsonUtil.setRtn(ng, params);
		}
		if (form.get("token") == null) {
			params.put("msg", "no token");
			result = JsonUtil.setRtn(ng, params);
		}
		if (form.get("code") == null) {
			params.put("msg", "no code");
			result = JsonUtil.setRtn(ng, params);
		}
		
		if (result.size() > 0) {
			return result;
		}
		
		String msg = form.get("msg")[0];
		String token = form.get("token")[0];
		String code = form.get("code")[0];
		
		
		String key = Messages.get("pamil.apns.key.path");
		String pw = Messages.get("pamil.apns.key.pw");
		String sec = Messages.get("pamil.apns.sec");
		String gateway_host = Messages.get("pamil.apns.gateway.host");
		int gateway_port = Integer.parseInt(Messages.get("pamil.apns.gateway.port"));
		
		try {
			if (Logger.isDebugEnabled()) {
				Logger.debug ("key" + key);
				Logger.debug ("pw : " + pw);
				Logger.debug ("sec : " + sec);
				Logger.debug ("gateway_host : " + gateway_host);
				Logger.debug ("gateway_port : " + gateway_port);
			}
			
			String fileStr = "";
			if (form.get("img") != null) {
				fileStr = form.get("img")[0];
				fileStr = fileStr.substring(fileStr.indexOf(Constants.BASE64) + Constants.BASE64.length());
				//==================================//
				//	Base64デコード	         //
				//==================================//
				byte[] outdata = Base64.decodeBase64(fileStr.getBytes());
				//==================================//
				//	結果書き出し	         //
				//==================================//
				File outf = new File("/Users/JP10844/398660229990_.txt");
				FileOutputStream fo = new FileOutputStream(outf);
				fo.write(outdata);
				fo.close();
			}
			
			
			
			Logger.info ("code : " + code);
			Logger.info ("token : " + token);
			//user存在チェック
			Finder<Long, Users> finder = new Finder<Long, Users>(Long.class, Users.class);
			Query<Users> query = finder.where("code='"+code+"' and token='"+token+"'");
			Users users = query.findUnique();
			
			if (users != null && users.code.equals(code)) {
				PushNotificationManager pushManager = new PushNotificationManager();
				PushNotificationPayload payload = PushNotificationPayload.complex();
		        payload.addAlert(msg);
		        payload.addBadge(1);
		        payload.addSound("default");
		        payload.addCustomDictionary("id", "1");
		        
		        BasicDevice device = new BasicDevice(token);
		        device.setDeviceId("iPhone");
		        
		        //push messages
		        Logger.debug (payload.toString());
		        
		        AppleNotificationServer appleNoti = 
		        		new AppleNotificationServerBasicImpl(key, pw,sec,gateway_host,gateway_port);
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
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = JsonUtil.setRtn(error, params);
		}
		
		return result;
	}
}
