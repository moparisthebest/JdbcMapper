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
import java.util.Set;
import java.util.Collections;
import java.io.File;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;

/**
 * User: ozzy
 * Date: Apr 13, 2004
 * Time: 4:19:09 PM
 */
public class Config {

    private static final Logger log = Logger.getInstance( Config.class );

    private Map suffixMap;
    private String servletURI;
    private File baseDirectory;

    public Config( List suffixList, String servletURI, String baseDirPath ) {
        this( (String[]) suffixList.toArray( new String[suffixList.size()] ), servletURI, baseDirPath );
    }

    public Config( String[] suffixList, String servletURI, String baseDirPath ) {
        this.servletURI = servletURI;
        suffixMap = new HashMap();
        baseDirectory = new File( baseDirPath );
        init( suffixList );
    }

    private void init( String[] suffixList ) {
        String suffix = null;
        if ( suffixList != null ) {
            for ( int i = 0; i < suffixList.length; i++ ) {
                suffix = suffixList[i];
                if ( log.isDebugEnabled() ) {
                    log.debug( "suffix( " + suffix + " )" );
                }
                suffixMap.put( suffix, suffix );
            }
        }
    }

    public boolean handleSuffix( String suffix ) {
        return suffixMap.containsKey( suffix );
    }

    // this URI specifies the portion to the right of the context root, not including the root.
    public String getServletURI() {
        return servletURI;
    }

    public Set getSuffixes() {
        return Collections.unmodifiableSet( getSuffixMap().keySet() );
    }

    public File getBaseDirectory() {
        return baseDirectory;
    }

    public boolean createBaseDirectory() {
        if ( baseDirectory.exists() ) {
            return true;
        }
        return baseDirectory.mkdirs();
    }

    private Map getSuffixMap() {
        return suffixMap;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );
        sb.append( "servletURI( " + getServletURI() + " )" );
        sb.append( ", suffixList( " + StringHelper.toString( getSuffixes().iterator(), "\n", "\n\t" ) + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
