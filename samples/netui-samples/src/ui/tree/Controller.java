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
package ui.tree;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.tags.tree.TreeElement;

@Jpf.Controller()
public class Controller
    extends PageFlowController {

    private TreeElement _root;

    public TreeElement getRoot() {
        return _root;
    }

    public void setRoot(TreeElement tree) {
	    _root = tree;
    }

    @Jpf.Action(
       forwards={
        @Jpf.Forward(name="success", path="frameSet.jsp")
       }
    )
    protected Forward begin()
    {
        return new Forward("success");
    }

    @Jpf.Action(
      forwards = { 
	    @Jpf.Forward(name = "success", path = "echo.jsp"),
	    @Jpf.Forward(name = "0.0.0.0", path = "content/0.0.0.0.jsp"),
	    @Jpf.Forward(name = "0.0.0.1", path = "content/0.0.0.1.jsp"),
	    @Jpf.Forward(name = "0.0.0", path = "content/0.0.0.jsp"),
	    @Jpf.Forward(name = "0.0", path = "content/0.0.jsp"),
	    @Jpf.Forward(name = "0.1.0", path = "content/0.1.0.jsp"),
	    @Jpf.Forward(name = "0.1.1", path = "content/0.1.1.jsp"),
	    @Jpf.Forward(name = "0.1", path = "content/0.1.jsp"),
	    @Jpf.Forward(name = "0.2.0", path = "content/0.2.0.jsp"),
	    @Jpf.Forward(name = "0.2.1", path = "content/0.2.1.jsp"),
	    @Jpf.Forward(name = "0.2", path = "content/0.2.jsp"),
	    @Jpf.Forward(name = "0", path = "content/0.jsp")
      }
    )
    protected Forward selectFrame()        
    {

        String selectNode = getRequest().getParameter(TreeElement.SELECTED_NODE);
        TreeElement n = _root.findNode(selectNode);

	    return new Forward( n.getTitle() );
    }

    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "tree.jsp")
})
    protected Forward selectTree()        {
        Forward success = new Forward("success");               
        return success;
    }
}

