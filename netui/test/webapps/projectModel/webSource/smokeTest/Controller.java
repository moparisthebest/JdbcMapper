package smokeTest;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.*;

import common.MyForm;

@Jpf.Controller(
    strutsMerge="/common/strutsMerge.xml",  // in /src/common, which is another source root
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="success.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submit( MyForm form )
    {
        return new Forward( "index" );
    }
}
