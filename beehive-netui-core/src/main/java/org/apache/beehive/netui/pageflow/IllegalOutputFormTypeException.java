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
 * Exception that occurs when the first output form for a {@link Forward} resolves to a
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward &#64;Jpf.Forward} annotation whose
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward#outputFormBean outputFormBean} or
 * {@link org.apache.beehive.netui.pageflow.annotations.Jpf.Forward#outputFormBeanType outputFormBeanType}
 * attribute demands a different form type.
 * 
 * @see Forward#addOutputForm
 */ 
public class IllegalOutputFormTypeException extends IllegalOutputFormException
{
    private String _requiredType;
    
    
    /**
     * @param forwardName the name of the relevant {@link Forward}.
     * @param actionName the name of the current action being run.
     * @param flowController the current {@link FlowController} instance.
     * @param outputFormType the type name of the relevant output form.
     * @param requiredType the name of the required form type.
     */ 
    public IllegalOutputFormTypeException( String forwardName, String actionName, FlowController flowController,
                                           String outputFormType, String requiredType )
    {
        super( forwardName, actionName, flowController, outputFormType );
        _requiredType = requiredType;
    }

    /**
     * Get the name of the required form type.
     * 
     * @return a String that is the name of the required form type.
     */ 
    public String getRequiredType()
    {
        return _requiredType;
    }

    protected Object[] getMessageArgs()
    {
        return new Object[]{ getForwardName(), getActionName(), getFlowControllerURI(), getOutputFormType(),
                             _requiredType };
    }

    public String[] getMessageParts()
    {
        return new String[]
        {
            "The forward \"", "\" on action ", " in page flow ", " has a first output form of type ",
            ", but is declared to require type ", "."
        };
    }
}
