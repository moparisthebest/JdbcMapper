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

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * Offers methods for rewriting URLs/query parameters.
 */
public abstract class URLRewriter
{
    /**
     * Flag indicating that other rewriters are allowed to be used in the chain.
     * The URLRewriterService API will not allow more than one URLRewriter in
     * the chain with this property equal to false.
     */
    private boolean _allowOtherRewriters = true;

    public void setAllowOtherRewriters( boolean allowOtherRewriters )
    {
        _allowOtherRewriters = allowOtherRewriters;
    }

    public boolean allowOtherRewriters()
    {
        return _allowOtherRewriters;
    }

    /**
     * Get the prefix to use when rewriting a query parameter name.
     * Loops through the list of registered URLRewriters to build up a the prefix.
     *
     * @param servletContext the current ServletContext.
     * @param request the current ServletRequest.
     * @param name the name of the query parameter.
     * @return a prefix to use to rewrite a query parameter name.
     */
    public abstract String getNamePrefix( ServletContext servletContext, ServletRequest request, String name );

    /**
     * This method will return two bits of information that are used by components that want run through
     * the AJAX facilities.  The <code>AjaxUrlInfo</code> class is returned and specifies this information.
     * @param servletContext the current ServletContext.
     * @param request the current ServletRequest.
     * @param nameable this object that is the target of the Ajax request.  Typically it is an INameable.
     */
    public AjaxUrlInfo getAjaxUrl(ServletContext servletContext, ServletRequest request, Object nameable)
    {
        // @todo: this should actually be an abstract method in the next major version.
        // @todo: the nameable has to be an INameable, this requires taking a dependency here on the NameService
        // For back compat, this method is implemented but only throws a runtime exception
        throw new RuntimeException("Not Implemented");
    }

    /**
     * Rewrite the given URL.
     * 
     * @param servletContext the current ServletContext.
     * @param request the current ServletRequest.
     * @param response the current ServletResponse.
     * @param url the MutableURI to be rewritten.
     * @param type the type of URL to be rewritten.  This is one of the following values:
     *    <ul>
     *    <li><code>action</code>: a standard (non-resource) URL
     *    <li><code>resource</code>: a resource (e.g., image) URL
     *    </ul>
     * @param needsToBeSecure a flag indicating whether the URL should be secure (SSL required) or not
     */
    public abstract void rewriteURL( ServletContext servletContext, ServletRequest request,
                                     ServletResponse response, MutableURI url, URLType type,
                                     boolean needsToBeSecure );

    /**
     * Tell whether rewritten form actions should be allowed to have query parameters.  If this returns
     * <code>false</code>, then a form-tag implementation should render query parameters into hidden
     * fields on the form instead of allowing them to remain in the URL.
     */
    public boolean allowParamsOnFormAction( ServletContext servletContext, ServletRequest request )
    {
        return false;
    }
}
