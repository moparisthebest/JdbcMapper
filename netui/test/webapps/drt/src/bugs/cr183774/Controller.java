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
package bugs.cr183774;

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
        "<pageflow-object id='action:testAction.do#cr183774.Controller.FooForm'>", 
        "  <property value='340' name='x'/>", 
        "  <property value='100' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:testAction.do@'>", 
        "  <property value='256,280,280,304' name='elbowsX'/>", 
        "  <property value='92,92,92,92' name='elbowsY'/>", 
        "  <property value='East_1' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#success#index.jsp#@action:testAction.do@'>", 
        "  <property value='304,280,280,256' name='elbowsX'/>", 
        "  <property value='81,81,81,81' name='elbowsY'/>", 
        "  <property value='West_0' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "  <property value='success' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='formbean:FooForm'/>", 
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
    @Jpf.Forward(name = "success", path = "index.jsp")
})
        protected Forward testAction(bugs.cr183774.Controller.FooForm form)        {
        Forward success = new Forward("success");
        
                return success;
    }

    public static class FooForm implements Serializable
    {
        private String foo;

        private String bar;

        public String getFoo()
        { return this.foo; }

        public void setFoo(String foo)
        { this.foo = foo; }

        public String getBar()
        { return this.bar; }

        public void setBar(String bar)
        { this.bar = bar; }
    }
                }
