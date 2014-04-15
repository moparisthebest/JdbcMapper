package pageFlowCore.inheritance;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import pageFlowCore.inheritance.super1.Super1;
import pageFlowCore.inheritance.super2.Super2;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="overrideMe", path="success.jsp")
    }
)
public class Controller extends Super2
{
    @Jpf.Action
    public Forward throwException() throws Exception
    {
        throw new Super1.Ex();
    }
}
