package bugs.j478;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;
import org.apache.beehive.netui.tags.tree.TreeRootElement;

@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction(name="begin", path="attributes.jsp"),
      @Jpf.SimpleAction(name="postback", navigateTo=Jpf.NavigateTo.currentPage),
      @Jpf.SimpleAction(name="goHome", path="attributes.jsp")
   }
)

public class Controller extends PageFlowController
{

    //Attributes
    TreeElement attrJspTree1;
    TreeElement attrJspTree2;
    TreeElement attrJspTree3;
    TreeElement attrJspTree4;

    TreeRootElement attrDynTree1;
    TreeRootElement attrDynTree2;
    TreeElement attrDynTree3;
    TreeElement attrDynTree4;


//Methods

    //Tree Creation
    public TreeRootElement getAttrDynTree1(){ return this.attrDynTree1; }
    public void setAttrDynTree1(TreeRootElement attrDynTree1){ this.attrDynTree1= attrDynTree1; }

    public TreeRootElement getAttrDynTree2(){ return this.attrDynTree2; }
    public void setAttrDynTree2(TreeRootElement attrDynTree2){ this.attrDynTree2= attrDynTree2; }

    public TreeElement getAttrDynTree3(){ return this.attrDynTree3; }
    public void setAttrDynTree3(MyTreeElement attrDynTree3){ this.attrDynTree3= attrDynTree3; }

    public TreeElement getAttrDynTree4(){ return this.attrDynTree4; }
    public void setAttrDynTree4(TreeElement attrDynTree4){ this.attrDynTree4 = attrDynTree4; }

   
    //Attributes
    public TreeElement getAttrJspTree1(){ return this.attrJspTree1; }
    public void setAttrJspTree1(TreeElement attrJspTree1){ this.attrJspTree1= attrJspTree1; }

    public TreeElement getAttrJspTree2(){ return this.attrJspTree2; }
    public void setAttrJspTree2(TreeElement attrJspTree2){ this.attrJspTree2= attrJspTree2; }

    public TreeElement getAttrJspTree3(){ return this.attrJspTree3; }
    public void setAttrJspTree3(TreeElement attrJspTree3){ this.attrJspTree3= attrJspTree3; }

    public TreeElement getAttrJspTree4(){ return this.attrJspTree4; }
    public void setAttrJspTree4(TreeElement attrJspTree4){ this.attrJspTree4= attrJspTree4; }

   

   protected void onCreate() {
      attrDynTree1 = new TreeRootElement( "Dynamic Tree 1", true );
     attrDynTree1.setTitle("HELLOOOOOOOOOO. This should appear in the HTML source.");
      attrDynTree1.addChild( new TreeElement("TreeElement1 named in the page flow", false) );
      attrDynTree1.getChild(0).addChild( new TreeElement("TreeElement2 named in the page flow", false) );
	attrDynTree1.getChild(0).getChild(0).addChild( new TreeElement("TreeElement3 named in the page flow", false) );
	attrDynTree1.getChild(0).getChild(0).addChild( new TreeElement("TreeElement4 named in the page flow", false) );
	attrDynTree1.getChild(0).getChild(0).addChild( new TreeElement("TreeElement5 named in the page flow", false) );
	attrDynTree1.getChild(0).getChild(0).getChild(2).setDisabled(true);

      attrDynTree2= new TreeRootElement( "Dynamic Tree 2", true );
      attrDynTree2.addChild( new TreeElement("NOT disabled", false) );
      attrDynTree2.getChild(0).addChild( new TreeElement("not disabled", false) );
	attrDynTree2.getChild(0).getChild(0).addChild( new TreeElement("disabled", false) );
	attrDynTree2.getChild(0).getChild(0).getChild(0).setDisabled(false);
	attrDynTree2.getChild(0).getChild(0).addChild( new TreeElement("not disabled", false) );
	attrDynTree2.getChild(0).getChild(0).addChild( new TreeElement("disabled", false) );
	attrDynTree2.getChild(0).getChild(0).getChild(2).setDisabled(true);


	attrDynTree2.setRootNodeExpandedImage("rootminus.gif");
	attrDynTree2.setRootNodeCollapsedImage("rootplus.gif");


      attrDynTree3= new TreeElement( "Dynamic Tree 3", true );
      attrDynTree3.addChild( new TreeElement("NOT disabled", true) );
      attrDynTree3.getChild(0).addChild( new TreeElement("not disabled", true) );
	attrDynTree3.getChild(0).getChild(0).addChild( new TreeElement("disabled", true) );
	attrDynTree3.getChild(0).getChild(0).getChild(0).setDisabled(true);
	attrDynTree3.getChild(0).getChild(0).addChild( new TreeElement("not disabled", true) );
	attrDynTree3.getChild(0).getChild(0).addChild( new TreeElement("disabled", true) );
	attrDynTree3.getChild(0).getChild(0).getChild(2).setDisabled(true);


  	attrDynTree4= new TreeElement( "Dynamic Tree 4", true );
	attrDynTree4.addChild( new TreeElement("NOT disabled", true) );
	attrDynTree4.getChild(0).addChild( new TreeElement("not disabled", true) );
	attrDynTree4.getChild(0).getChild(0).addChild( new TreeElement("disabled", true) );
	attrDynTree4.getChild(0).getChild(0).getChild(0).setDisabled(true);
	attrDynTree4.getChild(0).getChild(0).addChild( new TreeElement("not disabled", true) );
	attrDynTree4.getChild(0).getChild(0).addChild( new TreeElement("disabled", true) );
	attrDynTree4.getChild(0).getChild(0).getChild(2).setDisabled(true);

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
    @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
})
    protected Forward resetTrees()        {
        Forward forward = new Forward("success");

   		onCreate();
        
        //Attributes
        attrJspTree1 = null;
        attrJspTree2 = null;
        attrJspTree3 = null;
        attrJspTree4 = null;
                  
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

