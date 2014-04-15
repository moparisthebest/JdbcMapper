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
package tree.treeAttribute;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller (
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="postback", navigateTo=Jpf.NavigateTo.currentPage),
        @Jpf.SimpleAction(name="goAttribute1", path="attribute1.jsp"),
        @Jpf.SimpleAction(name="goAttribute2", path="attribute2.jsp"),
        @Jpf.SimpleAction(name="goAttribute3", path="attribute3.jsp"),
        @Jpf.SimpleAction(name="goAttribute4", path="attribute4.jsp"),
        @Jpf.SimpleAction(name="goAttribute5", path="attribute5.jsp"),
        @Jpf.SimpleAction(name="goAttribute6", path="attribute6.jsp"),
        @Jpf.SimpleAction(name="goAttribute7", path="attribute7.jsp"),
        @Jpf.SimpleAction(name="goAttribute8", path="attribute8.jsp")
    }

)

public class Controller extends PageFlowController
{
    
    TreeElement _tree1;
    TreeElement _tree2;
    TreeElement _tree3;
    TreeElement _tree4;
    TreeElement _tree5;
    TreeElement _tree6;
    TreeElement _tree7;
    TreeElement _tree8;
    
    //**************************** TREE PROPERTIES ***************************
    
    public TreeElement getTree1() {
        return _tree1;
    }
    
    public void setTree1(TreeElement tree) {
        _tree1 = tree;
    }
    
    public TreeElement getTree2() {
        return _tree2;
    }
    
    public void setTree2(TreeElement tree) {
        _tree2 = tree;
    }
    
    public TreeElement getTree3() {
        return _tree3;
    }
    
    public void setTree3(TreeElement tree) {
        _tree3 = tree;
    }
    
    public TreeElement getTree4() {
        return _tree4;
    }
    
    public void setTree4(TreeElement tree) {
        _tree4 = tree;
    }

    public TreeElement getTree5() {
        return _tree5;
    }
    
    public void setTree5(TreeElement tree) {
        _tree5 = tree;
    }

    public TreeElement getTree6() {
        return _tree6;
    }
    
    public void setTree6(TreeElement tree) {
        _tree6 = tree;
    }

    public TreeElement getTree7() {
        return _tree7;
    }
    
    public void setTree7(TreeElement tree) {
        _tree7 = tree;
    }

    public TreeElement getTree8() {
        return _tree8;
    }
    
    public void setTree8(TreeElement tree) {
        _tree8 = tree;
    }
    
    
    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "index.jsp")
    })
    protected Forward resetTrees()        {
        Forward forward = new Forward("success");
            _tree1 = null;
            _tree2 = null;
            _tree3 = null;
            _tree4 = null;        
            _tree5 = null;        
            _tree6 = null;        
            _tree7 = null;        
            _tree8 = null;        
            return forward;
    }

}
