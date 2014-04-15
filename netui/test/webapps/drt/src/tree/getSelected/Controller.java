package tree.getSelected;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.tree.TreeElement;
import org.apache.beehive.netui.tags.tree.TreeHelpers;

import javax.servlet.http.HttpSession;

@Jpf.Controller (
)
public class Controller extends PageFlowController
{
    private TreeElement _root;
    private String _action;
    private String _selected;

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
    public String getSelected() {
	return _selected;
    }

    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="index.jsp")
       }
    )
    protected Forward begin()
    {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(1031);
	_action = "begin";
	if (_root != null) {
	    TreeElement t = TreeHelpers.findSelected(_root);
	    _selected = (t != null)? t.getName() : "Nothing Selected";
	}
	else {
	    _selected = "No Tree";
	}
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward reset()
    {
        _root = null;
	_action = "reset";
	if (_root != null) {
	    TreeElement t = TreeHelpers.findSelected(_root);
	    _selected = (t != null)? t.getName() : "Nothing Selected";
	}
	else {
	    _selected = "No Tree";
	}
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward select()
    {
	_action = "select";
	if (_root != null) {
	    TreeElement t = TreeHelpers.findSelected(_root);
	    _selected = (t != null)? t.getName() : "Nothing Selected";
	}
	else {
	    _selected = "No Tree";
	}
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

