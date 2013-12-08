package pc.core;

import java.lang.reflect.Method;

import play.GlobalSettings;
import play.api.mvc.Handler;
import play.mvc.Action;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;

public class Global extends GlobalSettings {

	@Override
	public Action onRequest(Request arg0, Method arg1) {
		// TODO Auto-generated method stub
		return super.onRequest(arg0, arg1);
	}
	
	@Override
	public Handler onRouteRequest(RequestHeader arg0) {
		// TODO Auto-generated method stub
		return super.onRouteRequest(arg0);
	}
}
