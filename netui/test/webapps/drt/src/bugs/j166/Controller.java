package bugs.j166;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

import javax.servlet.http.HttpSession;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    TreeElement _tree = null;
    public TreeElement getTree() {
        return _tree;
    }
    public void setTree(TreeElement t) {
	_tree = t;
    }
    
    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="success", path="tree.jsp")
        }
    )
    protected Forward tree()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="success", navigateTo= Jpf.NavigateTo.currentPage)
        }
    )
    protected Forward clearTree()
    {
	_tree = null;
        return new Forward("success");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="success", navigateTo= Jpf.NavigateTo.currentPage)
        }
    )
    protected Forward select()
    {
        return new Forward("success");
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
