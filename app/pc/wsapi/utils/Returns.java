package pc.wsapi.utils;

import java.util.HashMap;

public class Returns {
	private String returnCode;
	private HashMap<String, Object> params;
	
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public HashMap<String, Object> getParams() {
		return params;
	}
	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
	
	
}
