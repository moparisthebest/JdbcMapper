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
package pageFlowCore.lifecycle;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Jpf.Controller
public class Controller extends PageFlowController
{
    private static final String INFO_LIST_ATTR = "info";

    @org.apache.beehive.controls.api.bean.Control()
    private TestControl ctrl;

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                path = "page1.jsp") 
        })
    public Forward begin()
    {
        return new Forward( "page1" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nested1",
                path = "nested1/Controller.jpf") 
        })
    public Forward nest()
    {
        return new Forward( "nested1" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "page1",
                path = "page1.jsp") 
        })
    public Forward done()
    {
        return new Forward( "page1" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nullboy",
                path = "nullflow/Controller.jpf") 
        })
    public Forward leave()
    {
        // Go to another pageflow so we can see onDestroy() work.
        return new Forward( "nullboy" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "samePage",
                navigateTo=Jpf.NavigateTo.currentPage) 
        })
    public Forward clear()
    {
        getSession().removeAttribute( INFO_LIST_ATTR );
        getRequest().removeAttribute( "requestNum" );
        return new Forward( "samePage" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "samePage",
                navigateTo=Jpf.NavigateTo.currentPage) 
        })
    public Forward doNothing()
    {
        return new Forward( "samePage" );
    }

    protected void onCreate()
    {
        if ( getRequest().getParameter( "clearHistory" ) != null )
        {
            getSession().removeAttribute( INFO_LIST_ATTR );
        }

        dump( "onCreate", getRequest(), getResponse(), getSession(), getServlet(), null );
    }

    protected void beforeAction()
    {
        dump( "beforeAction", getRequest(), getResponse(), getSession(), getServlet(), getMapping() );
    }

    protected void afterAction()
    {
        dump( "afterAction", getRequest(), getResponse(), getSession(), getServlet(), getMapping() );
    }
    
    protected void onDestroy( HttpSession session )
    {
        dump( "onDestroy", null, null, session, null, null );
    }

    
    public static class Info
    {
        public String _label;
        public String _ctrlMessage;
        public Integer _requestNum;
        public String _request;
        public String _response;

        public String getLabel()
        {
            return _label;
        }

        public void setLabel(String label)
        {
            _label = label;
        }

        public String getServlet()
        {
            return _servlet;
        }

        public void setServlet(String servlet)
        {
            _servlet = servlet;
        }

        public String getMapping()
        {
            return _mapping;
        }

        public void setMapping(String mapping)
        {
            _mapping = mapping;
        }

        public String getCtrlMessage()
        {
            return _ctrlMessage;
        }

        public void setCtrlMessage(String ctrlMessage)
        {
            _ctrlMessage = ctrlMessage;
        }

        public Integer getRequestNum()
        {
            return _requestNum;
        }

        public void setRequestNum(Integer requestNum)
        {
            _requestNum = requestNum;
        }

        public String getRequest()
        {
            return _request;
        }

        public void setRequest(String request)
        {
            _request = request;
        }

        public String getResponse()
        {
            return _response;
        }

        public void setResponse(String response)
        {
            _response = response;
        }

        public String getSession()
        {
            return _session;
        }

        public void setSession(String session)
        {
            _session = session;
        }

        public String _session;
        public String _mapping;
        public String _servlet;
    }

    private void dump( String label, HttpServletRequest request, HttpServletResponse response,
                       HttpSession session, ActionServlet servlet, ActionMapping mapping )
    {
        dump( label, request, response, session, servlet, mapping, ctrl != null ? ctrl.sayHello() : null );
    }

    public static void dump( String label, HttpServletRequest request, HttpServletResponse response,
                             HttpSession session, ActionServlet servlet, ActionMapping mapping,
                             String ctrlMessage )
    {
        List infoList = ( List ) session.getAttribute( INFO_LIST_ATTR );
        if ( infoList == null )
        {
            infoList = new ArrayList();
            session.setAttribute( INFO_LIST_ATTR, infoList );
        }

        String nullStr = "[null]";
        String presentStr = "[present]";
        Info info = new Info();
        info.setLabel(label);
        info.setCtrlMessage(ctrlMessage != null ? ctrlMessage : nullStr);

        Integer requestNum = null;

        if ( request != null )
        {
            requestNum = ( Integer ) request.getAttribute( "requestNum" );
        }
        else
        {
            // We're in onDestroy -- no request.
            requestNum = new Integer( -1 );
        }

        if ( requestNum == null )
        {
            for ( int i = infoList.size() -1; i >= 0; --i )
            {
                Info lastInfo = ( Info ) infoList.get( i );

                if ( lastInfo.getRequestNum().intValue() != -1 )
                {
                    requestNum = new Integer( lastInfo.getRequestNum().intValue() + 1 );
                    break;
                }
            }

            if ( requestNum == null )
            {
                requestNum = new Integer( 1 );
            }

            request.setAttribute( "requestNum", requestNum );
        }

        info.setRequestNum(requestNum);
        info.setRequest( request != null ? presentStr : nullStr );
        info.setResponse( response != null ? presentStr : nullStr );
        info.setSession( session != null ? presentStr : nullStr );
        info.setMapping( mapping != null ? presentStr : nullStr );
        info.setServlet( servlet != null ? presentStr : nullStr );

        infoList.add( info );
    }
}
