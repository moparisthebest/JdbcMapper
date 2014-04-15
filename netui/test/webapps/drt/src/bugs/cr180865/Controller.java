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
package bugs.cr180865;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.tags.divpanel.DivPanelState;

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
        "<pageflow-object id='action:postback.do'>", 
        "  <property value='440' name='x'/>", 
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
        "<pageflow-object id='action-call:@page:index.jsp@#@action:postback.do@'>", 
        "  <property value='256,330,330,404' name='elbowsX'/>", 
        "  <property value='92,92,92,92' name='elbowsY'/>", 
        "  <property value='East_1' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#success#index.jsp#@action:postback.do@'>", 
        "  <property value='404,330,330,256' name='elbowsX'/>", 
        "  <property value='81,81,81,81' name='elbowsY'/>", 
        "  <property value='West_0' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "  <property value='success' name='label'/>", 
        "</pageflow-object>", 
        "</view-properties>"
    }
)
public class Controller extends PageFlowController
{
    private DivPanelState _state;
    public DivPanelState getDivState() {
	return _state;
    }
    public void setDivState(DivPanelState state) {
	_state = state;
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        NameService ns = NameService.instance(getRequest().getSession());
	ns.debugSetNameIntValue(1);
        return new Forward("index");
    }



    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "index.jsp")
})
        protected Forward postback()        {
        Forward forward = new Forward("success");
        
                return forward;
    }
             }
