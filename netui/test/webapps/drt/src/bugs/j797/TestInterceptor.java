package bugs.j797;

import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptorContext;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptor;
import org.apache.beehive.netui.pageflow.interceptor.action.AfterNestedInterceptContext;
import org.apache.beehive.netui.pageflow.interceptor.action.InterceptorForward;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorException;
import org.apache.beehive.netui.pageflow.interceptor.InterceptorChain;

public class TestInterceptor
    extends ActionInterceptor
{
    /**
     * Return a URI to a nested page flow, which will be "injected" before the desired action is run.
     */
    public void preAction(ActionInterceptorContext context, InterceptorChain chain)
            throws InterceptorException
    {
        InterceptorForward fwd = new InterceptorForward( "/bugs/j797/nested/Controller.jpf" );
        setOverrideForward( fwd, context );
        chain.continueChain();
    }

    /**
     * This is called after our "injected" nested page flow is done, and before the original desired
     * action is run.
     */
    public void afterNestedIntercept(AfterNestedInterceptContext context) throws InterceptorException
    {
        context.getOriginalForward().setRestoreQueryString( false );
    }

    public void postAction(ActionInterceptorContext context, InterceptorChain chain)
            throws InterceptorException
    {
        chain.continueChain();
    }

}
