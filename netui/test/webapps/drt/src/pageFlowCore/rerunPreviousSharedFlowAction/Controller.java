package pageFlowCore.rerunPreviousSharedFlowAction;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin",  path="index.jsp")
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf", type=SharedFlow.class)
    }
)
public class Controller extends PageFlowController
{
}
