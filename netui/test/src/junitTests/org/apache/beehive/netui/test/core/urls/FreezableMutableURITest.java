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
package org.apache.beehive.netui.test.core.urls;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.core.urls.FreezableMutableURI;
import org.apache.beehive.netui.core.urls.MutableURI;
import org.apache.beehive.netui.core.urls.URIContext;

/**
 * FreezableMutableURI JUnit TestCase.
 */
public class FreezableMutableURITest extends TestCase
{
    //
    // Strings for tests. Elements are...
    // scheme, user info, host, port, path, query, fragment, 
    // and full uri
    //
    private String[][] _tests =
    {
        // test for relative path + query
        { null, null, null, null,
          "../test/start.jsp", "skip=true", null,
          "../test/start.jsp?skip=true" },

        // test for path + query
        { null, null, null, null,
          "/portal/MockPortal.jsp",
          "smokeTestAaltAction=goNested&amp;smokeTestA_submit=true", null,
          "/portal/MockPortal.jsp?smokeTestAaltAction=goNested&amp;smokeTestA_submit=true" },

        // test for scheme + host + path
        { "https", null, "localhost", null,
          "/tomcat-docs/jasper/docs/api/index.html", null, null,
          "https://localhost/tomcat-docs/jasper/docs/api/index.html" },

        // test for scheme + host + path + fragment
        { "http", null, "localhost", "8080", "/",
          null, "myFragment", "http://localhost:8080/#myFragment" },

        // test for query parameter names without values and multiple
        // instances of the same parameter name
        { "http", null, "localhost", "8080",
          "/test-servlet/TestServlet", "param1&amp;param1=&amp;param2", null,
          "http://localhost:8080/test-servlet/TestServlet?param1&amp;param1=&amp;param2" }
    };

    public FreezableMutableURITest( String name )
    {
        super( name );
    }

    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite( FreezableMutableURITest.class );
    }

    protected void setUp()
    {
    }

    protected void tearDown()
    {
    }

    public void testConstructors()
    {
        URIContext uriContext = MutableURI.getDefaultContext();
        String uriString = null;

        for ( int i = 0; i < _tests.length; i++ )
        {
            String scheme = _tests[i][0];
            String userInfo = _tests[i][1];
            String host = _tests[i][2];
            int port = FreezableMutableURI.UNDEFINED_PORT;
            String integer = _tests[i][3];
            if ( integer != null && integer.trim().length() > 0 )
            {
                port = Integer.parseInt( integer );
            }
            String path = _tests[i][4];
            String query = _tests[i][5];
            String fragment = _tests[i][6];
            uriString = _tests[i][7];

            try
            {
                FreezableMutableURI uri = new FreezableMutableURI( scheme, userInfo, host, port, path, query, fragment );
                assertEquals( uriString, uri.getURIString( uriContext ) );
                FreezableMutableURI other = new FreezableMutableURI( uriString, false );
                assertEquals( uri, other );
                other = new FreezableMutableURI( new URI( uriString ) );
                assertEquals( uri, other );
            }
            catch ( URISyntaxException e )
            {
                fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
            }
        }

        // opaque URI
        try
        {
            uriString = "news:comp.lang.java";
            FreezableMutableURI opaqueURI = new FreezableMutableURI( uriString, true );
            assertEquals( uriString, opaqueURI.getURIString( uriContext ) );
            assertTrue( opaqueURI.isAbsolute() );
            assertTrue( opaqueURI.isOpaque() );
        }
        catch ( URISyntaxException e )
        {
            fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
        }
    }

    public void testSetters()
    {
        URIContext uriContext = MutableURI.getDefaultContext();

        for ( int i = 0; i < _tests.length; i++ )
        {
            String scheme = _tests[i][0];
            String userInfo = _tests[i][1];
            String host = _tests[i][2];
            int port = FreezableMutableURI.UNDEFINED_PORT;
            String integer = _tests[i][3];
            if ( integer != null && integer.trim().length() > 0 )
            {
                port = Integer.parseInt( integer );
            }
            String path = _tests[i][4];
            String query = _tests[i][5];
            String fragment = _tests[i][6];
            String uriString = _tests[i][7];

            try
            {
                FreezableMutableURI uri = new FreezableMutableURI();
                uri.setScheme( scheme );
                uri.setHost( host );
                uri.setUserInfo( userInfo );
                uri.setPort( port );
                uri.setPath( path );
                uri.setQuery( query );
                uri.setFragment( fragment );
                assertEquals( uriString, uri.getURIString( uriContext ) );

                FreezableMutableURI other = new FreezableMutableURI( uriString, false );
                assertEquals( uri, other );
            }
            catch ( URISyntaxException e )
            {
                fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
            }
        }
    }

    public void testSettersWhenFrozen()
    {
        String uriString = "http://localhost:8080/test-servlet/TestServlet?param1&param2";
        FreezableMutableURI uri = null;
        try
        {
            uri = new FreezableMutableURI( uriString, true );
        }
        catch ( URISyntaxException e )
        {
            fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
        }

        URIContext uriContext = new URIContext();
        uriContext.setUseAmpEntity( false );
        assertEquals( uriString, uri.getURIString( uriContext ) );
        assertFalse( uri.isFrozen() );
        uri.setFrozen( true );
        assertTrue( uri.isFrozen() );

        boolean threw = false;
        try
        {
            uri.setScheme( "https" );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.setHost( "localhost" );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.setUserInfo( "userInfo" );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.setPort( 8443 );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.setPath( "/servlets/TestServlet" );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.setQuery( "foo=bar" );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.setFragment( "fragment" );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.addParameter( "name", "value", true );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.removeParameter( "param1" );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            HashMap map = new HashMap();
            uri.addParameters( map, true );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );

        threw = false;
        try
        {
            uri.setOpaque( "news", "comp.lang.java" );
        }
        catch ( IllegalStateException e )
        {
            threw = true;
        }
        assertTrue( threw );
    }

    public void testGettersWhenFrozen()
    {
        URIContext uriContext = MutableURI.getDefaultContext();
        String uriString = null;

        for ( int i = 0; i < _tests.length; i++ )
        {
            String scheme = _tests[i][0];
            String userInfo = _tests[i][1];
            String host = _tests[i][2];
            int port = FreezableMutableURI.UNDEFINED_PORT;
            String integer = _tests[i][3];
            if ( integer != null && integer.trim().length() > 0 )
            {
                port = Integer.parseInt( integer );
            }
            String path = _tests[i][4];
            String query = _tests[i][5];
            String fragment = _tests[i][6];
            uriString = _tests[i][7];

            try
            {
                FreezableMutableURI uri = new FreezableMutableURI( uriString, false );
                assertFalse( uri.isFrozen() );
                uri.setFrozen( true );
                assertTrue( uri.isFrozen() );
                assertEquals( uriString, uri.getURIString( uriContext ) );
                assertEquals( uri.getScheme(), scheme );
                assertEquals( uri.getHost(), host );
                assertEquals( uri.getUserInfo(), userInfo );
                assertEquals( uri.getPort(), port );
                assertEquals( uri.getPath(), path );
                assertEquals( uri.getQuery( uriContext ), query );
                assertEquals( uri.getFragment(), fragment );
            }
            catch ( URISyntaxException e )
            {
                fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
            }
        }

        // opaque URI
        try
        {
            String scheme = "news";
            String schemeSpecificPart = "comp.lang.java";
            uriString = scheme + ":" + schemeSpecificPart;
            FreezableMutableURI opaqueURI = new FreezableMutableURI( uriString, true );
            opaqueURI.setFrozen( true );
            assertTrue( opaqueURI.isFrozen() );
            assertEquals( uriString, opaqueURI.getURIString( uriContext ) );
            assertEquals( opaqueURI.getScheme(), scheme );
            assertEquals( opaqueURI.getSchemeSpecificPart(), schemeSpecificPart );
        }
        catch ( URISyntaxException e )
        {
            fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
        }
    }

    public void testgetURIStringForXML()
    {
        FreezableMutableURI uri = new FreezableMutableURI();
        uri.setScheme( "https" );
        uri.setHost( "localhost" );
        uri.setPort( 443 );
        uri.setPath( "/test" );
        uri.setQuery( "param1&param2&param3=&param3=true&param4=true" );
        String xmlString = "https://localhost:443/test?param1&amp;param2&amp;param3=&amp;param3=true&amp;param4=true";
        URIContext uriContext = new URIContext();
        uriContext.setUseAmpEntity( true );
        assertEquals( uri.getURIString( uriContext ), xmlString );
    }

    public void testEquals()
    {
        String queryString = "param1&param1=&param1=true&param2&param3=true";
        String uriString = "https://localhost:443/test?" + queryString;

        try
        {
            FreezableMutableURI uriA = new FreezableMutableURI();
            uriA.setScheme( "https" );
            uriA.setHost( "localhost" );
            uriA.setPort( 443 );
            uriA.setPath( "/test" );
            uriA.setQuery(queryString);

            FreezableMutableURI uriB = new FreezableMutableURI( uriString, true );
            URIContext uriContext = new URIContext();
            uriContext.setUseAmpEntity( false );
            assertEquals( uriA.getURIString( uriContext ), uriString );
            assertEquals( uriA.getURIString( uriContext ), uriB.getURIString( uriContext ) );

            FreezableMutableURI uriC = new FreezableMutableURI( new URI( uriString ) );

            // Test all properties of equality...
            // 1. hashCodes are equal
            assertEquals( uriA.hashCode(), uriB.hashCode() );

            // 2. reflexive
            assertTrue( uriA.equals( uriA ) );

            // 3. symmetric
            assertTrue( uriA.equals( uriB ) );
            assertTrue( uriB.equals( uriA ) );

            // 4. transitive
            assertTrue( uriA.equals( uriB ) );
            assertTrue( uriB.equals( uriC ) );
            assertTrue( uriC.equals( uriA ) );

            // 5. consistent
            uriC.setPath( "/differentPath" );
            assertFalse( uriC.equals( uriA ) );

            // 6. x.equals(null) should return false.
            assertFalse( uriA.equals( null ) );

            // and subclasses return false as not to break the symmetric property
            class AnotherMutableURI extends FreezableMutableURI
            {
                boolean reference = true;

                AnotherMutableURI( URI uri )
                {
                    super( uri );
                }

                void setReference( boolean ref )
                {
                    reference = ref;
                }

                boolean isReference()
                {
                    return reference;
                }
            }

            AnotherMutableURI another = new AnotherMutableURI( new URI( uriString ) );
            assertFalse( uriA.equals( another ) );

            // and superclasses return false as not to break the symmetric property
            MutableURI mutableURI = new MutableURI( new URI( uriString ) );
            assertFalse( uriA.equals( mutableURI ) );

            // test the frozen attribute
            assertTrue( uriA.equals( uriB ) );
            uriA.setFrozen( true );
            assertFalse( uriA.equals( uriB ) );
        }
        catch ( URISyntaxException e )
        {
            fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
        }
    }

    // basic diagnostic utility when writing tests
    private void dumpURI( FreezableMutableURI uri )
    {
        if ( uri == null )
        {
            System.out.println( "uri == null" );
        }
        else
        {
            System.out.println( "uri:        " + uri.getURIString( null ) );
            System.out.println( "scheme:     " + uri.getScheme() );
            System.out.println( "user info:  " + uri.getUserInfo() );
            System.out.println( "host:       " + uri.getHost() );
            System.out.println( "port:       " + uri.getPort() );
            System.out.println( "path:       " + uri.getPath() );
            System.out.println( "query:      " + uri.getQuery( null ) );
            System.out.println( "fragment:   " + uri.getFragment() );
            System.out.println( "encoding:   " + uri.getEncoding() );
        }
    }
}
