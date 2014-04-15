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
package tags.sessionBean;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**

 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/sessionBean/Controller.jpf"/>
 * <pageflow-object id="action:begin.do#sessionBean.Controller.NewFormBean">
 *   <property value="60" name="x"/>
 *   <property value="60" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:Action2.do#sessionBean.Controller.NewFormBean">
 *   <property value="300" name="x"/>
 *   <property value="60" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:Action3.do">
 *   <property value="160" name="x"/>
 *   <property value="200" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:jsp1.jsp@#@action:Action2.do#sessionBean.Controller.NewFormBean@">
 *   <property value="196,230,230,264" name="elbowsX"/>
 *   <property value="52,52,52,52" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:jsp1.jsp">
 *   <property value="160" name="x"/>
 *   <property value="60" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:jsp2.jsp@#@action:Action3.do@">
 *   <property value="264,230,230,196" name="elbowsX"/>
 *   <property value="192,192,192,192" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:jsp2.jsp">
 *   <property value="300" name="x"/>
 *   <property value="200" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#jsp1.jsp#@action:begin.do#sessionBean.Controller.NewFormBean@">
 *   <property value="96,110,110,124" name="elbowsX"/>
 *   <property value="52,52,52,52" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#jsp2.jsp#@action:Action2.do#sessionBean.Controller.NewFormBean@">
 *   <property value="300,300,300,300" name="elbowsX"/>
 *   <property value="104,130,130,156" name="elbowsY"/>
 *   <property value="South_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#jsp1.jsp#@action:Action3.do@">
 *   <property value="160,160,160,160" name="elbowsX"/>
 *   <property value="156,130,130,104" name="elbowsY"/>
 *   <property value="North_1" name="fromPort"/>
 *   <property value="South_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="formbeanprop:sessionBean.Controller.NewFormBean#name#java.lang.String"/>
 * <pageflow-object id="formbean:sessionBean.Controller.NewFormBean"/>
 * <pageflow-object id="formbeanprop:sessionBean.Controller.NameBean#name#java.lang.String"/>
 * <pageflow-object id="formbean:sessionBean.Controller.NameBean"/>
 * </view-properties>
 * ::


 */
@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/sessionBean/Controller.jpf'/>",
        "<pageflow-object id='action:begin.do#sessionBean.Controller.NewFormBean'>",
        "  <property value='60' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:Action2.do#sessionBean.Controller.NewFormBean'>",
        "  <property value='300' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:Action3.do'>",
        "  <property value='160' name='x'/>",
        "  <property value='200' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:jsp1.jsp@#@action:Action2.do#sessionBean.Controller.NewFormBean@'>",
        "  <property value='196,230,230,264' name='elbowsX'/>",
        "  <property value='52,52,52,52' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:jsp1.jsp'>",
        "  <property value='160' name='x'/>",
        "  <property value='60' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:jsp2.jsp@#@action:Action3.do@'>",
        "  <property value='264,230,230,196' name='elbowsX'/>",
        "  <property value='192,192,192,192' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:jsp2.jsp'>",
        "  <property value='300' name='x'/>",
        "  <property value='200' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#jsp1.jsp#@action:begin.do#sessionBean.Controller.NewFormBean@'>",
        "  <property value='96,110,110,124' name='elbowsX'/>",
        "  <property value='52,52,52,52' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#jsp2.jsp#@action:Action2.do#sessionBean.Controller.NewFormBean@'>",
        "  <property value='300,300,300,300' name='elbowsX'/>",
        "  <property value='104,130,130,156' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#jsp1.jsp#@action:Action3.do@'>",
        "  <property value='160,160,160,160' name='elbowsX'/>",
        "  <property value='156,130,130,104' name='elbowsY'/>",
        "  <property value='North_1' name='fromPort'/>",
        "  <property value='South_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='formbeanprop:sessionBean.Controller.NewFormBean#name#java.lang.String'/>",
        "<pageflow-object id='formbean:sessionBean.Controller.NewFormBean'/>",
        "<pageflow-object id='formbeanprop:sessionBean.Controller.NameBean#name#java.lang.String'/>",
        "<pageflow-object id='formbean:sessionBean.Controller.NameBean'/>",
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
     * @jpf:forward name="success" path="jsp1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "jsp1.jsp") 
        })
    protected Forward begin(NewFormBean form)
    {
        return new Forward("success");
       
    }
 
    /**
     * @jpf:action
     * @jpf:forward name="success" path="jsp2.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "jsp2.jsp") 
        })
    protected Forward Action2(NewFormBean form)
    {
        NameBean bean = (NameBean) getSession().getAttribute("nameBean");
        bean.setName(form.getName());
        return new Forward("success","formInput",form);
    }
 
    /**
     * @jpf:action
     * @jpf:forward name="success" path="jsp1.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "jsp1.jsp") 
        })
    protected Forward Action3()
    {
        return new Forward("success");
    }
 
    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class NewFormBean extends org.apache.struts.action.ActionForm
    {
        private String name= "xxx";

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }
    
    public static class NameBean extends org.apache.struts.action.ActionForm {
         private String name = "Default Value";

        public void setName(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }
    }
       
}
