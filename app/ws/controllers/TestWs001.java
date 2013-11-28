package ws.controllers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.F.Callback;
import play.libs.Json;
import play.mvc.*;
import pc.ws.test.views.html.wsTest01;



public class TestWs001 extends Controller {

    public static Result index() {
        return null;
    }
    
    public static Result wsTest01 () {
    	return ok(wsTest01.render());
    }
    
    public static Result echowebsocketindex() {
        return ok(wsTest01.render());
    }
    
    public static WebSocket<JsonNode> echowebsocketmessage() {
    	
        return new WebSocket<JsonNode>() {
            public void onReady(WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out){
                try { 
                    in.onMessage(new EchoCallback(out));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
    
    
    
    public static class EchoCallback implements Callback<JsonNode> {
        private WebSocket.Out<JsonNode> out;
        public EchoCallback(WebSocket.Out<JsonNode> out) {
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
