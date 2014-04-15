package bugs.j505;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.tree.TreeElement;
import org.apache.beehive.netui.tags.tree.TreeRootElement;

@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction(name="postback", navigateTo=Jpf.NavigateTo.currentPage)
   }
)

public class Controller extends PageFlowController
{
    TreeRootElement expandModesDynTree11;
    TreeElement expandModesJspTree11;

    public TreeElement getExpandModesJspTree11(){ return this.expandModesJspTree11; }
    public void setExpandModesJspTree11(TreeElement expandModesJspTree11){ this.expandModesJspTree11= expandModesJspTree11; }

    public TreeRootElement getExpandModesDynTree11(){ return this.expandModesDynTree11; }
    public void setExpandModesDynTree11(TreeRootElement expandModesDynTree11){ this.expandModesDynTree11= expandModesDynTree11; }


    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="index.jsp")
       }
    )
    protected Forward begin()
    {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(1111);
        return new Forward("index");
    }


    protected void onCreate()
    {
	expandModesDynTree11 = new TreeRootElement("TreeItem0",false);
	TreeElement item01 = new TreeElement("TreeItem0.1",false);
	item01.setExpandOnServer(true);
	expandModesDynTree11.addChild(item01);
		TreeElement item011 = new TreeElement("TreeItem0.1.1",false);
		item01.addChild(item011);
			item011.addChild(new TreeElement("TreeItem0.1.1.1",false));
			item011.addChild(new TreeElement("TreeItem0.1.1.2",false));
		TreeElement item012 = new TreeElement("TreeItem0.1.2",false);
		item012.setExpandOnServer(true);
		item01.addChild(item012);
			item012.addChild( new TreeElement("TreeItem0.1.2.1",false));
			TreeElement item0122 = new TreeElement("TreeItem0.1.2.2",true);
			item0122.setExpandOnServer(true);
			item012.addChild(item0122);
				item0122.addChild(new TreeElement("TreeItem0.1.2.2.1",false));
				TreeElement item01222 = new TreeElement("TreeItem0.1.2.2.2",false);
				item01222.setExpandOnServer(true);
				item0122.addChild(item01222);
				item0122.addChild( new TreeElement("TreeItem0.1.2.2.3",false));
			TreeElement item0123 = new TreeElement("TreeItem0.1.2.3", true);
			item012.addChild(item0123);
				item0123.addChild( new TreeElement("TreeItem0.1.2.3.1",false));
				TreeElement item01232 = new TreeElement("TreeItem0.1.2.3.2", false);
				item01232.setExpandOnServer(true);
				item0123.addChild(item01232);
					TreeElement item012321 = new TreeElement("TreeItem0.1.2.3.2.1", false);
					item012321.setExpandOnServer(true);
					item01232.addChild(item012321);
				item0123.addChild( new TreeElement("TreeItem0.1.2.3.3", false));
			TreeElement item0124 = new TreeElement("TreeItem0.1.2.4",false);
			item012.addChild(item0124);
				item0124.addChild( new TreeElement("TreeItem0.1.2.4.1",false));
				TreeElement item01242 = new TreeElement("TreeItem0.1.2.4.2",false);
				item01242.setExpandOnServer(false);
				item0124.addChild(item01242);
					TreeElement item012421 = new TreeElement("TreeItem0.1.2.4.2.1",false);
					item012421.setExpandOnServer(true);
					item01242.addChild(item012421);
						item012421.addChild(new TreeElement("TreeItem0.1.2.4.2.1.1",false));
				TreeElement item01243 = new TreeElement("TreeItem0.1.2.4.3",false);
				item01243.setExpandOnServer(false);
				item0124.addChild(item01243);
					item01243.addChild( new TreeElement("TreeItem0.1.2.4.3.1",false));
		item01.addChild(new TreeElement("TreeItem0.1.3",false));

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
    protected Forward resetTrees()        {
        Forward forward = new Forward("success");
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

