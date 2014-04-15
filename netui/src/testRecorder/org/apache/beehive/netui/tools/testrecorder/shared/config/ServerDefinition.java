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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.beehive.netui.tools.testrecorder.shared.util.StringHelper;

/**
 * Client side object that defines server access info.
 */
public class ServerDefinition {

    private String name;
    private String hostname;
    private int port;
    private List webappList;
    private Map webappMap;
    private Map testDefMap;
    private List testDefList;

    public ServerDefinition( String name, String hostname, int port ) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
        webappList = new ArrayList();
        webappMap = new HashMap();
        testDefMap = new HashMap();
        testDefList = new ArrayList();
    }

    public void addWebapp( WebappDefinition webapp ) {
        webappList.add( webapp );
        webappMap.put( webapp.getName(), webapp );
    }

    public WebappDefinition getWebapp( String webappName ) {
        return (WebappDefinition) webappMap.get( webappName );
    }

    public WebappDefinition[] getWebapps() {
        return (WebappDefinition[]) webappList.toArray( new WebappDefinition[webappList.size()] );
    }

    public int getWebappCount() {
        return webappList.size();
    }

    public void addTestDefinitions( WebappDefinition webapp, TestDefinitions tests ) {
        testDefMap.put( webapp, tests );
        testDefList.add( tests );
    }

    public TestDefinitions getTestDefinitions( String webappName ) throws ConfigException {
        WebappDefinition webapp = getWebapp( webappName );
        if ( webapp == null ) {
            throw new ConfigException( "No webapp exists for webapp with name( " + webappName );
        }
        return getTestDefinitions( webapp );
    }

    public TestDefinitions getTestDefinitions( WebappDefinition webapp ) {
        return (TestDefinitions) testDefMap.get( webapp );
    }

    public TestDefinitions[] getTestDefinitions() {
        return (TestDefinitions[]) testDefList.toArray( new TestDefinitions[testDefList.size()] );
    }

    public int getTestDefinitionsCount() {
        return testDefList.size();
    }

    public String getName() {
        return name;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 48 );
        sb.append( "[ " );
        sb.append( "name( " + getName() + " )" );
        sb.append( ", hostname( " + getHostname() + " )" );
        sb.append( ", port( " + getPort() + " )" );
        sb.append( ", webappList( " + StringHelper.toString( webappList, "\n", "\n\t" ) + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
