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

/**
 * Exception thrown in place of another {@link PageFlowException} when:
 *     <ul>
 *         <li>The requested session ID is different than the current session ID (or there is no current session), and</li>
 *         <li>the original exception to be thrown returns <code>true</code> for
 *             {@link PageFlowException#causeMayBeSessionExpiration}, and</li>
 *         <li>The <code>&lt;throw-session-expired-exception&gt;</code> element in WEB-INF/beehive-netui-config.xml is
 *             set to <code>true</code> (the default)</li>.
 *     </ul>
 * 
 * When this exception is thrown, the original exception (considered to be a secondary effect of the session expiration)
 * can be obtained through {@link #getEffect()}.
 */ 
public class SessionExpiredException
        extends PageFlowException
{
    private PageFlowException _effect;
    
    public SessionExpiredException( PageFlowException effect )
    {
        super( effect.getActionName(), effect.getFlowController() );
        _effect = effect;
    }

    protected Object[] getMessageArgs()
    {
        return new Object[]{ getActionName(), getFlowControllerURI() };
    }

    protected String[] getMessageParts()
    {
        return new String[]
        {
            "Action ", " on page flow ", " cannot be completed because the user session has expired."
        };
    }
    
    /**
     * Get the effect of the session expiration; this is the exception that was most likely caused by the session
     * expiring.
     */ 
    public Throwable getEffect()
    {
        return _effect;
    }

    /**
     * Tell whether the root cause may be session expiration in cases where the requested session ID is different than
     * the actual session ID.  In this case, the answer is <code>true</code> (since this is the exception that is thrown
     * in for session expiration).
     */ 
    public boolean causeMayBeSessionExpiration()
    {
        return false;
    }
}
