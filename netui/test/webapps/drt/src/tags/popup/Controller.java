package tags.popup;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="goNested", path="nested/Controller.jpf"),
        @Jpf.SimpleAction(name="goNested2", path="nested/Controller.jpf"),
        @Jpf.SimpleAction(name="nestedDone", forwardRef="_auto")
    }
)
public class Controller extends PageFlowController
{
}
