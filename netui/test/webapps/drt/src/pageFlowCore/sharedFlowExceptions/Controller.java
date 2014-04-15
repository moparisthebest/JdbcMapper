package pageFlowCore.sharedFlowExceptions;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf1", type=pageFlowCore.sharedFlowExceptions.sharedFlow1.SharedFlow.class),
        @Jpf.SharedFlowRef(name="sf2", type=pageFlowCore.sharedFlowExceptions.sharedFlow2.SharedFlow.class)
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action
    public Forward throwSharedFlow1Exception() throws Exception
    {
        throw new pageFlowCore.sharedFlowExceptions.sharedFlow1.SharedFlow.Ex();
    }

    @Jpf.Action
    public Forward throwSharedFlow2Exception() throws Exception
    {
        throw new NullPointerException();
    }
}
