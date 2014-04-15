/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package richTree;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.tree.TreeElement;
import org.apache.beehive.netui.tags.tree.TreeRootElement;

import java.io.Serializable;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    // make sure the initialize the trees.
    private TreeElement _tree1;
    private TreeElement _tree2;
    private TreeElement _tree3;
    private TreeElement _tree4;
    private TreeElement _tree5;
    private TreeElement _tree6;
    private TreeElement _tree7;
    private TreeElement _tree8;
    private TreeElement _tree9;
    private TreeElement _tree10;
    private TreeElement _tree11;
    private TreeElement _tree12;
    private TreeElement _tree13;
    private TreeElement _tree14;
    private TreeElement _tree15;
    private TreeElement _tree16;
    private TreeElement _tree17;
    private TreeElement _tree18;
    private TreeElement _tree19;
    private TreeElement _tree20;
    private TreeElement _tree21;
    private TreeElement _tree22;
    private TreeElement _tree23;
    private TreeElement _tree24;
    private TreeElement _tree25;
    private TreeElement _tree26;
    private TreeElement _tree27;
    private TreeElement _tree28;
    private TreeElement _tree29;
    private TreeElement _tree30;
    private TreeElement _tree31;
    private TreeElement _tree32;
    private TreeElement _tree33;
    private TreeElement _tree34;
    private TreeElement _tree35;
    private TreeElement _tree36;
    private TreeElement _tree37;

    private String _expand = "&nbsp;";
    private String _node = "&nbsp;";
    private String _status = "&nbsp;";
    private String _action = "&nbsp;";

    public TreeElement getTree1() {
        return _tree1;
    }

    public void setTree1(TreeElement tn) {
        _tree1 = tn;
    }

    public TreeElement getTree2() {
        return _tree2;
    }

    public void setTree2(TreeElement tn) {
        _tree2 = tn;
    }

    public TreeElement getTree3() {
        return _tree3;
    }

    public void setTree3(TreeElement tn) {
        _tree3 = tn;
    }

    public TreeElement getTree4() {
        return _tree4;
    }

    public void setTree4(TreeElement tn) {
        _tree4 = tn;
    }

    public TreeElement getTree5() {
        return _tree5;
    }

    public void setTree5(TreeElement tn) {
        _tree5 = tn;
    }

    public TreeElement getTree6() {
        return _tree6;
    }

    public void setTree6(TreeElement tn) {
        _tree6 = tn;
    }

    // Tree7 does not have a setter which should cause an error
    public TreeElement getTree7() {
        return _tree7;
    }

    public TreeElement getTree8() {
        return _tree8;
    }

    public void setTree8(TreeElement tn) {
        _tree8 = tn;
    }

    public TreeElement getTree9() {
        return _tree9;
    }

    public void setTree9(TreeElement tn) {
        _tree9 = tn;
    }

    public TreeElement getTree10() {
        return _tree10;
    }

    public void setTree10(TreeElement tn) {
        _tree10 = tn;
    }

    public TreeElement getTree11() {
        return _tree11;
    }

    public void setTree11(TreeElement tn) {
        _tree11 = tn;
    }

    public TreeElement getTree12() {
        return _tree12;
    }

    public void setTree12(TreeElement tn) {
        _tree12 = tn;
    }

    public TreeElement getTree13() {
        return _tree13;
    }

    public void setTree13(TreeElement tn) {
        _tree13 = tn;
    }

    public TreeElement getTree14() {
        return _tree14;
    }

    public void setTree14(TreeElement tn) {
        _tree14 = tn;
    }

    public TreeElement getTree15() {
        return _tree15;
    }

    public void setTree15(TreeElement tn) {
        _tree15 = tn;
    }
    
    public TreeElement getTree16() {
        return _tree16;
    }

    public void setTree16(TreeElement tn) {
        _tree16 = tn;
    }
    
    public TreeElement getTree17() {
        return _tree17;
    }

    public void setTree17(TreeElement tn) {
        _tree17 = tn;
    }
    
    public TreeElement getTree18() {
        return _tree18;
    }

    public void setTree18(TreeElement tn) {
        _tree18 = tn;
    }
    
    public TreeElement getTree19() {
        return _tree19;
    }

    public void setTree19(TreeElement tn) {
        _tree19 = tn;
    }

    public TreeElement getTree20() {
        return _tree20;
    }

    public void setTree20(TreeElement tn) {
        _tree20 = tn;
    }

    public TreeElement getTree21() {
        return _tree21;
    }

    public void setTree21(TreeElement tn) {
        _tree21 = tn;
    }

    public TreeElement getTree22() {
        return _tree22;
    }

    public void setTree22(TreeElement tn) {
        _tree22 = tn;
    }

    public TreeElement getTree23() {
        return _tree23;
    }

    public void setTree23(TreeElement tn) {
        _tree23 = tn;
    }

    public TreeElement getTree24() {
        return _tree24;
    }

    public void setTree24(TreeElement tn) {
        _tree24 = tn;
    }

    public TreeElement getTree25() {
        return _tree25;
    }

    public void setTree25(TreeElement tn) {
        _tree25 = tn;
    }

    public TreeElement getTree26() {
        return _tree26;
    }

    public void setTree26(TreeElement tn) {
        _tree26 = tn;
    }

    public TreeElement getTree27() {
        return _tree27;
    }

    public void setTree27(TreeElement tn) {
        _tree27 = tn;
    }

    public TreeElement getTree28() {
        return _tree28;
    }

    public void setTree28(TreeElement tn) {
        _tree28 = tn;
    }
    
    public TreeElement getTree29() {
        return _tree29;
    }

    public void setTree29(TreeElement tn) {
        _tree29 = tn;
    }

    public TreeElement getTree30() {
        return _tree30;
    }

    public void setTree30(TreeElement tn) {
        _tree30 = tn;
    }

    public TreeElement getTree31() {
        return _tree31;
    }

    public void setTree31(TreeElement tn) {
        _tree31 = tn;
    }

    public TreeElement getTree32() {
        return _tree32;
    }

    public void setTree32(TreeElement tn) {
        _tree32 = tn;
    }

    public TreeElement getTree33() {
        return _tree33;
    }

    public void setTree33(TreeElement tn) {
        _tree33 = tn;
    }

    public TreeElement getTree34() {
        return _tree34;
    }

    public void setTree34(TreeElement tn) {
        _tree34 = tn;
    }

    public TreeElement getTree35() {
        return _tree35;
    }

    public void setTree35(TreeElement tn) {
        _tree35 = tn;
    }

    public TreeElement getTree36() {
        return _tree36;
    }

    public void setTree36(TreeElement tn) {
        _tree36 = tn;
    }

    public TreeElement getTree37() {
        return _tree37;
    }

    public void setTree37(TreeElement tn) {
        _tree37 = tn;
    }
    
    //************************************************************************
    
    protected void onCreate()
    {
        buildTrees();
    }

    public String getExpand() {
        return _expand;
    }

    public String getNode() {
        return _node;
    }

    public String getStatus() {
        return _status;
    }
    
    public String getAction() {
        return _action;
    }

    public String getLeafPrefix() {
        return "Leaf:";
    }

    public String getContainerPrefix() {
        return "Container:";
    }

    
    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="index.jsp")
       }
    )
    protected Forward begin()
    {
        clearExpand();
        return new Forward("index");
    }

    
    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", path = "baseTree.jsp")
    })
    protected Forward goBaseTree()        {
        Forward forward = new Forward("success");
        clearExpand();
        return forward;
    }
    
              
    /**
     * This action is the default postback for a tree.  It will set the
     * Pageflow variables that indicate if the most recent event was an expand or select.
     */
    @Jpf.Action(forwards = { @org.apache.beehive.netui.pageflow.annotations.Jpf.Forward(name = "success", navigateTo = org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo.currentPage)})
    protected Forward postback()
    {
        Forward forward = new Forward("success");

        clearExpand();
        String name = null;
        String expand = null;

        // Handle a tree expand/contract event
        name = getRequest().getParameter(TreeElement.SELECTED_NODE);
        expand = getRequest().getParameter(TreeElement.EXPAND_NODE);

        if (expand != null)
            _expand = expand;
        if (name != null)
            _node = name;
        _action = "postback";
        return forward;
    }

    private void clearExpand()
    {
        _expand = "&nbsp;";
        _node = "&nbsp;";
        _status = "&nbsp;";
        _action = "&nbsp;";
    }

    private void buildTrees()
    {
        String icon = "folder_16_pad.gif";
        
        // Build TreeOne
        {
            TreeElement tree2;
            _tree1 = new TreeElement("Tree 0",true);
            _tree1.setAction("postback");
            TreeElement t1 = new TreeElement("Node: 0.0",true);
            _tree1.addChild(t1);
            TreeElement t2 = new TreeElement("Node: 0.0.0",true);
            t1.addChild(t2);

            tree2 = new TreeElement("Node 0.1",true);
            t1 = new TreeElement("Node: 0.1.0",true);
            tree2.addChild(t1);
            t2 = new TreeElement("Node: 0.1.0.0",true);
            t1.addChild(t2);
            t2 = new TreeElement("Node: 0.1.0.1",true);
            t1.addChild(t2);
            _tree1.addChild(tree2);
            t1 = new TreeElement("Node: 0.2",true);
            _tree1.addChild(t1);
        }
        
        // build treeFive
        {
            _tree5 = new TreeElement("Tree 0",true);
            _tree5.setAction("postback");
            TreeElement tree1 = new TreeElement("bug1", true);
            TreeElement tree2 = new TreeElement("bug2", true);
            TreeElement tree3 = new TreeElement("bug3", true);
            TreeElement tree4 = new TreeElement("bug4", true);
            tree3.addChild(tree4);
            tree2.addChild(tree3);
            tree1.addChild(tree2);
            _tree5.addChild(tree1);
        }
        
        // Build Tree Twenty
        {
            _tree21 = new TreeElement("Tree 0",true);
            _tree21.setAction("postback");
            TreeElement t1 = new TreeElement("Node: 0.0",true);
            _tree21.addChild(t1);
            TreeElement t2 = new TreeElement("Node: 0.0.0",true);
            t1.addChild(t2);

            TreeElement tree = new TreeElement("Node 0.1",true);
            t1 = new TreeElement("Node: 0.1.0",true);
            tree.addChild(t1);
            t2 = new TreeElement("Node: 0.1.0.0",true);
            t1.addChild(t2);
            t2 = new TreeElement("Node: 0.1.0.1",true);
            t1.addChild(t2);
            _tree21.addChild(tree);
            t1 = new TreeElement("Node: 0.2",true);
            _tree21.addChild(t1);
        }

	// Build Tree ThirtySix
	{
            _tree36 = new TreeRootElement("Tree 0",false);
            _tree36.setAction("postback");
	    _tree36.setExpandOnServer(true);
            TreeElement t1 = new TreeElement("Node: 0.0",false);
            _tree36.addChild(t1);
            TreeElement t2 = new TreeElement("Node: 0.0.0",false);
            t1.addChild(t2);
            t1 = new TreeElement("Node: 1.0",false);
            _tree36.addChild(t1);
            t2 = new TreeElement("Node: 1.0.0",false);
            t1.addChild(t2);
	}
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", path = "baseTreeTwo.jsp")
    })
    protected Forward goStaticBaseTree()        {
        Forward forward = new Forward("success");
        clearExpand();
        return forward;
    }
             
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "emptyTree.jsp")
})
    protected Forward goEmptyTree()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "treeBinding.jsp")
})
    protected Forward goTreeBinding()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "selectStyles.jsp")
})
    protected Forward goSelectStyles()        {
        Forward forward = new Forward("success");
        clearExpand();
        return forward;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "treeStyle.jsp")
})
    protected Forward goTreeStyle()        {
        Forward forward = new Forward("success");
        clearExpand();
        return forward;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "cr182056.jsp")
})
    protected Forward goCr182056()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "cr180331.jsp")
})
    protected Forward goCr180331()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "index.jsp")
        })
    protected Forward initTrees()        {
        Forward forward = new Forward("success");
        _tree1 = null;
        _tree2 = null;
        _tree3 = null;
        _tree4 = null;
        _tree5 = null;
        _tree6 = null;
        _tree7 = null;
        _tree8 = null;
        _tree9 = null;
        _tree10 = null;
        _tree11 = null;
        _tree12 = null;
        _tree13 = null;
        _tree14 = null;
        _tree15 = null;
        _tree16 = null;
        _tree17 = null;
        _tree18 = null;
        _tree19 = null;
        _tree20 = null;
        _tree21 = null;
        _tree22 = null;
        _tree23 = null;
        _tree24 = null;
        _tree25 = null;
        _tree26 = null;
        _tree27 = null;
        _tree28 = null;
        _tree29 = null;
        _tree30 = null;
        _tree31 = null;
        _tree32 = null;
        _tree33 = null;
        _tree34 = null;
        _tree35 = null;
        _tree36 = null;
        _tree37 = null;

        buildTrees();
        return forward;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "writeTreeError.jsp")
})
    protected Forward goWriteTreeError()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
             
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "baseClient.jsp")
})
    protected Forward goBaseClient()        {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(123);
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }

    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "runAtClient2.jsp")
})
    protected Forward goRunAtClient2()        {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(53);
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
             
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "treeHtml.jsp")
})
    protected Forward goTreeHtml()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "partialClient.jsp")
})
    protected Forward goPartialClient()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "treeHtmlTwo.jsp")
})
    protected Forward goTreeHtmlTwo()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
                
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "content.jsp")
})
    protected Forward goContent()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "clientContent.jsp")
})
    protected Forward goClientContent()        {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(137);
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "contentAnchor.jsp")
})
    protected Forward goContentAnchor()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage)
})
    protected Forward contentPostback()        {
        Forward forward = new Forward("success");
        clearExpand();
        _status = "content: ";
        return forward;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage)
})
    protected Forward contentPostbackTwo()        {
        Forward forward = new Forward("success");
        clearExpand();
        _status = "contentTwo: ";
        return forward;
    }
    
    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage)
})
    protected Forward postForm(FormBean form) {
        Forward forward = new Forward("success");
        clearExpand();
        _status = "PostForm: " + form.getText();
        return forward;
    }

    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "clientContentAnchor.jsp")
})
    protected Forward goClientContentAnchor()        {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(146);
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "disabled.jsp")
})
    protected Forward goDisabled()        {
        Forward success = new Forward("success");
        clearExpand();
        return success;
    }
    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "override.jsp")
})
    protected Forward goOverride()        {
        Forward success = new Forward("success");
        clearExpand();        
        return success;
    }
                
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage)
})
    protected Forward postbackTwo()        {
        Forward forward = new Forward("success");
        String name = null;
        String expand = null;

        // Handle a tree expand/contract event
        name = getRequest().getParameter(TreeElement.SELECTED_NODE);
        expand = getRequest().getParameter(TreeElement.EXPAND_NODE);

        if (expand != null)
            _expand = expand;
        if (name != null)
            _node = name;
        _action="postbackTwo";
        return forward;
    }
    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", navigateTo = Jpf.NavigateTo.currentPage)
})
    protected Forward postbackThree()        {
        Forward forward = new Forward("success");
          String name = null;
        String expand = null;

        // Handle a tree expand/contract event
        name = getRequest().getParameter(TreeElement.SELECTED_NODE);
        expand = getRequest().getParameter(TreeElement.EXPAND_NODE);

        if (expand != null)
            _expand = expand;
        if (name != null)
            _node = name;
      _action="postbackThree";
      return forward;
    }
    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "overrideTwo.jsp")
})
    protected Forward goOverrideTwo()        {
        Forward forward = new Forward("success");
        clearExpand();                
        return forward;
    }
                
    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "href.jsp")
})
    protected Forward goHref()        {
        Forward success = new Forward("success");
        clearExpand();                
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "selectAction.jsp")
})
    protected Forward goSelectAction()        {
        Forward success = new Forward("success");
        clearExpand();                
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "echo.jsp")
})
    protected Forward goSelectFrame()        {
        Forward success = new Forward("success");
        clearExpand();                
        String selectNode = getRequest().getParameter(TreeElement.SELECTED_NODE);
        TreeElement n = _tree37.findNode(selectNode);

	_action = n.getLabel();
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "echo.jsp")
})
    protected Forward goSelectFrameOverride()        {
        Forward success = new Forward("success");
        clearExpand();                
        String selectNode = getRequest().getParameter(TreeElement.SELECTED_NODE);
        TreeElement n = _tree37.findNode(selectNode);

	_action = "Override:" + n.getLabel();
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "treeFrame2.jsp")
})
    protected Forward goSelectTree()        {
        Forward success = new Forward("success");
        clearExpand();                
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "encodeContent.jsp")
})
    protected Forward goEncodeContent()        {
        Forward success = new Forward("success");
        clearExpand();                
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "noRoot.jsp")
})
    protected Forward goNoRoot()        {
        Forward success = new Forward("success");
        clearExpand();                
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "clientContentForm.jsp")
})
    protected Forward goClientContentForm()        {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(148);
        Forward success = new Forward("success");
        clearExpand();                
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "runAtClientError.jsp")
})
    protected Forward goRunAtClientError()        {
        Forward success = new Forward("success");
        clearExpand();                
        return success;
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "expandOnServer.jsp")
})
    protected Forward goExpandOnServer()        {
        Forward success = new Forward("success");
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(364);
        clearExpand();                
        return success;
    }
    
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "treeFrame.jsp")
})
    protected Forward goTreeFrame()        {
        Forward forward = new Forward("success");
        clearExpand();                
        return forward;
    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "treeSC.jsp")
})
    protected Forward goTreeSC()        {
        Forward forward = new Forward("success");
        clearExpand();                
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(223);
        return forward;
    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "treeSC2.jsp")
})
    protected Forward goTreeSC2()        {
        Forward forward = new Forward("success");
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(233);
        clearExpand();                
        return forward;
    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "treeSC3.jsp")
})
    protected Forward goTreeSC3()        {
        Forward forward = new Forward("success");
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(371);
        clearExpand();                
        return forward;
    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "treeSC4.jsp")
})
    protected Forward goTreeSC4()        {
        Forward forward = new Forward("success");
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(721);
        clearExpand();                
        return forward;
    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "dynamicClient.jsp")
})
    protected Forward goDynamicClient()        {
        Forward forward = new Forward("success");
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(319);
        clearExpand();                
        return forward;
    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "treeSCError.jsp")
})
    protected Forward goTreeSCError()        {
        Forward forward = new Forward("success");
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(443);
        clearExpand();                
        return forward;
    }

    public static final class FormBean implements Serializable
    {
        private String _text = null;

        public void setText( String text )
        {
            _text = text;
        }

        public String getText()
        {
            return _text;
        }
    }
}
