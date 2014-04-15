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
package tags.anchorActions;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/tags/anchorActions/Controller.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:ActionOne.do'>",
        "  <property value='500' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:ActionTwo.do'>",
        "  <property value='500' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:ActionThree.do'>",
        "  <property value='500' name='x'/>",
        "  <property value='220' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property value='360' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>",
        "  <property value='136,230,230,324' name='elbowsX'/>",
        "  <property value='132,132,132,132' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:ActionOne.do@'>",
        "  <property value='464,430,430,396' name='elbowsX'/>",
        "  <property value='132,132,121,121' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_0' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:ActionTwo.do@'>",
        "  <property value='464,430,430,396' name='elbowsX'/>",
        "  <property value='72,72,132,132' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:ActionThree.do@'>",
        "  <property value='464,430,430,396' name='elbowsX'/>",
        "  <property value='212,212,143,143' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_2' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:error.jsp'>",
        "  <property value='480' name='x'/>",
        "  <property value='320' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:goErrors.do'>",
        "  <property value='360' name='x'/>",
        "  <property value='320' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#error.jsp#@action:goErrors.do@'>",
        "  <property value='396,420,420,444' name='elbowsX'/>",
        "  <property value='312,312,312,312' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:goErrors.do@'>",
        "  <property value='360,360,360,360' name='elbowsX'/>",
        "  <property value='184,230,230,276' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Controller extends PageFlowController
{
    
    private String[] actions = {"ActionOne", "ActionTwo", "ActionThree"};
    private String nullAction = null;
    private String lastAction = null;
    private String invalidAction = "invalidAction";

    public String[] getActions() {
        return actions;
    }
    public String getNullAction() {
        return nullAction;
    }
    public String getInvalidAction() {
        return invalidAction;
    }
    public String getLastAction() {
        return lastAction;
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        lastAction = "begin";
        return new Forward( "success" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward ActionOne()
    {
        lastAction = "ActionOne";
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward ActionTwo()
    {
        lastAction = "ActionTwo";
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward ActionThree()
    {
        lastAction = "ActionThree";
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "error.jsp") 
        })
    protected Forward goErrors()
    {
		return new Forward( "success" );
	}
}
