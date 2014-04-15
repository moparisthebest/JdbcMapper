package miniTests.preventDoubleSubmit;

import org.apache.beehive.netui.pageflow.DoubleSubmitException;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    catches={
        @Jpf.Catch(type=DoubleSubmitException.class, path="caughtit.jsp")
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="anchorSubmit", preventDoubleSubmit=true, path="submitted.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        preventDoubleSubmit=true,
        forwards={
            @Jpf.Forward(name="submitted", path="submitted.jsp")
        }
    )
    public Forward formSubmit()
    {
        return new Forward("submitted");
    }
}
