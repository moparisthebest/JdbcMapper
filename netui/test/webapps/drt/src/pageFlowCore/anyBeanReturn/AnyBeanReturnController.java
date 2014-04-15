package pageFlowCore.anyBeanReturn;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf", type=SharedFlow.class)
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="goNested", path="nested/NestedController.jpf")
    }
)
public class AnyBeanReturnController extends PageFlowController
{
}
