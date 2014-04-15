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
package miniTests.returnToExceptions.delegate;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowException;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


/**
 * @jpf:controller nested="true"
 * @jpf:catch type="PageFlowException" method="goBack"
 * 
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/miniTests/returnToExceptions/delegate/DelegateController.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:done.do">
 *   <property value="400" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="return-to:@forward:return-to#prevPage#previousPage#@action:begin.do@@">
 *   <property value="140" name="x"/>
 *   <property value="140" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-to#prevPage#previousPage#@action:begin.do@">
 *   <property value="116,116,104,104" name="elbowsX"/>
 *   <property value="103,117,117,132" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="prevPage" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="return-to:@forward:return-to#curPage#currentPage#@action:begin.do@@">
 *   <property value="221" name="x"/>
 *   <property value="224" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-to#curPage#currentPage#@action:begin.do@">
 *   <property value="116,150,150,185" name="elbowsX"/>
 *   <property value="103,103,216,216" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="curPage" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="return-to:@forward:return-to#prevAction#previousAction#@action:begin.do@@">
 *   <property value="301" name="x"/>
 *   <property value="304" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-to#prevAction#previousAction#@action:begin.do@">
 *   <property value="116,190,190,265" name="elbowsX"/>
 *   <property value="103,103,296,296" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="prevAction" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="exit:delegateDone">
 *   <property value="560" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#done#delegateDone#@action:done.do@">
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="done" name="label"/>
 *   <property value="436,480,480,524" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */
@Jpf.Controller(
    nested = true,
    catches = {
        @Jpf.Catch(
            type = PageFlowException.class,
            method = "goBack") 
    })
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/miniTests/returnToExceptions/delegate/DelegateController.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='80' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:done.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='return-to:@forward:return-to#prevPage#previousPage#@action:begin.do@@'>",
        "  <property value='140' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-to#prevPage#previousPage#@action:begin.do@'>",
        "  <property value='116,116,104,104' name='elbowsX'/>",
        "  <property value='103,117,117,132' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='prevPage' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='return-to:@forward:return-to#curPage#currentPage#@action:begin.do@@'>",
        "  <property value='221' name='x'/>",
        "  <property value='224' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-to#curPage#currentPage#@action:begin.do@'>",
        "  <property value='116,150,150,185' name='elbowsX'/>",
        "  <property value='103,103,216,216' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='curPage' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='return-to:@forward:return-to#prevAction#previousAction#@action:begin.do@@'>",
        "  <property value='301' name='x'/>",
        "  <property value='304' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-to#prevAction#previousAction#@action:begin.do@'>",
        "  <property value='116,190,190,265' name='elbowsX'/>",
        "  <property value='103,103,296,296' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='prevAction' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='exit:delegateDone'>",
        "  <property value='560' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#done#delegateDone#@action:done.do@'>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='done' name='label'/>",
        "  <property value='436,480,480,524' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class DelegateController extends PageFlowController
{
    /**
     * @jpf:action
     * @jpf:forward name="prevPage" return-to="previousPage"
     * @jpf:forward name="curPage" return-to="currentPage"
     * @jpf:forward name="prevAction" return-to="previousAction"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "prevPage",
                navigateTo = Jpf.NavigateTo.previousPage),
            @Jpf.Forward(
                name = "curPage",
                navigateTo = Jpf.NavigateTo.currentPage),
            @Jpf.Forward(
                name = "prevAction",
                navigateTo = Jpf.NavigateTo.previousAction) 
        })
    protected Forward begin()
    {
        if ( getRequest().getAttribute( "returnToPreviousPage" ) != null )
        {
            return new Forward( "prevPage" );
        }
        else if ( getRequest().getAttribute( "returnToCurrentPage" ) != null )
        {
            return new Forward( "curPage" );
        }
        else if ( getRequest().getAttribute( "returnToPreviousAction" ) != null )
        {
            return new Forward( "prevAction" );
        }
        
        return null;
    }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="done" return-action="delegateDone"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "done",
                returnAction = "delegateDone") 
        })
    protected Forward goBack( PageFlowException ex, String actionName, String msg, Object form )
    {
        return new Forward( "done" );
    }
}
