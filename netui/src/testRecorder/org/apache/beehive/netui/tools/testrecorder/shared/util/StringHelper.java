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

package org.apache.beehive.netui.tools.testrecorder.shared.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 *
 */
public class StringHelper {

    public static String toString( Object[] objects ) {
        return toString( objects, null, null );
    }

    public static String toString( List list, String prefix, String delimiter ) {
        return toString( list.toArray(), prefix, delimiter );
    }

    public static String toString( Object[] objects, String prefix, String delimiter ) {
        if ( objects == null ) {
            return "NULL";
        }
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );

        if ( objects != null ) {
            int i = 0;
            Object object = null;
            Object[] array = null;
            for ( i = 0; i < objects.length; i++ ) {
                object = objects[i];
                try {
                    array = (Object[]) object;
                }
                catch (ClassCastException e) {
                }
                if ( array != null ) {
                    if ( prefix != null ) {
                        sb.append( prefix );
                    }
                    sb.append( "(" + i + ") [" + toString( array, null, " " ) + "]" );
                    if ( delimiter != null ) {
                        sb.append( delimiter );
                    }
                    array = null;
                }
                else {
                    if ( prefix != null ) {
                        sb.append( prefix );
                    }
                    sb.append( "(" + i + ") [" + object + "]" );
                    if ( delimiter != null ) {
                        sb.append( delimiter );
                    }
                }
            }
            if ( i == 0 ) {
                sb.append( "EMPTY" );
            }
        }
        else {
            sb.append( objects );
        }

        sb.append( " ]" );
        return sb.toString();
    }

    public static String toString( Iterator it, String prefix, String delimiter ) {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );

        int i = 0;
        Object object = null;
        Object[] array = null;
        for ( i = 0; it.hasNext(); i++ ) {
            object = it.next();
            try {
                array = (Object[]) object;
            }
            catch (ClassCastException e) {
            }
            if ( array != null ) {
                if ( prefix != null ) {
                    sb.append( prefix );
                }
                sb.append( "(" + i + ") [" + toString( array, null, " " ) + "]" );
                if ( delimiter != null ) {
                    sb.append( delimiter );
                }
                array = null;
            }
            else {
                if ( prefix != null ) {
                    sb.append( prefix );
                }
                sb.append( "(" + i + ") [" + object + "]" );
                if ( delimiter != null ) {
                    sb.append( delimiter );
                }
            }
        }
        if ( i == 0 ) {
            sb.append( "EMPTY" );
        }

        sb.append( " ]" );
        return sb.toString();
    }

    public static String toDelimitedString( Object[] objects, String delimiter ) {
        StringBuffer sb = new StringBuffer( 256 );
        if ( objects != null ) {
            int i = 0;
            for ( i = 0; i < objects.length; i++ ) {
                if ( i != 0 && delimiter != null ) {
                    sb.append( delimiter );
                }
                sb.append( objects[i] );
            }
        }
        return sb.toString();
    }

    public static String toString( Set set ) {
        Iterator it = set.iterator();
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );

        Object value = null;
        for ( int i = 0; it.hasNext(); i++ ) {
            value = it.next();
            if ( i == 0 ) {
                sb.append( "value(" + i + ")( " + value + " )" );
            }
            else {
                sb.append( ", value(" + i + ")( " + value + " )" );
            }
        }

        sb.append( " ]" );
        return sb.toString();
    }

    public static String toString( Map map ) {
        Iterator it = map.keySet().iterator();
        return toString( it, map );
    }

    public static String toString( Map map, String delimiter ) {
        if ( map == null ) {
            return "NULL";
        }
        Iterator it = map.keySet().iterator();
        return toString( it, map, delimiter );
    }

    public static String toString( Iterator it, Map map ) {
        return toString( it, map, null );
    }

    public static String toString( Iterator it, Map map, String delimiter ) {
        StringBuffer sb = new StringBuffer( 256 );
        sb.append( "[ " );

        Object key = null;
        Object value = null;
        for ( int i = 0; it.hasNext(); i++ ) {
            key = it.next();
            value = map.get( key );
            if ( i == 0 ) {
                sb.append( "key( " + key + " ), value( " + value + " )" );
            }
            else {
                sb.append( ", key( " + key + " ), value( " + value + " )" );
            }
            if ( delimiter != null ) {
                sb.append( delimiter );
            }
        }

        sb.append( " ]" );
        return sb.toString();
    }

    public static StringBuffer getStringFromList( List list ) {
        return getStringFromList( new StringBuffer( 64 * list.size() ), list );
    }

    public static StringBuffer getStringFromList( StringBuffer sb, List list ) {
        for ( int i = 0; i < list.size(); i++ ) {
            sb.append( list.get( i ) );
        }
        return sb;
    }

}
