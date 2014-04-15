package bugs.j461;
import org.apache.beehive.netui.pageflow.Forward;
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
   
   /**
    * A tree containing the list of products to browse.
    */
    private TreeElement productTree;
    
	//public TreeElement childTree;
	

    public TreeElement getProductTree(){
        return this.productTree;
    }
    
    public void setProductTree(TreeElement productTree){
    	this.productTree = productTree;
    }

   protected void onCreate() {
      makeTree();
	
   }

/**
 * This is an action that puts the name of the node selected into the
 * request so that it may be used on the page. 
 * The response is always back to the current page.
 * If a tree node has been selected, 
 * then an attribute with name 'productName' is put into the request.
 */

@Jpf.Action( forwards={
   @Jpf.Forward( name="success", navigateTo=Jpf.NavigateTo.currentPage )
} )
public Forward select() {
   // the node name is provided in the parameter list; I want the label
   return new Forward( "success" );
}


    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward newAction1()
    {
        Forward forward = new Forward("success");
        return forward;
    }

	public void makeTree() {
		productTree = new TreeElement("Root Node",true);
		productTree.addChild(0, new TreeElement("CHILDNODE",false));
		//productTree.removeChild(0);
	}



	public void reset(){
		makeTree();
	}
     @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward resetTree()
    {
		reset();
		return new Forward("success");
	}

	public void removeaKid(){
		productTree.removeChild(0);
	}
     @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward removeKid()
    {
		removeaKid();
		return new Forward("success");
	}



}

