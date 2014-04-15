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
package pageFlowCore.pfPageFlow;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Jpf.ViewProperties(
    value = { 
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
        "<view-properties>", 
        "<pageflow-object id='pageflow:/pageFlowCore/pfPageFlow/Controller.jpf'/>", 
        "<pageflow-object id='action:begin.do'/>", 
        "<pageflow-object id='action:sendError.do'/>", 
        "<pageflow-object id='action:getInfo.do'/>", 
        "<pageflow-object id='page:Begin.jsp'/>", 
        "<pageflow-object id='forward:path#begin#Begin.jsp#@action:begin.do@'/>", 
        "<pageflow-object id='forward:path#begin#Begin.jsp#@action:sendError.do@'/>", 
        "<pageflow-object id='forward:path#begin#Begin.jsp#@action:getInfo.do@'/>", 
        "<pageflow-object id='action-call:@page:Begin.jsp@#@action:sendError.do@'>", 
        "  <property value='204,170,170,136' name='elbowsX'/>", 
        "  <property value='232,232,232,232' name='elbowsY'/>", 
        "  <property value='West_1' name='fromPort'/>", 
        "  <property value='East_1' name='toPort'/>", 
        "</pageflow-object>", 
        "<pageflow-object id='action-call:@page:Begin.jsp@#@action:getInfo.do@'>", 
        "  <property value='204,170,170,136' name='elbowsX'/>", 
        "  <property value='243,243,361,361' name='elbowsY'/>", 
        "  <property value='West_2' name='fromPort'/>", 
        "  <property value='East_0' name='toPort'/>", 
        "</pageflow-object>", 
        "</view-properties>"
    }
)
@Jpf.Controller
public class Controller extends PageFlowController
{
    StringBuffer _sb;
    public String getResults()
    {
        return _sb.toString();
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward begin()
    {
        _sb = new StringBuffer();
        return new Forward("begin");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward sendError()
    {
        _sb = new StringBuffer();
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("Error raised from the Page Flow Test");
            sb.append("<br /><a href='Controller.jpf'>Return</a>");
            HttpServletResponse resp = getResponse();
            sendError(sb.toString(), resp);
        }
        catch (java.io.IOException e) {
            _sb.append("IOException trying to generate a sendError");
            return new Forward("begin");
        }
        return null;

    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward getInfo()
    {
        _sb = new StringBuffer();
        _sb.append("<table border='1' cellspacing='0'cellpaddinig='2'>");

        HttpServletRequest req = getRequest();
        _sb.append("<tr><td>Request</td><td>");
        _sb.append(req.getRequestURL());
        _sb.append("</td></tr>");


        HttpServletResponse resp = getResponse();
        _sb.append("<tr><td>Response</td><td>Committed:");
        _sb.append("" + resp.isCommitted());
        _sb.append("</td></tr>");


        HttpSession session = getSession();
        session.setMaxInactiveInterval( 3600 );
        _sb.append("<tr><td>Session</td><td>In active interval:");
        _sb.append("" + session.getMaxInactiveInterval());
        _sb.append("</td></tr>");

        // nestable
        _sb.append("<tr><td>Nestable</td><td>");
        _sb.append("" + isNestable());
        _sb.append("</td></tr>");

        // nestable
        _sb.append("<tr><td>PageFlow</td><td>");
        _sb.append("" + isPageFlow());
        _sb.append("</td></tr>");

        // get action
        _sb.append("<tr><td>Actions</td><td>");
        String[] actions = getActions();
        java.util.Arrays.sort( actions );
        for (int i=0;i<actions.length;i++) {
            _sb.append(actions[i]);
            _sb.append("<br />");
        }
        _sb.append("</td></tr>");


        // get mapping
        ActionMapping map = getMapping();
        _sb.append("<tr><td>ActionMapping</td><td>");
        _sb.append("" + map);
        _sb.append("</td></tr>");

        // get Taxonomy
        _sb.append("<tr><td>Taxonomy</td><td>");
        _sb.append("" + getTaxonomy());
        _sb.append("</td></tr>");

        
        _sb.append("</table>");

        return new Forward("begin");
    }
}
