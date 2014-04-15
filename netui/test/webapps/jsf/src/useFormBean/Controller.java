package useFormBean;

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
    private SomeFormBean _memberFormBean = new SomeFormBean();

    public SomeFormBean getMemberFormBean()
    {
        return _memberFormBean;
    }

    @Jpf.Action(
        useFormBean="_memberFormBean",
        forwards = {
            @Jpf.Forward(name = "success", path = "output.jsp")
        }
    )
    protected Forward submit(SomeFormBean bean)
    {
        return new Forward( "success" );
    }

    public static class SomeFormBean implements java.io.Serializable
    {
        private String _foo;
        public void setFoo( String foo ) { _foo = foo; }
        public String getFoo() { return _foo; }
    }
}
