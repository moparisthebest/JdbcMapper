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
package template.divPanelTree;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.tree.TreeElement;

/**
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/divPanelTree/Controller.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:index.jsp">
 *   <property value="240" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#index.jsp#@action:begin.do@">
 *   <property value="116,160,160,204" name="elbowsX"/>
 *   <property value="92,92,103,103" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_2" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="action:treePost.do">
 *   <property value="360" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:treePost.do@">
 *   <property value="276,300,300,324" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#index.jsp#@action:treePost.do@">
 *   <property value="324,300,300,276" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */
@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/divPanelTree/Controller.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='80' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>",
        "  <property value='116,160,160,204' name='elbowsX'/>",
        "  <property value='92,92,103,103' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:treePost.do'>",
        "  <property value='360' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:treePost.do@'>",
        "  <property value='276,300,300,324' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:treePost.do@'>",
        "  <property value='324,300,300,276' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Controller extends PageFlowController
{

    private TreeElement tree;
    public TreeElement getTree() {
        return tree;
    }
    public void setTree(TreeElement t) {
        tree = t;
    }

    /**
     * This method represents the point of entry into the pageflow
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(233);
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward treePost()
    {
        return new Forward("success");
    }
}
