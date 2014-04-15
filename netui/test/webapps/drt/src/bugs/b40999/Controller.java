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
package bugs.b40999;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/accesskeyGroups/Controller.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="420" name="x"/>
 *   <property value="160" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:post.do#accesskeyGroups.Controller.MyBean">
 *   <property value="200" name="x"/>
 *   <property value="160" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:index.jsp">
 *   <property value="300" name="x"/>
 *   <property value="60" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:page2.jsp@#@action:begin.do@">
 *   <property value="336,360,360,384" name="elbowsX"/>
 *   <property value="272,272,163,163" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_2" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:page2.jsp">
 *   <property value="300" name="x"/>
 *   <property value="280" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#index.jsp#@action:begin.do@">
 *   <property value="384,360,360,336" name="elbowsX"/>
 *   <property value="152,152,52,52" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#page2.jsp#@action:post.do#accesskeyGroups.Controller.MyBean@">
 *   <property value="236,250,250,264" name="elbowsX"/>
 *   <property value="152,152,272,272" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="formbeanprop:accesskeyGroups.Controller.MyBean#type#java.lang.String"/>
 * <pageflow-object id="formbeanprop:accesskeyGroups.Controller.MyBean#attributes#java.lang.String[]"/>
 * <pageflow-object id="formbean:accesskeyGroups.Controller.MyBean"/>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:post.do#accesskeyGroups.Controller.MyBean@">
 *   <property value="264,240,240,216" name="elbowsX"/>
 *   <property value="52,52,52,52" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
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
        "<pageflow-object id='pageflow:/accesskeyGroups/Controller.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='420' name='x'/>",
        "  <property value='160' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:post.do#accesskeyGroups.Controller.MyBean'>",
        "  <property value='200' name='x'/>",
        "  <property value='160' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='300' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:page2.jsp@#@action:begin.do@'>",
        "  <property value='336,360,360,384' name='elbowsX'/>",
        "  <property value='272,272,163,163' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:page2.jsp'>",
        "  <property value='300' name='x'/>",
        "  <property value='280' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>",
        "  <property value='384,360,360,336' name='elbowsX'/>",
        "  <property value='152,152,52,52' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#page2.jsp#@action:post.do#accesskeyGroups.Controller.MyBean@'>",
        "  <property value='236,250,250,264' name='elbowsX'/>",
        "  <property value='152,152,272,272' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='formbeanprop:accesskeyGroups.Controller.MyBean#type#java.lang.String'/>",
        "<pageflow-object id='formbeanprop:accesskeyGroups.Controller.MyBean#attributes#java.lang.String[]'/>",
        "<pageflow-object id='formbean:accesskeyGroups.Controller.MyBean'/>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:post.do#accesskeyGroups.Controller.MyBean@'>",
        "  <property value='264,240,240,216' name='elbowsX'/>",
        "  <property value='52,52,52,52' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Controller extends PageFlowController
{
    
    public String altText = "PageFlow Text for Alt";
    public String[] types = {"foo","bar","baz"};
    public String[] colors = {"red","blue","yellow"}; 
    public String[] attributes = {"fenders", "wheels", "windows"};


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
     * @jpf:forward name="success" path="page2.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "page2.jsp") 
        })
    protected Forward post(MyBean form)
    {
        return new Forward("success","form",form);
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class MyBean implements Serializable
    {
        private String[] attributes;

        private String color;

        private boolean disabled;

        private String name;

        private String type;

        private String description;

        public void setType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return this.type;
        }

        public void setAttributes(String[] attributes)
        {
            this.attributes = attributes;
        }

        public String[] getAttributes()
        {
            // For data binding to be able to post data back, complex types and
            // arrays must be initialized to be non-null.
            if(this.attributes == null || this.attributes.length == 0)
            {
                this.attributes = new String[1];
            }

            return this.attributes;
        }
    }
}
