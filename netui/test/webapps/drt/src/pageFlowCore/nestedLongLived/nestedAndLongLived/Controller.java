package pageFlowCore.nestedLongLived.nestedAndLongLived;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    longLived=true,
    nested=true,
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="done", returnAction="nestedDone")
    }
)
public class Controller extends PageFlowController
{
    private String _str;
    public void setStr( String str ) { _str = str; }
    public String getStr() { return _str; }
}
