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
package org.apache.beehive.netui.pageflow;

// java imports
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;

// internal imports
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.pageflow.internal.InternalConstants;

// external imports
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServletWrapper;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.util.RequestUtils;

/**
 * <p>
 * NetUI utility to wrap Struts logic for handling multipart requests.
 * </p>
 *
 * @exclude
 */
/* package */ class MultipartRequestUtils
{
    private static final Logger _log = Logger.getInstance(MultipartRequestUtils.class);
    
    private static final String PREHANDLED_MULTIPART_REQUEST_ATTR = InternalConstants.ATTR_PREFIX + "handledMultipart";
    
    /**
     * <p>
     * Handle a multipart request.  The return value of this method will be <code>null</code>
     * if the following conditions are not satisfied:
     * <ol>
     * <li>request.getContentType() != null</li>
     * <li>content type is "multipart/form-data"</li>
     * <li>request method is "POST"</li>
     * </ol>
     * If these are satisfied, a Struts {@link MultipartRequestHandler} is created.  This object is used
     * to provide a mapping over both the regular {@link HttpServletRequest} parameters and the 
     * paramters that Struts creates to represent the file(s) that was uploaded.  If file(s) were 
     * uploaded, the {@link java.util.Map} returned has key / value pairs of these two structures:
     * <ul>
     * <li><code>String / String[]</code></li>
     * <li><code>String / {@link org.apache.commons.fileupload.FileItem}</code></li>
     * </ul>
     * <br/>
     * Invokers of this method should be aware that in this case, not all types returned from 
     * what looks like <code>request.getParameterValues(String key)</code> will be <code>String[]</code>.
     * </p>
     *
     * @param request the request object
     * @param bean the current action's associated {@link ActionForm}
     * @return <code>null</code> if the request is <i>not</i> multipart.  Otherwise, a {@link java.util.Map} 
     * is returned that contains the key / value pairs of the parameters in the request and the uploaded
     * files.
     * @throws ServletException if an error occurs loading this file.  These exception messages
     * are not internationalized as Struts does not internationalize them either.
     */
    /* package */ static final Map handleMultipartRequest(HttpServletRequest request, ActionForm bean)
        throws ServletException
    {
        String contentType = request.getContentType();
        String method = request.getMethod();
        boolean isMultipart = false;

        Map multipartParameters = null;

        if(contentType != null &&
           contentType.startsWith("multipart/form-data") &&
           method.equalsIgnoreCase("POST"))
        {
            if ( ! InternalUtils.isMultipartHandlingEnabled( request ) )
            {
                throw new ServletException( "Received a multipart request, but multipart handling is not enabled." );
            }
            
            ActionServletWrapper servlet;
            
            if (bean != null)
            {
                servlet = bean.getServletWrapper();
            }
            else
            {
                ServletContext servletContext = InternalUtils.getServletContext(request);
                servlet = new ActionServletWrapper(InternalUtils.getActionServlet(servletContext));
            }
            
            // @struts: does this -- but we can't rely on the bean not being null.
            // if(bean instanceof ActionForm)
            //     servlet = bean.getServletWrapper();
            // else if ( ! ( bean instanceof ActionForm ) )
            //     throw new ServletException
            //         ("bean that's supposed to be populated from a multipart request is not of type " + 
            //          "\"org.apache.struts.action.ActionForm\", but type \"" + bean.getClass().getName() + "\"");
            
            MultipartRequestHandler multipartHandler = getCachedMultipartHandler(request);
            boolean preHandled = false;
            
            if (multipartHandler == null)
            {
                multipartHandler = getMultipartHandler(request);
            }
            else
            {
                preHandled = true;
            }
            
            if (bean != null)
            {
                bean.setMultipartRequestHandler(multipartHandler);
            }
            
            if(multipartHandler != null)
            {
                isMultipart = true;
                
                servlet.setServletFor(multipartHandler);
                multipartHandler.setMapping((ActionMapping)request.getAttribute(Globals.MAPPING_KEY));
                
                if (! preHandled)
                {
                    multipartHandler.handleRequest(request);
                }
                
                Boolean maxLengthExceeded = (Boolean)request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
                if(maxLengthExceeded != null && maxLengthExceeded.booleanValue())
                {
                    // bail from RequestUtils.processPopulate
                    // @struts: semantics -- if this check fails, no values are updated into the form
                    return null;
                }
                multipartParameters = MultipartRequestUtils.getAllParametersForMultipartRequest(request, multipartHandler);

                // @struts: does this
                //names = Collections.enumeration(multipartParameters.keySet());
            }
        }

        if(!isMultipart)
        {
            // @struts: does this -- NetUI does not so that the ProcessPopulate method can be made faster; this way,
            // ProcessPopulate doesn't care whether this is a MultipartRequest or not; it can just talk to a Map
            // to get key / value pairs.
            //names = request.getParameterNames();
            return null;
        }
        else
        {
            ScopedRequest scopedRequest = ScopedServletUtils.unwrapRequest( request );
            
            if ( scopedRequest != null )
            {
                multipartParameters = scopedRequest.filterParameterMap( multipartParameters );
            }
            return multipartParameters; 
        }
    }

    /**
     * Can be called early in the request processing cycle to cache a single multipart handler for the request.
     */
    static void preHandleMultipartRequest(HttpServletRequest request)
        throws ServletException
    {
        MultipartRequestHandler multipartHandler = getCachedMultipartHandler(request);
        
        if (multipartHandler == null)
        {
            multipartHandler = getMultipartHandler(request);
            
            if (multipartHandler != null)
            {
                //
                // Run the request through the handler, and cache the handler in the outer request.
                //
                multipartHandler.handleRequest(request);
                HttpServletRequest outerRequest = ScopedServletUtils.getOuterRequest(request);
                outerRequest.setAttribute(PREHANDLED_MULTIPART_REQUEST_ATTR, multipartHandler);
            }
        }
    }
    
    /**
     * Create an implementation of a {@link MultipartRequestHandler} for this
     * mulitpart request.
     *
     * @param request the current request object
     * @return the handler
     * @throws ServletException if an error occurs loading this file.  These exception messages
     * are not internationalized as Struts does not internationalize them either.
     */
    // @Struts: org.apache.struts.util.RequestUtils.getMultipartHandler
    private static final MultipartRequestHandler getMultipartHandler(HttpServletRequest request)
        throws ServletException
    {
        MultipartRequestHandler multipartHandler = null;
        String multipartClass = (String) request.getAttribute(Globals.MULTIPART_KEY);
        request.removeAttribute(Globals.MULTIPART_KEY);

        // Try to initialize the mapping specific request handler
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler)RequestUtils.applicationInstance(multipartClass);
            } catch (ClassNotFoundException cnfe) {
                _log.error(
                    "MultipartRequestHandler class \""
                        + multipartClass
                        + "\" in mapping class not found, "
                        + "defaulting to global multipart class");
            } catch (InstantiationException ie) {
                _log.error(
                    "InstantiaionException when instantiating "
                        + "MultipartRequestHandler \""
                        + multipartClass
                        + "\", "
                        + "defaulting to global multipart class, exception: "
                        + ie.getMessage());
            } catch (IllegalAccessException iae) {
                _log.error(
                    "IllegalAccessException when instantiating "
                        + "MultipartRequestHandler \""
                        + multipartClass
                        + "\", "
                        + "defaulting to global multipart class, exception: "
                        + iae.getMessage());
            }

            if (multipartHandler != null)
                return multipartHandler;
        }

        ModuleConfig moduleConfig = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
        multipartClass = moduleConfig.getControllerConfig().getMultipartClass();

        // Try to initialize the global request handler
        if (multipartClass != null) {
            try {
                multipartHandler = (MultipartRequestHandler) RequestUtils.applicationInstance(multipartClass);
            } catch (ClassNotFoundException cnfe) {
                throw new ServletException(
                    "Cannot find multipart class \""
                        + multipartClass
                        + "\""
                        + ", exception: "
                        + cnfe.getMessage());
            } catch (InstantiationException ie) {
                throw new ServletException(
                    "InstantiaionException when instantiating "
                        + "multipart class \""
                        + multipartClass
                        + "\", exception: "
                        + ie.getMessage());
            } catch (IllegalAccessException iae) {
                throw new ServletException(
                    "IllegalAccessException when instantiating "
                        + "multipart class \""
                        + multipartClass
                        + "\", exception: "
                        + iae.getMessage());
            }

            if (multipartHandler != null)
                return multipartHandler;
        }

        return multipartHandler;
    }

    /**
     * Get a {@link java.util.Map} object that reprensents the request parameters for 
     * a multipart request.  As described in {@link #handleMultipartRequest}, the 
     * Map returned here may contain either String[] or FileItem values.  The former
     * are regular request parameters and the latter are the Struts abstraction
     * on top of an uploaded file
     *
     * @param request the request
     * @param multipartHandler the multipart handler for this request
     * @return a Map of the key / value pairs of parameters in this request and object
     * representations of the uploaded files.
     */
    // @Struts: org.apache.struts.util.RequestUtils.getAllParametrsForMultipartRequest
    private static final Map getAllParametersForMultipartRequest(HttpServletRequest request, MultipartRequestHandler multipartHandler)
    {
        Map parameters = new HashMap();
        Enumeration e;

        Hashtable elements = multipartHandler.getAllElements();
        e = elements.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            parameters.put(key, elements.get(key));
        }

        if (request instanceof MultipartRequestWrapper) {
            request = ((MultipartRequestWrapper)request).getRequest();
            e = request.getParameterNames();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                parameters.put(key, request.getParameterValues(key));
            }
        } else {
            _log.debug("Gathering multipart parameters for unwrapped request");
        }

        return parameters;
    }
    
    static MultipartRequestHandler getCachedMultipartHandler( HttpServletRequest request )
    {
        HttpServletRequest req = ScopedServletUtils.getOuterRequest( request );
        return ( MultipartRequestHandler ) req.getAttribute( MultipartRequestUtils.PREHANDLED_MULTIPART_REQUEST_ATTR );    
    }
}
