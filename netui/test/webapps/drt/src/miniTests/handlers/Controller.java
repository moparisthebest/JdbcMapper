package miniTests.handlers;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowStack;
import org.apache.beehive.netui.pageflow.PreviousPageInfo;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.handler.ActionForwardHandler;
import org.apache.beehive.netui.pageflow.handler.BaseHandler;
import org.apache.beehive.netui.pageflow.handler.ExceptionsHandler;
import org.apache.beehive.netui.pageflow.handler.FlowControllerHandlerContext;
import org.apache.beehive.netui.pageflow.handler.Handlers;
import org.apache.beehive.netui.pageflow.handler.LoginHandler;
import org.apache.beehive.netui.pageflow.interceptor.action.ActionInterceptor;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.ExceptionConfig;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;


@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="roleRequiredAction", path="index.jsp", rolesAllowed={ "HandlersTest" } )
    }
)
public class Controller extends PageFlowController
{
    public String getActionForwardHandler()
    {
        return Handlers.get( getServletContext() ).getActionForwardHandler().toString();
    }

    public String getExceptionsHandler()
    {
        return Handlers.get( getServletContext() ).getExceptionsHandler().toString();
    }

    public String getLoginHandler()
    {
        return Handlers.get( getServletContext() ).getLoginHandler().toString();
    }

    public static class OverrideActionForwardHandler
        extends BaseHandler
        implements ActionForwardHandler
    {
        public ActionForward processForward(FlowControllerHandlerContext context, ActionForward fwd, ActionMapping mapping, 
                                            ExceptionConfig exceptionConfig, String actionName, ModuleConfig altModuleConfig,
                                            ActionForm form)
        {
            return getPreviousActionForwardHandler().processForward( context, fwd, mapping, exceptionConfig, actionName,
                                                                     altModuleConfig, form );
        }

        public ActionForward doAutoViewRender( FlowControllerHandlerContext context, ActionMapping mapping, ActionForm form )
        {
            return getPreviousActionForwardHandler().doAutoViewRender( context, mapping, form );
        }

        public ActionForward doReturnToPage( FlowControllerHandlerContext context, PreviousPageInfo prevPageInfo,
                                      PageFlowController currentPageFlow, ActionForm currentForm,
                                      String actionName, Forward pageFlowFwd )
        {
            return getPreviousActionForwardHandler().doReturnToPage( context, prevPageInfo, currentPageFlow, currentForm, actionName, pageFlowFwd );
        }

        public ActionForward doReturnToAction( FlowControllerHandlerContext context, String actionName, Forward pageFlowFwd )
        {
            return getPreviousActionForwardHandler().doReturnToAction( context, actionName, pageFlowFwd );
        }

        public ActionForward doNestingReturn( FlowControllerHandlerContext context, Forward pageFlowFwd, ActionMapping mapping,
                                       ActionForm form )
        {
            return getPreviousActionForwardHandler().doNestingReturn( context, pageFlowFwd, mapping, form );
        }

        public ActionForward handleInterceptorReturn( FlowControllerHandlerContext context, PageFlowController poppedPageFlow,
                                               PageFlowStack.PushedPageFlow pushedPageFlowWrapper, String returnAction,
                                               ActionMapping actionMapping, ActionForm form, ActionInterceptor interceptor )
        {
            return getPreviousActionForwardHandler().handleInterceptorReturn( context, poppedPageFlow, pushedPageFlowWrapper, returnAction,
                                                                 actionMapping, form, interceptor );
        }

        protected ActionForwardHandler getPreviousActionForwardHandler()
        {
            return ( ActionForwardHandler ) super.getPreviousHandler();
        }

        public String toString()
        {
            return getClass().getName() + " which is adapting " + getPreviousHandler().getClass().getName();
        }
    }

    public static class OverrideExceptionsHandler
        extends BaseHandler
        implements ExceptionsHandler
    {
        public ActionForward handleException( FlowControllerHandlerContext context, Throwable ex, ActionMapping actionMapping,
                                       ActionForm form )
            throws IOException, ServletException
        {
            return getPreviousExceptionsHandler().handleException( context, ex, actionMapping, form );
        }

        public Throwable unwrapException( FlowControllerHandlerContext context, Throwable ex )
        {
            return getPreviousExceptionsHandler().unwrapException( context, ex );
        }

        public void exposeException( FlowControllerHandlerContext context, Throwable ex, ActionMapping actionMapping )
        {
            getPreviousExceptionsHandler().exposeException( context, ex, actionMapping );
        }

        public boolean eatUnhandledException( FlowControllerHandlerContext context, Throwable ex )
        {
            return getPreviousExceptionsHandler().eatUnhandledException( context, ex );
        }

        protected ExceptionsHandler getPreviousExceptionsHandler()
        {
            return ( ExceptionsHandler ) super.getPreviousHandler();
        }

        public String toString()
        {
            return getClass().getName() + " which is adapting " + getPreviousHandler().getClass().getName();
        }
    }

    public static class OverrideLoginHandler1
        extends BaseHandler
        implements LoginHandler
    {
        public void login( FlowControllerHandlerContext context, String username, String password )
            throws LoginException
        {
            getPreviousLoginHandler().login( context, username, password );
        }
        
        public void logout( FlowControllerHandlerContext context, boolean invalidateSessions )
        {
            getPreviousLoginHandler().logout( context, invalidateSessions );
        }
        
        public boolean isUserInRole( FlowControllerHandlerContext context, String roleName )
        {
            return getPreviousLoginHandler().isUserInRole( context, roleName );
        }
        
        public Principal getUserPrincipal( FlowControllerHandlerContext context )
        {
            return getPreviousLoginHandler().getUserPrincipal( context );
        }

        protected LoginHandler getPreviousLoginHandler()
        {
            return ( LoginHandler ) super.getPreviousHandler();
        }

        public String toString()
        {
            return getClass().getName() + " which is adapting " + getPreviousHandler().getClass().getName();
        }
    }

    public static class OverrideLoginHandler2 extends OverrideLoginHandler1
    {
        public boolean isUserInRole( FlowControllerHandlerContext context, String roleName )
        {
            if ( roleName.equals( "HandlersTest" ) ) return true;
            return getPreviousLoginHandler().isUserInRole( context, roleName );
        }

        public Principal getUserPrincipal( FlowControllerHandlerContext context )
        {
            if ( ( ( HttpServletRequest ) context.getRequest() ).getServletPath().startsWith( "/miniTests/handlers/" ) )
            {
                return new Principal(){ public String getName() { return "handler-temp-user"; } };
            }

            return getPreviousLoginHandler().getUserPrincipal( context );
        }

        public String toString()
        {
            return getClass().getName() + " which is adapting " + getPreviousHandler().toString();
        }
    }
}
