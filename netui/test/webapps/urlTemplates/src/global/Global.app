package global;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    catches={
       @Jpf.Catch(type=java.lang.Exception.class, method="handleException"),
       @Jpf.Catch(type=PageFlowException.class, method="handlePageFlowException")
    }
)
public class Global extends GlobalApp
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="home", path="/index.jsp")
        }
    )
    public Forward home()
    {
        return new Forward( "home" );
    }

    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="errorPage", path="/error.jsp")
        }
    )
    protected Forward handleException( Exception ex, String actionName,
                                       String message, Object form )
    {
        System.err.print( "[" + getRequest().getContextPath() + "] " );
        System.err.println( "Unhandled exception caught in Global.app:" );
        ex.printStackTrace();
        return new Forward( "errorPage" );
    }

    @Jpf.ExceptionHandler
    public Forward handlePageFlowException( PageFlowException ex, String actionName,
                                            String message, Object form ) 
        throws java.io.IOException
    { 
        ex.sendError( getRequest(), getResponse() ); 
        return null; 
    } 
}
