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
package scopedJpf.jpfTest1;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" redirect="true" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/scopedJpf/jpfTest1/JpfTest1.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="140" name="x"/>
 *   <property value="240" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:finish.do">
 *   <property value="340" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/done.jsp">
 *   <property value="140" name="x"/>
 *   <property value="380" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/error.jsp">
 *   <property value="140" name="x"/>
 *   <property value="540" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:MainFrame.jsp">
 *   <property value="340" name="x"/>
 *   <property value="240" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:JspTest1.jsp@#@action:finish.do@">
 *   <property value="176,240,240,304" name="elbowsX"/>
 *   <property value="72,72,72,72" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:JspTest1.jsp">
 *   <property value="140" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:StartTest.jsp">
 *   <property value="140" name="x"/>
 *   <property value="680" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoPg1#MainFrame.jsp#@action:begin.do@">
 *   <property value="176,240,240,304" name="elbowsX"/>
 *   <property value="232,232,232,232" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="gotoPg1" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoDone#/resources/jsp/done.jsp"/>
 * <pageflow-object id="forward:path#gotoError#/resources/jsp/error.jsp"/>
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
            path = "/resources/jsp/error.jsp") 
    })
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/scopedJpf/jpfTest1/JpfTest1.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='140' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do'>",
        "  <property value='340' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/done.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='380' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/error.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='540' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:MainFrame.jsp'>",
        "  <property value='340' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:JspTest1.jsp@#@action:finish.do@'>",
        "  <property value='176,240,240,304' name='elbowsX'/>",
        "  <property value='72,72,72,72' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:JspTest1.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:StartTest.jsp'>",
        "  <property value='140' name='x'/>",
        "  <property value='680' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoPg1#MainFrame.jsp#@action:begin.do@'>",
        "  <property value='176,240,240,304' name='elbowsX'/>",
        "  <property value='232,232,232,232' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='gotoPg1' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>",
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>",
        "</view-properties>"
    })
public class JpfTest1 extends PageFlowController
    {
    private static  Integer _lock       = new Integer(0);
    public          String  scope;

    /**
     * @jpf:action
     * @jpf:forward name="gotoPg1" path="MainFrame.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "MainFrame.jsp") 
        })
    protected Forward begin()
        {
            //System.out.println(">>> JpfTest1.begin");
        return new Forward("gotoPg1");
        }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward finish()
       {
           //System.out.println(">>> JpfTest1.finish");
       return new Forward("gotoDone");
       }
    }
