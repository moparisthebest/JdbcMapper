package databinding.cellrepeatererror;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="root", type=webappRoot.SharedFlow.class)
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
}
