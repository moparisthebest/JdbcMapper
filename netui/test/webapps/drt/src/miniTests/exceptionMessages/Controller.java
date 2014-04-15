package miniTests.exceptionMessages;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    catches={
        @Jpf.Catch(type=Controller.Exception1.class, path="catchPage.jsp", message="${pageFlow.message1}"),
        @Jpf.Catch(type=Controller.Exception2.class, path="catchPage.jsp", message="${pageFlow.message1}", messageKey="foo"),
        @Jpf.Catch(type=Controller.Exception3.class, path="catchPage.jsp", messageKey="foo"),
        @Jpf.Catch(type=Controller.Exception4.class, method="handleEx", message="${pageFlow.message1}"),
        @Jpf.Catch(type=Controller.Exception5.class, method="handleEx", message="${pageFlow.message1}", messageKey="foo"),
        @Jpf.Catch(type=Controller.Exception6.class, method="handleEx", messageKey="foo")
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="miniTests.exceptionMessages.Messages")
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action() public Forward throw1() { throw new Exception1(); }
    @Jpf.Action() public Forward throw2() { throw new Exception2(); }
    @Jpf.Action() public Forward throw3() { throw new Exception3(); }
    @Jpf.Action() public Forward throw4() { throw new Exception4(); }
    @Jpf.Action() public Forward throw5() { throw new Exception5(); }
    @Jpf.Action() public Forward throw6() { throw new Exception6(); }

    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="methodPage", path="methodPage.jsp")
        }
    )
    public Forward handleEx( Exception ex, String actionName, String message, Object form )
    {
        return new Forward( "methodPage", "theMessage", message );
    }

    public String getMessage1() { return "message from page flow"; }

    public static class BaseException extends RuntimeException
    {
        public String getMessage()
        {
            return "this is the actual exception message for " + getClass().getName();
        }
    }

    public static class Exception1 extends BaseException {}
    public static class Exception2 extends BaseException {}
    public static class Exception3 extends BaseException {}
    public static class Exception4 extends BaseException {}
    public static class Exception5 extends BaseException {}
    public static class Exception6 extends BaseException {}
}
