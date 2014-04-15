package bugs.j519;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.tree.TreeElement;
import org.apache.beehive.netui.tags.tree.TreeRootElement;

@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction(name="postback", navigateTo=Jpf.NavigateTo.currentPage),
      @Jpf.SimpleAction(name="goHome", path="index.jsp")
   }
)

public class Controller extends PageFlowController
{
    //Tree Creation

    //Expansion/Contraction Modes
    TreeElement expandModesJspTree11;

    //Expansion/Contraction Modes
	//Dynamic (pageFlow) Trees

    TreeElement expandModesDynTree1;
    TreeElement expandModesDynTree2;
    TreeRootElement expandModesDynTree3;
    TreeRootElement expandModesDynTree4a;
    TreeRootElement expandModesDynTree4b;
    TreeRootElement expandModesDynTree4c;
    TreeRootElement expandModesDynTree4d;
    TreeRootElement expandModesDynTree4e;
    TreeRootElement expandModesDynTree5;
    TreeElement expandModesDynTree6;
    TreeElement expandModesDynTree7;
    TreeRootElement expandModesDynTree8;
    TreeRootElement expandModesDynTree9;
    TreeRootElement expandModesDynTree10;
    TreeRootElement expandModesDynTree11;
    TreeElement expandModesDynTree12;
    TreeElement expandModesDynTree13;
    TreeRootElement expandModesDynTree14;


//Methods

    public TreeElement getExpandModesJspTree11(){ return this.expandModesJspTree11; }
    public void setExpandModesJspTree11(TreeElement expandModesJspTree11){ this.expandModesJspTree11= expandModesJspTree11; }

    protected void onCreate() {
	}


    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="index.jsp")
       }
    )
    protected Forward begin()
    {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(1013);
        return new Forward("index");
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward mySelectionAction()
    {
        System.out.println("TEST MESSAGE: mySelectionAction() was called.");
        Forward forward = new Forward("success");
        return forward;
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward myExpansionAction()
    {
        System.out.println("TEST MESSAGE: myExpansionAction() was called.");
        Forward forward = new Forward("success");
        return forward;
    }
    
    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward myOverrideExpansionAction()
    {
        System.out.println("TEST MESSAGE: myOverrideExpansionAction() was been called.");
        Forward forward = new Forward("success");
        return forward;
    }
    
    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward myOverrideSelectionAction()
    {
        System.out.println("TEST MESSAGE: myOverrideSelectionAction() was called.");
        Forward forward = new Forward("success");
        return forward;
    }

    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path="index.jsp")
})
    protected Forward resetTrees()        {
        Forward forward = new Forward("success");

        //Tree Creation
        
	//Expansion/Contraction Modes
        expandModesJspTree11 = null;        
        
        onCreate();
                  
        return forward;
    }

    class MyTreeElement extends TreeElement{

        public MyTreeElement()
        {
            super();
        }

        public MyTreeElement(String s, boolean b)
        {
            super(s, b);
        }
    }


    class MyTreeRootElement extends TreeRootElement{

    }



}



