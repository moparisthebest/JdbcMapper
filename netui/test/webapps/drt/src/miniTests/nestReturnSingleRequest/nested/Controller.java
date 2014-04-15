package miniTests.nestReturnSingleRequest.nested;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Jpf.Controller(
    nested=true
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="done", returnAction="nestedDone", outputFormBeanType=String.class)
        }
    )
    public Forward begin()
        throws InterruptedException
    {
        return new Forward( "done", "GOT IT!" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="done", returnAction="nestedDone", outputFormBeanType=String.class)
        }
    )
    public Forward beginWithDelay()
        throws InterruptedException
    {
        Thread.sleep( 3000 );
        return new Forward( "done", "GOT IT!" );
    }

    protected void onCreate()
    {
        addMessage( getRequest().getSession(), "in onCreate" );
    }

    protected void onDestroy( HttpSession session )
    {
        addMessage( session, "in onDestroy" );
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
