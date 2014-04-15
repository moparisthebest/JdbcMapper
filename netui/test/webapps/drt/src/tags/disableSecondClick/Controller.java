package tags.disableSecondClick;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    private static int count = 0;

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward someAction()
        throws InterruptedException
    {
        System.err.println( "****** in someAction " + ++count );
        Thread.sleep( 3000 );
        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward anotherAction()
        throws InterruptedException
    {
        System.err.println( "****** in anotherAction " + ++count );
        Thread.sleep( 3000 );
        return new Forward( "index" );
    }
}
