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
package tags.content;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
@Jpf.ViewProperties(
    value = { 
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
        "<view-properties>", 
        "<pageflow-object id='pageflow:/tags/content/Controller.jpf'/>", 
        "<pageflow-object id='action:begin.do'>", 
        "  <property value='100' name='x'/>", 
        "  <property value='80' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='page:Begin.jsp'>", 
        "  <property value='240' name='x'/>", 
        "  <property value='80' name='y'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='forward:path#begin#Begin.jsp'/>", 
        "</view-properties>"
    }
)
public class Controller extends PageFlowController
{
    
    private String _nbsp = "&nbsp";
    public String getNbsp() {
        return _nbsp;
    }

    private String _amp = "&amp;";
    public String getAmp() {
        return _amp;
    }

    private String _lt = "&lt;";
    public String getLessThan() {
        return _lt;
    }

    private String _html = "<b>Bold</b>";
    public String getHtml() {
	return _html;
    }

    private String _text = "Some Text";
        public String getText() {
	return _text;
    }

    @Jpf.Action(
        )
    protected Forward begin(){
        return new Forward("begin");
    }
}


