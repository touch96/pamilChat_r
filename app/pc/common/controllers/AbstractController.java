package pc.common.controllers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.libs.F.Callback;
import play.mvc.Controller;
import play.mvc.WebSocket;


public abstract class AbstractController extends Controller {
	
//	protected boolean certification () {
//		
//	}
	
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

}
