package bindingInFaces;

import javax.servlet.http.HttpSession;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller(
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf", type=SharedFlow.class)
    }
)
public class BindingInFacesController extends PageFlowController
{
    private String _someProperty = "page flow property";

    public String getSomeProperty()
    {
        return _someProperty;
    }

    public void setSomeProperty( String someProperty )
    {
        _someProperty = someProperty;
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "page1.faces")
        }
    )
    protected Forward begin()
    {
        return new Forward( "success", "somePageInput", "a page input" );
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "results.jsp")
        }
    )
    protected Forward submit()
    {
        return new Forward("success");
    }

}
