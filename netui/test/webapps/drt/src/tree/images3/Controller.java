package tree.images3;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.tree.TreeElement;

import javax.servlet.http.HttpSession;

@Jpf.Controller (
    simpleActions = {
        @Jpf.SimpleAction(name = "select", path = "Test.jsp")
    }
)
public class Controller extends PageFlowController
{
    private TreeElement _root;

    public TreeElement getRoot()
    {
        return _root;
    }
    public void setRoot(TreeElement tree) 
    {
	_root = tree;
    }

    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="Test.jsp")
       }
    )
    protected Forward begin()
    {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(319);
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="Test.jsp")
        }
    )
    protected Forward reset()
    {
        _root = null;
        return new Forward("index");
    }

    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }
}

