package restorePreviousPage;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="page1.faces"),
        @Jpf.SimpleAction(name="go2", path="page2.faces"),
        @Jpf.SimpleAction(name="goCur", navigateTo=Jpf.NavigateTo.currentPage),
        @Jpf.SimpleAction(name="goPrev", navigateTo=Jpf.NavigateTo.previousPage)
    }
)
public class Controller extends PageFlowController
{
}
