package pageFlowCore.actionAttr;

import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="sharedFlowAction", path="success.jsp")
    }
)
public class SharedFlow extends SharedFlowController
{
}
