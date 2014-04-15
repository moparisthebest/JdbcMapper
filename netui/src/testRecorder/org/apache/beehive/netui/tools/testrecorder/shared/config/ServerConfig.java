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

/**
 * User: ozzy
 * Date: Apr 15, 2004
 * Time: 3:08:44 PM
 */
public class ServerConfig {

    private String name;
    private String hostname;
    private int port;

    public ServerConfig( String name, String hostname, int port ) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 48 );
        sb.append( "[ " );
        sb.append( "name( " + getName() + " )" );
        sb.append( ", hostname( " + getHostname() + " )" );
        sb.append( ", port( " + getPort() + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
