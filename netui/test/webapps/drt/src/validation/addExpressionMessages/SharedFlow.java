package validation.addExpressionMessages;

import org.apache.beehive.netui.pageflow.SharedFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller()
public class SharedFlow extends SharedFlowController
{
    public String getSharedFlowMessage()
    {
        return "a message in shared flow " + getDisplayName();
    }

}
