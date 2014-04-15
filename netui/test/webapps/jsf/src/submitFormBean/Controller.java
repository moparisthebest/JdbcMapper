package submitFormBean;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="input.faces")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "output.jsp")
        }
    )
    protected Forward submit( SomeFormBean bean )
    {
        return new Forward( "success", "foo", bean.getFoo() );
    }

    public static class SomeFormBean implements java.io.Serializable
    {
        private String _foo;
        public void setFoo( String foo ) { _foo = foo; }
        public String getFoo() { return _foo; }
    }
}
