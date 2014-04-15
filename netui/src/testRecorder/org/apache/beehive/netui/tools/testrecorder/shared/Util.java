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

package org.apache.beehive.netui.tools.testrecorder.shared;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public final class Util {

    private Util() {
    }

    public static List getListOfKeys( Map map ) {
        List keys = null;
        if ( map != null ) {
            Set keySet = map.keySet();
            if ( keySet.size() > 0 ) {
                keys = new ArrayList( keySet.size() );
                keys.addAll( keySet );
            }
        }
        if ( keys == null ) {
            keys = new ArrayList();
        }
        return keys;
    }

    public static String toString( Map map ) {
        Set keySet = null;
        StringBuffer sb = new StringBuffer();

        if ( map != null ) {
            keySet = map.keySet();
            sb.append( "Map(" + keySet.size() + ")[ " );
        }
        else {
            sb.append( "Map[ " );
        }

        if ( map != null ) {
            Iterator it = keySet.iterator();
            Object key = null;
            int i = 0;
            for ( i = 0; it.hasNext(); i++ ) {
                key = it.next();
                if ( i != 0 ) {
                    sb.append( "; " );
                }
                sb.append( "key(" + key + "), value( " + map.get( key ) + " )" );
            }
            if ( i == 0 ) {
                sb.append( "empty" );
            }
        }
        else {
            sb.append( "null" );
        }

        sb.append( " ]" );
        return sb.toString();
    }

    public static String toString( Object[] objects ) {
        if ( objects == null ) {
            return null;
        }
        StringBuffer sb = new StringBuffer( 16 * objects.length );
        sb.append( "[ " );
        int i = 0;
        for ( i = 0; i < objects.length; i++ ) {
            sb.append( ", [" + i + "]( " + objects[i] + " )" );
        }
        if ( i == 0 ) {
            sb.append( "EMPTY" );
        }
        sb.append( " ]" );
        return sb.toString();
    }

    public static String toString( Iterator it, int size ) {
        StringBuffer sb = new StringBuffer( 16 * size );
        sb.append( "[ " );
        int i = 0;
        for ( i = 0; it.hasNext(); i++ ) {
            sb.append( ", [" + i + "]( " + it.next() + " )" );
        }
        if ( i == 0 ) {
            sb.append( "EMPTY" );
        }
        sb.append( " ]" );
        return sb.toString();
    }
}
