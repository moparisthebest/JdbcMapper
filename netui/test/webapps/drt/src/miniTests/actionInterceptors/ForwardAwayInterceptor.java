package miniTests.actionInterceptors;

import org.apache.beehive.netui.pageflow.interceptor.action.*;
import org.apache.beehive.netui.pageflow.interceptor.*;
import java.net.*;
import java.util.*;

public class ForwardAwayInterceptor extends ActionInterceptor
{
    public void preAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        try
        {
            setOverrideForward( new InterceptorForward( new URI( "/miniTests/actionInterceptors/overrideForwards/alternate.jsp" ) ), context );
        }
        catch ( URISyntaxException e )
        {
            throw new InterceptorException( e );
        }
    }

    public void postAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        chain.continueChain();
    }
    
    public void afterNestedIntercept( AfterNestedInterceptContext context ) throws InterceptorException
    {
    }
}
