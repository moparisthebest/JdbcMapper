package miniTests.sharedFlowMessageKeys;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf", type=SharedFlow.class)
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="miniTests.sharedFlowMessageKeys.PageFlowMessages")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submit( MyForm form )
    {
        return null;
    }

    @Jpf.FormBean()
    public static class MyForm
    {
        @Jpf.ValidatableProperty(
            validateRequired=@Jpf.ValidateRequired(messageKey="foo")
        )
        public String getFoo()
        {
            return "";
        }
    }
}
