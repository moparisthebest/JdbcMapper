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
package miniTests.returnToExceptions;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


/**
 * @jpf:controller
 * 
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/miniTests/returnToExceptions/ReturnToExceptionsController.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:doReturnToPreviousPage.do">
 *   <property value="80" name="x"/>
 *   <property value="260" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:doReturnToCurrentPage.do">
 *   <property value="240" name="x"/>
 *   <property value="260" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:doReturnToPreviousAction.do">
 *   <property value="400" name="x"/>
 *   <property value="260" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:delegateDone.do">
 *   <property value="540" name="x"/>
 *   <property value="400" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:doReturnToCurrentPage.do@">
 *   <property value="240,240,240,240" name="elbowsX"/>
 *   <property value="144,180,180,216" name="elbowsY"/>
 *   <property value="South_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:doReturnToPreviousAction.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="103,103,252,252" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:doReturnToPreviousPage.do@">
 *   <property value="204,160,160,116" name="elbowsX"/>
 *   <property value="103,103,252,252" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:index.jsp">
 *   <property value="240" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/miniTests/returnToExceptions/delegate/DelegateController.jpf">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#index.jsp#@action:begin.do@">
 *   <property value="116,160,160,204" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/miniTests/returnToExceptions/delegate/DelegateController.jpf?returnToPreviousPage#@action:doReturnToPreviousPage.do@">
 *   <property value="80,80,60,60" name="elbowsX"/>
 *   <property value="216,150,150,84" name="elbowsY"/>
 *   <property value="North_1" name="fromPort"/>
 *   <property value="South_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#index.jsp#@action:delegateDone.do@">
 *   <property value="540,540,408,276" name="elbowsX"/>
 *   <property value="356,92,92,92" name="elbowsY"/>
 *   <property value="North_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/miniTests/returnToExceptions/delegate/DelegateController.jpf?returnToPreviousAction#@action:doReturnToPreviousAction.do@">
 *   <property value="364,230,230,96" name="elbowsX"/>
 *   <property value="241,241,32,32" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/miniTests/returnToExceptions/delegate/DelegateController.jpf?returnToCurrentPage#@action:doReturnToCurrentPage.do@">
 *   <property value="204,71,71,71" name="elbowsX"/>
 *   <property value="252,252,168,84" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="South_2" name="toPort"/>
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
        "<pageflow-object id='pageflow:/miniTests/returnToExceptions/ReturnToExceptionsController.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='80' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:doReturnToPreviousPage.do'>",
        "  <property value='80' name='x'/>",
        "  <property value='260' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:doReturnToCurrentPage.do'>",
        "  <property value='240' name='x'/>",
        "  <property value='260' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:doReturnToPreviousAction.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='260' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:delegateDone.do'>",
        "  <property value='540' name='x'/>",
        "  <property value='400' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:doReturnToCurrentPage.do@'>",
        "  <property value='240,240,240,240' name='elbowsX'/>",
        "  <property value='144,180,180,216' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:doReturnToPreviousAction.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='103,103,252,252' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:doReturnToPreviousPage.do@'>",
        "  <property value='204,160,160,116' name='elbowsX'/>",
        "  <property value='103,103,252,252' name='elbowsY'/>",
        "  <property value='West_2' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/miniTests/returnToExceptions/delegate/DelegateController.jpf'>",
        "  <property value='60' name='x'/>",
        "  <property value='40' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>",
        "  <property value='116,160,160,204' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#/miniTests/returnToExceptions/delegate/DelegateController.jpf?returnToPreviousPage#@action:doReturnToPreviousPage.do@'>",
        "  <property value='80,80,60,60' name='elbowsX'/>",
        "  <property value='216,150,150,84' name='elbowsY'/>",
        "  <property value='North_1' name='fromPort'/>",
        "  <property value='South_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:delegateDone.do@'>",
        "  <property value='540,540,408,276' name='elbowsX'/>",
        "  <property value='356,92,92,92' name='elbowsY'/>",
        "  <property value='North_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#/miniTests/returnToExceptions/delegate/DelegateController.jpf?returnToPreviousAction#@action:doReturnToPreviousAction.do@'>",
        "  <property value='364,230,230,96' name='elbowsX'/>",
        "  <property value='241,241,32,32' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#/miniTests/returnToExceptions/delegate/DelegateController.jpf?returnToCurrentPage#@action:doReturnToCurrentPage.do@'>",
        "  <property value='204,71,71,71' name='elbowsX'/>",
        "  <property value='252,252,168,84' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='South_2' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class ReturnToExceptionsController extends PageFlowController
{
    /**
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/miniTests/returnToExceptions/delegate/DelegateController.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/miniTests/returnToExceptions/delegate/DelegateController.jpf") 
        })
    protected Forward doReturnToPreviousPage()
    {
        getRequest().setAttribute( "returnToPreviousPage", Boolean.TRUE );
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/miniTests/returnToExceptions/delegate/DelegateController.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/miniTests/returnToExceptions/delegate/DelegateController.jpf") 
        })
    protected Forward doReturnToCurrentPage()
    {
        getRequest().setAttribute( "returnToCurrentPage", Boolean.TRUE );
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/miniTests/returnToExceptions/delegate/DelegateController.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/miniTests/returnToExceptions/delegate/DelegateController.jpf") 
        })
    protected Forward doReturnToPreviousAction()
    {
        getRequest().setAttribute( "returnToPreviousAction", Boolean.TRUE );
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward delegateDone()
    {
        return new Forward("success");
    }
}
