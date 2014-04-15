package pageFlowCore.actionAttr.anotherPageFlow;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="someAction", path="index.jsp"),
        @Jpf.SimpleAction(name="goBack", action="../begin")
    }
)
public class Controller extends PageFlowController
{
}
