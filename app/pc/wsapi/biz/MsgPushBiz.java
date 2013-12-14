package pc.wsapi.biz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import org.codehaus.jackson.JsonNode;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;


import pc.wsapi.beans.PushBean;
import pc.wsapi.dbs.Msghistory;
import pc.wsapi.dbs.Users;
import pc.wsapi.dbs.sqlbean.MsgHistorySQL;
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
	
	public JsonNode testPush (Map<String, String[]> form) throws Exception {
		Logger.debug ("MsgPushBiz - testPush");
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		
		String token = form.get("token")[0];
		String send_code = form.get("send_code")[0];
		String target = form.get("target")[0];
		String message = form.get("message")[0];
		
		
		//友達にプッシュ
		PushUtil.push(token, send_code + "さんから"+target+"さんへのメッセージ： " + message , 1);
		
		//returnコード
		params.put(msg, "ok");
		result = JsonUtil.setRtn(ok, params);
		return result;
	}
	/**
	 * 
	 * @param form
	 * @return
	 */
	public JsonNode sendMessage (String url , MultipartFormData form) {
		Logger.debug ("MsgPushBiz - sendMessage");
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		
		if (form == null) {
			params.put(msg, "no parameter");
			result = JsonUtil.setRtn(ng, params);
			return result;
		}
		
//		String[] token = form.asFormUrlEncoded().get("token");
		String token = "";
		String[] send_code = form.asFormUrlEncoded().get("send_code");
		String[] targets = form.asFormUrlEncoded().get("target");
		String[] sec = form.asFormUrlEncoded().get("sec");
		FilePart image = form.getFile("image");
		int badge = 1;
		
		if (send_code == null) {
			params.put(msg, "no send_code");
			result = JsonUtil.setRtn(ng, params);
		}
		if (targets == null) {
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
		
		//ファイル保存
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        String fileName = RandomStringUtils.randomAlphabetic(15)+"_"+send_code[0]+sdf1.format(new Date())+image.getFilename();
//        String contentType = image.getContentType();
        File file = image.getFile();
        
        String filePath = Messages.get("pamil.file.path")+send_code[0]+sdf1.format(new Date());
        String fileUrl = Messages.get("pamil.file.url")+send_code[0]+sdf1.format(new Date());
        
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
    	
    	ArrayList<PushBean> pushList = new ArrayList<PushBean>();
		for (String target : targets) {
			PushBean pushBean = new PushBean();
			//user存在チェック
			Finder<Long, Users> finder = new Finder<Long, Users>(Long.class, Users.class);
			Query<Users> query = finder.where("code='"+target+"'");
			Users users = query.findUnique();
			
			if (users.token == null) {
				params.put(msg, "no recieve user");
				result = JsonUtil.setRtn(ng, params);
			}
			
			if (result.size() > 0) {
				return result;
			}
			
			token = users.token;
			try {
				
				//dbへ内容格納
				Msghistory msghistory = new Msghistory();
				msghistory.target = target;
				msghistory.send_code = send_code[0];
				msghistory.type = "0";
				msghistory.sec = Integer.parseInt(sec[0]);
				msghistory.img = url;
				msghistory.save();
				
				Query<Msghistory> queryfr = Msghistory.find.where("target= ? and type= '0'");
				queryfr.setParameter(1, target);
				List<Msghistory> msgList = queryfr.findList();
				
				if (msgList != null && msgList.size() > 0) {
					badge = msgList.size() + 1;
				}
				
				pushBean.setBadge(badge);
				pushBean.setMsg("you have message from " + send_code[0]);
				pushBean.setToken(token);
				
				pushList.add(pushBean);
			} catch (Exception e) {
				e.printStackTrace();
				params.put(msg, e.fillInStackTrace());
				result = JsonUtil.setRtn(error, params);
				
			}

		}
		
		try {
			//友達にプッシュ
			PushUtil.pushs(pushList);
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
//		Query<Msghistory> query = Msghistory.find.where(" (target='"+code+"' or send_code='"+code+"')");
		RawSql rsql = 
				RawSqlBuilder.
				unparsed(
						"select m.msghistoryseq, m.send_code, m.target, m.img, m.sec, " +
						"case when m.send_code = ? then '0' else m.type end type , m.createdt " +
						"from msghistory m " +
						"where " +
						"m.target = ? or m.send_code = ? order by m.createdt desc " )
				.columnMapping("m.msghistoryseq", "msghistoryseq")
				.columnMapping("m.send_code", "send_code")
				.columnMapping("m.target", "target")
				.columnMapping("m.img", "img")
				.columnMapping("m.sec", "sec")
				.columnMapping("m.type", "type")
				.columnMapping("m.createdt", "createdt")
				.create();
		Query<MsgHistorySQL> query = Ebean.find(MsgHistorySQL.class);
		query.setRawSql(rsql);
		query.setParameter(1, code);
		query.setParameter(2, code);
		query.setParameter(3, code);

		List<MsgHistorySQL> msgList = query.findList();
		
		
		if (msgList != null && msgList.size() > 0) {
			params.put(msg, "have message");
			params.put(messageList, msgList);
			result = JsonUtil.setRtn(ok, params);
			
			Query<Msghistory> query2 = Msghistory.find.where(" target= ? and type='0' ");
			query2.setParameter(1, code);
			
			List<Msghistory> list = query2.findList();
			for (Msghistory msghistory : list) {
				msghistory.type = "1";
				msghistory.update();
			}
		} else {
			params.put(msg, "no message");
			result = JsonUtil.setRtn(ok, params);
		}

		return result;
	}
	
	/**
	 * 
	 * @param form
	 * @return
	 */
	public JsonNode updateReadStatus (Map<String, String[]> form) {
		Logger.debug ("MsgPushBiz - getNoreadMsgList");
		JsonNode result = Json.newObject();
		HashMap<String, Object> params = new HashMap<>();
		String code = form.get("code")[0];
		int msghistoryseq = Integer.parseInt(form.get("msghistoryseq")[0]);
		
		Query<Msghistory> query = Msghistory.find.where("msghistoryseq = ? and target = ?");
		query.setParameter(1, msghistoryseq);
		query.setParameter(2, code);
		
		Msghistory msghistory = query.findUnique();
		if (msghistory != null && msghistory.type != null) {
			msghistory.type = "2";
			msghistory.update();
			
			params.put(msg, "image read");
			result = JsonUtil.setRtn(ok, params);
			
		} else {
			params.put(ng, "no read");
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
