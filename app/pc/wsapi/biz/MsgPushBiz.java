package pc.wsapi.biz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import org.codehaus.jackson.JsonNode;
import com.avaje.ebean.Query;


import pc.wsapi.dbs.Msghistory;
import pc.wsapi.dbs.Users;
import pc.wsapi.utils.JsonUtil;
import pc.wsapi.utils.PushUtil;
import play.Logger;
import play.db.ebean.Model.Finder;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;

public class MsgPushBiz extends AbstractBiz {

	@Override
	public void execute(In<JsonNode> in, Out<JsonNode> out) {
		

	}
	


	@Override
	public JsonNode execute(Map<String, String[]> form) {
		return null;
	}
	
	/**
	 * 
	 * @param form
	 * @return
	 */
	public  JsonNode sendMessage (String url , MultipartFormData form) {
		Logger.debug ("MsgPushBiz - sendMessage");
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		
		if (form == null) {
			params.put(msg, "no parameter");
			result = JsonUtil.setRtn(ng, params);
			return result;
		}
		
		String[] token = form.asFormUrlEncoded().get("token");
		String[] send_code = form.asFormUrlEncoded().get("send_code");
		String[] target = form.asFormUrlEncoded().get("target");
		String[] sec = form.asFormUrlEncoded().get("sec");
		FilePart image = form.getFile("image");
		int badge = 1;
		
		if (token == null) {
			params.put(msg, "no token");
			result = JsonUtil.setRtn(ng, params);
		}
		if (send_code == null) {
			params.put(msg, "no send_code");
			result = JsonUtil.setRtn(ng, params);
		}
		if (target == null) {
			params.put(msg, "no target");
			result = JsonUtil.setRtn(ng, params);
		}
		if (sec == null) {
			params.put(msg, "no sec");
			result = JsonUtil.setRtn(ng, params);
		}
		
		if (image == null) {
			params.put(msg, "no file");
			result = JsonUtil.setRtn(ng, params);
		}
		
		//user存在チェック
		Finder<Long, Users> finder = new Finder<Long, Users>(Long.class, Users.class);
		Query<Users> query = finder.where("code='"+target[0]+"'");
		Users users = query.findUnique();
		
		if (users.token == null) {
			params.put(msg, "no recieve user");
			result = JsonUtil.setRtn(ng, params);
			
		}
		
		
		if (result.size() > 0) {
			return result;
		}
		
		try {
			//ファイル保存
	        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
	        String fileName = RandomStringUtils.randomAlphabetic(15)+"_"+send_code[0]+target[0]+sdf1.format(new Date())+image.getFilename();
//	        String contentType = image.getContentType();
	        File file = image.getFile();
	        
	        String filePath = Messages.get("pamil.file.path")+send_code[0]+target[0]+sdf1.format(new Date());
	        String fileUrl = Messages.get("pamil.file.url")+send_code[0]+target[0]+sdf1.format(new Date());
	        
	        Logger.debug("filePath : " + filePath);
	        Logger.debug("fileUrl : " + fileUrl);
	        Logger.debug("fileName : " + fileName);
	        
	        File dir = new File(filePath);
	        
	        if (!dir.exists()) {
	        	if (!dir.mkdir()) {
	        		Logger.debug("faild make dir");
	        	}
	        }
	        
        	if (file.renameTo(new File(filePath+"/"+fileName))) {
        		Logger.debug("success file make : " + (filePath+"/"+fileName));
        		url = "http://"+url + fileUrl + "/" + fileName;
        		Logger.debug("url : " + url);
        	} else {
        		//ファイル保存が失敗した場合の実装（エラー画像を表示）
        		Logger.debug("フィアル作成失敗");
        	}
			
			//dbへ内容格納
			Msghistory msghistory = new Msghistory();
			msghistory.target = target[0];
			msghistory.send_code = send_code[0];
			msghistory.type = Msghistory.Constants.type_noread;
			msghistory.sec = Integer.parseInt(sec[0]);
			msghistory.img = url;
			msghistory.save();
			
			Query<Msghistory> queryfr = Msghistory.find.where("target='"+target[0]+"'");
			List<Msghistory> msgList = queryfr.findList();
			
			if (msgList != null && msgList.size() > 0) {
				badge = msgList.size() + 1;
			}
			
			//友達にプッシュ
			PushUtil.push(token[0], "you have message from " + target[0] , badge);
			
			//returnコード
			params.put(msg, "ok");
			result = JsonUtil.setRtn(ok, params);

		} catch (Exception e) {
			e.printStackTrace();
			params.put(msg, e.fillInStackTrace());
			result = JsonUtil.setRtn(error, params);
			
		}
		
		return result;
	}
	
	/**
	 * 未読のメッセージ受信
	 * 
	 * @param form
	 * @return
	 */
	public JsonNode getNoreadMsgList (Map<String, String[]> form) {
		Logger.debug ("MsgPushBiz - getNoreadMsgList");
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		String code = form.get("code")[0];
		
		//メッセージ取得
		Query<Msghistory> query = Msghistory.find.where(" (target='"+code+"' or send_code='"+code+"')");
		List<Msghistory> msgList = query.findList();
		
		
		if (msgList != null && msgList.size() > 0) {
			params.put(msg, "have message");
			params.put(messageList, msgList);
			result = JsonUtil.setRtn(ok, params);
			
			//取得したメッセージを既読にする
			for (Msghistory msghistory : msgList) {
				msghistory.type = Msghistory.Constants.type_read;
				msghistory.update();
			}
		} else {
			params.put(msg, "no message");
			result = JsonUtil.setRtn(ng, params);
		}

		return result;
	}
	
	public File getMessage (Map<String, String[]> form) {
		Logger.debug ("MsgPushBiz - getMessage");
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		String recieve_code = form.get("receive_code")[0];
		int msghistoryseq = Integer.parseInt(form.get("msghistoryseq")[0]);
		
		Query<Msghistory> query = Msghistory.find.where("msghistoryseq='"+msghistoryseq+"' and recieve_code='"+recieve_code+"'");
		Msghistory msghistory = query.findUnique();
		
		if (StringUtils.isNotEmpty(msghistory.getImg())) {
			String imgPath = msghistory.getImg();
			File imgFile = new File(imgPath);
			
			if (imgFile.exists()) {
				return imgFile;
			} else {
				return null;
			}
			
//			params.put(msg, "image send");
//			params.put(image, imgFile);
//			params.put(sec, msghistory.sec);
//			result = JsonUtil.setRtn(ok, params);
		} else {
			return null;
//			params.put(msg, "no image");
//			result = JsonUtil.setRtn(ng, params);
		}
	}
	
	
}
