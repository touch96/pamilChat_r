package pc.wsapi.utils;

import java.util.LinkedList;
import java.util.ListIterator;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.feedback.AppleFeedbackServerBasicImpl;
import javapns.feedback.FeedbackServiceManager;
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
		String key = Messages.get("pamil.apns.key.path.real");
		String pw = Messages.get("pamil.apns.key.pw.real");
		String sec = Messages.get("pamil.apns.sec");
		String gateway_host = Messages.get("pamil.apns.gateway.host.real");
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
	
	public static ListIterator<Device> feedback (int count) throws Exception {
		String key = Messages.get("pamil.apns.key.path.real");
		String pw = Messages.get("pamil.apns.key.pw.real");
		String sec = Messages.get("pamil.apns.sec");
		String feedback_host = Messages.get("pamil.apns.feedback.host.real");
		int feedback_port = Integer.parseInt(Messages.get("pamil.apns.feedback.port"));
		
		FeedbackServiceManager feedbackManager = new FeedbackServiceManager();
		AppleFeedbackServerBasicImpl server = new AppleFeedbackServerBasicImpl(key, pw, sec,feedback_host,feedback_port);
		
		LinkedList<Device> devices = feedbackManager.getDevices(server);
		
		return devices.listIterator();
	}
}
