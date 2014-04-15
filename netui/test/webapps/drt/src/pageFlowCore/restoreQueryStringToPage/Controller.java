package pageFlowCore.restoreQueryStringToPage;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="page1.jsp"),
        @Jpf.SimpleAction(name="goPage2", path="page2.jsp"),
        @Jpf.SimpleAction(name="curPage", navigateTo=Jpf.NavigateTo.currentPage, restoreQueryString=true),
        @Jpf.SimpleAction(name="prevPage", navigateTo=Jpf.NavigateTo.previousPage, restoreQueryString=true)
    }
)
public class Controller extends PageFlowController
{
}
