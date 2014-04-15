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
package org.apache.beehive.controls.runtime.generator.apt;

import com.sun.mirror.apt.Filer;

import java.io.*;
import java.util.*;

/**
 * The controls client manifest (aka "client manifest") surfaces the set of
 * control types used by a client, and make the assembly process more
 * efficient.  The control client annotation processor generates a client
 * manifest documenting the set of used control types.  This manifest is a
 * java.util.Properties file that specifies:
 * 
 * - classname of the control client
 * - classnames of each control type used by that control client (the set
 *   identified by @Control and @ControlReference usages) and the
 *   corresponding default implementation binding
 *
 * Example client manifest:
 *
 * FooImpl.controls.properties
 * ---------------------------
 * .client.name=org.acme.controls.FooImpl
 * org.acme.controls.CustomerDbBean=org.apache.beehive.controls.scl.DatabaseControlImpl
 * org.acme.controls.DailyTimerBean=org.apache.beehive.controls.scl.TimerControlImpl
 *
 * The manifest is a generated artifact and is not user-editable.  Ideally, the apt
 * environment optimizes the writing of the manifest such that it's only written
 * to disk when changes occur (allowing external build tools to use the timestamp of
 * the manifest to determine whether assembly on a client needs to occur).
 */
public class ControlClientManifest
{
    public final static String CLIENT_NAME_PROP     = ".client.name";
    public final static String BEEHIVE_VERSION_PROP = ".beehive.version";
    public final static String FILE_EXTENSION       = ".controls.properties";

    /**
     * Loads a ControlClientManifest from an existing manifest file.
     * @param f the manifest file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public ControlClientManifest( File f ) throws FileNotFoundException, IOException
    {
        if ( !f.exists() )
            throw new FileNotFoundException( "Control manifest file=" + f + " not found");

        FileInputStream fis = new FileInputStream( f );
        _properties.load( fis );

        String client = _properties.getProperty( CLIENT_NAME_PROP );
        if ( client == null || client.equals("") )
            throw new IOException( "Control client manifest missing client name" );
    }

    /**
     * Creates a new ControlClientManifest
     * @param client the fully qualified classname of the control client
     */
    public ControlClientManifest( String client )
    {
        if ( client == null || client.equals("") )
            throw new RuntimeException( "Missing or empty client name" );

        _properties.setProperty( CLIENT_NAME_PROP, client );
    }

    /**
     * @return the name of the control client in this manifest
     */
    public String getControlClient()
    {
        return _properties.getProperty( CLIENT_NAME_PROP );
    }

    /**
     * Adds a new control type to the manifest
     * @param intf fully qualified name of the control type
     * @param impl fully qualified name of the default implementation for the control type
     */
    public void addControlType( String intf, String impl )
    {
        _properties.setProperty( intf, impl );
    }

    /**
     * @return a list of all control types listed in the manifest
     */
    public List<String> getControlTypes()
    {
        ArrayList<String> l = new ArrayList<String>();

        Set keys = _properties.keySet();
        for ( Object k : keys )
        {
            String propname = (String)k;
            if ( propname.equals( CLIENT_NAME_PROP ) )
                continue;

            l.add( propname );
        }

        return l;
    }

    /**
     * @param controlType
     * @return the default implementation for the control type listed in the manifest
     */
    public String getDefaultImpl( String controlType )
    {
        return (String)_properties.get( controlType );
    }

    /**
     * Emits the manifest via an apt Filer implementation
     * @param f an apt Filer
     * @param pkg the package structure to place the manifest in
     * @param mf the name of the manifest
     * @throws IOException
     */
    public void emit( Filer f, String pkg, File mf, String csn ) throws IOException
    {
        PrintWriter pw = f.createTextFile( Filer.Location.CLASS_TREE, pkg, mf, csn );

        pw.println( "# Apache Beehive Controls client manifest (auto-generated, do not edit!)");
        Set props = _properties.keySet();
        for ( Object p : props )
        {
            String name = (String)p;
            String value = _properties.getProperty(name);

            /* convert the name and value to a format excpected by the Properties.load() method */
            name = escapeJava(name);
            if (value != null)
                value = escapeJava(value);

            pw.println( name + "=" + value );
        }

        pw.flush();
        pw.close();
    }

    private static String escapeJava(String str) {
        if (str == null)
            return null;

        try {
            StringWriter writer = new StringWriter(str.length() * 2);
            escapeJavaStyleString(writer, str);
            return writer.toString();
        } catch (IOException ioe) {
            /* this should never ever happen while writing to a StringWriter */
            ioe.printStackTrace();
            return null;
        }
    }

    private static void escapeJavaStyleString(Writer out, String str)
        throws IOException {

        assert str != null : "Received a null string";
        assert out != null : "The writer must not be null";

        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            /* convert to unicode */
            if (ch > 0xfff)
                out.write("\\u" + hex(ch));
            else if (ch > 0xff) 
                out.write("\\u0" + hex(ch));
            else if (ch > 0x7f)
                out.write("\\u00" + hex(ch));
            else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.write('\\');
                        out.write('b');
                        break;
                    case '\n':
                        out.write('\\');
                        out.write('n');
                        break;
                    case '\t':
                        out.write('\\');
                        out.write('t');
                        break;
                    case '\f':
                        out.write('\\');
                        out.write('f');
                        break;
                    case '\r':
                        out.write('\\');
                        out.write('r');
                        break;
                    default :
                        if (ch > 0xf)
                            out.write("\\u00" + hex(ch));
                        else out.write("\\u000" + hex(ch));
                        break;
                }
            }
            else {
                switch (ch) {
                    case '\'':
                        out.write('\'');
                        break;
                    case '"':
                        out.write('\\');
                        out.write('"');
                        break;
                    case '\\':
                        out.write('\\');
                        out.write('\\');
                        break;
                    default :
                        out.write(ch);
                        break;
                }
            }
        }
    }

    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }

    private Properties _properties = new Properties();
}

