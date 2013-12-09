package pc.core;

import java.util.Map;
import java.util.Map.Entry;

import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class LogMeAction extends Action<LogMe>
{
    @Override
    public Result call(Http.Context context) throws Throwable
    {
        Logger.debug("request().path()     : " + context.request().path());
        
        if(Logger.isDebugEnabled()) {
            Map<String, String[]> map = context.request().body().asFormUrlEncoded();
            
            if (map != null) {
            	for(Entry<String, String[]> entry : map.entrySet()){
            		String[] values = entry.getValue();
            		for (String value : values) {
            			Logger.debug("value["+entry.getKey()+"] : " + value);
            		}
            	}
            }
        }
        
        return delegate.call(context);
    }
    
    @Override
    protected void finalize() throws Throwable {
    	Logger.debug("MyLogger.finalize");
    	super.finalize();
    }
    
    
}
