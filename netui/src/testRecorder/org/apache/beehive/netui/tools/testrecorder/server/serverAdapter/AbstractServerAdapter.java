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

import org.apache.beehive.netui.tools.testrecorder.shared.ResponseData;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * User: ozzy
 * Date: Jul 9, 2004
 * Time: 10:43:53 AM
 */
public abstract class AbstractServerAdapter implements ServerAdapter {

    /**
     * replace session ID with a non-unique string
     */
    public String replaceSessionID( String string ) {
        Matcher matcher = getSessionIdPattern().matcher( string );
        string = matcher.replaceAll( ResponseData.NON_UNIQUE_SESSION_ID );
        return string;

    }

    public abstract Pattern getSessionIdPattern();

}
