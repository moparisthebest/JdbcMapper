package bindingInFaces;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller
public class SharedFlow
    extends SharedFlowController
{
    private String _someProperty = "shared flow property";

    public String getSomeProperty()
    {
        return _someProperty;
    }

    public void setSomeProperty( String someProperty )
    {
        _someProperty = someProperty;
    }
}

