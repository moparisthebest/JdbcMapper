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
package singletonJpf.jpfTest8.subJpf1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;
import shared.QaTrace;

/**
 * @jpf:controller nested="true"
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/singletonJpf/jpfTest8/subJpf1/SubJpf1.jpf"/>
 * <pageflow-object id="action:begin.do#shared.FormA">
 *   <property value="100" name="x"/>
 *   <property value="140" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:finish.do#shared.FormA">
 *   <property value="60" name="x"/>
 *   <property value="300" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1a.jsp@#@action:finish.do#shared.FormA@">
 *   <property value="204,150,150,96" name="elbowsX"/>
 *   <property value="123,123,281,281" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="East_0" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:SubJsp1a.jsp">
 *   <property value="240" name="x"/>
 *   <property value="120" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="exit:returnSubJpf">
 *   <property value="540" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#go#returnSubJpf#@action:finish.do#shared.FormA@">
 *   <property value="96,300,300,504" name="elbowsX"/>
 *   <property value="292,292,72,72" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="go" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="action:gotoJpf2.do#shared.FormA">
 *   <property value="240" name="x"/>
 *   <property value="340" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="external-jpf:/singletonJpf/jpfTest8/jpf2/Jpf2.jpf">
 *   <property value="80" name="x"/>
 *   <property value="60" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#go#/singletonJpf/jpfTest8/jpf2/Jpf2.jpf#@action:gotoJpf2.do#shared.FormA@">
 *   <property value="204,160,160,116" name="elbowsX"/>
 *   <property value="332,332,52,52" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 *   <property value="go" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#go#SubJsp1a.jsp#@action:begin.do#shared.FormA@">
 *   <property value="136,170,170,204" name="elbowsX"/>
 *   <property value="132,132,112,112" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="go" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1a.jsp@#@action:gotoJpf2.do#shared.FormA@">
 *   <property value="240,240,240,240" name="elbowsX"/>
 *   <property value="164,230,230,296" name="elbowsY"/>
 *   <property value="South_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
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
        "<pageflow-object id='pageflow:/singletonJpf/jpfTest8/subJpf1/SubJpf1.jpf'/>",
        "<pageflow-object id='action:begin.do#shared.FormA'>",
        "  <property value='100' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do#shared.FormA'>",
        "  <property value='60' name='x'/>",
        "  <property value='300' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1a.jsp@#@action:finish.do#shared.FormA@'>",
        "  <property value='204,150,150,96' name='elbowsX'/>",
        "  <property value='123,123,281,281' name='elbowsY'/>",
        "  <property value='West_2' name='fromPort'/>",
        "  <property value='East_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:SubJsp1a.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='exit:returnSubJpf'>",
        "  <property value='540' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#go#returnSubJpf#@action:finish.do#shared.FormA@'>",
        "  <property value='96,300,300,504' name='elbowsX'/>",
        "  <property value='292,292,72,72' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:gotoJpf2.do#shared.FormA'>",
        "  <property value='240' name='x'/>",
        "  <property value='340' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:/singletonJpf/jpfTest8/jpf2/Jpf2.jpf'>",
        "  <property value='80' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#/singletonJpf/jpfTest8/jpf2/Jpf2.jpf#@action:gotoJpf2.do#shared.FormA@'>",
        "  <property value='204,160,160,116' name='elbowsX'/>",
        "  <property value='332,332,52,52' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#go#SubJsp1a.jsp#@action:begin.do#shared.FormA@'>",
        "  <property value='136,170,170,204' name='elbowsX'/>",
        "  <property value='132,132,112,112' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='go' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1a.jsp@#@action:gotoJpf2.do#shared.FormA@'>",
        "  <property value='240,240,240,240' name='elbowsX'/>",
        "  <property value='164,230,230,296' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class SubJpf1 extends PageFlowController
    {
    private static final String _SUB_VALUE = "SubJpf value";
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
        _log.tracePoint("SubJpf1.onCreate:" + _cnt);
        }

    /**
     * @jpf:action form="_form"
     * @jpf:forward name="go" path="SubJsp1a.jsp"
     */
    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "SubJsp1a.jsp") 
        })
    protected Forward begin(FormA form)
        {
        _log.tracePoint("SubJpf1.begin:" + _cnt);
        return new Forward("go");
        }


    /**
     * @jpf:action form="_form"
     * @jpf:forward name="go" path="/singletonJpf/jpfTest8/jpf2/Jpf2.jpf"
     */
    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "go",
                path = "/singletonJpf/jpfTest8/jpf2/Jpf2.jpf") 
        })
    protected Forward gotoJpf2(FormA form)
        {
        _log.tracePoint("SubJpf1.gotoJpf2(FormA):" + _cnt);
        form.setString1(_SUB_VALUE);
        return new Forward("go", form);
        }

    /**
     * @jpf:action
     * @jpf:forward name="go" return-action="returnSubJpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "go",
                returnAction = "returnSubJpf") 
        })
    public Forward finish(FormA form)
        {
        _log.tracePoint("SubJpf1.finish(FormA):" + _cnt);
        form.setString1(_SUB_VALUE);
        return new Forward("go", form);
        }
    }
