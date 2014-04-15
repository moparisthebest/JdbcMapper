package miniTests.targetScope;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="display", path="display.jsp"),
        @Jpf.SimpleAction(name="submit", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    private String _str;

    public void setStr( String str )
    {
        _str = str;
    }

    public String getStr()
    {
        return _str;
    }


    @Jpf.Action(
        forwards={
            @Jpf.Forward(name = "success", path = "index.jsp")
        }
    )
    protected Forward killMe()
    {
        remove();
        return new Forward("success");
    }
}
