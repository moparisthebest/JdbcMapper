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
package tags.bindingUpdateErrors;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;


@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = { 
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
        "<view-properties>", 
        "<pageflow-object id='pageflow:/tags/bindingUpdateErrors/Controller.jpf'/>", 
        "<pageflow-object id='formbean:NameBean'/>", 
        "<pageflow-object id='formbean:TypeBean'/>", 
        "<pageflow-object id='action:begin.do'>", 
        "  <property value='80' name='x'/>", 
        "  <property value='100' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:postName.do#tags.bindingUpdateErrors.Controller.NameBean'>", 
        "  <property value='400' name='x'/>", 
        "  <property value='260' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:postType.do#tags.bindingUpdateErrors.Controller.TypeBean'>", 
        "  <property value='400' name='x'/>", 
        "  <property value='120' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:index.jsp'>", 
        "  <property value='240' name='x'/>", 
        "  <property value='140' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'>", 
        "  <property value='116,160,160,204' name='elbowsX'/>", 
        "  <property value='92,92,132,132' name='elbowsY'/>", 
        "  <property value='East_1' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "  <property value='success' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#success#postType.do#@action:postName.do#tags.bindingUpdateErrors.Controller.NameBean@'>", 
        "  <property value='400,400,400,400' name='elbowsX'/>", 
        "  <property value='216,190,190,164' name='elbowsY'/>", 
        "  <property value='North_1' name='fromPort'/>", 
        "  <property value='South_1' name='toPort'/>", 
        "  <property value='success' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#success#index.jsp#@action:postType.do#tags.bindingUpdateErrors.Controller.TypeBean@'>", 
        "  <property value='364,320,320,276' name='elbowsX'/>", 
        "  <property value='123,123,132,132' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "  <property value='success' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:index.jsp@#@action:postName.do#tags.bindingUpdateErrors.Controller.NameBean@'>", 
        "  <property value='276,320,320,364' name='elbowsX'/>", 
        "  <property value='143,143,252,252' name='elbowsY'/>", 
        "  <property value='East_2' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "</pageflow-object>", 
        "</view-properties>"
    }
)
public class Controller extends PageFlowController
{
    String firstName;
    String lastName;
    /**
     * This method represents the point of entry into the pageflow
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

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "postType.do") 
        })
    protected Forward postName(NameBean form)
    {
        System.err.println("FirstName:" + form.getFirstName());
        System.err.println("LastName:" + form.getLastName());
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward postType(TypeBean form)
    {
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class NameBean implements Serializable
    {
        private String lastName;

        private String firstName;

        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        public String getFirstName()
        {
            return this.firstName;
        }

        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }

        public String getLastName()
        {
            return this.lastName;
        }
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
