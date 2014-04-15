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
package scopedJpf.jpfTest2.subJpf1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller nested="true"
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/scopedJpf/jpfTest2/subJpf1/SubJpf1.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="100" name="x"/>
 *   <property value="120" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:finish.do">
 *   <property value="300" name="x"/>
 *   <property value="280" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1a.jsp@#@action:finish.do@">
 *   <property value="300,300,300,300" name="elbowsX"/>
 *   <property value="124,180,180,236" name="elbowsY"/>
 *   <property value="South_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:SubJsp1a.jsp">
 *   <property value="300" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#SubJsp1a.jsp#@action:begin.do@">
 *   <property value="136,200,200,264" name="elbowsX"/>
 *   <property value="112,112,72,72" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="exit:nestedDone">
 *   <property value="600" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#done#nestedDone#@action:finish.do@">
 *   <property value="336,450,450,564" name="elbowsX"/>
 *   <property value="261,261,72,72" name="elbowsY"/>
 *   <property value="East_0" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="done" name="label"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 *
 */
@Jpf.Controller(
    nested = true)
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/scopedJpf/jpfTest2/subJpf1/SubJpf1.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do'>",
        "  <property value='300' name='x'/>",
        "  <property value='280' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1a.jsp@#@action:finish.do@'>",
        "  <property value='300,300,300,300' name='elbowsX'/>",
        "  <property value='124,180,180,236' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:SubJsp1a.jsp'>",
        "  <property value='300' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#SubJsp1a.jsp#@action:begin.do@'>",
        "  <property value='136,200,200,264' name='elbowsX'/>",
        "  <property value='112,112,72,72' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='exit:nestedDone'>",
        "  <property value='600' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#done#nestedDone#@action:finish.do@'>",
        "  <property value='336,450,450,564' name='elbowsX'/>",
        "  <property value='261,261,72,72' name='elbowsY'/>",
        "  <property value='East_0' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='done' name='label'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class SubJpf1 extends PageFlowController {
    public String scope;

    /**
     * @jpf:action
     * @jpf:forward name="success" path="SubJsp1a.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "SubJsp1a.jsp") 
        })
    protected Forward begin() {
        scope = getRequest().getParameter("jpfScopeID");
        //System.out.println(">>> SubJpf1.begin - scope: " + scope);
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="nestedDone"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "done",
                returnAction = "nestedDone") 
        })
    public Forward finish() {
        //System.out.println(">>> SubJpf1.done: " + this.toString());
        return new Forward("done");
    }
}
