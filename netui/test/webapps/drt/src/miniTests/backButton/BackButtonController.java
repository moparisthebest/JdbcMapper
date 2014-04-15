package miniTests.backButton;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="submit", path="index.jsp"),
        @Jpf.SimpleAction(name="goNested", path="nestedOne/NestedOneController.jpf"),
        @Jpf.SimpleAction(name="nestedOneDone", path="index.jsp")
    }
)
public class BackButtonController extends PageFlowController
{
    private String _state;
    
    public void setState( String state )
    {
        _state = state;
    }
    
    public String getState()
    { 
        return _state;
    }
}
