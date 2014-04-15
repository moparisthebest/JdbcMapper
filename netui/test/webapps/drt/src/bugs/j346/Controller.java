package bugs.j346;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction(name="begin", path="index.jsp"),
      @Jpf.SimpleAction(name="postback", navigateTo=Jpf.NavigateTo.currentPage)
   }
)

public class Controller extends PageFlowController
{

    TreeElement tree1;

    public TreeElement getTree1(){ return this.tree1; }
    public void setTree1(TreeElement tree1){ this.tree1= tree1; }

}

