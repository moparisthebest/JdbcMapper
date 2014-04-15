package pageFlowCore.anyBeanReturn;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller
public class SharedFlow extends SharedFlowController
{
    @Jpf.Action(
        forwards={
           @Jpf.Forward( name="resultPage", path="result.jsp" )
        }
    )
    protected Forward sharedFlowAction( String form )
    {
        return new Forward( "resultPage", "result", form );
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward( name="resultPage", path="result.jsp" )
        }
    )
    protected Forward sharedFlowAction()
    {
        throw new IllegalStateException( "should never get here" );
    }

}
