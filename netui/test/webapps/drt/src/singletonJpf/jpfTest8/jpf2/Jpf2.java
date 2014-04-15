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
package singletonJpf.jpfTest8.jpf2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" redirect="true" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoTraceResults" redirect="true" path="/resources/jsp/qaTraceResults.jsp"
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/singletonJpf/jpfTest8/jpf2/Jpf2.jpf"/>
 * <pageflow-object id="action:gotoJpf1.do#shared.FormA">
 *   <property value="500" name="x"/>
 *   <property value="260" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/done.jsp">
 *   <property value="140" name="x"/>
 *   <property value="260" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/error.jsp">
 *   <property value="140" name="x"/>
 *   <property value="420" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/qaTraceResults.jsp">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp2a.jsp@#@action:gotoJpf1.do#shared.FormA@">
 *   <property value="376,420,420,464" name="elbowsX"/>
 *   <property value="123,123,241,241" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:Jsp2a.jsp">
 *   <property value="340" name="x"/>
 *   <property value="120" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="external-jpf:/singletonJpf/jpfTest8/jpf1/Jpf1.jpf">
 *   <property value="640" name="x"/>
 *   <property value="260" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="external-jpf:/singletonJpf/jpfTest8/JpfTest8.jpf">
 *   <property value="640" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#go#/singletonJpf/jpfTest8/jpf1/Jpf1.jpf#@action:gotoJpf1.do#shared.FormA@">
 *   <property value="536,570,570,604" name="elbowsX"/>
 *   <property value="252,252,252,252" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="go" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoDone#/resources/jsp/done.jsp"/>
 * <pageflow-object id="forward:path#gotoError#/resources/jsp/error.jsp"/>
 * <pageflow-object id="forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp"/>
 * <pageflow-object id="action:begin.do#shared.FormA">
 *   <property value="140" name="x"/>
 *   <property value="120" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#go#Jsp2a.jsp#@action:begin.do#shared.FormA@">
 *   <property value="176,240,240,304" name="elbowsX"/>
 *   <property value="112,112,112,112" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="go" name="label"/>
 * </pageflow-object>
 * </view-properties>
 * ::
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
        "<pageflow-object id='pageflow:/singletonJpf/jpfTest8/jpf2/Jpf2.jpf'/>",
        "<pageflow-object id='action:gotoJpf1.do#shared.FormA'>",
        "  <property value='500' name='x'/>",
        "  <property value='260' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/done.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='260' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/error.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='420' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/qaTraceResults.jsp'>",
        "  <property value='60' name='x'/>",
        "  <property value='40' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp2a.jsp@#@action:gotoJpf1.do#shared.FormA@'>",
        "  <property value='376,420,420,464' name='elbowsX'/>",
        "  <property value='123,123,241,241' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:Jsp2a.jsp'>",
        "  <property value='340' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:/singletonJpf/jpfTest8/jpf1/Jpf1.jpf'>",
        "  <property value='640' name='x'/>",
        "  <property value='260' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:/singletonJpf/jpfTest8/JpfTest8.jpf'>",
        "  <property value='640' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#/singletonJpf/jpfTest8/jpf1/Jpf1.jpf#@action:gotoJpf1.do#shared.FormA@'>",
        "  <property value='536,570,570,604' name='elbowsX'/>",
        "  <property value='252,252,252,252' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>",
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>",
        "<pageflow-object id='forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp'/>",
        "<pageflow-object id='action:begin.do#shared.FormA'>",
        "  <property value='140' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#Jsp2a.jsp#@action:begin.do#shared.FormA@'>",
        "  <property value='176,240,240,304' name='elbowsX'/>",
        "  <property value='112,112,112,112' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Jpf2 extends PageFlowController
    {
    private static final String _JPF2_VALUE = "Jpf2 value";
    private shared.FormA _form = null;

    private shared.QaTrace _log = null;
    private int     _cnt = 0;

    /**
     * onCreate
     */
    public void onCreate() throws Exception
        {
        _log = shared.QaTrace.getTrace(getSession());
        _cnt = _log.newClass(this);
        _log.tracePoint("Jpf2.onCreate():" + _cnt);
        }

    /**
     * @jpf:action form="_form"
     * @jpf:forward name="go" path="Jsp2a.jsp"
     */
    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "Jsp2a.jsp") 
        })
    protected Forward begin(FormA form) throws Exception
        {
        _log.tracePoint("Jpf2.begin(FormA):" + _cnt);
        return new Forward("go", form);
        }

    /**
     * @jpf:action form="_form"
     * @jpf:forward name="go" path="/singletonJpf/jpfTest8/jpf1/Jpf1.jpf"
     */
    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "/singletonJpf/jpfTest8/jpf1/Jpf1.jpf") 
        })
    protected Forward gotoJpf1(FormA form)
        {
        _log.tracePoint("Jpf2.gotoJpf1(FormA):" + _cnt);
        form.setString1(_JPF2_VALUE);
        return new Forward("go", form);
        }
    }
