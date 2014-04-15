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
package javascript.jsFind;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/jsFind/JsFind.jpf"/>
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
 * <pageflow-object id="formbean:jsFind.JsFind.TypeBean"/>
 * <pageflow-object id="formbeanprop:jsFind.JsFind.TypeBean#type#java.lang.String"/>
 * <pageflow-object id="action:postForm.do#jsFind.JsFind.TypeBean">
 *   <property value="380" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:postForm.do#jsFind.JsFind.TypeBean@">
 *   <property value="276,310,310,344" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#index.jsp#@action:postForm.do#jsFind.JsFind.TypeBean@">
 *   <property value="416,416,240,240" name="elbowsX"/>
 *   <property value="81,60,60,56" name="elbowsY"/>
 *   <property value="East_0" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="success" name="label"/>
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
        "<pageflow-object id='pageflow:/jsFind/JsFind.jpf'/>",
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
        "<pageflow-object id='formbean:jsFind.JsFind.TypeBean'/>",
        "<pageflow-object id='formbeanprop:jsFind.JsFind.TypeBean#type#java.lang.String'/>",
        "<pageflow-object id='action:postForm.do#jsFind.JsFind.TypeBean'>",
        "  <property value='380' name='x'/>",
        "  <property value='100' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:postForm.do#jsFind.JsFind.TypeBean@'>",
        "  <property value='276,310,310,344' name='elbowsX'/>",
        "  <property value='92,92,92,92' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#index.jsp#@action:postForm.do#jsFind.JsFind.TypeBean@'>",
        "  <property value='416,416,240,240' name='elbowsX'/>",
        "  <property value='81,60,60,56' name='elbowsY'/>",
        "  <property value='East_0' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class JsFind extends PageFlowController
{
    private Map options;
    private String type;

    public Map getOptions()
    {
        return options;
    }

    public void setOptions(Map options)
    {
        this.options = options;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
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
        options = new HashMap();
        options.put("value1","Foo Value");
        options.put("value2","Bar Value");
        options.put("value3","Baz Value");
        
        return new Forward("success");
    }

    /**
     * @jpf:action

     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward postForm(TypeBean form)
    {
        type = form.getType();
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class TypeBean implements Serializable
    {
        private String type;

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
