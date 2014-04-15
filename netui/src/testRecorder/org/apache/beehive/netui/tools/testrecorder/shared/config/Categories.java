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


import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Iterator;
import java.util.Collections;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;
import org.apache.beehive.netui.tools.testrecorder.shared.util.StringHelper;

/**
 *
 */
public class Categories {

    private static final Logger LOGGER = Logger.getInstance( Categories.class );

    private Category[] _categories;
    // string -> Category
    private HashMap _categoryStringMap;
    // Category -> list of TestDefinition objects
    private HashMap _categoryTestMap;

    public Categories( Category[] categories ) {
        _categories = categories;
        _categoryStringMap = new HashMap();
        _categoryTestMap = new HashMap();
        init();
    }

    private void init() {
        for ( int i = 0; i < _categories.length; i++ ) {
            _categoryStringMap.put( _categories[i].getName(), _categories[i] );
            _categoryTestMap.put( _categories[i], new ArrayList() );
        }
    }

    public Category[] getCategories() {
        return _categories;
    }

    public Category getCategory( String categoryString ) {
        return (Category) getCategoryStringMap().get( categoryString );
    }

    private Map getCategoryStringMap() {
        return _categoryStringMap;
    }

    public List getTests( String category ) {
        assert category != null : "ERROR: category string may not be null";
        return getTests( getCategory( category ) );
    }

    /**
     *
     * @param category
     * @return a List of TestDefinition objects, returns null if no tests exist for the category
     */
    public List getTests( Category category ) {
        List list = (List) getCategoryTestMap().get( category );
        if ( list == null ) {
            return null;
        }
        return Collections.unmodifiableList( list);
    }

    private Map getCategoryTestMap() {
        return _categoryTestMap;
    }

    public void addTest( TestDefinition test ) throws ConfigException {
        List testCategories = test.getCategories();
        Category category = null;
        for ( int i = 0; i < testCategories.size(); i++ ) {
            category = (Category) testCategories.get( i );
            addTest( category, test );
        }
    }

    private void addTest( Category category, TestDefinition test ) throws ConfigException {
        List list = null;
        if ( !getCategoryTestMap().containsKey( category ) ) {
            ConfigException ce = new ConfigException( "ERROR: unable to find category( " + category + " )" );
            LOGGER.fatal( ce );
            throw ce;
        }
        list = (List) getCategoryTestMap().get( category );
        list.add( test );
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 64 );
        sb.append( "[ " );
        sb.append( "categories( \n" + StringHelper.toString( getCategories(), "\n\t", "\n\t" ) + " ), \n" );
        sb.append( "testMap( \n" + testMapToString() + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

    public String testMapToString() {
        Map map = getCategoryTestMap();
        Iterator it = map.keySet().iterator();
        StringBuffer sb = new StringBuffer( 16 * map.size() );
        Category category = null;
        List testList = null;
        sb.append( "[\n" );
        for ( int i = 0; it.hasNext(); i++ ) {
            category = (Category) it.next();
            testList = (List) map.get( category );
            if ( i != 0 ) {
                sb.append( "\n" );
            }
            sb.append( "\t[" + i + "] key(" + category.getName() + "), value( " +
                    TestDefinition.testListToString( testList ) + " )" );
        }
        sb.append( "\n]" );
        return sb.toString();
    }

}
