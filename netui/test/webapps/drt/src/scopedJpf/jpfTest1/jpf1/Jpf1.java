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
package scopedJpf.jpfTest1.jpf1;

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
 * <pageflow-object id="pageflow:/scopedJpf/jpfTest1/jpf1/Jpf1.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:refresh.do">
 *   <property value="240" name="x"/>
 *   <property value="340" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:goNested.do">
 *   <property value="100" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:nestedDone.do">
 *   <property value="150" name="x"/>
 *   <property value="250" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:finish.do">
 *   <property value="360" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/done.jsp">
 *   <property value="180" name="x"/>
 *   <property value="160" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/error.jsp">
 *   <property value="210" name="x"/>
 *   <property value="190" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp1.jsp@#@action:goNested.do@">
 *   <property value="234,185,185,136" name="elbowsX"/>
 *   <property value="253,253,72,72" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp1.jsp@#@action:finish.do@">
 *   <property value="276,300,300,324" name="elbowsX"/>
 *   <property value="212,212,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp1.jsp@#@action:refresh.do@">
 *   <property value="259,259,276,276" name="elbowsX"/>
 *   <property value="294,299,299,332" name="elbowsY"/>
 *   <property value="South_0" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:Jsp1.jsp">
 *   <property value="270" name="x"/>
 *   <property value="250" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@external-jpf:../subJpf1/SubJpf1.jpf@#@action:nestedDone.do@">
 *   <property value="234,210,210,186" name="elbowsX"/>
 *   <property value="253,253,242,242" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="external-jpf:../subJpf1/SubJpf1.jpf">
 *   <property value="270" name="x"/>
 *   <property value="250" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#Jsp1.jsp#@action:begin.do@">
 *   <property value="96,150,150,204" name="elbowsX"/>
 *   <property value="43,43,212,212" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#Jsp1.jsp#@action:refresh.do@">
 *   <property value="240,240,240,240" name="elbowsX"/>
 *   <property value="296,280,280,264" name="elbowsY"/>
 *   <property value="North_1" name="fromPort"/>
 *   <property value="South_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#../subJpf1/SubJpf1.jpf#@action:goNested.do@">
 *   <property value="136,185,185,234" name="elbowsX"/>
 *   <property value="72,72,242,242" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="return-to:@forward:return-to#success#currentPage#@action:nestedDone.do@@">
 *   <property value="321" name="x"/>
 *   <property value="324" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-to#success#currentPage#@action:nestedDone.do@">
 *   <property value="186,235,235,285" name="elbowsX"/>
 *   <property value="253,253,316,316" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoDone#/resources/jsp/done.jsp"/>
 * <pageflow-object id="forward:path#gotoError#/resources/jsp/error.jsp"/>
 * <pageflow-object id="action-call:@page:Jsp1.jsp@#@action:nestedDone.do@">
 *   <property value="234,210,210,186" name="elbowsX"/>
 *   <property value="253,253,242,242" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
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
        "<pageflow-object id='pageflow:/scopedJpf/jpfTest1/jpf1/Jpf1.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='60' name='x'/>",
        "  <property value='40' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:refresh.do'>",
        "  <property value='240' name='x'/>",
        "  <property value='340' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:goNested.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:nestedDone.do'>",
        "  <property value='150' name='x'/>",
        "  <property value='250' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do'>",
        "  <property value='360' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/done.jsp'>",
        "  <property value='180' name='x'/>",
        "  <property value='160' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/error.jsp'>",
        "  <property value='210' name='x'/>",
        "  <property value='190' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:goNested.do@'>",
        "  <property value='234,185,185,136' name='elbowsX'/>",
        "  <property value='253,253,72,72' name='elbowsY'/>",
        "  <property value='West_2' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:finish.do@'>",
        "  <property value='276,300,300,324' name='elbowsX'/>",
        "  <property value='212,212,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:refresh.do@'>",
        "  <property value='259,259,276,276' name='elbowsX'/>",
        "  <property value='294,299,299,332' name='elbowsY'/>",
        "  <property value='South_0' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:Jsp1.jsp'>",
        "  <property value='270' name='x'/>",
        "  <property value='250' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:../subJpf1/SubJpf1.jpf@#@action:nestedDone.do@'>",
        "  <property value='234,210,210,186' name='elbowsX'/>",
        "  <property value='253,253,242,242' name='elbowsY'/>",
        "  <property value='West_2' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:../subJpf1/SubJpf1.jpf'>",
        "  <property value='270' name='x'/>",
        "  <property value='250' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#Jsp1.jsp#@action:begin.do@'>",
        "  <property value='96,150,150,204' name='elbowsX'/>",
        "  <property value='43,43,212,212' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#Jsp1.jsp#@action:refresh.do@'>",
        "  <property value='240,240,240,240' name='elbowsX'/>",
        "  <property value='296,280,280,264' name='elbowsY'/>",
        "  <property value='North_1' name='fromPort'/>",
        "  <property value='South_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#../subJpf1/SubJpf1.jpf#@action:goNested.do@'>",
        "  <property value='136,185,185,234' name='elbowsX'/>",
        "  <property value='72,72,242,242' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='return-to:@forward:return-to#success#currentPage#@action:nestedDone.do@@'>",
        "  <property value='321' name='x'/>",
        "  <property value='324' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-to#success#currentPage#@action:nestedDone.do@'>",
        "  <property value='186,235,235,285' name='elbowsX'/>",
        "  <property value='253,253,316,316' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>",
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>",
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:nestedDone.do@'>",
        "  <property value='234,210,210,186' name='elbowsX'/>",
        "  <property value='253,253,242,242' name='elbowsY'/>",
        "  <property value='West_2' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Jpf1 extends PageFlowController
    {
    public          String  scope;

    /**
     * @jpf:action
     * @jpf:forward name="success" path="Jsp1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Jsp1.jsp") 
        })
    protected Forward begin()
        {
        scope = getRequest().getParameter("jpfScopeID");
        //System.out.println(">>> Jpf1.begin - scope: " + scope);
        return new Forward( "success" );
        }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="Jsp1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Jsp1.jsp") 
        })
    protected Forward refresh()
        {
            //System.out.println(">>> Jpf1.refresh: " + this.toString());
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
            //System.out.println(">>> Jpf1.goNested: " + this.toString());
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
            //System.out.println(">>> Jpf1.nestedDone: " + this.toString());
        return new Forward( "success" );
        }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward finish()
       {
           //System.out.println(">>> Jpf1.finish");
       return new Forward("gotoDone");
       }
    }
