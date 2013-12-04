import javapns.Push;


public class PushTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Push.alert("Hello World!", "/usr/local/keys/pamilchatserverkey.p12", "1234", false, "4a88decf09e2538310d4f114bc8681e7f1bcf67f646d14268b5c14b47207ba61");
	}

}
