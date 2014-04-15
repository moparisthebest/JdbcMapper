package pageFlowCore.sharedFlow.hasSharedFlow1.seesSharedFlow1.hasSharedFlow2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller
public class SharedFlow2 extends SharedFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="success",
                navigateTo=Jpf.NavigateTo.currentPage
            )
        }
    )
    public Forward sharedFlow2Action()
    {
        return new Forward( "success", "message", "hit SharedFlow2.jpfs" );
    }

}
