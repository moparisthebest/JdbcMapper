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
package formatter;

import org.apache.beehive.netui.core.urls.TemplatedURLFormatter;
import org.apache.beehive.netui.core.urls.URLRewriterService;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

// Servlet Filter used with the formatter test
public class FormatterFilter implements Filter {

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
                         throws IOException, ServletException {

        // create and register a custom TemplatedURLFormatter
        TemplatedURLFormatter formatter = new Formatter();
        URLRewriterService.registerTemplatedURLFormatter( request, formatter );

        // pass the request/response on
        chain.doFilter(request, response);

        // unregister the custom TemplatedURLFormatter
        URLRewriterService.unregisterTemplatedURLFormatter( request );
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }
}
