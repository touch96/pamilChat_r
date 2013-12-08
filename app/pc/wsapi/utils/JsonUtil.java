package pc.wsapi.utils;

import java.util.HashMap;

import org.codehaus.jackson.JsonNode;

import play.libs.Json;

public class JsonUtil {
	public static JsonNode setRtn (String code, HashMap<String, Object> params) {
		Returns rtn = new Returns();
		rtn.setReturnCode(code);
		rtn.setParams(params);
		
		return Json.toJson(rtn);
	}
}
