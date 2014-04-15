package pageFlowCore.sharedFlowExceptions.sharedFlow1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    catches={
        @Jpf.Catch(type=SharedFlow.Ex.class, method="handleIt")
    }
)
public class SharedFlow extends SharedFlowController
{
    public static class Ex extends Exception
    {
    }

    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="cur", navigateTo=Jpf.NavigateTo.currentPage)
        }
    )
    public Forward handleIt( Ex ex, String actionName, String message, Object form )
    {
        return new Forward( "cur", "message", "handled " + ex.getClass().getName() + " in sharedFlow1" );
    }
}
