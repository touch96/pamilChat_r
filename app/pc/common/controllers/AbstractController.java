package pc.common.controllers;

import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import pc.core.LogMe;
import play.Logger;
import play.api.templates.Html;
import play.libs.Json;
import play.libs.F.Callback;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.WebSocket;
import play.mvc.Result;

@LogMe
public abstract class AbstractController extends Controller {
	
	private static Map<String, String[]> req_params;
	private static MultipartFormData multipartFormData;
	
	
	public static Map<String, String[]> getReq_params() {
		
		req_params = request().body().asFormUrlEncoded();
		
		return req_params;
	}

	public static MultipartFormData getMultipartFormData() {
		
		multipartFormData = request().body().asMultipartFormData();
		
		return multipartFormData;
	}


	protected static class AstractCallback implements Callback<JsonNode> {
        private WebSocket.Out<JsonNode> out;
        public AstractCallback(WebSocket.Out<JsonNode> out) {
            this.out = out;
        }
        
        @Override
        public void invoke(JsonNode event) throws Throwable {
            ObjectNode echo = Json.newObject();
            echo.put("text", "[echo]" + event.get("id").asText());
            out.write(echo);
        }
    }
	
	protected static String currentUrl () {
		Logger.debug("uri : " + request().uri());
		Logger.debug("host : " + request().host());
		Logger.debug("method : " + request().method());
		Logger.debug("path : " + request().path());
		
		return request().host();
	}
	
	protected static Result ok_res (JsonNode jn) {
		
		Logger.debug("remoteAddress[" + request().remoteAddress() + "] :  " + jn.toString());
  
		return ok(jn);
	}
	
	protected static Result ok_res (Html html) {
		
		Logger.debug(html.contentType());
  
		return ok(html);
	}
	
	protected static Result error_res (JsonNode jn) {
		return ok(jn);
	}

}
