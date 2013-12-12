package pc.wsapi.biz;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.codehaus.jackson.JsonNode;

public abstract class AbstractBiz {
	protected EntityManagerFactory emf;
	
	public EntityManager getEm () {
		emf = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
		return emf.createEntityManager();
	}
	
	public abstract void execute (play.mvc.WebSocket.In<JsonNode> in, play.mvc.WebSocket.Out<JsonNode> out);
	public abstract JsonNode execute (Map<String, String[]> req_form);
	
	
	
	protected static final String rtn = "rtn";
	
	protected static final String ok = "ok";
	protected static final String ng = "ng";
	protected static final String error = "error";
	protected static final String msg = "msg";
	protected static final String messageList = "messageList";
	protected static final String friendsList = "friendsList";
	protected static final String image = "image";
	protected static final String sec = "sec";
}
