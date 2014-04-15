package miniTests.actionInterceptors.interruptChain;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.util.ArrayList;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="interruptChainBeforeAction", path="index.jsp"),
        @Jpf.SimpleAction(name="interruptChainAfterAction", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    protected void beforeAction()
    {
        ArrayList messages = ( ArrayList ) getRequest().getAttribute( "messages" );
        if ( messages != null ) messages.add( "in action " + getCurrentActionName() );
    }
}
