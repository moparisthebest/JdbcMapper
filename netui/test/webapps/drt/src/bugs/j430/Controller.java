package bugs.j430;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction(name="begin", path="index.jsp")
   }
)

public class Controller extends PageFlowController
{
    private String _string = null;
    public String getString(){ return _string; }
}

