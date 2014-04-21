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
 * Exception that occurs in iterative development mode when an action output attached to a {@link Forward} is of a
 * different type than was declared.
 */ 
public class MismatchedActionOutputException extends PageFlowException
{
    private String _actionOutputName;
    private String _expectedType;
    private String _actualType;
    private String _forwardName;

    public MismatchedActionOutputException( String actionName, FlowController flowController, String actionOutputName,
                                            String forwardName, String expectedType, String actualType )
    {
        super( actionName, flowController );
        _actionOutputName = actionOutputName;
        _expectedType = expectedType;
        _actualType = actualType;
        _forwardName = forwardName;
    }

    protected Object[] getMessageArgs()
    {
        return new Object[]{ _actionOutputName, _forwardName, getActionName(), getFlowControllerURI(), _actualType,
                             _expectedType };
    }

    protected String[] getMessageParts()
    {
        return new String[]{ "The action output \"", "\" on forward \"", "\" (action ", " in Page Flow ", ") is of type ",
                             ", but was declared to expect type ", "." };
    }

    public String getActionOutputName()
    {
        return _actionOutputName;
    }

    public String getForwardName()
    {
        return _forwardName;
    }

    public String getExpectedType()
    {
        return _expectedType;
    }

    public String getActualType()
    {
        return _actualType;
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
