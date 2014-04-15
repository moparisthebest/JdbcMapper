package miniTests.jspSeesSharedFlow.sharedFlow1;

import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="sfAction1", path="/miniTests/jspSeesSharedFlow/success.jsp"),
        @Jpf.SimpleAction(name="goPrev", navigateTo=Jpf.NavigateTo.previousPage)
    }
)
public class SharedFlow1 extends SharedFlowController
{
}
