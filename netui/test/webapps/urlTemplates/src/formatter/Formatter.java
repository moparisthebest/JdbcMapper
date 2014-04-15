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

import org.apache.beehive.netui.core.urls.MutableURI;
import org.apache.beehive.netui.core.urls.TemplatedURLFormatter;
import org.apache.beehive.netui.core.urls.URIContext;
import org.apache.beehive.netui.core.urltemplates.URLTemplate;
import org.apache.beehive.netui.core.urltemplates.URLTemplatesFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;


/**
 * Test implementation of TemplatedURLFormatter for formatting URLs
 * based on templates from a URL template config file.
 */
public class Formatter extends TemplatedURLFormatter
{
    /**
     * Format the given URL using a URL template, if defined in a URL
     * template config file.
     *
     * @param servletContext the current ServletContext.
     * @param request the current ServletRequest.
     * @param uri the MutableURI to be formatted into a String.
     * @param key key for the URL template type to use for formatting the URI
     * @param uriContext data required to write out the string form.
     * @return the URL as a <code>String</code>
     */
    public String getTemplatedURL( ServletContext servletContext,
                                   ServletRequest request, MutableURI uri,
                                   String key, URIContext uriContext ) {
        // Look for the template config and get the right template.
        // If it is found, apply the value to the template.
        String result = null;
        URLTemplate template = null;
        URLTemplatesFactory factory = URLTemplatesFactory.getURLTemplatesFactory( servletContext );

        if ( factory != null ) {
            String templateName = factory.getTemplateNameByRef( DEFAULT_TEMPLATE_REF, key );

            if ( templateName != null ) {
                template = factory.getURLTemplate( templateName );
            }
        }

        if ( template != null ) {
            result = formatURIWithTemplate( request, uri, uriContext, template, key );
        } else {
            // no template found, just return the uri as a String...
            result = uri.getURIString( uriContext );
        }

        return result;
    }

    private String formatURIWithTemplate( ServletRequest request, MutableURI uri,
                                          URIContext uriContext, URLTemplate template,
                                          String key ) {
        String scheme = uri.getScheme();
        String host = uri.getHost();
        int port = uri.getPort();
        String path = uri.getPath();

        if ( scheme == null || scheme.length() == 0 ) { scheme = request.getScheme(); }

        if ( host == null || host.length() == 0 ) { host = request.getServerName(); }

        if ( port < 0 ) { port = request.getServerPort(); }

        // for this test, override host and path for actions...
        if ( key.equals( URLTemplatesFactory.ACTION_TEMPLATE )
                || key.equals( URLTemplatesFactory.SECURE_ACTION_TEMPLATE ) ) {
            host = "beehive.apache.org";
            path = "/prefix" + path;
        }

        template.substitute( TemplatedURLFormatter.SCHEME_TOKEN, scheme );
        template.substitute( TemplatedURLFormatter.DOMAIN_TOKEN, host );
        template.substitute( TemplatedURLFormatter.PORT_TOKEN, port );
        template.substitute( TemplatedURLFormatter.PATH_TOKEN, path );

        String query = null;
        query = uri.getQuery( uriContext );

        if ( query == null ) {
            query = "";
        }

        template.substitute( TemplatedURLFormatter.QUERY_STRING_TOKEN, query );

        return template.format();
    }
}
