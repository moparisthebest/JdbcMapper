package webappRoot;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;



@Jpf.Controller()
public class SharedFlow extends SharedFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="success",
                navigateTo=Jpf.NavigateTo.currentPage
            )
        }
    )
    public Forward pageFlowCore_sharedFlow_globalAction()
    {
        return new Forward( "success", "message", "hit globalAction" );
    }
}
