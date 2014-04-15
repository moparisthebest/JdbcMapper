package pageFlowCore.inheritControls;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller()
public abstract class Controller extends PageFlowController
{
    @Control()
    protected BaseControl baseClassControl;
}
