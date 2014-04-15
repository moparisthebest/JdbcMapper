package bugs.j480;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller
public class Controller extends PageFlowController
{
    public String getFoo ()
    {
        return "foo";
    }

    @Jpf.Action
        (forwards = {
            @Jpf.Forward(name = "success",
                     path = "Test.jsp")
    })
    public Forward begin ()
    {
        return new Forward ("success");
    }
}