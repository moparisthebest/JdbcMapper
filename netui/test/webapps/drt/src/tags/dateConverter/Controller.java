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
package tags.dateConverter;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.Date;

/**
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/bug/BugController.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:index.jsp">
 *   <property value="240" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#index.jsp#@action:begin.do@">
 *   <property value="116,160,160,204" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="formbean:bug.BugController.FormBean"/>
 * <pageflow-object id="formbeanprop:bug.BugController.FormBean#date#java.util.Date"/>
 * <pageflow-object id="formbeanprop:bug.BugController.FormBean#type#java.lang.String"/>
 * <pageflow-object id="action:results.do#bug.BugController.FormBean">
 *   <property value="380" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:showResults.jsp">
 *   <property value="520" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#showResults.jsp#@action:results.do#bug.BugController.FormBean@">
 *   <property value="416,450,450,484" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:results.do#bug.BugController.FormBean@">
 *   <property value="276,310,310,344" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
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
        "<pageflow-object id='pageflow:/bug/BugController.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='80' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>",
        "  <property value='116,160,160,204' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='formbean:bug.BugController.FormBean'/>",
        "<pageflow-object id='formbeanprop:bug.BugController.FormBean#date#java.util.Date'/>",
        "<pageflow-object id='formbeanprop:bug.BugController.FormBean#type#java.lang.String'/>",
        "<pageflow-object id='action:results.do#bug.BugController.FormBean'>",
        "  <property value='380' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:showResults.jsp'>",
        "  <property value='520' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#showResults.jsp#@action:results.do#bug.BugController.FormBean@'>",
        "  <property value='416,450,450,484' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:results.do#bug.BugController.FormBean@'>",
        "  <property value='276,310,310,344' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Controller extends PageFlowController
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
        FormBean fb = new FormBean();
        fb.setDate(new Date("10/25/2003"));
        fb.setType("This is the type");
        return new Forward("success",fb);
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="showResults.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "showResults.jsp") 
        })
    protected Forward results(FormBean form)
    {
        System.err.println("form date:" + form.getDate());
        Forward f = new Forward("success");
        f.addPageInput("form",form);
        return f; 
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class FormBean implements Serializable
    {
        private String type;

        private Date date;

        public void setDate(Date date)
        {
            this.date = date;
        }

        public Date getDate()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null. This type doesn't have
            // a default constructor, so Workshop cannot initialize it for you.

            // TODO: Initialize date if it is null.
            //if(this.date == null)
            //{
            //    this.date = new Date(?);
            //}

            return this.date;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return this.type;
        }
    }
}
