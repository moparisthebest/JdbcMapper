package bugs.j297;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.tree.TreeElement;

import javax.servlet.http.HttpSession;

@Jpf.Controller (
)
public class Controller extends PageFlowController
{
    private String _action;
    private TreeElement _root;

    public TreeElement getRoot()
    {
        return _root;
    }
    public void setRoot(TreeElement tree) 
    {
	_root = tree;
    }
    
    public String getAction() {
	return _action;
    }

    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="Test.jsp")
       }
    )
    protected Forward begin()
    {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(653);
	_action="begin";
        return new Forward("index");
    }

    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="Test.jsp")
       }
    )
    protected Forward select()
    {
	_action="select";
        return new Forward("index");
    }

    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="Test.jsp")
       }
    )
    protected Forward subTreeSelect()
    {
	_action="subTreeSelect";
        return new Forward("index");
    }

    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="Test.jsp")
       }
    )
    protected Forward selectOverride()
    {
	_action="selectOverride";
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
	_action="reset";
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

