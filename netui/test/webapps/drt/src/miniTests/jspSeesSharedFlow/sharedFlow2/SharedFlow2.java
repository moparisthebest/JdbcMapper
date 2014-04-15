package miniTests.jspSeesSharedFlow.sharedFlow2;

import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="sfAction2", path="/miniTests/jspSeesSharedFlow/success.jsp")
    }
)
public class SharedFlow2 extends SharedFlowController
{
}

