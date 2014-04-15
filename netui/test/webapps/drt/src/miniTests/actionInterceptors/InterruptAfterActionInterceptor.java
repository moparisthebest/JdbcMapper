package miniTests.actionInterceptors;

import org.apache.beehive.netui.pageflow.interceptor.action.*;
import org.apache.beehive.netui.pageflow.interceptor.*;
import java.net.*;
import java.util.*;

public class InterruptAfterActionInterceptor extends BaseInterceptor
{
    public void postAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        addMessage( context, "in postAction() in " + getClass().getName() );
        addMessage( context, getClass().getName() + ": interrupting chain" );
    }
}
