package miniTests.actionInterceptors.overrideForwards;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="forwardAway", path="error.jsp"),
        @Jpf.SimpleAction(name="forwardToNested", path="index.jsp"),
        @Jpf.SimpleAction(name="forwardToNestedThenAway", path="error.jsp"),
        @Jpf.SimpleAction(name="forwardToNull", path="error.jsp")
    }
)
public class Controller extends PageFlowController
{
}
