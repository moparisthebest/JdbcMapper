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
package mockportal;

import org.apache.beehive.netui.core.urls.URLRewriterService;
import org.apache.beehive.netui.pageflow.PageFlowUtils;
import org.apache.beehive.netui.pageflow.FlowControllerFactory;
import org.apache.beehive.netui.pageflow.ActionResult;
import org.apache.beehive.netui.pageflow.RequestContext;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.scoping.ScopedResponse;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MockPortletTag extends BodyTagSupport
{
    private String _portletID;
    private String _pageFlowURI;
    private String _strutsModulePath;
    private String _listenTo;
    private boolean _verbose;

    private static final String CURRENT_URL_ATTR_PREFIX = "mockportal.currentURL:";
    private static final String STORED_ATTRS_ATTR = "_netui:mockPortalStoredAttrs";

    /**
     * Remove all session-scoped MockPortal information.  Used to clean up when running automated tests.
     */
    public static void reset( HttpServletRequest request )
    {
        HttpSession session = request.getSession( false );

        if ( session != null )
        {
            for ( Enumeration e = session.getAttributeNames(); e.hasMoreElements(); )
            {
                String attrName = ( String ) e.nextElement();
                if ( attrName.startsWith( CURRENT_URL_ATTR_PREFIX ) ) session.removeAttribute( attrName );
            }
        }
    }

    public int doStartTag() throws JspException
    {
        try
        {
            //
            // outerRequest and outerResponse are the "real" request and response.
            //
            HttpServletRequest outerRequest = ( HttpServletRequest ) pageContext.getRequest();
            HttpServletResponse outerResponse = ( HttpServletResponse ) pageContext.getResponse();
            ServletContext outerServletContext = pageContext.getServletContext();

            PrintWriter out = outerResponse.getWriter();

            //
            // We're having each portlet keep track of its current URL in the session.  'Cause it was easy.
            //
            String currentURL = ( String ) outerRequest.getSession().getAttribute( CURRENT_URL_ATTR_PREFIX + _portletID );

            //
            // Override the request/response (etc.) with scoped versions.  Keep track of these scoped
            // objects -- together they constitute the scoped environment for the portlet.
            //
            String requestURI = currentURL != null ? outerRequest.getContextPath() + currentURL : null;
            ScopedRequest scopedRequest =
                    ScopedServletUtils.getScopedRequest( outerRequest, requestURI, outerServletContext, _portletID, true );
            ScopedResponse scopedResponse =
                    ScopedServletUtils.getScopedResponse( outerResponse, scopedRequest );

            if ( _listenTo != null )
            {
                scopedRequest.addListenScope( _listenTo );
            }

            // Set up some attributes for MockPortalUrlRewriter.
            URLRewriterService.registerURLRewriter( scopedRequest, new MockPortalUrlRewriter());
            scopedRequest.setAttribute( "portletID", _portletID );
            scopedRequest.setAttribute( "repostURL", outerRequest.getRequestURI() );

            PageFlowController pfc = PageFlowUtils.getCurrentPageFlow( scopedRequest, pageContext.getServletContext() );
            if ( pfc == null )
            {
                if ( _pageFlowURI != null )
                {
                    scopedRequest.setRequestURI( outerRequest.getContextPath() + _pageFlowURI );
                    FlowControllerFactory factory = FlowControllerFactory.get( pageContext.getServletContext() );
                    pfc = factory.getPageFlowForRequest( new RequestContext( scopedRequest, scopedResponse ) );
                }
            }

            out.println( "<table border=\"1\" cellspacing=\"1\" cellpadding=\"5\" width=\"100%\">" );
            out.println( "<tr bordercolor=\"White\"><td bgcolor=\"#EEEEFF\">" );
            out.print( "<b>Portlet " );
            out.print( _portletID );
            out.println( "</b></td></tr><tr bordercolor=\"White\"><td bgcolor=\"#EEEEFF\">" );

            String action = null;

            boolean submittedThisPortlet = ( scopedRequest.getParameter( "_submit" ) != null );

            if ( currentURL == null )   // First time -- execute the begin action
            {
                if ( pfc != null )
                {
                    currentURL = pfc.getURI();
                    action = "begin";  // @TODO add a tag attr to allow overriding this
                }

                submittedThisPortlet = true;
            }

            ActionResult actionResult = null;

            if ( submittedThisPortlet )
            {
                scopedRequest.setActiveRequest();

                String requestAction = scopedRequest.getParameter( "altAction" );
                if ( requestAction != null )
                {
                    action = requestAction;
                }

                if ( action == null || action.length() == 0 )
                {
                    action = scopedRequest.getParameter( "actionSelect" );
                }

                printValue( out, "Action was", action, "green" );

                try
                {
                    String modulePath = ( pfc != null ? pfc.getModulePath() : null );
                    action = modulePath + "/" + action;
                    actionResult = PageFlowUtils.strutsLookup( outerServletContext, scopedRequest, scopedResponse,
                                                               action, null );

                    if ( actionResult != null )
                    {
                        printValue( out, "Action result URI", actionResult.getURI(), "green" );
                        printValue( out, "Action result isRedirect", new Boolean( actionResult.isRedirect() ), "green" );

                        if ( _verbose )
                        {
                            printValue( out, "Action result isError", new Boolean( actionResult.isError() ), "green" );
                            printValue( out, "Action result statusCode", new Integer( actionResult.getStatusCode() ), "green" );
                            printValue( out, "Action result statusMessage", actionResult.getStatusMessage(), "green" );
                        }
                    }
                    else
                    {
                        printValue( out, "Action result", actionResult, "green" );
                    }

                    currentURL = actionResult != null ? actionResult.getURI() : null;

                    if ( actionResult != null && actionResult.isRedirect() )
                    {
                        // Redirect URIs include the context path.  Strip it -- we're doing a forward.
                        currentURL = currentURL.substring( scopedRequest.getContextPath().length() );
                    }
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    out.println( "<font color=\"red\"><pre>" );

                    StringWriter err = new StringWriter();
                    e.printStackTrace( new PrintWriter( err ) );
                    out.println( err.toString() );
                    out.println( "</pre></font>" );
                }
            }
            else
            {
                restoreAttributes( outerRequest.getSession(), scopedRequest );
            }

            outerRequest.getSession().setAttribute( CURRENT_URL_ATTR_PREFIX + _portletID , currentURL );

            PageFlowController currentPageFlow =
                    PageFlowUtils.getCurrentPageFlow( scopedRequest, pageContext.getServletContext() ) ;
            String type = ( currentPageFlow != null ? currentPageFlow.getClass().getName() : "null" );
            printValue( out, "PageFlow/StrutsModule type", type );

            printValue( out, "Current URL", currentURL );
            out.println( "</td></tr>" );

            if ( currentPageFlow != null && ! submittedThisPortlet )
            {
                currentPageFlow.refresh( scopedRequest, scopedResponse );
            }


            //
            // MockPortal.jsp uses the mockCurrentUrl attribute to choose which page to jsp:include.
            //
            if ( currentURL != null ) //&& currentURL.endsWith( ".jsp" ) )
            {
                pageContext.setAttribute( "mockCurrentUrl", currentURL );
                out.println( "<tr><td>" );  // block-quote any body content            
                out.flush();
                scopedRequest.getRequestDispatcher( currentURL ).include( scopedRequest, scopedResponse );
                out.println( "</td></tr>" );  // block-quote any body content
            }
            else
            {
                pageContext.setAttribute( "mockCurrentUrl", "" );
            }

            out.println( "</table><br><br>" );
            out.flush();
        }
        catch ( ServletException e )
        {
            System.err.println( "****" );
            Throwable ex = e.getRootCause();
            if (ex != null)
                ex.printStackTrace();
            else
                e.printStackTrace();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }


        return EVAL_BODY_INCLUDE;
    }

    protected void printValue( PrintWriter out, String prefix, Object value )
            throws IOException
    {
        printValue( out, prefix, value, "black" );
    }

    protected void printValue( PrintWriter out, String prefix, Object value, String color )
        throws IOException
    {
        out.print( "<font color=\"" + color + "\">" );
        out.print( prefix + ": <b>" );
        out.print( value != null ? value : "[none]" );
        out.println( "</b></font><br>" );
    }

    public int doEndTag() throws JspException
    {
        HttpServletRequest outerRequest = ( HttpServletRequest ) pageContext.getRequest();
        ScopedRequest scopedRequest =
                ScopedServletUtils.getScopedRequest( outerRequest, null, pageContext.getServletContext(),
                                                     _portletID, true );
        persistAttributes( outerRequest.getSession(), scopedRequest );

        pageContext.removeAttribute( "mockCurrentUrl" );

        return super.doEndTag();
    }

    public String getPortletID()
    {
        return _portletID;
    }

    public void setPortletID( String portletID )
    {
        _portletID = portletID;
    }

    public String getPageFlowURI()
    {
        return _pageFlowURI;
    }

    public void setPageFlowURI( String pageFlowURI )
    {
        _pageFlowURI = pageFlowURI;
    }

    public String getListenTo()
    {
        return _listenTo;
    }

    public void setListenTo( String listenTo )
    {
        _listenTo = listenTo;
    }

    public String getStrutsModulePath()
    {
        return _strutsModulePath;
    }

    public void setStrutsModulePath( String strutsModulePath )
    {
        _strutsModulePath = strutsModulePath;
    }

    public void release()
    {
        _listenTo = null;
    }

    public boolean isVerbose()
    {
        return _verbose;
    }

    public void setVerbose( boolean verbose )
    {
        _verbose = verbose;
    }

    private void persistAttributes( HttpSession session, ScopedRequest scopedRequest )
    {
        String attrName = scopedRequest.getScopedName( STORED_ATTRS_ATTR );
        Map attrs = scopedRequest.getAttributeMap();
        Map serializableAttrs = new HashMap();

        for ( Iterator i = attrs.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = ( Map.Entry ) i.next();

            if ( entry.getValue() instanceof Serializable )
            {
                serializableAttrs.put( entry.getKey(), entry.getValue() );
            }
        }

        session.setAttribute( attrName, serializableAttrs );
    }

    private void restoreAttributes( HttpSession session, ScopedRequest scopedRequest )
    {
        String attrName = scopedRequest.getScopedName( STORED_ATTRS_ATTR );
        Map savedAttrs = ( Map ) session.getAttribute( attrName );
        scopedRequest.setAttributeMap( savedAttrs );
    }
}
