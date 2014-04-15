package initMemberFields;

import javax.servlet.http.HttpSession;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf", type=InitMemberFieldsSharedFlow.class)
    }
)
public class InitMemberFieldsController extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name = "success", path = "page1.faces")
        }
    )
    protected Forward begin()
    {
        return new Forward("success");
    }
}
