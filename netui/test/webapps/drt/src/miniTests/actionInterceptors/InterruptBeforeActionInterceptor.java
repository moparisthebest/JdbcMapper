package miniTests.actionInterceptors;

import org.apache.beehive.netui.pageflow.interceptor.action.*;
import org.apache.beehive.netui.pageflow.interceptor.*;
import java.net.*;
import java.util.*;

public class InterruptBeforeActionInterceptor extends BaseInterceptor
{
    public void preAction( ActionInterceptorContext context, InterceptorChain chain )
            throws InterceptorException
    {
        addMessage( context, "in preAction() in " + getClass().getName() );
        addMessage( context, getClass().getName() + ": interrupting chain" );
    }
}
