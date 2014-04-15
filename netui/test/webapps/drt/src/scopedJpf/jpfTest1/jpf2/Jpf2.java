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
package scopedJpf.jpfTest1.jpf2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/scopedJpf/jpfTest1/jpf2/Jpf2.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="640" name="x"/>
 *   <property value="240" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:refresh.do">
 *   <property value="940" name="x"/>
 *   <property value="380" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:goNested.do">
 *   <property value="140" name="x"/>
 *   <property value="240" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:nestedDone.do">
 *   <property value="500" name="x"/>
 *   <property value="360" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:finish.do">
 *   <property value="940" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/done.jsp">
 *   <property value="140" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/error.jsp">
 *   <property value="140" name="x"/>
 *   <property value="380" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp2.jsp@#@action:goNested.do@">
 *   <property value="764,470,470,176" name="elbowsX"/>
 *   <property value="303,303,232,232" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp2.jsp@#@action:refresh.do@">
 *   <property value="836,870,870,904" name="elbowsX"/>
 *   <property value="303,303,372,372" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp2.jsp@#@action:finish.do@">
 *   <property value="836,870,870,904" name="elbowsX"/>
 *   <property value="281,281,72,72" name="elbowsY"/>
 *   <property value="East_0" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:Jsp2.jsp">
 *   <property value="800" name="x"/>
 *   <property value="300" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@external-jpf:../subJpf1/SubJpf1.jpf@#@action:nestedDone.do@">
 *   <property value="376,420,420,464" name="elbowsX"/>
 *   <property value="232,232,341,341" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="external-jpf:../subJpf1/SubJpf1.jpf">
 *   <property value="340" name="x"/>
 *   <property value="240" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#Jsp2.jsp#@action:begin.do@">
 *   <property value="676,720,720,764" name="elbowsX"/>
 *   <property value="232,232,303,303" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_2" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#Jsp2.jsp#@action:refresh.do@">
 *   <property value="904,870,870,836" name="elbowsX"/>
 *   <property value="361,361,303,303" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_2" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#../subJpf1/SubJpf1.jpf#@action:goNested.do@">
 *   <property value="176,240,240,304" name="elbowsX"/>
 *   <property value="232,232,232,232" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="return-to:@forward:return-to#success#currentPage#@action:nestedDone.do@@">
 *   <property value="161" name="x"/>
 *   <property value="164" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-to#success#currentPage#@action:nestedDone.do@">
 *   <property value="464,330,330,197" name="elbowsX"/>
 *   <property value="352,352,156,156" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoDone#/resources/jsp/done.jsp"/>
 * <pageflow-object id="forward:path#gotoError#/resources/jsp/error.jsp"/>
 * <pageflow-object id="action-call:@page:Jsp2.jsp@#@action:nestedDone.do@">
 *   <property value="764,650,650,536" name="elbowsX"/>
 *   <property value="303,303,341,341" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="East_0" name="toPort"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 *
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "gotoDone",
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp") 
    })
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/scopedJpf/jpfTest1/jpf2/Jpf2.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='640' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:refresh.do'>",
        "  <property value='940' name='x'/>",
        "  <property value='380' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:goNested.do'>",
        "  <property value='140' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:nestedDone.do'>",
        "  <property value='500' name='x'/>",
        "  <property value='360' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do'>",
        "  <property value='940' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/done.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/error.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='380' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp2.jsp@#@action:goNested.do@'>",
        "  <property value='764,470,470,176' name='elbowsX'/>",
        "  <property value='303,303,232,232' name='elbowsY'/>",
        "  <property value='West_2' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp2.jsp@#@action:refresh.do@'>",
        "  <property value='836,870,870,904' name='elbowsX'/>",
        "  <property value='303,303,372,372' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp2.jsp@#@action:finish.do@'>",
        "  <property value='836,870,870,904' name='elbowsX'/>",
        "  <property value='281,281,72,72' name='elbowsY'/>",
        "  <property value='East_0' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:Jsp2.jsp'>",
        "  <property value='800' name='x'/>",
        "  <property value='300' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:../subJpf1/SubJpf1.jpf@#@action:nestedDone.do@'>",
        "  <property value='376,420,420,464' name='elbowsX'/>",
        "  <property value='232,232,341,341' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:../subJpf1/SubJpf1.jpf'>",
        "  <property value='340' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#Jsp2.jsp#@action:begin.do@'>",
        "  <property value='676,720,720,764' name='elbowsX'/>",
        "  <property value='232,232,303,303' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#Jsp2.jsp#@action:refresh.do@'>",
        "  <property value='904,870,870,836' name='elbowsX'/>",
        "  <property value='361,361,303,303' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_2' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#../subJpf1/SubJpf1.jpf#@action:goNested.do@'>",
        "  <property value='176,240,240,304' name='elbowsX'/>",
        "  <property value='232,232,232,232' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='return-to:@forward:return-to#success#currentPage#@action:nestedDone.do@@'>",
        "  <property value='161' name='x'/>",
        "  <property value='164' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-to#success#currentPage#@action:nestedDone.do@'>",
        "  <property value='464,330,330,197' name='elbowsX'/>",
        "  <property value='352,352,156,156' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>",
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>",
        "<pageflow-object id='action-call:@page:Jsp2.jsp@#@action:nestedDone.do@'>",
        "  <property value='764,650,650,536' name='elbowsX'/>",
        "  <property value='303,303,341,341' name='elbowsY'/>",
        "  <property value='West_2' name='fromPort'/>",
        "  <property value='East_0' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Jpf2 extends PageFlowController
    {
    public          String  scope;

    /**
     * @jpf:action
     * @jpf:forward name="success" path="Jsp2.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Jsp2.jsp") 
        })
    protected Forward begin()
        {
        scope = getRequest().getParameter("jpfScopeID");
        //System.out.println(">>> Jpf2.begin - scope: " + scope);
        return new Forward( "success" );
        }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="Jsp2.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Jsp2.jsp") 
        })
    protected Forward refresh()
        {
            //System.out.println(">>> Jpf2.refresh: " + this.toString());
        return new Forward( "success" );
        }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="../subJpf1/SubJpf1.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "../subJpf1/SubJpf1.jpf") 
        })
    protected Forward goNested()
        {
            //System.out.println(">>> Jpf2.goNested: " + this.toString());
        return new Forward( "success" );
        }

    /**
     * @jpf:action
     * @jpf:forward name="success" return-to="currentPage"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward nestedDone()
        {
            //System.out.println(">>> Jpf2.nestedDone: " + this.toString());
        return new Forward( "success" );
        }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward finish()
       {
           //System.out.println(">>> Jpf2.finish");
       return new Forward("gotoDone");
       }
    }
