package miniTests.actionInterceptors;

import org.apache.beehive.netui.pageflow.interceptor.action.*;
import org.apache.beehive.netui.pageflow.interceptor.*;
import java.net.*;
import java.util.*;
import java.io.IOException;

public class ForwardToNullInterceptor extends ActionInterceptor
{
    public void preAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        setOverrideForward( null, context );

        try
        {
            context.getResponse().getWriter().println( "Action interceptor " + getClass().getName() + " overrode the forward to null, then wrote this message to the response." );
        }
        catch ( IOException e )
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
