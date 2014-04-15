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
package singletonJpf.jpfTest6.jpf1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;
import shared.QaTrace;

/**
 * @jpf:controller singleton="true"
 * @jpf:forward name="gotoDone" redirect="true" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoTraceResults" redirect="true" path="/resources/jsp/qaTraceResults.jsp"
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/singletonJpf/jpfTest6/jpf1/Jpf1.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:gotoJpf2.do#shared.FormA">
 *   <property value="310" name="x"/>
 *   <property value="170" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:finish.do#shared.FormA">
 *   <property value="190" name="x"/>
 *   <property value="290" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/done.jsp">
 *   <property value="130" name="x"/>
 *   <property value="110" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/error.jsp">
 *   <property value="160" name="x"/>
 *   <property value="140" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp1a.jsp@#@action:gotoJpf2.do#shared.FormA@">
 *   <property value="226,250,250,274" name="elbowsX"/>
 *   <property value="162,162,162,162" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp1a.jsp@#@action:finish.do#shared.FormA@">
 *   <property value="190,190,190,190" name="elbowsX"/>
 *   <property value="214,230,230,246" name="elbowsY"/>
 *   <property value="South_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:Jsp1a.jsp">
 *   <property value="190" name="x"/>
 *   <property value="170" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="external-jpf:/singletonJpf/jpfTest6/jpf2/Jpf2.jpf">
 *   <property value="80" name="x"/>
 *   <property value="60" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="external-jpf:/singletonJpf/jpfTest6/JpfTest6.jpf">
 *   <property value="90" name="x"/>
 *   <property value="70" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#go#Jsp1a.jsp#@action:begin.do@">
 *   <property value="96,125,125,154" name="elbowsX"/>
 *   <property value="32,32,162,162" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="go" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#go#/singletonJpf/jpfTest6/jpf2/Jpf2.jpf#@action:gotoJpf2.do#shared.FormA@">
 *   <property value="274,195,195,116" name="elbowsX"/>
 *   <property value="151,151,52,52" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 *   <property value="go" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#go#/singletonJpf/jpfTest6/JpfTest6.jpf#@action:finish.do#shared.FormA@">
 *   <property value="154,140,140,126" name="elbowsX"/>
 *   <property value="282,282,62,62" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 *   <property value="go" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoDone#/resources/jsp/done.jsp"/>
 * <pageflow-object id="forward:path#gotoError#/resources/jsp/error.jsp"/>
 * <pageflow-object id="page:/resources/jsp/qaTraceResults.jsp">
 *   <property value="220" name="x"/>
 *   <property value="200" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp"/>
 * </view-properties>
 * ::
 *
 */
@Jpf.Controller(
    longLived = true,
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
        "<pageflow-object id='pageflow:/singletonJpf/jpfTest6/jpf1/Jpf1.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='60' name='x'/>",
        "  <property value='40' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:gotoJpf2.do#shared.FormA'>",
        "  <property value='310' name='x'/>",
        "  <property value='170' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do#shared.FormA'>",
        "  <property value='190' name='x'/>",
        "  <property value='290' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/done.jsp'>",
        "  <property value='130' name='x'/>",
        "  <property value='110' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/error.jsp'>",
        "  <property value='160' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp1a.jsp@#@action:gotoJpf2.do#shared.FormA@'>",
        "  <property value='226,250,250,274' name='elbowsX'/>",
        "  <property value='162,162,162,162' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp1a.jsp@#@action:finish.do#shared.FormA@'>",
        "  <property value='190,190,190,190' name='elbowsX'/>",
        "  <property value='214,230,230,246' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:Jsp1a.jsp'>",
        "  <property value='190' name='x'/>",
        "  <property value='170' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:/singletonJpf/jpfTest6/jpf2/Jpf2.jpf'>",
        "  <property value='80' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:/singletonJpf/jpfTest6/JpfTest6.jpf'>",
        "  <property value='90' name='x'/>",
        "  <property value='70' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#Jsp1a.jsp#@action:begin.do@'>",
        "  <property value='96,125,125,154' name='elbowsX'/>",
        "  <property value='32,32,162,162' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#/singletonJpf/jpfTest6/jpf2/Jpf2.jpf#@action:gotoJpf2.do#shared.FormA@'>",
        "  <property value='274,195,195,116' name='elbowsX'/>",
        "  <property value='151,151,52,52' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#/singletonJpf/jpfTest6/JpfTest6.jpf#@action:finish.do#shared.FormA@'>",
        "  <property value='154,140,140,126' name='elbowsX'/>",
        "  <property value='282,282,62,62' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>",
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>",
        "<pageflow-object id='page:/resources/jsp/qaTraceResults.jsp'>",
        "  <property value='220' name='x'/>",
        "  <property value='200' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp'/>",
        "</view-properties>"
    })
public class Jpf1 extends PageFlowController
    {
    private static final String _INIT_VALUE = "Initial value";
    private static final String _NEW_VALUE = "New value";
    public String field1 = null;
    private FormA _form = null;

    private QaTrace _log = null;
    private int     _cnt = 0;

    /**
     * onCreate
     */
    public void onCreate() throws Exception
        {
        _log = QaTrace.getTrace(getSession());
        _cnt = _log.newClass(this);
        _log.tracePoint("Jpf1.onCreate:" + _cnt);
        }

    /**
     * @jpf:action
     * @jpf:forward name="go" path="Jsp1a.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "Jsp1a.jsp") 
        })
    protected Forward begin() throws Exception
        {
        _log.tracePoint("Jpf1.begin:" + _cnt);
        if (_form == null)
            {
            _form = new FormA(getSession());
            _form.setString1(_INIT_VALUE);
            }
        if (field1 == null)
            {
            field1 = _INIT_VALUE;
            }
        return new Forward("go", _form);
        }

    /**
     * @jpf:action form="_form"
     * @jpf:forward name="go" path="/singletonJpf/jpfTest6/jpf2/Jpf2.jpf"
     */
    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "/singletonJpf/jpfTest6/jpf2/Jpf2.jpf") 
        })
    protected Forward gotoJpf2(FormA form)
        {
        _log.tracePoint("Jpf1.gotoJpf2:" + _cnt);
        field1 = _NEW_VALUE;
        form.setString1(_NEW_VALUE);
        return new Forward("go");
        }

    /**
     * @jpf:action form="_form"
     * @jpf:forward name="go" path="/singletonJpf/jpfTest6/JpfTest6.jpf"
     */
    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "/singletonJpf/jpfTest6/JpfTest6.jpf") 
        })
    protected Forward finish(FormA form)
        {
        _log.tracePoint("Jpf1.finish:" + _cnt);
        return new Forward("go");
        }
    }
