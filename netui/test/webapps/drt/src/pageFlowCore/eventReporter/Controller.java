package pageFlowCore.eventReporter;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.util.internal.ServletUtils;

import java.util.List;

/**
 * Used in the EventReporter test.
 */
@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    protected void onCreate()
    {
    }

    public List< String > getHistory()
    {
        return TestEventReporter.getHistory( getSession() );
    }

    public String getRegistrationMessage()
    {
        return ( String ) getServletContext().getAttribute( "pageFlowCore.eventReporter_registrationMessage" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        catches={
            @Jpf.Catch(type=IllegalStateException.class, path="index.jsp")
        }
    )
    public Forward throwException( MyForm form )
    {
        throw new IllegalStateException( "intentional exception" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward removeMe()
    {
        remove();
        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="finish", path="finish.html")
        }
    )
    public Forward finish()
    {
        remove();
        TestEventReporter.clearHistory( getSession() );

        // Make sure the HTML page isn't allowed to be cached, so that "finish.do" isn't cached
        // (i.e., is always run).
        ServletUtils.preventCache( getResponse() );
        return new Forward( "finish" );
    }

    public static class MyForm implements java.io.Serializable
    {
        private String _foo;

        public String getFoo()
        {
            return _foo;
        }

        public void setFoo( String foo )
        {
            _foo = foo;
        }
    }
}
