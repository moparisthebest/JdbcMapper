package pageFlowCore.rerunPreviousSharedFlowAction;

import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="sharedAction", path="success.jsp"),
        @Jpf.SimpleAction(name="prevAction", navigateTo=Jpf.NavigateTo.previousAction)
    }
)
public class SharedFlow extends SharedFlowController
{
}
