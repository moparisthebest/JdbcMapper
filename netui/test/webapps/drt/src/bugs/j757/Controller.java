package bugs.j757;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    catches={
        @Jpf.Catch(type=Exception.class, path="error.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action()
    public Forward throwException()
    {
        throw new IllegalStateException( "exception thrown in page flow" );
    }
}
