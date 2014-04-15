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
package singletonJpf.jpfTest6;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.QaTrace;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" redirect="true" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoTraceResults" redirect="true" path="/resources/jsp/qaTraceResults.jsp"
 * @jpf:view-properties view-properties::
 *   <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 *   <view-properties>
 *   <pageflow-object id="pageflow:/singletonJpf/jpfTest6/JpfTest6.jpf"/>
 *   <pageflow-object id="action:begin.do">
 *     <property value="140" name="x"/>
 *     <property value="120" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="action:gotoJpf1.do">
 *     <property value="500" name="x"/>
 *     <property value="380" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="action:gotoJpf2.do">
 *     <property value="500" name="x"/>
 *     <property value="80" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="action:finish.do">
 *     <property value="500" name="x"/>
 *     <property value="240" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="page:/resources/jsp/qaTraceResults.jsp">
 *     <property value="140" name="x"/>
 *     <property value="260" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="page:/resources/jsp/error.jsp">
 *     <property value="140" name="x"/>
 *     <property value="420" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="action-call:@page:JspTest6a.jsp@#@action:finish.do@">
 *     <property value="376,420,420,464" name="elbowsX"/>
 *     <property value="112,112,232,232" name="elbowsY"/>
 *     <property value="East_1" name="fromPort"/>
 *     <property value="West_1" name="toPort"/>
 *   </pageflow-object>
 *   <pageflow-object id="action-call:@page:JspTest6a.jsp@#@action:gotoJpf1.do@">
 *     <property value="376,420,420,464" name="elbowsX"/>
 *     <property value="123,123,361,361" name="elbowsY"/>
 *     <property value="East_2" name="fromPort"/>
 *     <property value="West_0" name="toPort"/>
 *   </pageflow-object>
 *   <pageflow-object id="action-call:@page:JspTest6a.jsp@#@action:gotoJpf2.do@">
 *     <property value="376,420,420,464" name="elbowsX"/>
 *     <property value="101,101,83,83" name="elbowsY"/>
 *     <property value="East_0" name="fromPort"/>
 *     <property value="West_2" name="toPort"/>
 *   </pageflow-object>
 *   <pageflow-object id="page:JspTest6a.jsp">
 *     <property value="340" name="x"/>
 *     <property value="120" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="external-jpf:jpf1/Jpf1.jpf">
 *     <property value="640" name="x"/>
 *     <property value="380" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="external-jpf:jpf2/Jpf2.jpf">
 *     <property value="640" name="x"/>
 *     <property value="80" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="page:StartTest.jsp">
 *     <property value="140" name="x"/>
 *     <property value="560" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#go#JspTest6a.jsp#@action:begin.do@">
 *     <property value="176,240,240,304" name="elbowsX"/>
 *     <property value="112,112,123,123" name="elbowsY"/>
 *     <property value="East_1" name="fromPort"/>
 *     <property value="West_2" name="toPort"/>
 *     <property value="go" name="label"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#go#jpf1/Jpf1.jpf#@action:gotoJpf1.do@">
 *     <property value="536,570,570,604" name="elbowsX"/>
 *     <property value="372,372,372,372" name="elbowsY"/>
 *     <property value="East_1" name="fromPort"/>
 *     <property value="West_1" name="toPort"/>
 *     <property value="go" name="label"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#go#jpf2/Jpf2.jpf#@action:gotoJpf2.do@">
 *     <property value="536,570,570,604" name="elbowsX"/>
 *     <property value="72,72,72,72" name="elbowsY"/>
 *     <property value="East_1" name="fromPort"/>
 *     <property value="West_1" name="toPort"/>
 *     <property value="go" name="label"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp"/>
 *   <pageflow-object id="forward:path#gotoError#/resources/jsp/error.jsp"/>
 *   <pageflow-object id="page:/resources/jsp/done.jsp">
 *     <property value="60" name="x"/>
 *     <property value="40" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#gotoDone#/resources/jsp/done.jsp"/>
 *   </view-properties>
 *   ::
 *
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "gotoDone",
            redirect = true,
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp"),
        @Jpf.Forward(
            name = "gotoTraceResults",
            redirect = true,
            path = "/resources/jsp/qaTraceResults.jsp") 
    })
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/singletonJpf/jpfTest6/JpfTest6.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='140' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:gotoJpf1.do'>",
        "  <property value='500' name='x'/>",
        "  <property value='380' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:gotoJpf2.do'>",
        "  <property value='500' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do'>",
        "  <property value='500' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/qaTraceResults.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='260' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/error.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='420' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:JspTest6a.jsp@#@action:finish.do@'>",
        "  <property value='376,420,420,464' name='elbowsX'/>",
        "  <property value='112,112,232,232' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:JspTest6a.jsp@#@action:gotoJpf1.do@'>",
        "  <property value='376,420,420,464' name='elbowsX'/>",
        "  <property value='123,123,361,361' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:JspTest6a.jsp@#@action:gotoJpf2.do@'>",
        "  <property value='376,420,420,464' name='elbowsX'/>",
        "  <property value='101,101,83,83' name='elbowsY'/>",
        "  <property value='East_0' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:JspTest6a.jsp'>",
        "  <property value='340' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:jpf1/Jpf1.jpf'>",
        "  <property value='640' name='x'/>",
        "  <property value='380' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:jpf2/Jpf2.jpf'>",
        "  <property value='640' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:StartTest.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='560' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#JspTest6a.jsp#@action:begin.do@'>",
        "  <property value='176,240,240,304' name='elbowsX'/>",
        "  <property value='112,112,123,123' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#jpf1/Jpf1.jpf#@action:gotoJpf1.do@'>",
        "  <property value='536,570,570,604' name='elbowsX'/>",
        "  <property value='372,372,372,372' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#jpf2/Jpf2.jpf#@action:gotoJpf2.do@'>",
        "  <property value='536,570,570,604' name='elbowsX'/>",
        "  <property value='72,72,72,72' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp'/>",
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>",
        "<pageflow-object id='page:/resources/jsp/done.jsp'>",
        "  <property value='60' name='x'/>",
        "  <property value='40' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>",
        "</view-properties>"
    })
public class JpfTest6 extends PageFlowController
    {
    private QaTrace _log = null;
    private int     _cnt = 0;

    /**
     * onCreate
     */
    public void onCreate() throws Exception
        {
        _log = QaTrace.getTrace(getSession(), true);
        _cnt = _log.newClass(this);
        _log.tracePoint("JpfTest6.onCreate:" + _cnt);
        }

    /**
     * @jpf:action
     * @jpf:forward name="go" path="JspTest6a.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "JspTest6a.jsp") 
        })
    protected Forward begin() throws Exception
        {
        _log.tracePoint("JpfTest6.begin:" + _cnt);
        return new Forward("go");
        }

    /**
     * @jpf:action
     * @jpf:forward name="go" path="jpf1/Jpf1.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "jpf1/Jpf1.jpf") 
        })
    protected Forward gotoJpf1()
        {
        _log.tracePoint("JpfTest6.gotoJpf1:" + _cnt);
        return new Forward("go");
        }

    /**
     * @jpf:action
     * @jpf:forward name="go" path="jpf2/Jpf2.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "jpf2/Jpf2.jpf") 
        })
    protected Forward gotoJpf2()
        {
        _log.tracePoint("JpfTest6.gotoJpf2:" + _cnt);
        return new Forward("go");
        }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward finish()
        {
        _log.tracePoint("JpfTest6.finish:" + _cnt);
        return new Forward("gotoTraceResults");
        }
    }
