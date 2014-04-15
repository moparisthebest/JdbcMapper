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
package org.apache.beehive.netui.core.urls;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;


/**
 * Methods for registering URL rewriters, adding URL rewriters
 * to the chain, and for rewriting URLs using registered rewriters.
 *
 * <p> Note that when a URLRewriter is registered with this service
 * it is added to a chain (a List) of rewriters. When rewriting
 * occurs, we loop through each rewriter in the list. The only exception
 * to this is when a rewriter that does not allow other rewriters
 * to be used is registered. This then becomes the exclusive rewriter
 * to use and no other rewriters can be registered. </p>
 *
 * <p> The final step of the full rewriting process should be to run the
 * rewritten URI through the templated URL formatting process. See
 * {@link #getTemplatedURL} </p>
 *
 * <p> Also note that this API allows a client to register their own templated
 * URI formatter so they can manage their own templates and formatting. </p>
 */
public class URLRewriterService
{
    private static final Logger _log = Logger.getInstance( URLRewriterService.class );

    private static final String URL_REWRITERS_KEY = "url_rewriters";
    private static final String TEMPLATTED_URL_FORMATTER_KEY = "templated_url_formatter";

    /**
     * Get the prefix to use when rewriting a query parameter name.
     * Loops through the list of registered URLRewriters to build up a the prefix.
     *
     * @param servletContext the current ServletContext.
     * @param request        the current ServletRequest.
     * @param name           the name of the query parameter.
     * @return a prefix to use to rewrite a query parameter name.
     */
    public static String getNamePrefix( ServletContext servletContext, ServletRequest request, String name )
    {
        ArrayList/*< URLRewriter >*/ rewriters = getRewriters( request );

        InternalStringBuilder prefix = new InternalStringBuilder();

        if ( rewriters != null )
        {
            for ( Iterator i = rewriters.iterator(); i.hasNext(); )  
            {
                URLRewriter rewriter = ( URLRewriter ) i.next();
                String nextPrefix = rewriter.getNamePrefix( servletContext, request, name );
                if ( nextPrefix != null ) { prefix.append( nextPrefix ); }
            }
        }

        return prefix.toString();
    }

    /**
     * This method will return two bits of information that are used by components that want run through
     * the AJAX facilities.  The <code>AjaxUrlInfo</code> class is returned and specifies this information.  Unlike
     * the other URLRewriter method, this is a true Chain of Responsibility (CoR) implementation.  The first URLRewriter
     * to return the AjaxUrlInfo object wins and that is returned from this call.  The reason for this is that
     * the implementation of the Ajax Context is also a true CoR implementation.  These must match.
     * @param servletContext the current ServletContext.
     * @param request the current ServletRequest.
     * @param nameable this object that is the target of the Ajax request.  Typically it is an INameable.
     */
    public static AjaxUrlInfo getAjaxUrl(ServletContext servletContext, ServletRequest request, Object nameable)
    {
        ArrayList/*< URLRewriter >*/ rewriters = getRewriters( request );

        if ( rewriters != null )
        {
            for ( Iterator i = rewriters.iterator(); i.hasNext(); )
            {
                URLRewriter rewriter = ( URLRewriter ) i.next();
                AjaxUrlInfo info = rewriter.getAjaxUrl(servletContext,request,nameable);
                if (info != null)
                    return info;
            }
        }
        return null;
    }

    /**
     * Rewrite the given URL, looping through the list of registered URLRewriters.
     *
     * <p> Once the MutableURI has been rewritten, and if it is an instance of
     * {@link FreezableMutableURI}, then this method will set the URI to a frozen
     * state. I.e. immutable. If a user then tries to use a setter method on the
     * rewritten URI, the FreezableMutableURI will throw an IllegalStateException. </p>
     *
     * <p> Note that after the rewritting the caller should run the rewritten URI
     * through the templated URI formatting process as the last step in rewriting.
     * See {@link #getTemplatedURL} </p>
     *
     * @param servletContext the current ServletContext.
     * @param request        the current ServletRequest.
     * @param response       the current ServletResponse.
     * @param url            the URL to be rewritten.
     * @param type           the type of URL to be rewritten.  This is one of the following values:
     *    <ul>
     *    <li><code>action</code>: a standard (non-resource) URL
     *    <li><code>resource</code>: a resource (e.g., image) URL
     *    </ul>
     * @param needsToBeSecure a flag indicating whether the URL should be secure (SSL required) or not
     * @see #registerURLRewriter
     */
    public static void rewriteURL( ServletContext servletContext, ServletRequest request,
                                   ServletResponse response, MutableURI url, URLType type,
                                   boolean needsToBeSecure )
    {
        ArrayList/*< URLRewriter >*/ rewriters = getRewriters( request );

        if ( rewriters != null )
        {
            for ( Iterator i = rewriters.iterator(); i.hasNext(); )  
            {
                URLRewriter rewriter = ( URLRewriter ) i.next();
                rewriter.rewriteURL( servletContext, request, response, url, type, needsToBeSecure );
            }
        }

        if ( url instanceof FreezableMutableURI )
        {
            ( ( FreezableMutableURI ) url ).setFrozen( true );
        }
    }

    /**
     * Get the unmodifiable list of URLRewriter objects in the request that will be used if
     * {@link #rewriteURL} is called.
     *
     * @param request  the current ServletRequest.
     * @return an unmodifiable list of the URLRewriters that have been registered.
     */
    public static List/*< URLRewriter >*/ getURLRewriters( ServletRequest request )
    {
        return Collections.unmodifiableList( getRewriters( request ) );
    }

    /**
     * Register a URLRewriter (add to a list) in the request.  It will be added to the end
     * of a list of URLRewriter objects and will be used if {@link #rewriteURL} is called.
     *
     * @param request  the current ServletRequest.
     * @param rewriter the URLRewriter to register.
     * @return <code>false</code> if a URLRewriter has been registered
     *         that does not allow other rewriters. Otherwise, <code>true</code>
     *         if the URLRewriter was added to the chain or already exists in
     *         the chain.
     */
    public static boolean registerURLRewriter( ServletRequest request, URLRewriter rewriter )
    {
        ArrayList/*< URLRewriter >*/ rewriters = getRewriters( request );

        if ( rewriters == null )
        {
            rewriters = new ArrayList/*< URLRewriter >*/();
            rewriters.add( rewriter );
            request.setAttribute( URL_REWRITERS_KEY, rewriters );
        }
        else
        {
            return addRewriter( rewriters, rewriter, rewriters.size() );
        }

        return true;
    }

    /**
     * Register a URLRewriter (add to a list) in the request.  It will be added at the
     * specified position in this list of URLRewriter objects and will be used if
     * {@link #rewriteURL} is called.
     *
     * @param index    the place to insert the URLRewriter
     * @param request  the current ServletRequest.
     * @param rewriter the URLRewriter to register.
     * @return <code>false</code> if a URLRewriter has been registered
     *         that does not allow other rewriters. Otherwise, <code>true</code>
     *         if the URLRewriter was added to the chain or already exists in
     *         the chain.
     */
    public static boolean registerURLRewriter( int index, ServletRequest request, URLRewriter rewriter )
    {
        ArrayList/*< URLRewriter >*/ rewriters = getRewriters( request );

        if ( rewriters == null )
        {
            rewriters = new ArrayList/*< URLRewriter >*/();
            rewriters.add( rewriter );
            request.setAttribute( URL_REWRITERS_KEY, rewriters );
        }
        else
        {
            return addRewriter( rewriters, rewriter, index );
        }

        return true;
    }

    private static ArrayList/*< URLRewriter >*/ getRewriters( ServletRequest request )
    {
        return ( ArrayList/*< URLRewriter >*/ ) ScopedServletUtils.getScopedRequestAttribute( URL_REWRITERS_KEY, request );
    }
    
    private static boolean addRewriter( ArrayList/*< URLRewriter >*/ rewriters, URLRewriter rewriter, int index )
    {
        if ( otherRewritersAllowed( rewriters ) )
        {
            if ( !rewriters.contains( rewriter ) )
            {
                if ( !rewriter.allowOtherRewriters() )
                {
                    rewriters.clear();

                    if ( rewriters.size() > 0 && _log.isDebugEnabled() )
                    {
                        InternalStringBuilder message = new InternalStringBuilder();
                        message.append( "Register exclusive URLRewriter, \"");
                        message.append( rewriter.getClass().getName() );
                        message.append( "\". This removes any other URLRewriter objects already registered in the chain." );
                        _log.debug( message.toString() );
                    }
                }
                rewriters.add( index, rewriter );
            }
        }
        else
        {
            if ( _log.isDebugEnabled() )
            {
                InternalStringBuilder message = new InternalStringBuilder();
                message.append( "Cannot register URLRewriter, \"");
                message.append( rewriter.getClass().getName() );
                message.append( "\". The URLRewriter, \"" );
                message.append( rewriters.get( 0 ).getClass().getName() );
                message.append( "\", is already registered and does not allow other rewriters." );
                _log.debug( message.toString() );
            }

            return false;
        }

        return true;
    }

    private static boolean otherRewritersAllowed( ArrayList/*< URLRewriter >*/ rewriters )
    {
        if ( rewriters != null && rewriters.size() == 1 &&
             ! ( ( URLRewriter ) rewriters.get( 0 ) ).allowOtherRewriters() )
        {
            return false;
        }

        return true;
    }

    /**
     * Unregister the URLRewriter (remove from the list) from the request.
     *
     * @param request the current ServletRequest.
     * @param rewriter the URLRewriter to unregister
     * @see #registerURLRewriter
     */
    public static void unregisterURLRewriter( ServletRequest request, URLRewriter rewriter )
    {
        if ( rewriter == null ) { return; }

        ArrayList/*< URLRewriter >*/ rewriters = getRewriters( request );

        if ( rewriters == null )
        {
            return;
        }
        else
        {
            rewriters.remove( rewriter );

            if ( rewriters.size() == 0 )
            {
                request.removeAttribute( URL_REWRITERS_KEY );
            }
        }
    }

    /**
     * Unregister the URLRewriter (remove from the list) from the request.
     *
     * @param request the current ServletRequest.
     */
    public static void unregisterAllURLRewriters( ServletRequest request )
    {
        request.removeAttribute( URL_REWRITERS_KEY );
    }

    /**
     * Tell whether rewritten form actions should be allowed to have query parameters.  If this returns
     * <code>false</code>, then a form-tag implementation should render query parameters into hidden
     * fields on the form instead of allowing them to remain in the URL.
     */
    public static boolean allowParamsOnFormAction( ServletContext servletContext, ServletRequest request )
    {
        ArrayList/*< URLRewriter >*/ rewriters = getRewriters( request );

        if ( rewriters != null )
        {
            for ( Iterator i = rewriters.iterator(); i.hasNext(); )  
            {
                URLRewriter rewriter = ( URLRewriter ) i.next();
                if ( !rewriter.allowParamsOnFormAction( servletContext, request ) ) { return false; }
            }
        }

        return true;
    }

    /**
     * Print out information about the chain of URLRewriters in this request.
     *
     * @param request the current HttpServletRequest.
     * @param output a PrintStream to output chain of URLRewriters in this request.
     *               If <code>null</null>, <code>System.err</code> is used.
     */
    public static void dumpURLRewriters( ServletRequest request, PrintStream output )
    {
        ArrayList/*< URLRewriter >*/ rewriters = getRewriters( request );

        if ( output == null ) output = System.err;
        output.println( "*** List of URLRewriter objects: " + rewriters );

        if ( rewriters != null )
        {
            int count = 0;
            for ( Iterator i = rewriters.iterator(); i.hasNext(); )  
            {
                URLRewriter rewriter = ( URLRewriter ) i.next();
                output.println( "        " + count++ + ".  " + rewriter.getClass().getName() );
                output.println( "            allows other rewriters: " + rewriter.allowOtherRewriters() );
                output.println( "            rewriter: " + rewriter );
            }
        }
        else
        {
            output.println( "        No URLRewriter objects are registered with this request." );
        }
    }

    /**
     * Format the given URI using a URL template, if defined in the URL template
     * config file, WEB-INF/url-template-config.xml. The {@link URIContext}
     * encapsulates some additional data needed to write out the string form.
     * E.g. It defines if the &quot;&amp;amp;&quot; entity or the
     * '&amp;' character should be used to separate quary parameters.
     *
     * <p>First try to use ther per-request registered <code>TemplatedURLFormatter</code>.
     * If one is not registered, try to use the per-webapp default
     * <code>TemplatedURLFormatter</code>, defined in beehive-netui-config.xml
     * (with a class name) and set as an attribute of the ServletContext. Otherwise,
     * with no formatter, just return {@link MutableURI#getURIString(URIContext)}.
     *
     * @param servletContext the current ServletContext.
     * @param request the current ServletRequest.
     * @param uri the MutableURI to be formatted into a String.
     * @param key the URL template type to use for formatting the URI
     * @param uriContext data required to write out the string form.
     * @return the URL as a <code>String</code>
     */
    public static String getTemplatedURL( ServletContext servletContext,
                                          ServletRequest request, MutableURI uri,
                                          String key, URIContext uriContext )
    {
        TemplatedURLFormatter formatter = getTemplatedURLFormatter( request );
        if ( formatter == null )
        {
            formatter = TemplatedURLFormatter.getTemplatedURLFormatter( request );
            if ( formatter == null )
            {
                return uri.getURIString( uriContext );
            }
        }

        return formatter.getTemplatedURL( servletContext, request, uri, key, uriContext );
    }

    private static TemplatedURLFormatter getTemplatedURLFormatter( ServletRequest request )
    {
        return ( TemplatedURLFormatter ) ScopedServletUtils.getScopedRequestAttribute( TEMPLATTED_URL_FORMATTER_KEY, request );
    }

    /**
     * Register a TemplatedURLFormatter in the request.
     *
     * <p> The TemplatedURLFormatter should be used as a final step in the rewriting
     * process to format the rewritten URL as defined by a template in the
     * WEB-INF/url-template-config.xml. There can only be one TemplatedURLFormatter,
     * not a chain as with the URLRewriters. </p>
     *
     * @param request the current ServletRequest.
     * @param formatter the TemplatedURLFormatter to register.
     */
    public static void registerTemplatedURLFormatter( ServletRequest request, TemplatedURLFormatter formatter )
    {
        request.setAttribute( TEMPLATTED_URL_FORMATTER_KEY, formatter );
    }

    /**
     * Unregister the TemplatedURLFormatter from the request.
     *
     * @param request the current ServletRequest.
     */
    public static void unregisterTemplatedURLFormatter( ServletRequest request )
    {
        request.removeAttribute( TEMPLATTED_URL_FORMATTER_KEY );
    }
}
