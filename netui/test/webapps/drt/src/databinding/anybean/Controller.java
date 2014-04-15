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
package databinding.anybean;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
@Jpf.ViewProperties(
        value = { 
            "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
            "<view-properties>", 
            "<pageflow-object id='formbean:Customer'/>", 
            "<pageflow-object id='action:begin.do'>", 
            "  <property value='80' name='x'/>", 
            "  <property value='100' name='y'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'>", 
            "  <property value='116,140,140,164' name='elbowsX'/>", 
            "  <property value='92,92,92,92' name='elbowsY'/>", 
            "  <property value='East_1' name='fromPort'/>", 
            "  <property value='West_1' name='toPort'/>", 
            "  <property value='index' name='label'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='action:showAnyBeanForm.do'>", 
            "  <property value='320' name='x'/>", 
            "  <property value='220' name='y'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='forward:path#success#updateAnyBean.jsp#@action:showAnyBeanForm.do@'>", 
            "  <property value='320,320,320,320' name='elbowsX'/>", 
            "  <property value='264,280,280,296' name='elbowsY'/>", 
            "  <property value='South_1' name='fromPort'/>", 
            "  <property value='North_1' name='toPort'/>", 
            "  <property value='success' name='label'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='action:updateAnyBean.do#Customer'>", 
            "  <property value='320' name='x'/>", 
            "  <property value='460' name='y'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='forward:path#success#updateAnyBean.jsp#@action:updateAnyBean.do#Customer@'>", 
            "  <property value='320,375,375,356' name='elbowsX'/>", 
            "  <property value='504,504,332,332' name='elbowsY'/>", 
            "  <property value='South_1' name='fromPort'/>", 
            "  <property value='East_1' name='toPort'/>", 
            "  <property value='success' name='label'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='page:index.jsp'>", 
            "  <property value='220' name='x'/>", 
            "  <property value='100' name='y'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='page:updateAnyBean.jsp'>", 
            "  <property value='320' name='x'/>", 
            "  <property value='340' name='y'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='action-call:@page:index.jsp@#@action:showAnyBeanForm.do@'>", 
            "  <property value='231,231,257,284' name='elbowsX'/>", 
            "  <property value='144,212,212,212' name='elbowsY'/>", 
            "  <property value='South_2' name='fromPort'/>", 
            "  <property value='West_1' name='toPort'/>", 
            "</pageflow-object>", 
            "<pageflow-object id='action-call:@page:updateAnyBean.jsp@#@action:updateAnyBean.do#Customer@'>", 
            "  <property value='320,320,320,320' name='elbowsX'/>", 
            "  <property value='384,400,400,416' name='elbowsY'/>", 
            "  <property value='South_1' name='fromPort'/>", 
            "  <property value='North_1' name='toPort'/>", 
            "</pageflow-object>", 
            "</view-properties>"
        }
    )
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }

    @Jpf.Action(forwards = {
    @Jpf.Forward(name = "success", path = "updateAnyBean.jsp")})    
    protected Forward showAnyBeanForm()    
    {   
       Forward forward = new Forward("success");
       
       return forward;
    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", path = "updateAnyBean.jsp")
    })    
    protected Forward updateAnyBean(Customer form)    
    {
       Forward forward = new Forward("success");
       forward.addActionOutput("name", form.getName());
       return forward;
    }
    
    public static class Customer
    {
        private String _name = null;
        
        public String getName() {return _name;}
        public void setName(String name) {_name = name;}
    }
}
