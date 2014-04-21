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
package org.apache.beehive.netui.pageflow.internal;

import org.apache.beehive.netui.pageflow.handler.LoginHandler;
import org.apache.beehive.netui.pageflow.handler.BaseHandler;
import org.apache.beehive.netui.pageflow.handler.FlowControllerHandlerContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.security.auth.login.LoginException;
import java.io.Serializable;
import java.security.Principal;


/**
 * Implements default J2EE web-tier login handling.
 */ 
public class DefaultLoginHandler
        extends DefaultHandler
        implements LoginHandler
{
    public DefaultLoginHandler( ServletContext servletContext )
    {
        init( null, null, servletContext );
    }

    public void login( FlowControllerHandlerContext context, String username, String password )
        throws LoginException
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        assert context.getResponse() instanceof HttpServletResponse : "don't support ServletResponse currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
        AdapterManager.getServletContainerAdapter( getServletContext() ).login( username, password, request, response );
    }

    public void logout( FlowControllerHandlerContext context, boolean invalidateSessions )
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        assert context.getResponse() instanceof HttpServletResponse : "don't support ServletResponse currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        HttpServletResponse response = ( HttpServletResponse ) context.getResponse();
        AdapterManager.getServletContainerAdapter( getServletContext() ).logout( invalidateSessions, request, response );
    }

    public boolean isUserInRole( FlowControllerHandlerContext context, String roleName )
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        return request.isUserInRole( roleName );
    }

    public Principal getUserPrincipal( FlowControllerHandlerContext context )
    {
        assert context.getRequest() instanceof HttpServletRequest : "don't support ServletRequest currently.";
        HttpServletRequest request = ( HttpServletRequest ) context.getRequest();
        return request.getUserPrincipal();
    }
}
