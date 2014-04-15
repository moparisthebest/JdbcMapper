package pageFlowCore.anyBeanReturn.nested;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller(
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class NestedController extends PageFlowController
{
    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="done", returnAction="sf.sharedFlowAction", outputFormBeanType=String.class)
        }
    )
    protected Forward done()
    {
    	return new Forward( "done", "Hello from Nested" );
    }

}
