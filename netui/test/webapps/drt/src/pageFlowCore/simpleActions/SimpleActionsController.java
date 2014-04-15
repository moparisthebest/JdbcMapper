package pageFlowCore.simpleActions;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(
            name="conditional",
            conditionalForwards={
                @Jpf.ConditionalForward(condition="${pageFlow.choice=='page1'}", path="page1.jsp"),
                @Jpf.ConditionalForward(condition="${pageFlow.choice=='page2'}", path="page2.jsp")
            },
            path="page3.jsp"
        ),
        @Jpf.SimpleAction(name="goPrev", navigateTo=Jpf.NavigateTo.previousPage)
    }
)
public class SimpleActionsController extends PageFlowController
{
    private String _choice;

    public String getChoice()
    {
        return _choice;
    }

    public void setChoice( String choice )
    {
        _choice = choice;
    }
}
