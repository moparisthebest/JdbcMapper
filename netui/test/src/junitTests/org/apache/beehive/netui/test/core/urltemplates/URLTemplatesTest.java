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
package org.apache.beehive.netui.test.core.urltemplates;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.core.urltemplates.URLTemplate;
import org.apache.beehive.netui.core.urltemplates.URLTemplateDescriptor;
import org.apache.beehive.netui.core.urltemplates.URLTemplates;

/**
 * URLTemplates JUnit TestCase.
 */
public class URLTemplatesTest extends TestCase
{
    // Strings to test templates...
    private static String[][] _templateStrings =
    {
        { "default", "{url:scheme}://{url:domain}:{url:port}/{url:path}?{url:queryString}{url:currentPage}" },
        { "jpf-action", "http://{url:domain}:{url:port}/{url:path}?jpf=action&amp;{url:queryString}" },
        { "jpf-secure-action", "https://{url:domain}:{url:port}/{url:path}?jpf=action&amp;{url:queryString}" },
        { "jpf-resource", "http://{url:domain}:{url:port}/{url:path}?jpf=resource&amp;{url:queryString}" },
        { "jpf-secure-resource", "https://{url:domain}:{url:port}/{url:path}?jpf=resource&amp;{url:queryString}" },
        { "jpf-resource", "http://{url:domain}:{url:port}/{url:path}?jpf=resource&amp;{url:queryString}" },
        { "jpf-secure-resource", "https://{url:domain}:{url:port}/{url:path}?jpf=resource&amp;{url:queryString}" },
        { "wsrp-action", "http://{url:domain}:{url:port}/{url:path}?wsrp=action&amp;{url:queryString}" },
        { "wsrp-secure-action", "https://{url:domain}:{url:port}/{url:path}?wsrp=action&amp;{url:queryString}" },
        { "wsrp-resource", "http://{url:domain}:{url:port}/{url:path}?wsrp=resource&amp;{url:queryString}" },
        { "wsrp-secure-resource", "https://{url:domain}:{url:port}/{url:path}?wsrp=resource&amp;{url:queryString}" },
        { "wsrp-resource", "http://{url:domain}:{url:port}/{url:path}?wsrp=resource&amp;{url:queryString}" },
        { "wsrp-secure-resource", "https://{url:domain}:{url:port}/{url:path}?wsrp=resource&amp;{url:queryString}" },
        { "wsrp-render", "http://{url:domain}:{url:port}/{url:path}?wsrp=render&amp;{url:queryString}" },
        { "wsrp-secure-render", "https://{url:domain}:{url:port}/{url:path}?wsrp=render&amp;{url:queryString}" },
        { "extraVarTemplate", "{url:scheme}://{url:domain}:{url:port}/{url:path}?{url:queryString}{url:currentPage}&amp;{foo:bar}" }
    };

    // Strings to test template reference groups...
    // refGroupName, then template names for action, secure-action, resource. and secure-resource key's
    private static String[][] _refGroups =
    {
        { "default-url-templates", "jpf-action", "jpf-secure-action", "jpf-resource", "jpf-secure-resource" },
        { "wsrp-url-templates", "wsrp-action", "wsrp-secure-action", "wsrp-resource", "wsrp-secure-resource" }
    };

    private static final List/*<String>*/ KNOWN_TEMPLATE_TOKENS =
            Arrays.asList( new String[]{ URLTemplateDescriptor.SCHEME_TOKEN, URLTemplateDescriptor.DOMAIN_TOKEN,
                    URLTemplateDescriptor.PORT_TOKEN } );

    private static final List/*<String>*/ REQUIRED_TEMPLATE_TOKENS =
            Arrays.asList( new String[]{ URLTemplateDescriptor.PATH_TOKEN, URLTemplateDescriptor.QUERY_STRING_TOKEN } );

    private URLTemplates _urlTemplates = new URLTemplates();

    public URLTemplatesTest( String name )
    {
        super( name );
    }

    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite( URLTemplatesTest.class );
    }

    protected void setUp()
    {
        for (int i = 0; i < _templateStrings.length; i++)
        {
            String name = _templateStrings[i][0];
            String temp = _templateStrings[i][1];
            URLTemplate urlTemplate = new URLTemplate( temp );
            urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
            _urlTemplates.addTemplate( name, urlTemplate );
        }
    }

    protected void tearDown()
    {
    }

    public void testAddAndGetTemplate()
    {
        String name = "test-template";
        String temp = "{url:scheme}://{url:domain}:{url:port}/{url:path}?{url:queryString}";
        URLTemplate urlTemplate = new URLTemplate( temp );
        urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
        _urlTemplates.addTemplate( name, urlTemplate );

        // Note that getTemplate() returns a copy so a test on should fail.
        URLTemplate returnedTemplate = _urlTemplates.getTemplate( name );
        assertFalse( urlTemplate == returnedTemplate );
        assertEquals( temp, returnedTemplate.getTemplate() );

        for (int i = 0; i < _templateStrings.length; i++)
        {
            name = _templateStrings[i][0];
            temp = _templateStrings[i][1];
            urlTemplate = _urlTemplates.getTemplate( name );
            assertTrue( urlTemplate != null );
            assertEquals( temp, urlTemplate.getTemplate() );
        }
    }

    public void testAddBadTemplate()
    {
        String name = "test-bad-template";
        String temp = "{url:scheme}://{url:domain}:{url:port}/{url:path}?{url:queryString}";
        URLTemplate urlTemplate = new URLTemplate( temp );
        urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );

        boolean threw = false;
        try
        {
            _urlTemplates.addTemplate( "", urlTemplate );
        }
        catch ( IllegalArgumentException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            _urlTemplates.addTemplate( name, null );
        }
        catch ( IllegalArgumentException e )
        {
            threw = true;
        }
        assertTrue( threw );
    }

    public void testAddAndGetTemplateRefGroup()
    {
        for (int i = 0; i < _refGroups.length; i++)
        {
            String name = _refGroups[i][0];
            HashMap/*< String, String >*/ refGroup = new HashMap/*< String, String >*/();
            refGroup.put( URLTemplateDescriptor.ACTION_TEMPLATE, _refGroups[i][1] );
            refGroup.put( URLTemplateDescriptor.SECURE_ACTION_TEMPLATE, _refGroups[i][2] );
            refGroup.put( URLTemplateDescriptor.RESOURCE_TEMPLATE, _refGroups[i][3] );
            refGroup.put( URLTemplateDescriptor.SECURE_RESOURCE_TEMPLATE, _refGroups[i][4] );
            _urlTemplates.addTemplateRefGroup( name, refGroup );
        }

        for (int i = 0; i < _refGroups.length; i++)
        {
            String group = _refGroups[i][0];
            String key = URLTemplateDescriptor.ACTION_TEMPLATE;
            String name = _urlTemplates.getTemplateNameByRef( group, key );
            assertEquals( _refGroups[i][1], name );
            URLTemplate urlTemplate = _urlTemplates.getTemplate( name );
            assertTrue( urlTemplate != null );

            key = URLTemplateDescriptor.SECURE_ACTION_TEMPLATE;
            name = _urlTemplates.getTemplateNameByRef( group, key );
            assertEquals( _refGroups[i][2], name );
            urlTemplate = _urlTemplates.getTemplate( name );
            assertTrue( urlTemplate != null );

            key = URLTemplateDescriptor.RESOURCE_TEMPLATE;
            name = _urlTemplates.getTemplateNameByRef( group, key );
            assertEquals( _refGroups[i][3], name );
            urlTemplate = _urlTemplates.getTemplate( name );
            assertTrue( urlTemplate != null );

            key = URLTemplateDescriptor.SECURE_RESOURCE_TEMPLATE;
            name = _urlTemplates.getTemplateNameByRef( group, key );
            assertEquals( _refGroups[i][4], name );
            urlTemplate = _urlTemplates.getTemplate( name );
            assertTrue( urlTemplate != null );
        }

    }

    public void testAddBadTemplateRefGroup()
    {
        HashMap/*< String, String >*/ refGroup = new HashMap/*< String, String >*/();

        // null ref group
        boolean threw = false;
        try
        {
            _urlTemplates.addTemplateRefGroup( "refGroupName", null );
        }
        catch ( IllegalArgumentException e )
        {
            threw = true;
        }
        assertTrue( threw );

        // empty ref group
        threw = false;
        try
        {
            _urlTemplates.addTemplateRefGroup( "refGroupName", refGroup );
        }
        catch ( IllegalArgumentException e )
        {
            threw = true;
        }
        assertTrue( threw );

        String name = _refGroups[0][0];
        refGroup.put( URLTemplateDescriptor.ACTION_TEMPLATE, _refGroups[0][1] );
        refGroup.put( URLTemplateDescriptor.SECURE_ACTION_TEMPLATE, _refGroups[0][2] );
        refGroup.put( URLTemplateDescriptor.RESOURCE_TEMPLATE, _refGroups[0][3] );
        refGroup.put( URLTemplateDescriptor.SECURE_RESOURCE_TEMPLATE, _refGroups[0][4] );

        threw = false;
        try
        {
            _urlTemplates.addTemplateRefGroup( null, refGroup );
        }
        catch ( IllegalArgumentException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            _urlTemplates.addTemplateRefGroup( "", refGroup );
        }
        catch ( IllegalArgumentException e )
        {
            threw = true;
        }
        assertTrue( threw );
    }
}
