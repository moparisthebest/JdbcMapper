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
package miniTests.breakoutNesting.nested;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    nested = true)
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/miniTests/breakoutNesting/nested/NestedController.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='80' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:done.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:breakout.do'>",
        "  <property value='240' name='x'/>",
        "  <property value='360' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:nestDeeper.do'>",
        "  <property value='240' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:doubleNestedDone.do'>",
        "  <property value='560' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:nestDeeper.do@'>",
        "  <property value='240,240,240,240' name='elbowsX'/>",
        "  <property value='176,140,140,104' name='elbowsY'/>",
        "  <property value='North_1' name='fromPort'/>",
        "  <property value='South_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:done.do@'>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='212,212,212,212' name='elbowsY'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:breakout.do@'>",
        "  <property value='240,240,240,240' name='elbowsX'/>",
        "  <property value='264,290,290,316' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:/miniTests/breakoutNesting/otherEntryPoint.do'>",
        "  <property value='240' name='x'/>",
        "  <property value='500' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:doubleNested/DoubleNestedController.jpf'>",
        "  <property value='400' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>",
        "  <property value='116,160,160,204' name='elbowsX'/>",
        "  <property value='212,212,212,212' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#/miniTests/breakoutNesting/otherEntryPoint.do#@action:breakout.do@'>",
        "  <property value='240,240,240,240' name='elbowsX'/>",
        "  <property value='404,430,430,456' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#doubleNested/DoubleNestedController.jpf#@action:nestDeeper.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='52,52,52,52' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='exit:nestedDone'>",
        "  <property value='560' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:doubleNested/DoubleNestedController.jpf@#@action:doubleNestedDone.do@'>",
        "  <property value='436,480,480,524' name='elbowsX'/>",
        "  <property value='52,52,52,52' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#done#nestedDone#@action:done.do@'>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='done' name='label'/>",
        "  <property value='436,480,480,524' name='elbowsX'/>",
        "  <property value='212,212,212,212' name='elbowsY'/>",
        "</pageflow-object>",
        "<pageflow-object id='return-to:@forward:return-to#success#currentPage#@action:doubleNestedDone.do@@'>",
        "  <property value='720' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-to#success#currentPage#@action:doubleNestedDone.do@'>",
        "  <property value='596,640,640,684' name='elbowsX'/>",
        "  <property value='52,52,52,52' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class NestedController extends PageFlowController
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
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="done" return-action="nestedDone"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "done",
                returnAction = "nestedDone") 
        })
    public Forward done()
    {
        return new Forward("done");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/miniTests/breakoutNesting/otherEntryPoint.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/miniTests/breakoutNesting/otherEntryPoint.do") 
        })
    protected Forward breakout()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="/miniTests/breakoutNesting/other/OtherController.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "/miniTests/breakoutNesting/other/OtherController.jpf") 
        })
    protected Forward utterBreakout()
    {
        return new Forward("success");
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="doubleNested/DoubleNestedController.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "doubleNested/DoubleNestedController.jpf") 
        })
    protected Forward nestDeeper()
    {
        return new Forward("success");
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
    protected Forward doubleNestedDone()
    {
        return new Forward("success");
    }
}
