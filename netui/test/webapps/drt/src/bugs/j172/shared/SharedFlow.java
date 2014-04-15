package bugs.j172.shared;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    messageBundles={
        @Jpf.MessageBundle(bundlePath="bugs.j172.Messages")
    },
    catches={
        @Jpf.Catch(type=Exception.class, method="handleEx", messageKey="err")
    }
)
public class SharedFlow extends SharedFlowController
{
    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="errorPage", path="error.jsp")
        }
    )
    public Forward handleEx( Exception ex, String actionName, String message, Object form )
    {
        return new Forward( "errorPage", "message", message );
    }
}
