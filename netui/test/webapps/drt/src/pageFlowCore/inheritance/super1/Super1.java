package pageFlowCore.inheritance.super1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import pageFlowCore.inheritance.super0.Super0;    // in /src

@Jpf.Controller(
    catches={
        @Jpf.Catch(type=Super1.Ex.class, method="handleEx")
    },
    simpleActions={
        @Jpf.SimpleAction(name="simpleAction1", path="index.jsp")
    }
)
public class Super1 extends Super0
{
    public static class Ex extends Exception
    {
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="current", navigateTo=Jpf.NavigateTo.currentPage)
        }
    )
    public Forward methodAction1()
    {
        return new Forward( "current", "message", "in " + Super1.class.getName() + ".methodAction1()" );
    }

    @Jpf.ExceptionHandler(
        forwards={
            @Jpf.Forward(name="cur", navigateTo=Jpf.NavigateTo.currentPage)
        }
    )
    public Forward handleEx( Ex ex, String actionName, String message, Object form )
    {
        return new Forward( "cur", "message", "in " + Super1.class.getName() + ".handleEx()" );
    }
}
