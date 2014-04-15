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
 * Exception thrown by {@link Forward} when its name does not resolve to one defined by a
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward} annotation in the current action's
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Action &#64;Jpf.Action} annotation.
 */ 
public class UnresolvableForwardException extends PageFlowException
{
    private String _forwardName;

    /**
     * Constructor.
     * 
     * @param forwardName the name of the unresolvable {@link Forward}.
     */ 
    public UnresolvableForwardException( String forwardName, String actionName, FlowController fc )
    {
        super( actionName, fc );
        _forwardName = forwardName;
    }

    /**
     * Get the name of the unresolvable {@link Forward}.
     * 
     * @return a String that is the name of the unresolvable {@link Forward}.
     */ 
    public String getForwardName()
    {
        return _forwardName;
    }

    /**
     * Set the name of the unresolvable {@link Forward}.
     * 
     * @param forwardName a String that is the name of the unresolvable {@link Forward}.
     */ 
    public void setForwardName( String forwardName )
    {
        _forwardName = forwardName;
    }

    protected Object[] getMessageArgs()
    {
        return new Object[]{ _forwardName, getActionName(), getFlowControllerURI() };
    }

    public String[] getMessageParts()
    {
        return new String[]
        {
            "Unable to find a forward named \"", "\" on action ", " in Page Flow ", "."
        };
    }

    /**
     * Tell whether the root cause may be session expiration in cases where the requested session ID is different than
     * the actual session ID.  In this case, the answer is <code>false</code>.
     */ 
    public boolean causeMayBeSessionExpiration()
    {
        return false;
    }
}
