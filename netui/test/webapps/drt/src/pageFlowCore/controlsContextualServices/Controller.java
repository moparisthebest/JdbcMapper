package pageFlowCore.controlsContextualServices;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller()
public class Controller extends PageFlowController
{
    @Control
    HelloControl _hello;

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward begin()
    {
        
        return new Forward( "index", "message", _hello.sayHi() );
    }
}
