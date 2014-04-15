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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base request/response context.
 */ 
public class RequestContext
{
    private ServletRequest _request;
    private ServletResponse _response;

    public RequestContext( ServletRequest request, ServletResponse response )
    {
        _request = request;
        _response = response;
    }

    public ServletRequest getRequest()
    {
        return _request;
    }

    public ServletResponse getResponse()
    {
        return _response;
    }
    
    HttpServletRequest getHttpRequest()
    {
        assert _request instanceof HttpServletRequest : "HttpServletRequest is currently required";
        return ( HttpServletRequest ) _request;
    }
    
    HttpServletResponse getHttpResponse()
    {
        assert _response instanceof HttpServletResponse : "HttpServletResponse is currently required";
        return ( HttpServletResponse ) _response;
    }
}
