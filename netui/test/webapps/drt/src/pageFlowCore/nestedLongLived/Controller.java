package pageFlowCore.nestedLongLived;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="goNested", path="nested/Controller.jpf"),
        @Jpf.SimpleAction(name="goNestedLongLived", path="nestedAndLongLived/Controller.jpf"),
        @Jpf.SimpleAction(name="nestedDone", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
}
