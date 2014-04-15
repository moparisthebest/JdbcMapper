package miniTests.backButton.nestedOne;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="backButton", path="/miniTests/backButton/BackButtonController.jpf"),
        @Jpf.SimpleAction(name="goNestedTwo", path="/miniTests/backButton/nestedTwo/NestedTwoController.jpf"),
        @Jpf.SimpleAction(name="nestedTwoDone", path="index.jsp"),
        @Jpf.SimpleAction(name="done", returnAction="nestedOneDone")
    }
)
public class NestedOneController extends PageFlowController
{
}
