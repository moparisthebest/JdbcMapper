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
package org.apache.beehive.netui.pageflow;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Exception thrown when:
 *     <ul>
 *         <li>
 *             An action ({@link org.apache.beehive.netui.pageflow.annotations.Jpf.Action &#64;Jpf.Action}
 *             or {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction})
 *             marked with <code>loginRequired=true</code> is hit when there is no logged-in user, or,
 *         </li>
 *         <li>
 *             An action marked with <code>rolesAllowed="</code><i>list of roles</i><code>"</code> is hit when there is
 *             no logged-in user.
 *         </li>
 *     </ul>
 * If the requested session-ID is different than the current session-ID, the {@link LoginExpiredException}
 * will be thrown instead of the <code>NotLoggedInException</code>.
 */ 
public class NotLoggedInException
        extends PageFlowException
        implements ResponseErrorCodeSender
{
    public NotLoggedInException( String actionName, FlowController fc )
    {
        super( actionName, fc );
    }

    protected Object[] getMessageArgs()
    {
        return new Object[]{ getActionName(), getFlowControllerURI() };
    }

    public String[] getMessageParts()
    {
        return new String[]
        {
            "Action ", " on page flow ", " requires a current user, but there is no logged-in user."
        };
    }

    public void sendResponseErrorCode( HttpServletResponse response ) throws IOException
    {
        response.sendError( HttpServletResponse.SC_BAD_REQUEST, getLocalizedMessage() );
    }

    /**
     * Tell whether the root cause may be session expiration in cases where the requested session ID is different than
     * the actual session ID.  In this case, the answer is <code>true</code>.
     */ 
    public boolean causeMayBeSessionExpiration()
    {
        return true;
    }
}
