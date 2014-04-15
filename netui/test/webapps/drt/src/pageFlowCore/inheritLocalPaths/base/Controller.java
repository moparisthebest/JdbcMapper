package pageFlowCore.inheritLocalPaths.base;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="page1.jsp"),
        @Jpf.SimpleAction(name="done", returnAction="nestedDone")
    },
    forwards={
        @Jpf.Forward(name="page3", path="page3.jsp")
    },
    catches={
        @Jpf.Catch(type=Controller.IntentionalException1.class, path="page4.jsp"),
        @Jpf.Catch(type=Controller.IntentionalException2.class, method="handleIt")
    }
)
public abstract class Controller extends PageFlowController
{
    private int _actionCount = 0;

    public static final class IntentionalException1 extends Exception {}
    public static final class IntentionalException2 extends Exception {}

    public int getActionCount()
    {
        return _actionCount;
    }

    protected void beforeAction()
    {
        ++_actionCount;
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="page2", path="page2.jsp")
        }
    )
    public Forward goPage2()
    {
        return new Forward( "page2" );
    }

    @Jpf.Action()
    public Forward goPage3()
    {
        return new Forward( "page3" );
    }

    @Jpf.Action()
    public Forward goPage4()
        throws IntentionalException1
    {
        throw new IntentionalException1();
    }

    @Jpf.Action()
    public Forward goPage5()
        throws IntentionalException2
    {
        throw new IntentionalException2();
    }

    @Jpf.Action(
        catches={
            @Jpf.Catch(type=Controller.IntentionalException1.class, path="page6.jsp")
        }
    )
    public Forward goPage6()
        throws IntentionalException1
    {
        throw new IntentionalException1();
    }

    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="page5", path="page5.jsp")
        }
    )
    public Forward handleIt( IntentionalException2 ex, String actionName, String message, Object form )
    {
        return new Forward( "page5" );
    }
}
