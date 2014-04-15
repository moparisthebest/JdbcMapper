package miniTests.flowControllerFactory;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * The factory miniTests.flowControllerFactory.TestFactory loads this class on a request for
 * /miniTests/flowControllerFactory/Controller.jpf.
 */
@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller
    extends PageFlowController
{
    private String _initVal;

    /**
     * Note that this page flow class couldn't be constructed by the framework normally, since it
     * doesn't have a default constructor.  The factory miniTests.flowControllerFactory.TestFactory
     * knows how to construct it.
     */
    public Controller(String initVal)
    {
        _initVal = initVal;
    }

    public String getInitVal()
    {
        return _initVal;
    }
}
