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

import org.apache.beehive.netui.tools.testrecorder.server.FilterData;
import org.apache.beehive.netui.tools.testrecorder.server.DefaultFilterData;
import org.apache.beehive.netui.tools.testrecorder.server.state.State;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.regex.Pattern;

/**
 * User: ozzy
 * Date: Oct 23, 2003
 * Time: 10:46:55 AM
 */
public class DefaultServerAdapter extends AbstractServerAdapter {

    protected static final Pattern sessIdPattern =
            Pattern.compile( ";jsessionid=.[a-zA-Z0-9]+" );

    public FilterData genFilterDataInstance( ServletRequest request, ServletResponse response, State state ) {
        return new DefaultFilterData( request, response, this );
    }

    public DefaultServerAdapter() {
    }

    public Pattern getSessionIdPattern() {
        return sessIdPattern;
    }
}
