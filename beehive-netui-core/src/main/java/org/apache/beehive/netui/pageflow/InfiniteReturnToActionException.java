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
 * Exception that occurs when the
 * <code>navigateTo={@link org.apache.beehive.netui.pageflow.annotations.Jpf.NavigateTo#previousAction previousAction}</code>
 * attribute is used on a {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward}, a
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.SimpleAction &#64;Jpf.SimpleAction}, or a
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.ConditionalForward &#64;Jpf.ConditionalForward},
 * but the previous action was the same as the current action (an infinite loop).
 */ 
public class InfiniteReturnToActionException extends PageFlowException
{
    public InfiniteReturnToActionException( String actionName, FlowController fc )
    {
        super( actionName, fc );
    }

    protected Object[] getMessageArgs()
    {
        return new Object[]{ getActionName(), getFlowControllerURI() };
    }

    protected String[] getMessageParts()
    {
        return new String[]
        {
            "Infinite loop of returnTo=\"action\" for action ",
            " in Page Flow ",
            "."
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
