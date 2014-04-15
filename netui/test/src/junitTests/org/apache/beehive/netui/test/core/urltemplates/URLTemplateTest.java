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

/**
 * URLTemplate JUnit TestCase.
 */
public class URLTemplateTest extends TestCase
{

    private static final List/*<String>*/ KNOWN_TEMPLATE_TOKENS =
            Arrays.asList( new String[]{ URLTemplateDescriptor.SCHEME_TOKEN, URLTemplateDescriptor.DOMAIN_TOKEN,
                    URLTemplateDescriptor.PORT_TOKEN } );

    private static final List/*<String>*/ REQUIRED_TEMPLATE_TOKENS =
            Arrays.asList( new String[]{ URLTemplateDescriptor.PATH_TOKEN, URLTemplateDescriptor.QUERY_STRING_TOKEN } );

    public URLTemplateTest( String name )
    {
        super( name );
    }

    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite( URLTemplateTest.class );
    }

    protected void setUp()
    {
    }

    protected void tearDown()
    {
    }

    public void testVerifyTemplate()
    {
        String temp = "{url:scheme}://{url:domain}:{url:port}/{url:path}?{url:queryString}";
        URLTemplate urlTemplate = new URLTemplate( temp );
        boolean valid = urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
        assertEquals( temp, urlTemplate.getTemplate() );
        assertTrue( valid );
    }

    public void testVerifyBadTemplate()
    {
        // badly formatted known token, {uri:domain}
        String temp = "{url:scheme}://url:domain}:{url:port}/{url:path}?{url:queryString}";
        URLTemplate urlTemplate = new URLTemplate( temp );
        boolean valid = urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
        assertFalse( valid );

        // missing required token
        temp = "{url:scheme}://{url:domain}:{url:port}/{url:path}";
        urlTemplate = new URLTemplate( temp );
        valid = urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
        assertFalse( valid );

        // another missing required token
        temp = "{url:scheme}://{url:domain}:{url:port}/?{url:queryString}";
        urlTemplate = new URLTemplate( temp );
        valid = urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
        assertFalse( valid );
    }

    public void testSubstitute()
    {
        String temp = "{url:scheme}://{url:domain}:{url:port}/{url:path}?{url:queryString}{url:currentPage}";
        String intermediateResult = "https://myhost.com:8443/my/path?param1=true&amp;foo";
        String intermediateResultWithToken = "https://myhost.com:8443/my/path?param1=true&amp;foo{url:currentPage}";
        String result = "https://myhost.com:8443/my/path?param1=true&amp;foo&cur=arb";
        URLTemplate urlTemplate = new URLTemplate( temp );
        urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
        urlTemplate.substitute( "{url:domain}", "myhost.com" );
        urlTemplate.substitute( "{url:port}", 8443 );
        HashMap/*< String, String >*/ tokensAndValues = new HashMap/*< String, String >*/();
        tokensAndValues.put( "{url:scheme}", "https" );
        tokensAndValues.put( "{url:path}", "/my/path" );
        tokensAndValues.put( "{url:queryString}", "param1=true&amp;foo" );
        urlTemplate.substitute( tokensAndValues );
        assertEquals( intermediateResult, urlTemplate.format() );
        assertEquals( intermediateResultWithToken, urlTemplate.format( false ) );
        urlTemplate.substitute( "{url:currentPage}", "&cur=arb" );
        assertEquals( result, urlTemplate.format() );
    }

    public void testArbitraryTokenTemplate()
    {
        String temp = "{scheme}://{my-domain}:{secureport}/{pre}/{url:path}?{url:queryString}"
                + "&token_1={token_1}{token-2}{token:3}";
        String result = "https://myhost.com:443/abc/my/path?param1=true&amp;foo"
                + "&token_1=xyz%20123";
        URLTemplate urlTemplate = new URLTemplate( temp );
        urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
        assertEquals( temp, urlTemplate.getTemplate() );
        HashMap/*< String, String >*/ tokensAndValues = new HashMap/*< String, String >*/();
        tokensAndValues.put( "{scheme}", "https" );
        tokensAndValues.put( "{my-domain}", "myhost.com" );
        tokensAndValues.put( "{secureport}", "443" );
        tokensAndValues.put( "{pre}", "abc" );
        tokensAndValues.put( "{url:path}", "/my/path" );
        tokensAndValues.put( "{url:queryString}", "param1=true&amp;foo" );
        tokensAndValues.put( "{token:3}", "123" );
        tokensAndValues.put( "{token-2}", "%20" );
        tokensAndValues.put( "{token_1}", "xyz" );
        urlTemplate.substitute( tokensAndValues );
        assertEquals( result, urlTemplate.format() );
    }

    public void testEmptyQuery()
    {
        String temp = "http://{url:domain}:80/{url:path}?{url:queryString}";
        String result = "http://myhost.com:80/my/path";
        URLTemplate urlTemplate = new URLTemplate( temp );
        urlTemplate.verify( KNOWN_TEMPLATE_TOKENS, REQUIRED_TEMPLATE_TOKENS );
        urlTemplate.substitute( "{url:domain}", "myhost.com" );
        urlTemplate.substitute( "{url:path}", "/my/path" );
        assertEquals( result, urlTemplate.format() );
        assertEquals( result, urlTemplate.format(true) );
        urlTemplate.substitute( "{url:queryString}", "" );
        assertEquals( result, urlTemplate.format() );
        assertEquals( result, urlTemplate.format(true) );
    }
}
