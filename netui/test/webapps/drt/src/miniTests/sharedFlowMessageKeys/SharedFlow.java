package miniTests.sharedFlowMessageKeys;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    messageBundles={
        @Jpf.MessageBundle(bundlePath="miniTests.sharedFlowMessageKeys.SharedFlowMessages")
    },
    catches={
        @Jpf.Catch(type=SharedFlow.Exception1.class, path="index.jsp", messageKey="bar"),
        @Jpf.Catch(type=SharedFlow.Exception2.class, path="index.jsp", message="got the hardcoded shared flow exception message")
    }
)
public class SharedFlow extends SharedFlowController
{
    @Jpf.Action(
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward submit( MyForm form )
    {
        return null;
    }

    @Jpf.Action()
    public Forward throwException1() throws Exception1
    {
        throw new Exception1();
    }

    @Jpf.Action()
    public Forward throwException2() throws Exception2
    {
        throw new Exception2();
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

    public static class Exception1 extends Exception {}
    public static class Exception2 extends Exception {}
}
