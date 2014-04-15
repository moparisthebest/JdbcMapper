package pageFlowCore.controlsContextualServices;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.netui.pageflow.PageFlowController;

@ControlImplementation(isTransient=true)
public class HelloControlImpl implements HelloControl
{ 
    @Context
    PageFlowController pageFlow;

    public String sayHi()
    {
        return "Hi.  The page flow URI is: " + pageFlow.getURI();
    }
}
