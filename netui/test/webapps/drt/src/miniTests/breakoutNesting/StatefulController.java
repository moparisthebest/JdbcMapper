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
package miniTests.breakoutNesting;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/miniTests/breakoutNesting/StatefulController.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='200' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:goNested.do'>",
        "  <property value='260' name='x'/>",
        "  <property value='360' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:nestedDone.do'>",
        "  <property value='580' name='x'/>",
        "  <property value='360' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:otherEntryPoint.do'>",
        "  <property value='260' name='x'/>",
        "  <property value='40' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:goNested.do@'>",
        "  <property value='260,260,260,260' name='elbowsX'/>",
        "  <property value='244,280,280,316' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:begin.do@'>",
        "  <property value='224,180,180,136' name='elbowsX'/>",
        "  <property value='181,181,181,181' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='260' name='x'/>",
        "  <property value='200' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:nested/NestedController.jpf'>",
        "  <property value='420' name='x'/>",
        "  <property value='360' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>",
        "  <property value='136,180,180,224' name='elbowsX'/>",
        "  <property value='192,192,192,192' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#nested/NestedController.jpf#@action:goNested.do@'>",
        "  <property value='296,340,340,384' name='elbowsX'/>",
        "  <property value='352,352,352,352' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:nestedDone.do@'>",
        "  <property value='580,580,438,296' name='elbowsX'/>",
        "  <property value='316,192,192,192' name='elbowsY'/>",
        "  <property value='North_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:otherEntryPoint.do@'>",
        "  <property value='260,260,260,260' name='elbowsX'/>",
        "  <property value='84,120,120,156' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:nested/NestedController.jpf@#@action:nestedDone.do@'>",
        "  <property value='456,500,500,544' name='elbowsX'/>",
        "  <property value='352,352,352,352' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class StatefulController extends PageFlowController
{
    private String someState = "default val";
    public String getSomeState() {
        return someState;
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
    protected Forward begin()
    {
        someState = "begin action modified this";
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="nested/NestedController.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "nested/NestedController.jpf") 
        })
    protected Forward goNested()
    {
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
    protected Forward nestedDone()
    {
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
    protected Forward otherEntryPoint()
    {
        return new Forward("success");
    }
}
