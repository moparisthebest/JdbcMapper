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
package tree.treeMultiAttr;

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
        @Jpf.SimpleAction(name="goAttribute5", path="attribute5.jsp")
    }
)
@Jpf.ViewProperties(
    value = { 
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
        "<view-properties>", 
        "<pageflow-object id='pageflow:/treeMultiAttr/Controller.jpf'/>", 
        "<pageflow-object id='action:resetTrees.do'>", 
        "  <property value='60' name='x'/>", 
        "  <property value='180' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:index.jsp'>", 
        "  <property value='200' name='x'/>", 
        "  <property value='180' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#success#index.jsp#@action:resetTrees.do@'>", 
        "  <property value='96,130,130,164' name='elbowsX'/>", 
        "  <property value='161,161,172,172' name='elbowsY'/>", 
        "  <property value='East_0' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "  <property value='success' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:attribute1.jsp'>", 
        "  <property value='480' name='x'/>", 
        "  <property value='40' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:attribute2.jsp'>", 
        "  <property value='480' name='x'/>", 
        "  <property value='100' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:attribute3.jsp'>", 
        "  <property value='480' name='x'/>", 
        "  <property value='160' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:attribute4.jsp'>", 
        "  <property value='480' name='x'/>", 
        "  <property value='240' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:resetTrees.do@'>", 
        "  <property value='164,130,130,96' name='elbowsX'/>", 
        "  <property value='183,183,172,172' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:begin.do'>", 
        "  <property value='380' name='x'/>", 
        "  <property value='180' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:attribute4.jsp@#@action:begin.do@'>", 
        "  <property value='444,430,430,416' name='elbowsX'/>", 
        "  <property value='232,232,183,183' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_2' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:goAttribute1.do'>", 
        "  <property value='300' name='x'/>", 
        "  <property value='40' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:goAttribute2.do'>", 
        "  <property value='300' name='x'/>", 
        "  <property value='100' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:goAttribute3.do'>", 
        "  <property value='300' name='x'/>", 
        "  <property value='160' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:goAttribute4.do'>", 
        "  <property value='300' name='x'/>", 
        "  <property value='240' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:goAttribute4.do@'>", 
        "  <property value='236,250,250,264' name='elbowsX'/>", 
        "  <property value='183,183,232,232' name='elbowsY'/>", 
        "  <property value='East_2' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:goAttribute3.do@'>", 
        "  <property value='236,250,250,264' name='elbowsX'/>", 
        "  <property value='183,183,152,152' name='elbowsY'/>", 
        "  <property value='East_2' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:goAttribute2.do@'>", 
        "  <property value='236,250,250,264' name='elbowsX'/>", 
        "  <property value='161,161,92,92' name='elbowsY'/>", 
        "  <property value='East_0' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:goAttribute1.do@'>", 
        "  <property value='236,250,250,264' name='elbowsX'/>", 
        "  <property value='172,172,32,32' name='elbowsY'/>", 
        "  <property value='East_1' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:attribute1.jsp@#@action:begin.do@'>", 
        "  <property value='444,430,430,416' name='elbowsX'/>", 
        "  <property value='32,32,172,172' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:attribute2.jsp@#@action:begin.do@'>", 
        "  <property value='444,430,430,416' name='elbowsX'/>", 
        "  <property value='92,92,183,183' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_2' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:attribute3.jsp@#@action:begin.do@'>", 
        "  <property value='444,430,430,416' name='elbowsX'/>", 
        "  <property value='152,152,183,183' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_2' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:attribute5.jsp'>", 
        "  <property value='480' name='x'/>", 
        "  <property value='300' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:attribute5.jsp@#@action:begin.do@'>", 
        "  <property value='444,430,430,416' name='elbowsX'/>", 
        "  <property value='292,292,183,183' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_2' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:goAttribute5.do'>", 
        "  <property value='300' name='x'/>", 
        "  <property value='300' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:goAttribute5.do@'>", 
        "  <property value='236,250,250,264' name='elbowsX'/>", 
        "  <property value='183,183,292,292' name='elbowsY'/>", 
        "  <property value='East_2' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "</view-properties>"
    }
)
public class Controller extends PageFlowController
{
    TreeElement _tree1;
    TreeElement _tree2;
    TreeElement _tree3;
    TreeElement _tree4;
    TreeElement _tree5;
    
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
        
        return forward;
    }
}
