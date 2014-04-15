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

package org.apache.beehive.netui.tools.testrecorder.server.serverAdapter;

import org.apache.beehive.netui.tools.testrecorder.shared.Logger;

/**
 * This is basically a Factory for creating a Service Adapter.
 */
public class ServerAdapterUtils {

    private static final Logger log = Logger.getInstance( ServerAdapterUtils.class );

    private static final String SERVER_ADAPTER_PROP = "testRecorder.serveradapter";
    private static final String CATALINA_HOME_PROP = "catalina.home";
    private static final String WL_SERVER_ADAPTER_CLASS =
            "org.apache.beehive.netui.tools.testrecorder.server.serverAdapter.WeblogicServerAdapter";
    private static final String TOMCAT_SERVER_ADAPTER_CLASS =
            "org.apache.beehive.netui.tools.testrecorder.server.serverAdapter.DefaultServerAdapter";

    private static ServerAdapter SERVER_ADAPTER = createServerAdapter();

    /**
     * prevent the creation of the ServerAdapterUtils.  This is only accessed statically.
     */
    private ServerAdapterUtils() {}

    /**
     * Return the current ServerAdaptor.
     * @return
     */
    public static final ServerAdapter getServerAdapter() {
        return SERVER_ADAPTER;
    }

    // this will create the ServerAdapter.
    private static ServerAdapter createServerAdapter() {
        String serverAdapterClassName = System.getProperty( SERVER_ADAPTER_PROP );
        if ( serverAdapterClassName == null ) {
            if ( System.getProperty( CATALINA_HOME_PROP ) != null ) {
                serverAdapterClassName = TOMCAT_SERVER_ADAPTER_CLASS;
            }
            else {
                serverAdapterClassName = WL_SERVER_ADAPTER_CLASS;
            }
        }
        ServerAdapter serverAdapter = null;
        try {
            Class serverAdapterClass = Class.forName( serverAdapterClassName );
            serverAdapter = (ServerAdapter) serverAdapterClass.newInstance();
        }
        catch ( ClassNotFoundException e ) {
            String msg = "WARNING: Could not find ServerAdapter class( " +
                    serverAdapterClassName + " ) exception( " + e.getMessage() + " )" +
                    ", using " + DefaultServerAdapter.class.getName() + ".";
            System.out.println( msg );
            if ( log.isWarnEnabled() ) {
                log.warn( msg, e );
            }
            serverAdapter = new DefaultServerAdapter();
        }
        catch ( Exception e ) {
            String msg = "WARNING: failed to instantiate ServerAdapter class( " +
                    serverAdapterClassName + " ) exception( " + e.getMessage() + " )" +
                    ", using " + DefaultServerAdapter.class.getName() + ".";
            System.out.println( msg );
            if ( log.isWarnEnabled() ) {
                log.warn( msg, e );
            }
            serverAdapter = new DefaultServerAdapter();
        }
        if ( log.isInfoEnabled() ) {
            log.info( "Created server adapter of type( " +
                    serverAdapter.getClass().getName() + " )" );
        }
        return serverAdapter;
    }

}
