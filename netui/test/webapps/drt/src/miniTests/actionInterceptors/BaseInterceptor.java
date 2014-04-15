package miniTests.actionInterceptors;

import org.apache.beehive.netui.pageflow.interceptor.action.*;
import org.apache.beehive.netui.pageflow.interceptor.*;
import java.net.*;
import java.util.*;

public class BaseInterceptor extends ActionInterceptor
{
    public void preAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        addMessage( context, "in preAction() in " + getClass().getName() );
        chain.continueChain();
    }

    public void postAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        addMessage( context, "in postAction() in " + getClass().getName() );
        chain.continueChain();
    }
    
    public void afterNestedIntercept( AfterNestedInterceptContext context ) throws InterceptorException
    {
        addMessage( context, "in afterNestedIntercept() in " + getClass().getName() );
    }

    protected static void addMessage( ActionInterceptorContext context, String msg )
    {
        ArrayList messages = ( ArrayList ) context.getRequest().getAttribute( "messages" );
        if ( messages == null )
        {
            messages = new ArrayList();
            context.getRequest().setAttribute( "messages", messages );
        }
        messages.add( msg );
    }
}
