package bugs.j324;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction(name="begin", path="index.jsp")
   }
)

public class Controller extends PageFlowController
{
    private TreeElement selectExpandJspTree1;
    private String _action = "begin";

    //Actions and Events (Select and Expand)
    public TreeElement getSelectExpandJspTree1(){ return this.selectExpandJspTree1; }
    public void setSelectExpandJspTree1(TreeElement selectExpandJspTree1){ this.selectExpandJspTree1= selectExpandJspTree1; }
    public String getAction() {return _action;}

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward mySelectionAction()
    {
	_action="mySelectionAction";
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward myExpansionAction()
    {
	_action="myExpansionAction";
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward myTreeAction()
    {
	_action="myTreeAction";
        Forward forward = new Forward("success");
        return forward;
    }
    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
})
    protected Forward resetTrees()        {
	_action="resetTrees";
        Forward forward = new Forward("success");
        selectExpandJspTree1= null;
        return forward;
    }

}
