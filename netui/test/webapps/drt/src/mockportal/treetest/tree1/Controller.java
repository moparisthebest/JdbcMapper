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
package mockportal.treetest.tree1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.tree.ITreeRootElement;
import org.apache.beehive.netui.tags.tree.TreeElement;
import org.apache.beehive.netui.tags.tree.TreeRenderState;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

@Jpf.Controller (
    simpleActions = {
        @Jpf.SimpleAction(name = "select", path = "index.jsp")
    }
)
public class Controller extends PageFlowController
{
    private TreeElement _root;
    private int[] _children = {3,2,0,2,0,2,1,1,2};
    private int _child = 0;

    public TreeElement getRoot()
    {
        return _root;
    }

    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="index", path="index.jsp")
       }
    )
    protected Forward begin()
    {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(9);
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward reset()
    {
	_child = 0;
        _root = new ToolTreeRootElement("0", false, getChildCount());
        return new Forward("index");
    }

    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
        _root = new ToolTreeRootElement("0", false, getChildCount());
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }

    private int getChildCount()
    {
        if (_child == _children.length)
            return 0;
        return _children[_child++];
    }

    class ToolTreeElement extends TreeElement
    {
        private boolean _leaf;
        private int _children;

        public ToolTreeElement(String s, boolean b, int children)
        {
            super(s, b);
            _leaf = (children == 0);
            _children  = children;
            setExpandOnServer(!_leaf);
        }

        public void onExpand(ServletRequest request, ServletResponse response)
        {
            if (_children > 0 && size() == 0) {
                for (int i=0;i<_children;i++) {
                    String name = getLabel() + "." + i;
                    ToolTreeElement m = new ToolTreeElement(name, false, getChildCount());
                    addChild(m);
                }
            }
        }

        public boolean isLeaf()
        {
            return _leaf;
        }
    }

    class ToolTreeRootElement extends ToolTreeElement implements ITreeRootElement
    {
        private String _name = null;
        private TreeRenderState _trs = null;
        private TreeElement _selectedNode;
	private String _rootNodeExpandedImage;
	private String _rootNodeCollapsedImage;

        public ToolTreeRootElement(String s, boolean b, int children)
        {
            super(s, b, children);
        }

	public TreeElement getSelectedNode() {
	    return _selectedNode;
	}

        public void changeSelected(String selectNode,ServletRequest request)
        {
            // if there is a selectedNode then we need to raise the onSelect
            // event on that node indicating it will soon not be selected
            TreeElement n = findNode(selectNode);
            if (n == null) {
                return;
            }

            // change the node that was selected so it is no longer selected
            if (_selectedNode != null) {
                _selectedNode.onSelect(request);
                _selectedNode.setSelected(false);
            }

            // change the node that is to be selected
            n.onSelect(request);
            n.setSelected(true);
            _selectedNode = n;
            return;
        }

        public TreeRenderState getTreeRenderState()
        {
            return _trs;
        }

        public void setTreeRenderState(TreeRenderState treeRenderState)
        {
            _trs = treeRenderState;
        }

        public void setObjectName(String s)
        {
            _name = s;
        }

        public String getObjectName()
        {
            return _name;
        }

	/**
	 * @return
	 */
	public String getRootNodeExpandedImage()
	{
	    return _rootNodeExpandedImage;
	}
	
	/**
	 * @param rootNodeExpandedImage
	 */
	public void setRootNodeExpandedImage(String rootNodeExpandedImage)
	{
	    _rootNodeExpandedImage = rootNodeExpandedImage;
	}
	
	/**
	 * @return
	 */
	public String getRootNodeCollapsedImage()
	{
	    return _rootNodeCollapsedImage;
	}
	
	/**
	 * @param rootNodeCollapsedImage
	 */
	public void setRootNodeCollapsedImage(String rootNodeCollapsedImage)
	{
	    _rootNodeCollapsedImage = rootNodeCollapsedImage;
	}
    }
}

