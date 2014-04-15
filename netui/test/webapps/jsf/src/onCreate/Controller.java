package onCreate;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="page1.faces")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="success",
                path="page2.faces",
                actionOutputs={
                    @Jpf.ActionOutput(name="foo", type=String.class, required=true)
                }
            )
        }
    )
    public Forward go2()
    {
        return new Forward("success", "foo", "Got the action output.");
    }
}
