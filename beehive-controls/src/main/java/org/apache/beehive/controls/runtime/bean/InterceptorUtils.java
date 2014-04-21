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
package org.apache.beehive.controls.runtime.bean;

import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Class used to support prioritizing interceptors on methods of a {@link ControlBean}.
 */
public final class InterceptorUtils {

    private InterceptorUtils() {}
    
    /**
     * Filename that contains ordering priority for controls interceptor services.
     * Each line in the file is a fully qualified interface name.  The first line in the file
     * is highest priority.
     */
    public static final String INTERCEPTOR_CONFIG_FILE = "controls-interceptors.config";

    // todo: this interceptor priority list should be stored by ClassLoader instead of shared between them
    /**
     * List that keeps track of the interceptors in their priority order.
     */
    private static ArrayList<String> _interceptorPriorities;

    /**
     * Applies externally defined (via {@link #INTERCEPTOR_CONFIG_FILE}) ordering priority for
     * controls interceptor services.
     *
     * @param interceptors
     * @return String[]
     */
    public static String[] prioritizeInterceptors( String [] interceptors )
    {
        if ( interceptors == null )
            return null;

        // Read external configuration to obtain desired prioritization.
        if ( _interceptorPriorities == null )
        {
            // Only attempt to read the external configuration once; bounce the VM if you
            // want to try again.
            _interceptorPriorities = new ArrayList<String>();
            BufferedReader in = null;
            try
            {
                InputStream configFileStream =
                    ControlBeanContext.class.getClassLoader().getResourceAsStream( INTERCEPTOR_CONFIG_FILE );

                if ( configFileStream != null )
                {
                    in = new BufferedReader(new InputStreamReader(configFileStream));
                    String str;
                    while ((str = in.readLine()) != null)
                        _interceptorPriorities.add(str);
                }
            }
            catch (IOException e)
            {
                // ignore
            }
            finally
            {
                try {
                    if (in != null)
                        in.close();
                }
                catch ( IOException ie ) { /* ignore */ }
            }
        }

        // Put input list of interceptors into a Set for easy lookup
        Set<String> input = new HashSet<String>();
        for ( String ii : interceptors )
            input.add( ii );

        // Scan through priorities list, building a prioritized list
        ArrayList<String> prioritized = new ArrayList<String>(interceptors.length);
        for ( String p : _interceptorPriorities )
        {
            if ( input.contains(p) )
            {
                input.remove(p);
                prioritized.add(p);
            }
        }

        // Anything still left in the input set did not have a priority associated with it,
        // so they just go at the end in arbitrary order.
        for ( String p : input )
            prioritized.add(p);

        return prioritized.toArray(new String[prioritized.size()]);
    }
}
