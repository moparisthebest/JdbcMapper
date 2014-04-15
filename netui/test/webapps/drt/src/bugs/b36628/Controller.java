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
package bugs.b36628;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @jpf:controller
 * @jpf:catch type="java.lang.NoSuchFieldException"
 *            method="handleIt" message-key="theMessage"
 * @jpf:catch type="java.lang.IllegalArgumentException"
 *            method="handleIt2" message-key="theMessage"
 *            
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/b36628/Controller.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:throwRT.do">
 *   <property value="420" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:throwIA.do">
 *   <property value="420" name="x"/>
 *   <property value="220" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:throwIA.do@">
 *   <property value="276,330,330,384" name="elbowsX"/>
 *   <property value="103,103,212,212" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:throwRT.do@">
 *   <property value="276,330,330,384" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:index.jsp">
 *   <property value="240" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:error.jsp@#@action:begin.do@">
 *   <property value="80,80,80,80" name="elbowsX"/>
 *   <property value="196,170,170,144" name="elbowsY"/>
 *   <property value="North_1" name="fromPort"/>
 *   <property value="South_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:error.jsp">
 *   <property value="80" name="x"/>
 *   <property value="240" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:error2.jsp@#@action:begin.do@">
 *   <property value="124,120,120,116" name="elbowsX"/>
 *   <property value="232,232,103,103" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_2" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:error2.jsp">
 *   <property value="160" name="x"/>
 *   <property value="240" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:foo/error.jsp">
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
 * </view-properties>
 * ::

 
 
 */
@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = java.lang.NoSuchFieldException.class,
            method = "handleIt",
            messageKey = "theMessage"),
        @Jpf.Catch(
            type = java.lang.IllegalArgumentException.class,
            method = "handleIt2",
            messageKey = "theMessage") 
    })
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/b36628/Controller.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='80' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:throwRT.do'>",
        "  <property value='420' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:throwIA.do'>",
        "  <property value='420' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:throwIA.do@'>",
        "  <property value='276,330,330,384' name='elbowsX'/>",
        "  <property value='103,103,212,212' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:throwRT.do@'>",
        "  <property value='276,330,330,384' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:error.jsp@#@action:begin.do@'>",
        "  <property value='80,80,80,80' name='elbowsX'/>",
        "  <property value='196,170,170,144' name='elbowsY'/>",
        "  <property value='North_1' name='fromPort'/>",
        "  <property value='South_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:error.jsp'>",
        "  <property value='80' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:error2.jsp@#@action:begin.do@'>",
        "  <property value='124,120,120,116' name='elbowsX'/>",
        "  <property value='232,232,103,103' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_2' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:error2.jsp'>",
        "  <property value='160' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:foo/error.jsp'>",
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
        "</view-properties>"
    })
 public class Controller extends PageFlowController
{
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
     * @jpf:exception-handler
     * @jpf:forward name="errorPage1" path="error.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "errorPage1",
                path = "error.jsp") 
        })
    protected Forward handleIt(NoSuchFieldException ex, String actionName,
					  String message, Object form)
    {
        ActionErrors errors = new ActionErrors();
        errors.add("name", new ActionError("theMessage")); 
        getRequest().setAttribute(Globals.ERROR_KEY,errors);
        return new Forward( "errorPage1" );
    }
    
    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage2" path="error2.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "errorPage2",
                path = "error2.jsp") 
        })
    protected Forward handleIt2(IllegalArgumentException ex, String actionName,
					  String message, Object form)
    {
        ActionErrors errors = new ActionErrors();
        errors.add("name", new ActionError("theMessageTwo")); 
        getRequest().setAttribute(Globals.ERROR_KEY,errors);
        return new Forward( "errorPage2" );
    }

     /**
      * @jpf:action
      */
    @Jpf.Action(
        )
     protected Forward throwRT()
        throws NoSuchFieldException
     {
        if (true)
           throw new NoSuchFieldException("No Such Field Excpetion Was Thrown");
        return new Forward("success");
    }
    
     /**
      * @jpf:action
      */
    @Jpf.Action(
        )
     protected Forward throwIA()
     {
        if (true)
           throw new IllegalArgumentException("Illegal Argument Was Thrown");
        return new Forward("success");
    }
    
}
