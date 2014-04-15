package miniTests.actionInterceptors.interceptedAfterAction;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="another", path="another.jsp")
    }
)
public class Controller extends PageFlowController
{
    protected void beforeAction()
    {
        getRequest().setAttribute( "actionRun" , Boolean.TRUE );
    }
}
