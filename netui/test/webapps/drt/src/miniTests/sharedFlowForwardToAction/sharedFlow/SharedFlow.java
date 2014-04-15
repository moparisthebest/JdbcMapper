package miniTests.sharedFlowForwardToAction.sharedFlow;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="action1", path="action2.do")
    }
)
public class SharedFlow extends SharedFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="back", navigateTo=Jpf.NavigateTo.currentPage)
        }
    )
    public Forward action2()
    {
        getRequest().setAttribute( "message", "in " + getDisplayName() + ".action2()" );
        return new Forward( "back" );
    }
}
