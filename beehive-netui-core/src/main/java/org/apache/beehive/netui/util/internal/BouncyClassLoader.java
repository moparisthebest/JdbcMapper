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
package org.apache.beehive.netui.util.internal;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.apache.beehive.netui.util.internal.concurrent.InternalConcurrentHashMap;
import java.util.Map;
import java.util.Iterator;
import java.security.SecureClassLoader;

import org.apache.beehive.netui.util.logging.Logger;


/**
 * ClassLoader that takes precendence over its parent for loading classes, and which is aware of timestamps on its
 * binaries (and thus knows when it needs to be bounced).
 */
public class BouncyClassLoader
        extends SecureClassLoader
{
    private static final Logger _log = Logger.getInstance( BouncyClassLoader.class );
    
    private InternalConcurrentHashMap/*< File, Long >*/ _timestamps = new InternalConcurrentHashMap/*< File, Long >*/();
    private File[] _classDirs;
    
    
    public BouncyClassLoader( File[] classDirs, ClassLoader parent )
    {
        super( parent );
        _classDirs = classDirs;
    }
    
    public Class loadClass( String name ) throws ClassNotFoundException
    {
        Class ret = findLoadedClass( name );
        if ( ret != null ) return ret;
        
        String baseName = File.separatorChar + name.replace( '.', File.separatorChar ).concat( ".class" );
        
        for ( int i = 0; i < _classDirs.length; ++i )
        {
            File file = new File( _classDirs[i].getPath() + baseName );
            
            if ( file.exists() )
            {
                _timestamps.put( file, new Long( file.lastModified() ) );
                byte[] bytes = getBytes( file );
                
                if (_log.isDebugEnabled()) {
                    _log.debug("Loading class " + name + " from file " + file);
                }
                if ( bytes != null ) return super.defineClass( name, bytes, 0, bytes.length );
            }
        }
        
        return super.loadClass( name );
    }

    public URL getResource(String name)
    {
        for ( int i = 0; i < _classDirs.length; ++i )
        {
            File file = new File( _classDirs[i].getPath(), name );
            
            if ( file.exists() )
            {
                _timestamps.put( file, new Long( file.lastModified() ) );
                
                try {
                    if (_log.isDebugEnabled()) {
                        _log.debug("Loading resource " + name + " from file " + file);
                    }
                    return file.toURL();
                } catch (MalformedURLException e) {
                    // Shouldn't happen if the file exists.
                    e.printStackTrace();
                    assert false : "malformed URL for file " + file.getPath();
                }
            }
        }
        
        return super.getResource(name);
    }

    private byte[] getBytes( File file )
    {
        try
        {
            BufferedInputStream in = new BufferedInputStream( new FileInputStream( file ) );
            
            try
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int c;
                
                while ( ( c = in.read() ) != -1 )
                {
                    out.write( c );
                }
                
                return out.toByteArray();
            }
            finally
            {
                in.close();
            }
        }
        catch ( FileNotFoundException e )
        {
            _log.error( "Could not read class file " + file, e );
        }
        catch ( IOException e )
        {
            _log.error( "Error while reading class file " + file, e );
        }
        
        return null;
    }

    public boolean isStale()
    {
        for ( Iterator i = _timestamps.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = ( Map.Entry ) i.next();
            if ( ( ( File ) entry.getKey() ).lastModified() > ( ( Long ) entry.getValue() ).longValue() ) {
                if (_log.isDebugEnabled()) {
                    _log.debug("Detected modified class/resource in " + entry.getKey());
                }
                return true;
            }
        }
        
        return false;
    }
}
