package pageFlowCore.inheritControls.derived;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import pageFlowCore.inheritControls.Controller;


@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class DerivedController extends Controller
{
    public String getControlMessage()
    {
        return baseClassControl.hello();
    }
}
