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
package bugs.b34998;

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
        "<pageflow-object id='pageflow:/bugs/b34998/Controller.jpf'/>", 
        "<pageflow-object id='formbean:Name'/>", 
        "<pageflow-object id='action:begin.do'>", 
        "  <property value='100' name='x'/>", 
        "  <property value='120' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action:EnterName.do#bugs.b34998.Controller.Name'>", 
        "  <property value='280' name='x'/>", 
        "  <property value='240' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:begin.jsp'>", 
        "  <property value='280' name='x'/>", 
        "  <property value='120' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:results.jsp'>", 
        "  <property value='460' name='x'/>", 
        "  <property value='100' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#success#results.jsp#@action:EnterName.do#bugs.b34998.Controller.Name@'>", 
        "  <property value='316,370,370,424' name='elbowsX'/>", 
        "  <property value='232,232,92,92' name='elbowsY'/>", 
        "  <property value='East_1' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "  <property value='success' name='label'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:begin.jsp@#@action:begin.do@'>", 
        "  <property value='244,190,190,136' name='elbowsX'/>", 
        "  <property value='101,101,101,101' name='elbowsY'/>", 
        "  <property value='West_0' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:begin.jsp@#@action:EnterName.do#bugs.b34998.Controller.Name@'>", 
        "  <property value='280,280,280,280' name='elbowsX'/>", 
        "  <property value='164,180,180,196' name='elbowsY'/>", 
        "  <property value='South_1' name='fromPort'/>", 
        "  <property value='North_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#begin#begin.jsp#@action:begin.do@'>", 
        "  <property value='136,190,190,244' name='elbowsX'/>", 
        "  <property value='112,112,112,112' name='elbowsY'/>", 
        "  <property value='East_1' name='fromPort'/>", 
        "  <property value='West_1' name='toPort'/>", 
        "  <property value='begin' name='label'/>", 
        "</pageflow-object>", 
        "</view-properties>"
    }
)
public class Controller extends PageFlowController
{
    private String text;
    public String getText() {
        return text;
    }
    
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "begin.jsp") 
        })
    public Forward begin()
    {
        return new Forward("begin");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "results.jsp") 
        })
    protected Forward EnterName(Name form)
    {
        text = form.getText();
        char[] c = text.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<c.length;i++) {
            if (c[i] == '\r') {
                if (c[i+1] == '\n')
                    sb.append("<br/>");
                else {
                    sb.append("[Funny Char Sequence:");
                    sb.append("" + (int) c[i]);
                    sb.append(" ");
                    sb.append("" + (int) c[i+1]);
                    sb.append("]");
                }
                continue;
            }
            sb.append(c[i]);
        }
        text = sb.toString();
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class Name implements Serializable
    {
        private java.lang.String text;

        public void setText(java.lang.String text)
        {
            this.text = text;
        }

        public java.lang.String getText()
        {
            return this.text;
        }
    }
}
