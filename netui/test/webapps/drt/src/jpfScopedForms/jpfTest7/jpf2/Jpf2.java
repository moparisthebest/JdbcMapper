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
package jpfScopedForms.jpfTest7.jpf2;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;
import shared.QaTrace;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" redirect="true" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoTraceResults" redirect="true" path="/resources/jsp/qaTraceResults.jsp"
 * @jpf:view-properties view-properties::
 *   <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 *   <view-properties>
 *   <pageflow-object id="pageflow:/jpfScopedForms/jpfTest7/jpf2/Jpf2.jpf"/>
 *   <pageflow-object id="action:begin.do">
 *     <property value="100" name="x"/>
 *     <property value="140" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="action:gotoJpf1.do#shared.FormA">
 *     <property value="400" name="x"/>
 *     <property value="80" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="action:finish.do#shared.FormA">
 *     <property value="400" name="x"/>
 *     <property value="240" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="action-call:@page:Jsp2a.jsp@#@action:gotoJpf1.do#shared.FormA@">
 *     <property value="276,320,320,364" name="elbowsX"/>
 *     <property value="132,132,83,83" name="elbowsY"/>
 *     <property value="East_1" name="fromPort"/>
 *     <property value="West_2" name="toPort"/>
 *   </pageflow-object>
 *   <pageflow-object id="action-call:@page:Jsp2a.jsp@#@action:finish.do#shared.FormA@">
 *     <property value="276,320,320,364" name="elbowsX"/>
 *     <property value="143,143,232,232" name="elbowsY"/>
 *     <property value="East_2" name="fromPort"/>
 *     <property value="West_1" name="toPort"/>
 *   </pageflow-object>
 *   <pageflow-object id="page:Jsp2a.jsp">
 *     <property value="240" name="x"/>
 *     <property value="140" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="external-jpf:/jpfScopedForms/jpfTest7/jpf1/Jpf1.jpf">
 *     <property value="540" name="x"/>
 *     <property value="80" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#go#Jsp2a.jsp#@action:begin.do@">
 *     <property value="136,170,170,204" name="elbowsX"/>
 *     <property value="132,132,132,132" name="elbowsY"/>
 *     <property value="East_1" name="fromPort"/>
 *     <property value="West_1" name="toPort"/>
 *     <property value="go" name="label"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#go#/jpfScopedForms/jpfTest7/jpf1/Jpf1.jpf#@action:gotoJpf1.do#shared.FormA@">
 *     <property value="436,470,470,504" name="elbowsX"/>
 *     <property value="72,72,72,72" name="elbowsY"/>
 *     <property value="East_1" name="fromPort"/>
 *     <property value="West_1" name="toPort"/>
 *     <property value="go" name="label"/>
 *   </pageflow-object>
 *   <pageflow-object id="page:/resources/jsp/done.jsp">
 *     <property value="60" name="x"/>
 *     <property value="40" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#gotoDone#/resources/jsp/done.jsp"/>
 *   <pageflow-object id="page:/resources/jsp/error.jsp">
 *     <property value="80" name="x"/>
 *     <property value="60" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#gotoError#/resources/jsp/error.jsp"/>
 *   <pageflow-object id="page:/resources/jsp/qaTraceResults.jsp">
 *     <property value="100" name="x"/>
 *     <property value="80" name="y"/>
 *   </pageflow-object>
 *   <pageflow-object id="forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp"/>
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
        "<pageflow-object id='pageflow:/jpfScopedForms/jpfTest7/jpf2/Jpf2.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:gotoJpf1.do#shared.FormA'>",
        "  <property value='400' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do#shared.FormA'>",
        "  <property value='400' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp2a.jsp@#@action:gotoJpf1.do#shared.FormA@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='132,132,83,83' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp2a.jsp@#@action:finish.do#shared.FormA@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='143,143,232,232' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:Jsp2a.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:/jpfScopedForms/jpfTest7/jpf1/Jpf1.jpf'>",
        "  <property value='540' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#Jsp2a.jsp#@action:begin.do@'>",
        "  <property value='136,170,170,204' name='elbowsX'/>",
        "  <property value='132,132,132,132' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#/jpfScopedForms/jpfTest7/jpf1/Jpf1.jpf#@action:gotoJpf1.do#shared.FormA@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='72,72,72,72' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/done.jsp'>",
        "  <property value='60' name='x'/>",
        "  <property value='40' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>",
        "<pageflow-object id='page:/resources/jsp/error.jsp'>",
        "  <property value='80' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>",
        "<pageflow-object id='page:/resources/jsp/qaTraceResults.jsp'>",
        "  <property value='100' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp'/>",
        "</view-properties>"
    })
public class Jpf2 extends PageFlowController
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
        _log.tracePoint("Jpf2.onCreate: " + _cnt);
        }

    /*
     * Constructor
     */
    public Jpf2()
        {
        super();
        }

    /**
     * @jpf:action
     * @jpf:forward name="go" path="Jsp2a.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "Jsp2a.jsp") 
        })
    protected Forward begin() throws Exception
        {
        _log.tracePoint("Jpf2.begin: " + _cnt);
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
     * @jpf:forward name="go" path="/jpfScopedForms/jpfTest7/jpf1/Jpf1.jpf"
     */
    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "/jpfScopedForms/jpfTest7/jpf1/Jpf1.jpf") 
        })
    protected Forward gotoJpf1(FormA form)
        {
        _log.tracePoint("Jpf2.gotoJpf12: " + _cnt);
        field1 = _NEW_VALUE;
        form.setString1(_NEW_VALUE);
        return new Forward("go");
        }

    /**
     * @jpf:action form="_form"
     */
    @Jpf.Action(
        useFormBean = "_form")
    protected Forward finish(FormA form)
        {
        _log.tracePoint("Jpf2.finish: " + _cnt);
        return new Forward("gotoTraceResults");
        }
    }
