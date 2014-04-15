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

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * User: ozzy
 * Date: Apr 9, 2004
 * Time: 2:36:12 PM
 */
public class TestDefinitions {

    private List testDefinitions;
    private Categories categories;
    private Webapps webapps;
    // test name -> TestDefinition
    private Map testsByNameMap;
    // webapp name -> List of TestDefinition objects
    private Map testsByWebappMap;

    public TestDefinitions( List testDefinitions, Categories categories, Webapps webapps ) {
        this.testDefinitions = testDefinitions;
        this.categories = categories;
        this.webapps = webapps;
        testsByNameMap = new HashMap();
        testsByWebappMap = new HashMap();
        init();
    }

    private void init() {
        TestDefinition testDefinition = null;
        for ( int i = 0; i < testDefinitions.size(); i++ ) {
            testDefinition = (TestDefinition) testDefinitions.get(i);
            addToMap( testDefinition );
        }
    }

    private void addToMap( TestDefinition test ) {
        getTestsByNameMap().put( test.getName(), test );
        List list = (List) getTestsByWebappMap().get( test.getWebapp().getName() );
        if ( list == null ) {
            list = new ArrayList();
            getTestsByWebappMap().put( test.getWebapp().getName(), list );
        }
        list.add( test );
    }

    public void add( TestDefinition test ) {
        WebappConfig webapp = test.getWebapp();
        WebappConfig temp = webapps.getWebapp( webapp.getName() );
        if ( temp == null || temp != webapp ) {
            throw new RuntimeConfigException( "ERROR: webapp does not exist in this set of definitions, webapp( " +
                    webapp + " ), found match( " + temp + " )");
        }
        testDefinitions.add( test );
        addToMap( test );
    }

    public List getTestDefinitions() {
        return Collections.unmodifiableList( testDefinitions );
    }

    public Categories getCategories() {
        return categories;
    }

    public Webapps getWebapps() {
        return webapps;
    }

    public TestDefinition getTest( String name ) {
        return (TestDefinition) getTestsByNameMap().get( name );
    }

    public List getTestList( String webappName ) {
        return (List) getTestsByWebappMap().get( webappName );
    }

    private Map getTestsByWebappMap() {
        return testsByWebappMap;
    }

    private Map getTestsByNameMap() {
        return testsByNameMap;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 64 );
        sb.append( "[ " );
        sb.append( "tests( " + StringHelper.toString( getTestDefinitions(), "\n", "\n" ) + " )" );
        sb.append( ",\ncategories( " + categories + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
