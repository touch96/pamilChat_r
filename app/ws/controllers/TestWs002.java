package ws.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.apache.commons.codec.binary.Base64;

import pc.ws.test.views.html.wsTest02;
import pc.wsapi.constants.Constants;
import play.libs.Json;
import play.libs.F.Callback;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import sun.misc.BASE64Decoder;

public class TestWs002 extends Controller {
	
    public static Result _index() {
    	return ok(wsTest02.render());
    }

    
    public static WebSocket<JsonNode> upload () {
    	return new WebSocket<JsonNode>() {
    		
    		//handshake
			@Override
			public void onReady(play.mvc.WebSocket.In<JsonNode> in,
					play.mvc.WebSocket.Out<JsonNode> out) {
				
				in.onMessage(new Callback_(out));
				
			}
			
			
    		
    	};
    }
    
    
    public static class Callback_ implements Callback<JsonNode> {
        private WebSocket.Out<JsonNode> out;
        public Callback_(WebSocket.Out<JsonNode> out) {
            this.out = out;
        }
        @Override
        public void invoke(JsonNode event) throws Throwable {
            System.out.println("invoke!!");
            
            
            String fileStr = event.get("data").get("file").asText();
            fileStr = fileStr.substring(fileStr.indexOf(Constants.BASE64) + Constants.BASE64.length());
 //           BASE64Decoder decoder = new BASE64Decoder();
            
//            byte[] file = decoder.decodeBuffer(fileStr);
//            System.out.println("file length : " + file.length);
//            
//            File img  = new File("/Users/JP10844/","02Seeee.jpg");
//            FileOutputStream _out = new FileOutputStream("/Users/JP10844/398660229990_.jpg");
//            BufferedOutputStream outBuf = new BufferedOutputStream(_out);
//            outBuf.write(file);
            
            try {
            	

            	
    			//==================================//
    			//	Base64デコード	         //
    			//==================================//
    			byte[] outdata = Base64.decodeBase64(fileStr.getBytes());
    			//==================================//
    			//	結果書き出し	         //
    			//==================================//
    			File outf = new File("/Users/JP10844/398660229990_.jpg");
    			FileOutputStream fo = new FileOutputStream(outf);
    			fo.write(outdata);
    			fo.close();
            } catch (Exception e) {
            	e.printStackTrace();
            }
            
            
//            if (event.get("data").get("file").isBinary()) {
//                byte[] file = event.get("data").get("file").getBinaryValue();
//                System.out.println("file length : " + file.length);
//                
//            	System.out.println("binary");
//            } else if (event.get("data").get("file").isArray())  {
//            	System.out.println("array");
//            }
            ObjectNode echo = Json.newObject();
            echo.put("text", "[return]通信ok");
            out.write(echo);
        }
    }
    
}
