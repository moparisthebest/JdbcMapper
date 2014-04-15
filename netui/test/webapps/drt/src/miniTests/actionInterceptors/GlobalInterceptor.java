package miniTests.actionInterceptors;

import org.apache.beehive.netui.pageflow.interceptor.action.*;
import org.apache.beehive.netui.pageflow.interceptor.*;
import java.net.*;
import java.util.*;

public class GlobalInterceptor extends BaseInterceptor
{
    private String _prop1;
    private String _prop2;
    
    public void init( InterceptorConfig config )
    {
        _prop1 = config.getCustomProperty( "prop1" );
        _prop2 = config.getCustomProperty( "prop2" );
    }

    public void preAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        if ( context.getPageFlow().getURI().equals( "/miniTests/actionInterceptors/globalInterceptors/Controller.jpf" ) )
        {
            addMessage( context, "in preAction()" );

            if ( context.getActionName().equals( "interceptAndNest" ) )
            {
                try
                {
                    setOverrideForward( new InterceptorForward( new URI( "/miniTests/actionInterceptors/nested/Controller.jpf" ) ), context );
                }
                catch ( URISyntaxException e )
                {
                    throw new InterceptorException( e );
                }
            }
        }

        chain.continueChain();
    }

    public void postAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        if ( context.getPageFlow().getURI().equals( "/miniTests/actionInterceptors/globalInterceptors/Controller.jpf" ) )
        {
            addMessage( context, "in postAction()" );
            addMessage( context, "custom property 'prop1' is " + _prop1 );
            addMessage( context, "custom property 'prop2' is " + _prop2 );
        }

        chain.continueChain();
    }
    
    public void afterNestedIntercept( AfterNestedInterceptContext context ) throws InterceptorException
    {
        if ( context.getPageFlow().getURI().equals( "/miniTests/actionInterceptors/globalInterceptors/Controller.jpf" ) )
        {
            addMessage( context, "in afterNestedIntercept()" );
        }
    }
}
