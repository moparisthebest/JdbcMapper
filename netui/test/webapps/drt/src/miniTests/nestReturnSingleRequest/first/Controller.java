package miniTests.nestReturnSingleRequest.first;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="goNested", path="/miniTests/nestReturnSingleRequest/nested/Controller.jpf"),
        @Jpf.SimpleAction(name="goNestedWithDelay", path="/miniTests/nestReturnSingleRequest/nested/beginWithDelay.do")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.jsp")
        }
    )
    public Forward nestedDone( String retVal )
    {
        return new Forward( "success", "retVal", retVal );
    }

    protected void onCreate()
    {
        addMessage( getRequest().getSession(), "in onCreate" );
    }

    protected void onDestroy( HttpSession session )
    {
        addMessage( session, "in onDestroy" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward clearMessages( String retVal )
    {
        getSession().removeAttribute( "messages" );
        return new Forward( "index" );
    }

    private void addMessage( HttpSession session, String msg )
    {
        List messages = ( List ) session.getAttribute( "messages" );

        if ( messages == null )
        {
            messages = new ArrayList();
            session.setAttribute( "messages", messages );
        }

        messages.add( getClass().getName() + ": " + msg );
    }
}
