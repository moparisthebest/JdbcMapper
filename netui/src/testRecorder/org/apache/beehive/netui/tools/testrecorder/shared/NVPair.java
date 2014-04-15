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

/**
 */
public class NVPair {

    private String name;
    private String value;

    public NVPair( String name, String value ) {
        if ( name == null || value == null )
            throw new RuntimeException( "name( " + name + " ) and value( " + value + " ) must not be null." );

        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean equals( Object object ) {
        if ( this == object )
            return true;
        else if ( object == null || getClass() != object.getClass() )
            return false;

        NVPair other = (NVPair) object;
        if ( this.getName().equals( other.getName() ) && this.getValue().equals( other.getValue() ) )
            return true;
        else return false;
    }

    public String toString () {
        return "name( " + name + " ), value( " + value + " )";
    }
}
