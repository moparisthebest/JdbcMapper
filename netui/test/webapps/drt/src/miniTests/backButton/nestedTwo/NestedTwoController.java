package miniTests.backButton.nestedTwo;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowStack;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="backButton", path="/miniTests/backButton/nestedOne/NestedOneController.jpf"),
        @Jpf.SimpleAction(name="done", returnAction="nestedTwoDone")
    }
)
public class NestedTwoController extends PageFlowController
{
    public int getStackSize()
    {
        return PageFlowStack.get( getRequest() ).size();
    }
}
