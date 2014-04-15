package pageFlowCore.inheritance.super2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import pageFlowCore.inheritance.super1.Super1;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="simpleAction2", path="index.jsp"),
        @Jpf.SimpleAction(name="overrideMe", path="index.jsp")
    }
)
public class Super2 extends Super1
{
    @Jpf.Action
    public Forward methodAction2()
    {
        return null;
    }
}
