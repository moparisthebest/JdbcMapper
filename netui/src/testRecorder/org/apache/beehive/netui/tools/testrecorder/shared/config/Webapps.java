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

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 * User: ozzy
 * Date: Apr 12, 2004
 * Time: 5:35:43 PM
 */
public class Webapps {

    // List of WebappConfig objects
    private List webappList;
    // name -> WebappConfig
    private Map webappNameMap;

    public Webapps( List webappList ) {
        this.webappList = webappList;
        webappNameMap = new HashMap();
        init();
    }

    private void init() {
        WebappConfig webapp = null;
        for ( int i = 0; i < webappList.size(); i++ ) {
            webapp = (WebappConfig) webappList.get( i );
            webappNameMap.put( webapp.getName(), webapp );
        }
    }

    public List getWebappList() {
        return Collections.unmodifiableList( webappList );
    }

    private Map getWebappNameMap() {
        return webappNameMap;
    }

    public WebappConfig getWebapp( String name ) {
        return (WebappConfig) getWebappNameMap().get( name );
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );
        sb.append( ", webappList( " + StringHelper.toString( getWebappList(), "\n", "\n\t" ) + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
