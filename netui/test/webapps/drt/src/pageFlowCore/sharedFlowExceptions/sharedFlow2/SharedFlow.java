package pageFlowCore.sharedFlowExceptions.sharedFlow2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    catches={
        @Jpf.Catch(type=Exception.class, method="handleIt")
    }
)
public class SharedFlow extends SharedFlowController
{
    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="cur", navigateTo=Jpf.NavigateTo.currentPage)
        }
    )
    public Forward handleIt( Exception ex, String actionName, String message, Object form )
    {
        return new Forward( "cur", "message", "handled " + ex.getClass().getName() + " in sharedFlow2" );
    }
}
