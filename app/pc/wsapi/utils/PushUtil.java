package pc.wsapi.utils;

import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;
import javapns.notification.ResponsePacket;
import play.Logger;
import play.i18n.Messages;

public class PushUtil {
	public static boolean push (String token , String msg) throws Exception {
		String key = Messages.get("pamil.apns.key.path");
		String pw = Messages.get("pamil.apns.key.pw");
		String sec = Messages.get("pamil.apns.sec");
		String gateway_host = Messages.get("pamil.apns.gateway.host");
		int gateway_port = Integer.parseInt(Messages.get("pamil.apns.gateway.port"));
		
		if (Logger.isDebugEnabled()) {
			Logger.debug ("key" + key);
			Logger.debug ("pw : " + pw);
			Logger.debug ("sec : " + sec);
			Logger.debug ("gateway_host : " + gateway_host);
			Logger.debug ("gateway_port : " + gateway_port);
		}
		
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
        	
        	return false;
        }
        
        return true;

	}
}
