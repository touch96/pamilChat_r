package pc.wsapi.utils;

import java.util.HashMap;

public class Returns {
	private String code;
	private HashMap<String, Object> params;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public HashMap<String, Object> getParams() {
		return params;
	}
	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
	
	
}
