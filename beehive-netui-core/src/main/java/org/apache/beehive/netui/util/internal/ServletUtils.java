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
package org.apache.beehive.netui.util.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import java.io.PrintStream;
import java.util.Enumeration;

import org.apache.beehive.netui.util.logging.Logger;

public class ServletUtils {

    private static final Logger LOG = Logger.getInstance(ServletUtils.class);

    public static final String SESSION_MUTEX_ATTRIBUTE = ServletUtils.class.getName() + ".MUTEX";

    /**
     * Print parameters and attributes in the given request.
     * 
     * @param request the current HttpServletRequest.
     * @param output a PrintStream to which to output request parameters and request/session
     *            attributes; if <code>null</null>, <code>System.err</code> is used.
     * 
     */
    public static void dumpRequest( ServletRequest request, PrintStream output )
    {
        if ( output == null )
        {
            output = System.err;
        }

        output.println( "*** ServletRequest " + request );
        
        if ( request instanceof HttpServletRequest )
        {
            output.println( "        uri = " + ( ( HttpServletRequest ) request ).getRequestURI() );
        }

        for ( Enumeration e = request.getParameterNames(); e.hasMoreElements(); )
        {
            String name = ( String ) e.nextElement();
            output.println( "            parameter " + name + " = " + request.getParameter( name ) );
        }

        for ( Enumeration e = request.getAttributeNames(); e.hasMoreElements(); )
        {
            String name = ( String ) e.nextElement();
            output.println( "            attribute " + name + " = " + request.getAttribute( name ) );
        }

        if ( request instanceof HttpServletRequest )
        {
            HttpSession session = ( ( HttpServletRequest ) request ).getSession( false );
            
            if ( session != null )
            {
                for ( Enumeration e = session.getAttributeNames(); e.hasMoreElements(); )
                {
                    String name = ( String ) e.nextElement();
                    output.println( "            session attribute " + name + " = " + session.getAttribute( name ) );
                }
            }
        }
    }

    /**
     * Print attributes in the given ServletContext.
     * 
     * @param context the current ServletContext.
     * @param output a PrintStream to which to output ServletContext attributes; if <code>null</null>,
                     <code>System.err</code> is used.
     * 
     */
    public static void dumpServletContext( ServletContext context, PrintStream output )
    {
        if ( output == null )
        {
            output = System.err;
        }

        output.println( "*** ServletContext " + context );

        for ( Enumeration e = context.getAttributeNames(); e.hasMoreElements(); )
        {
            String name = ( String ) e.nextElement();
            output.println( "            attribute " + name + " = " + context.getAttribute( name ) );
        }
    }
    
    /**
     * Set response headers to prevent caching of the response by the browser.
     * 
     * @param response the current ServletResponse
     */ 
    public static void preventCache( ServletResponse response )
    {
        if ( response instanceof HttpServletResponse )
        {
            HttpServletResponse httpResponse = ( HttpServletResponse ) response;
            httpResponse.setHeader( "Pragma", "No-cache" );
            httpResponse.setHeader( "Cache-Control", "no-cache,no-store,max-age=0" );
            httpResponse.setDateHeader( "Expires", 1 );
        }
    }
    
    /**
     * Get the base filename of the given URI.
     *
     * @param uri the URI from which to get the base filename.
     * @return a String containing everything after the last slash of the given URI.
     */
    public static String getBaseName( String uri )
    {
        int lastSlash = uri.lastIndexOf( '/' );
        assert lastSlash != -1 : uri;
        assert lastSlash < uri.length() - 1 : "URI must not end with a slash: " + uri;
        return uri.substring( lastSlash + 1 );
    }
 
    /**
     * Get the directory path of the given URI.
     *
     * @param uri the URI from which to get the directory path.
     * @return a String containing everything before the last slash of the given URI.
     */
    public static String getDirName( String uri )
    {
        int lastSlash = uri.lastIndexOf( '/' );
        assert lastSlash != -1 : uri;
        assert uri.length() > 1 : uri;
        assert lastSlash < uri.length() - 1 : "URI must not end with a slash: " + uri;
        return uri.substring( 0, lastSlash );
    }
    
    
    /**
     * This initializes the 'cause' on the exception before throwing it; otherwise, the chain of exceptions is hidden
     * because of legacy behavior in ServletException ('rootCause' vs. 'cause').
     */
    public static void throwServletException(Throwable cause)
            throws ServletException
    {
        ServletException servletException = new ServletException(cause);

        // todo: future cleanup
        // the servlet 2.5 api sets does the equivalent of initCause in its constructor
        // where the servlet 2.4 api does not, so check for the 2.5 behavior before setting initCause
        if (servletException.getCause() == null) {
            servletException.initCause(cause);
        }
        throw servletException;
    }

    /**
     * Returns a mutex object for the given {@link HttpSession} that can be used
     * as a lock for a given session. For example, to synchronize lazy
     * initialization of session scoped objects.
     *
     * <p>The semantics for locking on an HttpSession object are unspecified, and
     * servlet containers are free to implement the HttpSession in such a way
     * that acquiring a lock on the HttpSession itself is not safe.  When used
     * in conjunction with a HttpSessionListener (such as NetUI's
     * HttpSessionMutexListener) that puts a mutex object on the session when
     * the session is created, this method provides a lock that is 100% safe
     * to use across servlet containers. If a HttpSessionListener is not
     * registered in web.xml and there is no object for the given attribute name,
     * the HttpSession itself is returned as the next best lock.</p>
     *
     * @param httpSession the current session
     * @param attributeName the attribute name of the mutex object on the session
     * @return a mutex that can be used to serialize operations on the HttpSession
     */
    public static Object getSessionMutex(HttpSession httpSession, String attributeName) {
        assert httpSession != null : "HttpSession must not be null";
        assert attributeName != null : "The attribute name must not be null";

        Object mutex = httpSession.getAttribute(attributeName);
        if(mutex == null)
            mutex = httpSession;

        assert mutex != null;

        if(LOG.isDebugEnabled())
            LOG.debug("Using session lock of type: " + mutex.getClass());

        return mutex;
    }
}
