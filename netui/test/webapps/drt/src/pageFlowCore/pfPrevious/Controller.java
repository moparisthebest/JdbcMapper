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
package pageFlowCore.pfPrevious;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionForm;

import java.io.Serializable;

@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _url;
    private String _path = "/Begin.jsp";
    private Serializable _data;
    private String _error="";

    public String getPageInfo()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border='1' cellspacing='0' cellpadding='5pt'>");

        // Previous form
        Object fd = getPreviousFormBean();
        if (fd != _data) {
            _error += "Invalid Form: types mis-match<br />";
        }

        sb.append("<tr><td>Previous Form</td><td>");
        if (fd == null)
            sb.append("<b>No Form Data</b>");
        else
            sb.append(fd.getClass().getName());
        sb.append("</td></tr>");

        // Action URI
        String x = getPreviousActionURI();
        if ( ( _url != null || x != null ) && (_url == null || ! _url.equals(x)))
            _error += "Invalid URL<br />";

        sb.append("<tr><td>Previous Action URI</td><td>");
        sb.append(x);
        sb.append("</td></tr>");

        // Forward Path

        x = getPreviousForwardPath();
        if (_path != x) {
            if (_path == null) {
                _error += "Invalid ForwardPath: _path is null and go unexpected non null";
            }
            else if (x == null) {
                _error += "Invalid ForwardPath: _path is not null, but got a null";
            }
            else  if (!_path.equals(x)) {
                _error += "Invalid ForwardPath [expected: " + _path +
                    " | found: " + x + " ]<br />";
            }
        }

        sb.append("<tr><td>Previous Forward Path</td><td>");
        sb.append(x);
        sb.append("</td></tr>");

        // return the string buffer
        sb.append("</table>");
        return sb.toString();
    }

    public String getError() {
        return _error;
    }

    public void beforeAction()
    {
        _url = PageFlowUtils.getRelativeURI( getRequest(), null );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward begin()
    {
        return new Forward("begin");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward postback()
    {
        _error = "";
        _data = null;
        _path = getPreviousForwardPath();
        return new Forward("begin");
    }

   @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    public Forward form(Form form)
    {
        _error = "";
        _data = form;
        _path = getPreviousForwardPath();
        return new Forward("begin");
    }

    public static class Form implements Serializable
    {
        private String _name;
        public String getName() {
            return _name;
        }
        public void setName(String name) {
            _name = name;
        }
    }

    protected boolean alwaysTrackPreviousAction()
    {
        return true;
    }
    
    protected boolean alwaysTrackPreviousPage()
    {
        return true;
    }
}
