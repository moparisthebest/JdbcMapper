package miniTests.sharedFlowForwardToAction;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="shared", type=miniTests.sharedFlowForwardToAction.sharedFlow.SharedFlow.class)
    }
)
public class Controller extends PageFlowController
{
}
