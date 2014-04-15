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

package org.apache.beehive.netui.tools.testrecorder.shared.config;

import org.apache.beehive.netui.tools.testrecorder.shared.util.StringHelper;
import org.apache.beehive.netui.tools.testrecorder.shared.Logger;

import java.io.File;

/**
 * Client side object defining webapp access.
 * User: ozzy
 */
public class WebappDefinition {

    private static final Logger log = Logger.getInstance( WebappDefinition.class );

    private String name;
    private String description;
    // includes leading slash
    private String contextRoot;
    private String servletUri;

    public WebappDefinition( String name, String description, String contextRoot, String servletUri) {
        this.name = name;
        this.description = description;
        this.contextRoot = contextRoot;
        this.servletUri = contextRoot + "/" + servletUri;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public String getServletUri() {
        return servletUri;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );
        sb.append( "name( " + getName() + " )" );
        sb.append( ", description( " + getDescription() + " )" );
        sb.append( ", contextRoot( " + getContextRoot() + " )" );
        sb.append( ", servletURI( " + getServletUri() + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
