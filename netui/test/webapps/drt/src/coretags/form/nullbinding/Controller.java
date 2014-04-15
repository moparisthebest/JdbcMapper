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
package coretags.form.nullbinding;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
@Jpf.ViewProperties(
    value = { 
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
        "<view-properties>", 
        "<pageflow-object id='pageflow:/coretags/form/nullbinding/Controller.jpf'/>", 
        "<pageflow-object id='action:begin.do'>", 
        "  <property value='80' name='x'/>", 
        "  <property value='100' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:index.jsp'>", 
        "  <property value='220' name='x'/>", 
        "  <property value='100' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'>", 
        "  <property value='116,140,140,164' name='elbowsX'/>", 
        "  <property value='92,92,92,92' name='elbowsY'/>", 
        "  <property value='East_1' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "  <property value='index' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:begin.do@'>", 
        "  <property value='184,150,150,116' name='elbowsX'/>", 
        "  <property value='92,92,92,92' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:${pageFlow.nullValue}.do'>", 
        "  <property value='340' name='x'/>", 
        "  <property value='100' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:${pageFlow.nullValue}.do@'>", 
        "  <property value='256,280,280,304' name='elbowsX'/>", 
        "  <property value='92,92,92,92' name='elbowsY'/>", 
        "  <property value='East_1' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='formbean:FormBean'/>", 
        "</view-properties>"
    }
)
public class Controller extends PageFlowController
{
    private String _nullValue = null;

    public String getNullValue() {
        return _nullValue;
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }

    public static class FormBean implements Serializable
    {
        private String type;

        public String getType()
        { return this.type; }

        public void setType(String type)
        { this.type = type; }
    }
    
}
