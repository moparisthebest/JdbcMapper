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
package org.apache.beehive.netui.core.urls;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.beehive.netui.core.URLCodec;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;

/**
 * Mutable class for creating URIs.
 *
 * <p> There is little checking that an instance of this class produces a legal
 * URI reference as defined by <a href="http://www.ietf.org/rfc/rfc2396.txt">
 * <i>RFC&nbsp;2396: Uniform Resource Identifiers (URI): Generic Syntax</i></a>.
 * The minimal checking for syntax is on constructors that take a String
 * representation or the URI, a {@link URI}, or a {@link URL}.
 * To avoid the cost of continually checking the syntax, it is up to the
 * user to ensure that the components are set correctly. </p>
 *
 * <p> The setters of this class also assume that the data components are
 * already encoded correctly for the given encoding of this URI, unless noted
 * otherwise as in the methods to add unecoded parameters to the query.
 * Then this class will handle the encoding.
 * See {@link #addParameter( String name, String value, boolean encoded )}
 * and {@link #addParameters( Map newParams, boolean encoded )}
 * </p>
 *
 * <p> There is a static convenience method in this class so callers can
 * easily encode unencoded components before setting it in this object. </p>
 */
public class MutableURI
{
    /** Value used to set the port as undefined. */
    public static final int UNDEFINED_PORT = -1;

    /** Value used to set the encoding as undefined. */
    public static final String DEFAULT_ENCODING = "UTF-8";

    /** Character encoding used for the URI. */
    private String _encoding;

    /** Protocol scheme. */
    private String _scheme;

    /**
     * Flag indicating whether this is for an opaque URI or not.
     * An "opaque URI is an absolute URI whose scheme-specific part does not
     * begin with a slash character". This class will allow a caller to set
     * the URI to opaque and use a specific scheme-specific part.
     */
    private boolean _opaque = false;
    private String _schemeSpecificPart;

    /**
     * User information "may consist of a user name and, optionally,
     * scheme-specific information about how to gain authorization to
     * access the server.
     */
    private String _userInfo;

    /** Host */
    private String _host;

    /** Port */
    private int _port = UNDEFINED_PORT;

    /** Path */
    private String _path;

    /** Query parameters */
    private QueryParameters _parameters;

    /** Fragment */
    private String _fragment;

    /* Separators for query parameters */
    private static final String AMP_ENTITY = "&amp;";
    private static final String AMP_CHAR = "&";

    /**
     * Constructs a <code>MutableURI</code>.
     */
    public MutableURI()
    {
    }

    /**
     * Constructs a <code>MutableURI</code>.
     *
     * @param  uriString the string to be parsed into a URI
     * @param  encoded Flag indicating whether the string is
     *                 already encoded.
     */
    public MutableURI( String uriString, boolean encoded ) throws URISyntaxException
    {
        assert uriString != null : "The uri cannot be null.";

        if ( uriString == null )
        {
            throw new IllegalArgumentException( "The URI cannot be null." );
        }

        URI uri = null;

        if ( encoded )
        {
            // Get (parse) the components using java.net.URI
            uri = new URI( uriString );
        }
        else
        {
            // Parse, then encode this string into its components using URI
            uri = encodeURI( uriString );
        }

        setURI( uri );
    }

    /**
     * Constructs a <code>MutableURI</code>. Assumes the individual components
     * are already encoded and escaped.
     *
     * @param scheme the name of the protocol to use
     * @param userInfo the username and password
     * @param host the name of the host
     * @param port the port number on the host
     * @param path the file on the host
     * @param query the query part of this URI
     * @param fragment the fragment part of this URI (internal reference in the URL)
     */
    public MutableURI( String scheme, String userInfo, String host, int port,
                       String path, String query, String fragment )
    {
        setScheme( scheme );
        setUserInfo( userInfo );
        setHost( host );
        setPort( port );
        setPath( path );
        setQuery( query );
        setFragment( fragment );
    }

    /**
     * Constructs a <code>MutableURI</code>.
     *
     * @param  uri the initial value for this mutable URI
     */
    public MutableURI( URI uri )
    {
        assert uri != null : "The URI cannot be null.";

        if ( uri == null )
        {
            throw new IllegalArgumentException( "The URI cannot be null." );
        }

        setURI( uri );
    }

    /**
     * Constructs a <code>MutableURI</code>.
     *
     * <p> This is just a convenience constructor that functions the same as
     * {@link #MutableURI(URI)} constructor with
     * {@link java.net.URL#toURI()} as the argument. </p>
     *
     * <p>Note, any URL instance that complies with RFC 2396 can be converted
     * to a URI. However, some URLs that are not strictly in compliance
     * can not be converted to a URI. See {@link java.net.URL} </p>
     *
     * @param  url the initial value for this mutable URI
     * @exception URISyntaxException if this URL is not formatted strictly
     *            to RFC2396 and cannot be converted to a URI.
     * @see        java.net.URL#toURI()
     */
    public MutableURI( URL url ) throws URISyntaxException
    {
        assert url != null : "The URL cannot be null.";

        if ( url == null )
        {
            throw new IllegalArgumentException( "The URL cannot be null." );
        }

        URI uri = url.toURI();
        setURI( uri );
    }

    /**
     * Set the value of the <code>MutableURI</code>.
     *
     * <p> This method can also be used to clear the <code>MutableURI</code>. </p>
     *
     * @param  uriString the string to be parsed into a URI
     * @param  encoded Flag indicating whether the string is
     *                 already encoded.
     */
    public void setURI( String uriString, boolean encoded ) throws URISyntaxException
    {
        if ( uriString == null )
        {
            setScheme( null );
            setUserInfo( null );
            setHost( null );
            setPort( UNDEFINED_PORT );
            setPath( null );
            setQuery( null );
            setFragment( null );
        }
        else
        {
            URI uri = null;

            if ( encoded )
            {
                // Get (parse) the components using java.net.URI
                uri = new URI( uriString );
            }
            else
            {
                // Parse, then encode this string into its components using URI
                uri = encodeURI( uriString );
            }

            setURI( uri );
        }
    }

    /**
     * Set the value of the <code>MutableURI</code>.
     *
     * <p> This method can also be used to clear the <code>MutableURI</code>. </p>
     *
     * @param  uri       the URI
     */
    public void setURI( URI uri )
    {

        if ( uri == null )
        {
            setScheme( null );
            setUserInfo( null );
            setHost( null );
            setPort( UNDEFINED_PORT );
            setPath( null );
            setQuery( null );
            setFragment( null );
        }
        else if ( uri.isOpaque() )
        {
            setUserInfo( null );
            setHost( null );
            setPort( UNDEFINED_PORT );
            setPath( null );
            setQuery( null );
            setOpaque( uri.getScheme(), uri.getSchemeSpecificPart() );
            setFragment( uri.getRawFragment() );
        }
        else
        {
            setSchemeSpecificPart( null );
            setScheme( uri.getScheme() );
            setUserInfo( uri.getRawUserInfo() );
            setHost( uri.getHost() );
            setPort( uri.getPort() );
            setPath( uri.getRawPath() );
            setQuery( uri.getRawQuery() );
            setFragment( uri.getRawFragment() );
        }
    }

    /**
     * Set the encoding used when adding unencoded parameters.
     *
     * @param encoding
     */
    public void setEncoding( String encoding )
    {
        _encoding = encoding;
    }

    /**
     * Returns the character encoding that is used when adding unencoded parameters.
     *
     * @return encoding
     */
    public String getEncoding()
    {
        return _encoding;
    }

    /**
     * Sets the protocol/scheme.
     *
     * @param scheme protocol/scheme
     */
    public void setScheme( String scheme )
    {
        _scheme = null;
        if ( scheme != null && scheme.length() > 0 )
        {
            _scheme = scheme;
        }
    }

    /**
     * Returns the protocol/scheme. If no protocol was previously set,
     * returns null.
     *
     * @return protocol/scheme
     */
    public String getScheme()
    {
        return _scheme;
    }

    /**
     * Sets the userInfo. Assumes this component is already escaped.
     *
     * @param userInfo userInfo
     */
    public void setUserInfo( String userInfo )
    {
        _userInfo = null;
        if ( userInfo != null && userInfo.length() > 0 )
        {
            _userInfo = userInfo;
        }
    }

    /**
     * Returns the userInfo. If no host was previously set, returns
     * null.
     *
     * @return userInfo
     */
    public String getUserInfo()
    {
        return _userInfo;
    }

    /**
     * Sets the host.
     *
     * @param host host
     */
    public void setHost( String host )
    {
        _host = null;
        if ( host != null && host.length() > 0 )
        {
            //
            // Here's some very minimal support for IPv6 addresses.
            // If the literal IPv6 address is not enclosed in square brackets
            // then add them.
            //
            boolean needBrackets = ( ( host.indexOf( ':' ) >= 0 )
                    && !host.startsWith( "[" )
                    && !host.endsWith( "]" ) );

            if ( needBrackets )
            {
                _host = '[' + host + ']';
            }
            else
            {
                _host = host;
            }

            _opaque = false;
            setSchemeSpecificPart( null );
        }

        if ( _host == null )
        {
            setUserInfo( null );
            setPort( UNDEFINED_PORT );
        }
    }

    /**
     * Returns the host. If no host was previously set, returns
     * null.
     *
     * @return host
     */
    public String getHost()
    {
        return _host;
    }

    /**
     * Sets the port.
     *
     * @param port port
     */
    public void setPort( int port )
    {
        assert ( port >= 0 && port <= 65535 ) || ( port == UNDEFINED_PORT )
                 : "Invalid port" ;

        if ( ( port > 65535 ) || ( port < 0 && port != UNDEFINED_PORT ) )
        {
             throw new IllegalArgumentException( "A port must be between 0 and 65535 or equal to "
                                                 + UNDEFINED_PORT + ".");
        }

        _port = port;
    }

    /**
     * Returns the port. If no port was previously set, returns
     * null.
     *
     * @return port
     */
    public int getPort()
    {
        return _port;
    }

    /**
     * Sets the path. Assumes this component is already escaped.
     *
     * @param path path
     */
    public void setPath( String path )
    {
        // Note that an empty path is OK
        if ( path == null )
        {
            _path = null;
            setQuery( null );
            setFragment( null );
        }
        else
        {
            _path = path;
            _opaque = false;
            setSchemeSpecificPart( null );
        }
    }

    /**
     * Returns the path.
     *
     * @return path
     */
    public String getPath()
    {
        return _path;
    }

    /**
     * Sets (and resets) the query string.
     * This method assumes that the query is already encoded and escaped.
     *
     * @param query Query string
     */
    public void setQuery( String query )
    {
        _parameters = null;

        if ( query == null || query.length() == 0 ) { return; }

        for ( StringTokenizer tok = new StringTokenizer( query, "&" ); tok.hasMoreElements(); )
        {
            String queryItem = tok.nextToken();

            if ( queryItem.startsWith( "amp;" ) )
            {
                queryItem = queryItem.substring( 4 );
            }

            int eq = queryItem.indexOf( '=' );
            if ( eq != -1 )
            {
                addParameter( queryItem.substring( 0, eq ) , queryItem.substring( eq + 1 ), true );
            }
            else
            {
                addParameter( queryItem, null, true );
            }
        }
    }

    /**
     * Returns the query string (encoded/escaped).
     *
     * <p> The context states whether or not to use the default delimiter,
     * usually the &quot;&amp;amp;&quot; entity, to separate the parameters.
     * Otherwise, the &quot;&amp;&quot; character is used. </p>
     *
     * @param uriContext has property indicating if we use the HTML Amp entity
     *        to separate the query parameters.
     * @return encoded query string.
     */
    public String getQuery( URIContext uriContext )
    {
        if(_parameters == null || !_parameters.hasParameters())
            return null;

        String paramSeparator = AMP_ENTITY;
        if ( uriContext == null )
        {
            uriContext = getDefaultContext();
        }

        if ( !uriContext.useAmpEntity() )
        {
            paramSeparator = AMP_CHAR;
        }

        InternalStringBuilder query = new InternalStringBuilder( 64 );
        boolean firstParam = true;
        for(Iterator iterator = _parameters.iterator(); iterator.hasNext(); ) {
            QueryParameters.Parameter parameter = (QueryParameters.Parameter)iterator.next();
            String name = parameter.name;
            String value = parameter.value;

            if (firstParam)
                firstParam = false;
            else query.append( paramSeparator );

            query.append( name );

            /* todo: does the '=' need to be here in order for the parsing to work correctly? */
            if(value != null) {
                query.append( '=' ).append( value );
            }
        }

        return query.toString();
    }

    /**
     * Add a parameter for the query string.
     * <p> If the encoded flag is true then this method assumes that
     * the name and value do not need encoding or are already encoded
     * correctly. Otherwise, it translates the name and value with the
     * character encoding of this URI and adds them to the set of
     * parameters for the query. If the encoding for this URI has
     * not been set, then the default encoding used is "UTF-8". </p>
     * <p> Multiple values for the same parameter can be set by
     * calling this method multiple times with the same name. </p>
     *
     * @param name  name
     * @param value value
     * @param encoded Flag indicating whether the names and values are
     *                already encoded.
     */
    public void addParameter( String name, String value, boolean encoded )
    {
        if ( name == null )
        {
            throw new IllegalArgumentException( "A parameter name may not be null." );
        }

        if ( !encoded )
        {
            name = encode( name );
            value = encode( value );
        }

        if ( _parameters == null )
        {
            _parameters = new QueryParameters();
            _opaque = false;
            setSchemeSpecificPart( null );
        }

        _parameters.addParameter(name, value);
    }

    /**
     * <p>
     * Adds all the parameters in a {@link Map} to a URI's query string.
     * </p>
     * <p>
     * If the encoded flag is true then this method assumes that
     * the name and value do not need encoding or are already encoded
     * correctly. Otherwise, it translates the name and value with the
     * character encoding of this URI and adds them to the set of
     * parameters for the query. If the encoding for this URI has
     * not been set, then the default encoding used is "UTF-8".
     * </p>
     * <p>
     * The query parameters are added in the order the keys of the Map.
     * </p>
     *
     * @param newParams the map of new parameters to add to the URI
     * @param encoded Flag indicating whether the names and values are already encoded.
     */
    public void addParameters( Map newParams, boolean encoded ) {
        if ( newParams == null )
            throw new IllegalArgumentException("Cannot add null map of parameters.");

        if ( newParams.size() == 0 )
            return;

        if(_parameters == null) {
            _parameters = new QueryParameters();
            _opaque = false;
            setSchemeSpecificPart( null );
        }

        Iterator keys = newParams.keySet().iterator();
        while ( keys.hasNext() )
        {
            String name = ( String ) keys.next();
            String encodedName = name;

            if ( !encoded ) { encodedName = encode( name ); }

            Object newValue = newParams.get( name );
            if ( newValue == null )
            {
                addParameter(encodedName, null, false);
            }
            else if ( newValue instanceof String )
            {
                addValue( encodedName, ( String ) newValue, encoded );
            }
            else if ( newValue instanceof String[] )
            {
                String newValues[] = ( String[] ) newValue;
                for ( int i = 0; i < newValues.length; i++ )
                {
                    addValue( encodedName, newValues[i], encoded );
                }
            }
            else if ( newValue instanceof List )
            {
                Iterator newValues = ( ( List ) newValue ).iterator();
                while ( newValues.hasNext() )
                {
                    addValue( encodedName, newValues.next().toString(), encoded );
                }
            }
            else /* Convert other objects to a string */
            {
                addValue( encodedName, newValue.toString(), encoded );
            }
        }
    }

    private void addValue( String name, String value, boolean encoded ) {
        if (!encoded)
            value = encode(value);

        _parameters.addParameter(name, value);
    }

    /**
     * Returns the value of the parameter. If several values are associated with the given
     * parameter name, the first value is returned.
     *
     * @param name a name of the parameter
     * @return value associated with the given parameter name
     */
    public String getParameter( String name )
    {
        if (_parameters == null || !_parameters.hasParameters())
            return null;

        List parameters = _parameters.getParameterValues(name);
        if(parameters != null && parameters.size() > 0)
            return (String)parameters.get(0);
        else return null;
    }

    /**
     * Returns the values of the given parameter.
     *
     * @param name name of the parameter
     * @return an unmodifable {@link java.util.List} of values for the given parameter name
     */
    public List/*< String >*/ getParameters( String name )
    {
        if ( _parameters == null || !_parameters.hasParameters())
            return Collections.EMPTY_LIST;
        else {

            List parameters = _parameters.getParameterValues(name);
            if(parameters == null)
                return Collections.EMPTY_LIST;
            else return Collections.unmodifiableList(parameters);
        }
    }

    /**
     * Returns an unmodifiable {@link Map} of all parameters.
     *
     * @return an unmodifiable {@link java.util.Map} of names and values for all parameters
     */
    public Map/*< String, List< String > >*/ getParameters()
    {
        if(_parameters == null || !_parameters.hasParameters())
            return Collections.EMPTY_MAP;
        else return _parameters.getParameters();
    }

    /**
     * Removes the given parameter.
     *
     * @param name name
     */
    public void removeParameter( String name )
    {
        if (_parameters == null || !_parameters.hasParameters())
            return;

        _parameters.removeParameter(name);
    }

    /**
     * Sets the fragment.
     *
     * @param fragment fragment
     */
    public void setFragment( String fragment )
    {
        _fragment = null;
        if ( fragment != null && fragment.length() > 0 )
        {
            _fragment = fragment;
        }
    }

    /**
     * Returns the fragment.
     *
     * @return fragment
     */
    public String getFragment()
    {
        return _fragment;
    }

    /**
     * Tells whether or not this URI is absolute.
     *
     * <p> A URI is absolute if, and only if, it has a scheme component. </p>
     *
     * @return  <tt>true</tt> if, and only if, this URI is absolute
     */
    public boolean isAbsolute() {
        return getScheme() != null;
    }

    /**
     * Tells whether or not this URI is opaque.
     *
     * @return <tt>true</tt> if this URI was explicitly set as opaque
     */
    public boolean isOpaque()
    {
        return _opaque;
    }

    /**
     * Sets the URI to be opaque using the given scheme and
     * schemeSpecificPart.
     * <p> From {@link URI}: &quot;A URI is opaque if, and only
     * if, it is absolute and its scheme-specific part does not begin with
     * a slash character ('/'). An opaque URI has a scheme, a
     * scheme-specific part, and possibly a fragment; all other components
     * are undefined.&quot; </p>
     *
     * @param scheme the scheme component of this URI
     * @param schemeSpecificPart the scheme-specific part of this URI
     */
    public void setOpaque( String scheme, String schemeSpecificPart )
    {
        if ( scheme == null || scheme.length() == 0
                || schemeSpecificPart == null
                || schemeSpecificPart.length() == 0
                || schemeSpecificPart.indexOf( '/' ) > 0 )
        {
            throw new IllegalArgumentException( "Not a proper opaque URI." );
        }

        _opaque = true;
        setScheme( scheme );
        setSchemeSpecificPart( schemeSpecificPart );
        setUserInfo( null );
        setHost( null );
        setPort( UNDEFINED_PORT );
        setPath( null );
        setQuery( null );
    }

    /**
     * Returns the scheme-specific part of this URI if it is opaque.
     * Otherwise, this method returns null.
     * @return the scheme-specific part of this URI if this URI was
     *         explicitly set as opaque. Otherwise, return null.
     */
    public String getSchemeSpecificPart()
    {
        if ( isOpaque() )
            return _schemeSpecificPart;

        return null;
    }

    /**
     * Set the scheme-specific part of this (opaque) URI
     * @param schemeSpecificPart the scheme-specific part of this URI
     */
    protected void setSchemeSpecificPart( String schemeSpecificPart )
    {
        _schemeSpecificPart = schemeSpecificPart;
    }

    /**
     * Returns a string form of this URI. The {@link URIContext}
     * encapsulates the data needed to write out the string form.
     *
     * <p> E.g. Defines if the &quot;&amp;amp;&quot; entity or the
     * '&amp;' character should be used to separate quary parameters. </p>
     *
     * @param uriContext data required to write out the string form.
     * @return the URI as a <code>String</code>
     */
    public String getURIString( URIContext uriContext )
    {
        InternalStringBuilder buf = new InternalStringBuilder( 128 );

        // Append the scheme
        if ( getScheme() != null )
        {
            buf.append( getScheme() ).append( ':' );

            if ( isOpaque() )
            {
                buf.append( getSchemeSpecificPart() );
            }
        }

        // Append the user info, host and, port
        if ( getHost() != null)
        {
            buf.append( "//" );

            if ( getUserInfo() != null )
            {
                buf.append( getUserInfo() );
                buf.append( '@' );
            }

            buf.append( getHost() );

            if ( getPort() != UNDEFINED_PORT )
            {
                buf.append( ':' ).append( getPort() );
            }
        }

        // Append the path.
        if ( getPath() != null )
        {
            if ( isAbsolute() )
            {
                // absolute URI so
                appendEnsureSeparator( buf, getPath() );
            }
            else
            {
                buf.append( getPath() );
            }
        }

        // Append the parameters (the query)
        if (_parameters != null && _parameters.hasParameters())
        {
            buf.append( '?' );
            buf.append( getQuery( uriContext ) );
        }

        // Append the fragment
        if ( getFragment() != null && getFragment().length() > 0 )
        {
            buf.append( '#' ).append( getFragment() );
        }

        return buf.toString();
    }

    /**
     * Returns a default <code>URIContext</code>.
     *
     * @return the URIContext with default data.
     */
    public static URIContext getDefaultContext()
    {
        URIContext uriContext = new URIContext();
        uriContext.setUseAmpEntity( true );

        return uriContext;
    }

    private static void appendEnsureSeparator( InternalStringBuilder buf, String token )
    {
        if ( token != null && token.length() > 0 )
        {
            if ( buf.charAt( buf.length() - 1 ) != '/' && token.charAt( 0 ) != '/' )
            {
                buf.append( '/' );
            }
            if ( buf.charAt( buf.length() - 1 ) == '/' && token.charAt( 0 ) == '/' )
            {
                token = token.substring( 1 );
            }
            buf.append( token );
        }
    }

    /**
     * Convenience method to encode unencoded components of a URI.
     *
     * @param url      the string to be encoded by {@link URLCodec}
     * @param encoding the character encoding to use
     * @return         the encoded string
     */
    public static String encode( String url, String encoding )
    {
        String encodedURL = null;
        try
        {
            encodedURL = URLCodec.encode( url, encoding );
        }
        catch ( java.io.UnsupportedEncodingException e )
        {
            // try utf-8 as a default encoding
            try
            {
                encodedURL = URLCodec.encode( url, DEFAULT_ENCODING );
            }
            catch ( java.io.UnsupportedEncodingException ignore )
            {
            }
        }
        return encodedURL;
    }

    /**
     * Convenience method to encode unencoded components of a URI.
     * This implementation uses the value of the character encoding
     * field of this instance.
     *
     * @param url      the string to be encoded by {@link URLCodec}
     * @return         the encoded string
     */
    public String encode( String url )
    {
        return encode( url, _encoding );
    }

    /**
     * Determines if the passed-in Object is equivalent to this MutableURI.
     *
     * @param object the Object to test for equality.
     * @return true if object is a MutableURI with all values equal to this
     *         MutableURI, false otherwise
     */
    
    public boolean equals( Object object )
    {
        if ( object == this ) { return true; }

        if ( object == null || !object.getClass().equals( this.getClass() ) )
        {
            return false;
        }

        MutableURI testURI = ( MutableURI ) object;

        if ( isOpaque() )
        {
            if ( ( _scheme == testURI.getScheme() || ( _scheme != null && _scheme.equalsIgnoreCase( testURI.getScheme() ) ) ) &&
                    ( _schemeSpecificPart == testURI.getSchemeSpecificPart()
                            || ( _schemeSpecificPart != null && _schemeSpecificPart.equals( testURI.getSchemeSpecificPart() ) ) ) &&
                    ( _fragment == testURI.getFragment() || ( _fragment != null && _fragment.equals( testURI.getFragment() ) ) ) )
            {
                return true;
            }
        }

        if ( ( _scheme == testURI.getScheme() || ( _scheme != null && _scheme.equalsIgnoreCase( testURI.getScheme() ) ) ) &&
                ( _userInfo == testURI.getUserInfo() || ( _userInfo != null && _userInfo.equals( testURI.getUserInfo() ) ) ) &&
                ( _host == testURI.getHost() || ( _host != null && _host.equalsIgnoreCase( testURI.getHost() ) ) ) &&
                _port == testURI.getPort() &&
                ( _path == testURI.getPath() || ( _path != null && _path.equals( testURI.getPath() ) ) ) &&
                ( _fragment == testURI.getFragment() || ( _fragment != null && _fragment.equals( testURI.getFragment() ) ) ) &&
                ( _encoding == testURI.getEncoding() || ( _encoding != null && _encoding.equals( testURI.getEncoding() ) ) ) )
        {
            if(_parameters == testURI._parameters || _parameters != null && _parameters.equals(testURI._parameters))
                return true;
        }

        return false;
    }

    /**
     * Returns a hash code value for the object.
     * <p> Implemented in conjunction with equals() override. </p>
     * <p> This is a mutable class implying that we're basing the hash
     * code on the member data that can change. Therefor it's important
     * not to use this class as a key in a hashtable as it would still
     * appear with an enumeration but not when calling contains.
     * I.E. The object could get lost in the hashtable. A call for the
     * hashcode would return a different value than when it was first
     * placed in the hashtable. </p>
     *
     * <p> With this in mind, we simply return the same value to support
     * the rules of equality. </p>
     *
     * @return a hash code value for this object.
     */
    public int hashCode()
    {
        return 0;
    }

    /**
     * Parse a URI reference, as a <code>String</code>, into its
     * components and use {@link java.net.URI} to encode the
     * components correctly. This comes from the parsing
     * algorithm of the Apache Commons HttpClient code for
     * its URI class.
     *
     * @param original the original character sequence
     * @throws URISyntaxException If an error occurs.
     */
    protected static URI encodeURI( String original ) throws URISyntaxException
    {
        if ( original == null )
        {
            throw new IllegalArgumentException( "URI-Reference required" );
        }

        String scheme = null;
        String authority = null;
        String path = null;
        String query = null;
        String fragment = null;
        String tmp = original.trim();
        int length = tmp.length();
        int from = 0;

        // The test flag whether the URI is started from the path component.
        boolean isStartedFromPath = false;
        int atColon = tmp.indexOf( ':' );
        int atSlash = tmp.indexOf( '/' );

        if ( atColon < 0 || ( atSlash >= 0 && atSlash < atColon ) )
        {
            isStartedFromPath = true;
        }

        int at = indexFirstOf( tmp, isStartedFromPath ? "/?#" : ":/?#", from );

        if ( at == -1 )
        {
            at = 0;
        }

        // Parse the scheme.
        if ( at < length && tmp.charAt( at ) == ':' )
        {
            scheme = tmp.substring( 0, at ).toLowerCase();
            from = ++at;
        }

        // Parse the authority component.
        if ( 0 <= at && at < length )
        {
            if ( tmp.charAt( at ) == '/' )
            {
                if ( at + 2 < length && tmp.charAt( at + 1 ) == '/' )
                {
                    // the temporary index to start the search from
                    int next = indexFirstOf( tmp, "/?#", at + 2 );
                    if ( next == -1 )
                    {
                        next = ( tmp.substring( at + 2 ).length() == 0 ) ? at + 2 : tmp.length();
                    }
                    authority = tmp.substring( at + 2, next );
                    from = at = next;
                }
            }
            else if ( scheme != null && tmp.indexOf( '/', at + 1 ) < 0 )
            {
                int next = tmp.indexOf( '#', at );
                if ( next == -1 )
                {
                    next = length;
                }
                String ssp = tmp.substring( at, next );
                if ( next != length )
                {
                    fragment = tmp.substring( next + 1 );
                }
                return new URI( scheme, ssp, fragment );
            }
        }

        // Parse the path component.
        if ( from < length )
        {
            int next = indexFirstOf( tmp, "?#", from );
            if ( next == -1 )
            {
                next = length;
            }
            path = tmp.substring( from, next );
            at = next;
        }

        // Parse the query component.
        if ( 0 <= at && at + 1 < length && tmp.charAt( at ) == '?' )
        {
            int next = tmp.indexOf( '#', at + 1 );
            if ( next == -1 )
            {
                next = length;
            }
            query = tmp.substring( at + 1, next );
            at = next;
        }

        // Parse the fragment component.
        if ( 0 <= at && at + 1 <= length && tmp.charAt( at ) == '#' )
        {
            if ( at + 1 == length )
            { // empty fragment
                fragment = "";
            }
            else
            {
                fragment = tmp.substring( at + 1 );
            }
        }

        // Use java.net.URI to encode components and return.
        return new URI( scheme, authority, path, query, fragment );
    }

    /**
     * Get the earliest index, searching for the first occurrance of
     * any one of the given delimiters.
     *
     * @param s      the string to be indexed
     * @param delims the delimiters used to index
     * @param offset the from index
     * @return the earlier index if there are delimiters
     */
    protected static int indexFirstOf( String s, String delims, int offset )
    {
        if ( s == null || s.length() == 0 )
        {
            return -1;
        }
        if ( delims == null || delims.length() == 0 )
        {
            return -1;
        }

        // check boundaries
        if ( offset < 0 )
        {
            offset = 0;
        }
        else if ( offset > s.length() )
        {
            return -1;
        }

        // s is never null
        int min = s.length();
        char[] delim = delims.toCharArray();
        for ( int i = 0; i < delim.length; i++ )
        {
            int at = s.indexOf( delim[i], offset );
            if ( at >= 0 && at < min )
            {
                min = at;
            }
        }

        return ( min == s.length() ) ? -1 : min;
    }

   /**
     * <p/>
     * Abstraction to deal with query parameters on a URI.  This class holds name / value pairs of parameters
     * in a fixed order of addition and provides methods for manipulating and querying information
     * from the query parameter set.
     * </p>
     * <p/>
     * All modifications to a set of query parameters should be made through this API; for example, to
     * add a query parameter, call {@link #addParameter(String, String)} rather than obtaining the
     * value {@link List} for a query parameter key and adding a parameter directly to this {@link List}.
     * </p>
     * <p/>
     * In general, this class returns unmodifiable {@link List} and {@link Map} data structures.
     * </p>
     */
    private static class QueryParameters {

        /* todo: could probably use a more complex yet faster data structure to store Parameter objects */
        /**
         * The list of query parameters, maintained in FIFO order.
         */
        private LinkedList _orderedQueryParameters;

       private QueryParameters() {
       }

        /**
         * Checks whether the query string is empty or contains a set of parameters.
         *
         * @return <code>true</code> if the query string has parameters; <code>false</code> otherwise
         */
        private boolean hasParameters() {
            return _orderedQueryParameters != null && _orderedQueryParameters.size() > 0;
        }

        /**
         * Add a parameter to the list of query parameters.  The caller is responsible for encoding query parameters
         * names and values as needed.
         *
         * @param name  the name of the parameter
         * @param value the value of the parameter
         */
        private void addParameter(String name, String value) {
            if (_orderedQueryParameters == null)
                _orderedQueryParameters = new LinkedList();
            _orderedQueryParameters.add(new Parameter(name, value));
        }

        /**
         * Remove all of the query string parameters associated with the given name.
         *
         * @param name the name of the parameters to remove
         */
        private void removeParameter(String name) {
            if (_orderedQueryParameters == null || _orderedQueryParameters.size() == 0)
                return;

            if (name == null)
                return;

            for (int i = 0; i < _orderedQueryParameters.size(); i++) {
                Parameter param = (Parameter) _orderedQueryParameters.get(i);
                if (param.name.equals(name))
                    _orderedQueryParameters.remove(param);
            }
        }

        /**
         * Get the parameters associated with a particular query parameter name.  This method returns
         * an unmodifiable {@link List}.
         */
       private List getParameterValues(String name) {
            if (_orderedQueryParameters == null || _orderedQueryParameters.size() == 0)
                return Collections.EMPTY_LIST;

            LinkedList queryParamsForName = new LinkedList();
            for (int i = 0; i < _orderedQueryParameters.size(); i++) {
                Parameter parameter = (Parameter) _orderedQueryParameters.get(i);
                if (parameter.name.equals(name))
                    queryParamsForName.add(parameter.value);
            }

            return Collections.unmodifiableList(queryParamsForName);
        }

        /**
         * Get an interator for parameters that iterates over the natural order of the query parameters
         * in the URL.  This iterator contains elements of type {@link QueryParameters.Parameter}.
         *
         * @return an iterator
         */
        private Iterator iterator() {
            if(_orderedQueryParameters == null)
                return Collections.emptyList().iterator();
            else return _orderedQueryParameters.iterator();
        }

        /**
         * <p/>
         * Get a {@link Map} of all query parameters.  This Map contains pairs of &lt;String, List&gt; where
         * the String is the name of a parameter and the List is the set of values associated with a name.
         * There is <b>no</b> guarantee of order of either the the parameters names or the values associated
         * with a given name.
         * </p>
         *
         * @return a Map containing &lt;String, List&gt; pairs
         */
        private Map getParameters() {
            if(_orderedQueryParameters == null)
                return Collections.EMPTY_MAP;

            LinkedHashMap paramMap = new LinkedHashMap();
            Iterator iterator = _orderedQueryParameters.iterator();
            while (iterator.hasNext()) {
                Parameter parameter = (Parameter) iterator.next();
                String name = parameter.name;
                String value = parameter.value;

                if (paramMap.containsKey(name)) {
                    LinkedList list = (LinkedList) paramMap.get(name);
                    list.add(value);
                }
                else {
                    LinkedList list = new LinkedList();
                    list.add(value);
                    paramMap.put(name, list);
                }
            }

            /* mark all of the contained List elements as unmodifiable */
            Iterator keyIterator = paramMap.keySet().iterator();
            while (keyIterator.hasNext()) {
                Object key = keyIterator.next();
                List value = (List) paramMap.get(key);
                paramMap.put(key, Collections.unmodifiableList(value));
            }

            /* todo: need to wrap these List(s) in unmodificable lists */
            return Collections.unmodifiableMap(paramMap);
        }

       public boolean equals(Object object) {
           if(this == object) return true;
           if(!(object instanceof QueryParameters)) return false;

           final QueryParameters queryParameters = (QueryParameters)object;
           if((_orderedQueryParameters == null || _orderedQueryParameters.size() == 0) &&
               (queryParameters._orderedQueryParameters == null || queryParameters._orderedQueryParameters.size() == 0))
               return true;

           return this._orderedQueryParameters.equals(queryParameters._orderedQueryParameters);
       }

       public int hashCode() {
           return _orderedQueryParameters != null ? _orderedQueryParameters.hashCode() : 0;
       }

       /**
        * Struct representing a URI query parameter.
        */
        private static class Parameter {
           private String name;
           private String value;

           private Parameter(String name, String value) {
               assert name != null;

               this.name = name;
               this.value = value;
           }

           public boolean equals(Object object) {
               if(this == object) return true;
               if(!(object instanceof Parameter)) return false;

               final Parameter parameter = (Parameter)object;

               if(value != null ? !value.equals(parameter.value) : parameter.value != null) return false;

               assert name != null : "Encountered a parameter with a null name!";
               return name.equals(parameter.name);
           }

           public int hashCode() {
               int result;
               result = (name != null ? name.hashCode() : 0);
               result = 29 * result + value.hashCode();
               return result;
           }
       }
   }
}