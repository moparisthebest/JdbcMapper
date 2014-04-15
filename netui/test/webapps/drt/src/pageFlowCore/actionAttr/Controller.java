package pageFlowCore.actionAttr;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="simpleAction", action="theAction"),
        @Jpf.SimpleAction(
            name="conditionalForward",
            path="index.jsp",
            conditionalForwards={
                @Jpf.ConditionalForward(condition="${true}", action="theAction")
            }
        ),
        @Jpf.SimpleAction(name="theAction", path="success.jsp"),
        @Jpf.SimpleAction(name="toSharedFlowAction", action="sf.sharedFlowAction"),
        @Jpf.SimpleAction(name="toAnotherPageFlowAction", action="/pageFlowCore/actionAttr/anotherPageFlow/someAction")
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf", type=SharedFlow.class)
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="theAction", action="theAction")
        }
    )
    public Forward forward()
    {
        return new Forward( "theAction" );
    }
}
