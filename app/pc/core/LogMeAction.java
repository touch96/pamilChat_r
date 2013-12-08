package pc.core;

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
        return delegate.call(context);
    }
    
    @Override
    protected void finalize() throws Throwable {
    	Logger.debug("MyLogger.finalize");
    	super.finalize();
    }
    
    
}
