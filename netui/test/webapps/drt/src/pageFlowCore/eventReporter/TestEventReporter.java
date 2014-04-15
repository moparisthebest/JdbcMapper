package pageFlowCore.eventReporter;

import org.apache.beehive.netui.pageflow.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.config.ModuleConfig;
import java.util.*;

/**
 * Used in the EventReporter test.
 */
public class TestEventReporter
    extends DefaultPageFlowEventReporter
{
    private static final String HISTORY_ATTR = TestEventReporter.class.getName() + ":history";

    public TestEventReporter( ServletContext servletContext )
    {
        super( servletContext );
    }

    public void actionRaised( RequestContext requestContext, FlowController flowController, ActionMapping mapping,
                              ActionForm form )
                              
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "actionRaised" );
        msg.addParam( "FlowController", flowController );
        msg.addParam( "ActionMapping", mapping );
        msg.addParam( "ActionForm", form );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void actionSuccess( RequestContext requestContext, FlowController flowController, ActionMapping mapping,
                               ActionForm form, ActionForward result, long timeTakenMillis )
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "actionSuccess" );
        msg.addParam( "FlowController", flowController );
        msg.addParam( "ActionMapping", mapping );
        msg.addParam( "ActionForm", form );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        msg.addParam( "ActionForward", result );
        msg.addParam( "TimeTakenMillis", new Long( timeTakenMillis ) );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void exceptionRaised( RequestContext requestContext, Throwable ex, ActionMapping actionMapping,
                                 ActionForm form, FlowController flowController )
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "exceptionRaised" );
        msg.addParam( "Throwable", ex );
        msg.addParam( "ActionMapping", actionMapping );
        msg.addParam( "ActionForm", form );
        msg.addParam( "FlowController", flowController );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void exceptionHandled( RequestContext requestContext, Throwable ex, ActionMapping actionMapping,
                                  ActionForm form, FlowController flowController, ActionForward result,
                                  long timeTakenMillis )
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "exceptionHandled" );
        msg.addParam( "Throwable", ex );
        msg.addParam( "ActionMapping", actionMapping );
        msg.addParam( "ActionForm", form );
        msg.addParam( "FlowController", flowController );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        msg.addParam( "ActionForward", result );
        msg.addParam( "TimeTakenMillis", new Long( timeTakenMillis ) );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void flowControllerCreated( RequestContext requestContext, FlowController flowController )
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "flowControllerCreated" );
        msg.addParam( "FlowController", flowController );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void flowControllerDestroyed( FlowController flowController, Object storageLocation )
    {
        if ( flowController.getURI().startsWith( "/pageFlowCore/eventReporter" ) )
        {
            LogMsg2 msg = new LogMsg2( "flowControllerDestroyed" );
            msg.addParam( "FlowController", flowController );
            msg.addParam( "StorageLocation", storageLocation );
            getHistory( ( HttpSession ) storageLocation ).add( msg.toString() );
        }
    }

    public void beginActionRequest( RequestContext requestContext )
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "beginActionRequest" );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void endActionRequest( RequestContext requestContext, long timeTakenMillis )
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "endActionRequest" );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        msg.addParam( "TimeTakenMillis", new Long( timeTakenMillis ) );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void beginPageRequest( RequestContext requestContext )
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "beginPageRequest" );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void endPageRequest( RequestContext requestContext, long timeTakenMillis )
    {
        if ( ! interestingRequest( requestContext ) ) return;
        LogMsg2 msg = new LogMsg2( "endPageRequest" );
        msg.addParam( "Request", requestContext.getRequest() );
        msg.addParam( "Response", requestContext.getResponse() );
        msg.addParam( "TimeTakenMillis", new Long( timeTakenMillis ) );
        getHistory( requestContext ).add( msg.toString() );
    }

    public void flowControllerRegistered( String modulePath, String controllerClassName, ModuleConfig moduleConfig )
    {
        if ( modulePath.equals( "/pageFlowCore/eventReporter" ) )
        {
            getServletContext().setAttribute( "pageFlowCore.eventReporter_registrationMessage",
                                              "Controller class is " + controllerClassName );
        }
    }

    private static boolean interestingRequest( RequestContext requestContext )
    {
        String servletPath = ( ( HttpServletRequest ) requestContext.getRequest() ).getServletPath();
        return servletPath.startsWith( "/pageFlowCore/eventReporter" ) && servletPath.indexOf( "finish" ) == -1;
    }

    public static List< String > getHistory( RequestContext rc )
    {
        return getHistory( ( ( HttpServletRequest ) rc.getRequest() ).getSession() );
    }

    public static List< String > getHistory( HttpSession session )
    {
        List< String > hist = ( List< String > ) session.getAttribute( HISTORY_ATTR );
        if ( hist == null )
        {
            hist = new ArrayList< String >();
            session.setAttribute( HISTORY_ATTR, hist );
        }
        return hist;
    }

    public static void clearHistory( HttpSession session )
    {
        session.removeAttribute( HISTORY_ATTR );
    }

    private static class LogMsg2 extends LogMsg
    {
        public LogMsg2( String msg )
        {
            super( msg );
        }

        public void addParam( String name, Object value )
        {
            if ( name.equals( "TimeTakenMillis" ) )
            {
                value = "XXX";
            }
            else if ( value instanceof HttpServletRequest )
            {
                value = '{' + ( ( HttpServletRequest ) value ).getRequestURI() + '}';
            }
            else if ( value instanceof HttpServletResponse )
            {
                value = "{response}";
            }
            else if ( value instanceof HttpSession )
            {
                value = "{HTTP session}";
            }
            else if ( value != null )
            {
                // We don't want the "@XXXXX" at the end of toString values for objects that
                // don't define toString().
                value = value.toString().replaceAll( "@[a-f0-9]*", "" );
            }

            super.addParam( name, value );
        }
    }
}
