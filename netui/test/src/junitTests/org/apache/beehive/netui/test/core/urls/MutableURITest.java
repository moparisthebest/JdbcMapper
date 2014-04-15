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
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.core.urls.MutableURI;
import org.apache.beehive.netui.core.urls.URIContext;

/**
 * MutableURI JUnit TestCase.
 */
public class MutableURITest extends TestCase
{
    private static final String DEFAULT_ENCODING = "UTF-8";

    //
    // Strings for tests. Elements are...
    // scheme, user info, host, port, path, query, fragment,
    // and full uri
    //
    private String[][] _tests =
    {
        // test the ftp scheme + host + path
        { "ftp", null, "ftp.is.co.za", null,
          "/rfc/rfc1808.txt", null, null,
          "ftp://ftp.is.co.za/rfc/rfc1808.txt" },

        // include user info
        { "telnet", "user:pword", "bogus-machine.com", null,
          "", null, null,
          "telnet://user:pword@bogus-machine.com" },

        // test for IPv6 addresses
        { "http", null, "[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]", "80",
          "/index.html", null, null,
          "http://[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]:80/index.html" },

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

        // test for fragment only
        { null, null, null, null,
          "", null, "myFragment", "#myFragment" },

        // test for scheme + host + path + fragment
        { "http", null, "localhost", "8080", "/",
          null, "myFragment", "http://localhost:8080/#myFragment" },

        // test for empty path with fragment
        { "http", null, "localhost", "8080", "",
          null, "myFragment", "http://localhost:8080#myFragment" },

        // test for query parameter names without values and multiple
        // instances of the same parameter name
        { "http", null, "localhost", "8080",
          "/test-servlet/TestServlet", "param1&amp;param1=&amp;param2", null,
          "http://localhost:8080/test-servlet/TestServlet?param1&amp;param1=&amp;param2" },

        // test URI with a session ID
        { "http", null, "localhost", "8080",
          "/Web/d/newAction1.do;jsessionid=F0C07D10C0E8CD22618ED1178F0F62C8",
          null, null,
          "http://localhost:8080/Web/d/newAction1.do;jsessionid=F0C07D10C0E8CD22618ED1178F0F62C8" }
    };

    public void testConstructors()
    {
        URIContext uriContext = MutableURI.getDefaultContext();
        String uriString = null;

        for (int i = 0; i < _tests.length; i++ )
        {
            String scheme = _tests[i][0];
            String userInfo = _tests[i][1];
            String host = _tests[i][2];
            int port = MutableURI.UNDEFINED_PORT;
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
                MutableURI uri = new MutableURI( scheme, userInfo, host, port,
                                                 path, query, fragment );
                assertEquals( uriString, uri.getURIString( uriContext ) );
                MutableURI other = new MutableURI( uriString, false );
                assertEquals( uri, other );
                other = new MutableURI( new URI( uriString ) );
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
            MutableURI opaqueURI = new MutableURI( uriString, true );
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

        for (int i = 0; i < _tests.length; i++ )
        {
            String scheme = _tests[i][0];
            String userInfo = _tests[i][1];
            String host = _tests[i][2];
            int port = MutableURI.UNDEFINED_PORT;
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
                MutableURI uri = new MutableURI();
                uri.setScheme( scheme );
                uri.setHost( host );
                uri.setUserInfo( userInfo );
                uri.setPort( port );
                uri.setPath( path );
                uri.setQuery( query );
                uri.setFragment( fragment );
                assertEquals( uriString, uri.getURIString( uriContext ) );

                MutableURI other = new MutableURI( uriString, false );
                assertEquals( uri, other );
            }
            catch ( URISyntaxException e )
            {
                fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
            }
        }
    }

    public void testGetters()
    {
        URIContext uriContext = MutableURI.getDefaultContext();

        for (int i = 0; i < _tests.length; i++ )
        {
            String scheme = _tests[i][0];
            String userInfo = _tests[i][1];
            String host = _tests[i][2];
            int port = MutableURI.UNDEFINED_PORT;
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
                MutableURI uri = new MutableURI( uriString, false );
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
    }

    public void testIsAbsolute()
    {
        try
        {
            MutableURI uri = new MutableURI( "http://localhost/test?param1=true", true );
            assertTrue( uri.isAbsolute() );
            uri = new MutableURI( "/test?param1=true", true );
            assertFalse( uri.isAbsolute() );
        }
        catch ( URISyntaxException e )
        {
            fail( "Test failed with a URISyntaxException: " + e.getMessage() );
        }
    }

    public void testQueryParameters()
    {
        MutableURI origURI = new MutableURI();
        origURI.setEncoding( DEFAULT_ENCODING );
        MutableURI uri = new MutableURI();
        uri.setEncoding( DEFAULT_ENCODING );
        String path = "/path";
        String name = "paramName";
        String value = "paramValue";
        String query = name + "=" + value;

        // test setQuery()
        origURI.setPath( path );
        uri.setPath( path );
        origURI.setQuery( query );
        uri.setQuery( query );
        assertEquals( uri.getParameter( name ), value );

        // test for query parameter separated by "&amp;" entity
        query = "param1&amp;param1=&amp;param2";
        URIContext uriContext = new URIContext();
        uriContext.setUseAmpEntity( false );
        uri.setQuery( query );
        assertEquals( uri.getQuery( uriContext ), "param1&param1=&param2" );
        uriContext.setUseAmpEntity( true );
        assertEquals( uri.getQuery( uriContext ), query );

        // now use addParameter() to set the query
        uri.setQuery( null );
        uri.addParameter( name, value, true );
        assertEquals( uri, origURI );
        assertEquals( uri.getParameter( name ), value );
        assertEquals( uri.getParameters( name ).size(), 1 );
        assertEquals( uri.getParameters().size(), 1 );

        // remove the parameter
        uri.removeParameter( name );
        assertFalse( uri.equals( origURI ) );
        assertEquals( uri.getParameters( name ).size(), 0 );
        assertEquals( uri.getParameters().size(), 0 );

        // add a parameter with a null value (just a name)
        uri.addParameter( name, null, true );
        List/*< String >*/ values = uri.getParameters( name );
        assertEquals( values.size(), 1 );
        assertEquals( values.get( 0 ), null );

        // ask for a parameter that does not exist
        values = uri.getParameters( "bogus name" );
        assertEquals( values.size(), 0 );

        // add a parameter values with the same name
        String[] initValues = { "", null, "value1", "value2" };
        uri.setQuery( null );
        int i = 0;

        for ( i = 0; i < initValues.length; i++ )
        {
           uri.addParameter( name, initValues[i], true );
        }

        assertEquals( uri.getParameter( name ), initValues[0] );
        values = uri.getParameters( name );
        assertEquals( values.size(), initValues.length );
        assertEquals( uri.getParameters().size(), 1 );
        i = 0;

        for ( java.util.Iterator ii = values.iterator(); ii.hasNext(); )
        {
            String v = ( String ) ii.next();
            assertEquals( v, initValues[i++] );
        }

        // and add a bunch of paramters
        String[][] initParams =
        {
            { "param1", "value1" }, { "param2", "value2" },
            { "param3", "value3" }, { "param4", "value4" },
            { "param5", "value5" }, { "param6", "value6" },
            { "param7", "value7" }, { "param8", "value8" },
            { "param9", "value9" }, { "param10", "value10" },
            { "param11", "value11" }, { "param12", "value12" }
        };

        uri.setQuery( null );
        HashMap map = new HashMap();

        for ( i = 0; i < initParams.length; i++ )
        {
            map.put( initParams[i][0], initParams[i][1] );
        }

        uri.addParameters( map, true );
        assertEquals( uri.getParameters( initParams[0][0] ).size(), 1 );
        assertEquals( uri.getParameters().size(), initParams.length );
        Map/*< String, List< String > >*/ params = uri.getParameters();

        for ( java.util.Iterator j = params.keySet().iterator(); j.hasNext(); )
        {
            String n = ( String ) j.next();
            for ( java.util.Iterator k = ( ( List ) params.get( n ) ).iterator(); k.hasNext(); )
            {
                String v = ( String ) k.next();
                assertTrue( map.containsKey( n ) );
                assertEquals( v, map.get( n ) );
            }
        }
    }

    public void testEncoding()
    {
        String path = "/path";

        MutableURI utf8EncodedUri = new MutableURI();
        utf8EncodedUri.setEncoding( DEFAULT_ENCODING );
        utf8EncodedUri.setPath( path );

        MutableURI eucJPEncodedUri = new MutableURI();
        eucJPEncodedUri.setEncoding( "EUC_JP" );
        eucJPEncodedUri.setPath( path );

        // test encoding of URI reserved characters, etc.
        String reserved = "semi;slash/colon:at@and&eq=pl+doller$comm,q?end";
        String mark = "hyph-un_per.ex!tilda~ast*ap'lp(rp)";
        utf8EncodedUri.addParameter( reserved, mark, false );
        eucJPEncodedUri.addParameter( reserved, mark, false );
        URIContext uriContext = new URIContext();
        uriContext.setUseAmpEntity( false );
        assertEquals( utf8EncodedUri.getURIString( uriContext ), eucJPEncodedUri.getURIString( uriContext ) );
        assertEquals( utf8EncodedUri.getQuery( uriContext ),
                      "semi%3Bslash%2Fcolon%3Aat%40and%26eq%3Dpl%2Bdoller%24comm%2Cq%3Fend"
                      + "=" + "hyph-un_per.ex%21tilda%7East*ap%27lp%28rp%29" );

        utf8EncodedUri.setQuery( null );
        eucJPEncodedUri.setQuery( null );
        String unwise = "lcb{rcb}bar|bs\\ctr^lb[rb]lq`";
        String delims = "lt<gt>lb#perc%\"quotes\"";
        String excluded = "space " + delims;
        utf8EncodedUri.addParameter( unwise, excluded, false );
        eucJPEncodedUri.addParameter( unwise, excluded, false );
        assertEquals( utf8EncodedUri.getURIString( uriContext ), eucJPEncodedUri.getURIString( uriContext ) );
        assertEquals( utf8EncodedUri.getQuery( uriContext ),
                      "lcb%7Brcb%7Dbar%7Cbs%5Cctr%5Elb%5Brb%5Dlq%60"
                      + "=" + "space+lt%3Cgt%3Elb%23perc%25%22quotes%22" );

        // test encoding of multibyte characters in a URI.
        utf8EncodedUri.setQuery( null );
        eucJPEncodedUri.setQuery( null );
        String name = "name";
        String japaneseUnicode = "\u63d0\u51fa\u6e08\u307f";
        utf8EncodedUri.addParameter( name, japaneseUnicode, false );
        eucJPEncodedUri.addParameter( name, japaneseUnicode, false );
        assertFalse( utf8EncodedUri.getURIString( uriContext ).equals( eucJPEncodedUri.getURIString( uriContext ) ) );
        assertEquals( utf8EncodedUri.getQuery( uriContext ),
                      name + "=" + "%E6%8F%90%E5%87%BA%E6%B8%88%E3%81%BF" );
        assertEquals( eucJPEncodedUri.getQuery( uriContext ),
                      name + "=" + "%C4%F3%BD%D0%BA%D1%A4%DF" );

        // test encoding in constructor.
        try
        {
            // mark characters OK unescaped in the path
            MutableURI uri = new MutableURI( "http://localhost:80/mark/" + mark, false );
            assertEquals( "http://localhost:80/mark/" + mark,
                          uri.getURIString( uriContext ) );

            // reserved characters OK unescaped in the
            // path and the '?' indicates the start of the query
            uri = new MutableURI( "http://localhost:80/reserved/" + reserved, false );
            assertEquals( "http://localhost:80/reserved/" + reserved,
                          uri.getURIString( uriContext ) );

            // unwise
            uri = new MutableURI( "http://localhost:80/unwise/" + unwise, false );
            assertEquals( "http://localhost:80/unwise/lcb%7Brcb%7Dbar%7Cbs%5Cctr%5Elb%5Brb%5Dlq%60",
                          uri.getURIString( uriContext ) );

            // excluded. note that '#' is interpreted as fragment
            // and should not be escaped in this test
            uri = new MutableURI( "http://localhost:80/excluded/" + excluded, false );
            assertEquals( "http://localhost:80/excluded/space%20lt%3Cgt%3Elb#perc%25%22quotes%22",
                          uri.getURIString( uriContext ) );
        }
        catch ( URISyntaxException e )
        {
            fail( "Test failed with a URISyntaxException: " + e.getMessage() );
        }
    }

    public void testGetURIStringForXML()
    {
        MutableURI uri = new MutableURI();
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
        /*
        Note, query strings need to maintain the same order of parameters in order to be considered equal */
        String queryString = "param1&param2&param1=&param3=true&param1=true";
        String uriString = "https://localhost:443/test?" + queryString;

        try
        {
            MutableURI uriA = new MutableURI();
            uriA.setScheme( "https" );
            uriA.setHost( "localhost" );
            uriA.setPort( 443 );
            uriA.setPath( "/test" );
            uriA.setQuery(queryString);

            MutableURI uriB = new MutableURI( uriString, true );
            URIContext uriContext = new URIContext();
            uriContext.setUseAmpEntity( false );
            assertEquals( uriA.getURIString( uriContext ), uriString );
            assertEquals( uriA.getURIString( uriContext ), uriB.getURIString( uriContext ) );

            MutableURI uriC = new MutableURI( new URI( uriString ) );

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
            class AnotherMutableURI extends MutableURI
            {
                boolean reference = true;

                AnotherMutableURI( URI uri ) { super( uri ); }

                void setReference( boolean ref ) { reference = ref; }

                boolean isReference() { return reference; }
            }

            AnotherMutableURI another = new AnotherMutableURI( new URI( uriString ) );
            assertFalse( uriA.equals( another ) );

            // also, an extra test that setURI() resets the values of the object
            uriA.setURI( "", true );
            assertTrue( uriA.equals( new MutableURI( "", true ) ) );
        }
        catch ( URISyntaxException e )
        {
            fail( "Test failed for URI, \"" + uriString + "\", with a URISyntaxException: " + e.getMessage() );
        }
    }

    public void testOpaque()
    {
        try
        {
            String scheme = "news";
            String schemeSpecificPart = "comp.lang.java";
            String uriString = scheme + ":" + schemeSpecificPart;
            URIContext uriContext = MutableURI.getDefaultContext();

            // constructor, setters, getters
            MutableURI uriA = new MutableURI( "mailto:abc@tuv.wxyz.com", true );
            assertTrue( uriA.isAbsolute() );
            assertTrue( uriA.isOpaque() );
            uriA.setURI( uriString, true );
            assertTrue( uriA.isAbsolute() );
            assertTrue( uriA.isOpaque() );
            assertEquals( uriString, uriA.getURIString( uriContext ) );
            assertEquals( uriA.getScheme(), scheme );
            assertEquals( uriA.getSchemeSpecificPart(), schemeSpecificPart );

            MutableURI uriB = new MutableURI( "http://localhost/test", true );
            assertFalse( uriB.isOpaque() );
            uriB.setURI( "/test?param1=true", true );
            assertFalse( uriB.isOpaque() );

            // check equality
            uriB.setOpaque( scheme, schemeSpecificPart );
            assertTrue( uriB.isOpaque() );
            assertTrue( uriA.equals( uriA ) );
            assertTrue( uriA.equals( uriB ) );
            assertTrue( uriB.equals( uriA ) );

            // change to non-opaque URI
            uriB.setPath( "/test" );
            assertFalse( uriB.isOpaque() );
            uriB.setOpaque( scheme, schemeSpecificPart );
            assertTrue( uriB.isOpaque() );
            uriB.setHost( "localhost" );
            assertFalse( uriB.isOpaque() );
            uriB.setURI( "/test?param1=true", true );
            assertEquals( uriB.getScheme(), null );
        }
        catch ( URISyntaxException e )
        {
            fail( "Test failed with a URISyntaxException: " + e.getMessage() );
        }
    }

    /**
     * This method is used as a diagnostic utility when writing tests.  It should
     * not be removed!
     *
     * @param uri the URI to print
     */
    private void dumpURI( MutableURI uri )
    {
        if ( uri == null )
            System.out.println( "uri == null" );
        else {
            System.out.println( "uri:        " + uri.getURIString( null) );
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

    public MutableURITest( String name )
    {
        super( name );
    }

    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite( MutableURITest.class );
    }
}
