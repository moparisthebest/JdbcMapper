package bugs.j797;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="doRestoreQueryString", path="next.jsp"),
        @Jpf.SimpleAction(name="doNotRestoreQueryString", path="next.jsp")
    }
)
public class Controller extends PageFlowController
{
}
