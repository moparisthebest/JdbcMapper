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
package pageFlowCore.pfScoping.a.nested;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:controller nested="true"
 * @jpf:view-properties view-properties::
 * <view-properties>

 *   <pageflow-object id="action:begin.do">

 *     <property value="80" name="x"/>

 *     <property value="100" name="y"/>

 *   </pageflow-object>

 *   <pageflow-object id="page:index.jsp">

 *     <property value="240" name="x"/>

 *     <property value="100" name="y"/>

 *   </pageflow-object>

 *   <pageflow-object id="action:done.do">

 *     <property value="400" name="x"/>

 *     <property value="100" name="y"/>

 *   </pageflow-object>

 *   <pageflow-object id="exit:nestedDone">

 *     <property value="560" name="x"/>

 *     <property value="100" name="y"/>

 *   </pageflow-object>

 *   <pageflow-object id="forward:return-action#done#nestedDone#@action:done.do@">

 *     <property value="East_1" name="fromPort"/>

 *     <property value="West_1" name="toPort"/>

 *     <property value="done" name="label"/>

 *   </pageflow-object>

 *   <pageflow-object id="action-call:@page:index.jsp@#@action:done.do@">

 *     <property value="East_1" name="fromPort"/>

 *     <property value="West_1" name="toPort"/>

 *   </pageflow-object>

 * </view-properties>
 * ::
 */
@Jpf.Controller(
    nested = true)
@Jpf.ViewProperties(
    value = {
        "<view-properties>",
        "",
        "  <pageflow-object id='action:begin.do'>",
        "",
        "    <property value='80' name='x'/>",
        "",
        "    <property value='100' name='y'/>",
        "",
        "  </pageflow-object>",
        "",
        "  <pageflow-object id='page:index.jsp'>",
        "",
        "    <property value='240' name='x'/>",
        "",
        "    <property value='100' name='y'/>",
        "",
        "  </pageflow-object>",
        "",
        "  <pageflow-object id='action:done.do'>",
        "",
        "    <property value='400' name='x'/>",
        "",
        "    <property value='100' name='y'/>",
        "",
        "  </pageflow-object>",
        "",
        "  <pageflow-object id='exit:nestedDone'>",
        "",
        "    <property value='560' name='x'/>",
        "",
        "    <property value='100' name='y'/>",
        "",
        "  </pageflow-object>",
        "",
        "  <pageflow-object id='forward:return-action#done#nestedDone#@action:done.do@'>",
        "",
        "    <property value='East_1' name='fromPort'/>",
        "",
        "    <property value='West_1' name='toPort'/>",
        "",
        "    <property value='done' name='label'/>",
        "",
        "  </pageflow-object>",
        "",
        "  <pageflow-object id='action-call:@page:index.jsp@#@action:done.do@'>",
        "",
        "    <property value='East_1' name='fromPort'/>",
        "",
        "    <property value='West_1' name='toPort'/>",
        "",
        "  </pageflow-object>",
        "",
        "</view-properties>"
    })
public class NestedController extends PageFlowController
{


    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
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
}
