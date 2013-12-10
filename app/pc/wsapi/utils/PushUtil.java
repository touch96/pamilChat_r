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
	public static boolean push (String token , String msg, int badge) throws Exception {
		//productバージョン(早くビルドを自動化させないと)
//		String key = Messages.get("pamil.apns.key.path.real");
//		String pw = Messages.get("pamil.apns.key.pw.real");
//		String sec = Messages.get("pamil.apns.sec");
//		String gateway_host = Messages.get("pamil.apns.gateway.host.real");
//		int gateway_port = Integer.parseInt(Messages.get("pamil.apns.gateway.port"));
		
		String key = Messages.get("pamil.apns.key.path.sandbox");
		String pw = Messages.get("pamil.apns.key.pw.sandbox");
		String sec = Messages.get("pamil.apns.sec");
		String gateway_host = Messages.get("pamil.apns.gateway.host.sandbox");
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
        payload.addBadge(badge);
        payload.addSound("default");
        payload.addCustomDictionary("id", "1");
        
        BasicDevice device = new BasicDevice(token);
        device.setDeviceId("iPhone");
        
        //push messages
        Logger.debug (payload.toString());
        
        AppleNotificationServer appleNoti = 
        		new AppleNotificationServerBasicImpl(key, pw,sec,gateway_host,gateway_port);
        pushManager.initializeConnection(appleNoti);
        
        //send push(プッシュの件数が多くなる場合を想定して、１コネクトあたり、複数のプッシュができるように改修が必要）
        PushedNotification pushed = pushManager.sendNotification(device, payload);
        
        
		if (pushed.isSuccessful()) {
			/* Apple accepted the notification and should deliver it */  
			Logger.debug("Push notification sent successfully to: " +
			pushed.getDevice().getToken());
		/* Still need to query the Feedback Service regularly */  
		} else {
			String invalidToken = pushed.getDevice().getToken();
			Logger.warn ("Push notification sent failed to : " + invalidToken);
			/* Add code here to remove invalidToken from your database */
			
            /* Find out more about what the problem was */  
            Exception theProblem = pushed.getException();
            theProblem.printStackTrace();

            /* If the problem was an error-response packet returned by Apple, get it */  
            ResponsePacket theErrorResponse = pushed.getResponse();
            if (theErrorResponse != null) {
            	Logger.debug (theErrorResponse.getMessage());
            }
		}
        
		LinkedList<Device> devices = feedback();
		for ( Device dev : devices) {
			Logger.debug ("dev.getToken() : " + dev.getToken());
			Logger.debug("dev.getDeviceId() : " + dev.getDeviceId());
		}
        
        return true;
	}
	
	public static LinkedList<Device> feedback () throws Exception {
		String key = Messages.get("pamil.apns.key.path.real");
		String pw = Messages.get("pamil.apns.key.pw.real");
		String sec = Messages.get("pamil.apns.sec");
		String feedback_host = Messages.get("pamil.apns.feedback.host.real");
		int feedback_port = Integer.parseInt(Messages.get("pamil.apns.feedback.port"));
		
		FeedbackServiceManager feedbackManager = new FeedbackServiceManager();
		AppleFeedbackServerBasicImpl server = new AppleFeedbackServerBasicImpl(key, pw, sec,feedback_host,feedback_port);
		
		return feedbackManager.getDevices(server);
	}
}
