package miniTests.actionInterceptors.globalInterceptors;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="interceptAndNest", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
}
