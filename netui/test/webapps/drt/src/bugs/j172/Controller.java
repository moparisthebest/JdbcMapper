package bugs.j172;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf", type=bugs.j172.shared.SharedFlow.class)
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action()
    public Forward raiseEx()
    {
        throw new IllegalStateException("hi");
    }
}
