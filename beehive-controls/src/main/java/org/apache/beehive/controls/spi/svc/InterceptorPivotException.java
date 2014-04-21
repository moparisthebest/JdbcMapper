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
package org.apache.beehive.controls.spi.svc;

/**
 * The InterceptorPivotException class declares a checked exception that is thrown by 
 * an Interceptor upon pivoting.  For example, if an interceptor wishes to stop a method
 * from executing further and return a value, it can throw this exception and embed in 
 * the exception the return value that it wishes the method to return. 
 */
public class InterceptorPivotException
    extends Exception
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    private Object returnValue;
    private String interceptorName;
    
    /**
	 * Constructs a InterceptorPivotException object with the specified interceptor.
	 * 
	 * @param interceptorName name of the interceptor that generated this exception
	 */
    public InterceptorPivotException(String interceptorName)
    {
        super();
        this.interceptorName = interceptorName;
    }

    /**
	 * Constructs a InterceptorPivotException object with the specified interceptor
	 * and return value for the method that is intercepted.
	 * 
	 * @param interceptorName name of the interceptor that generated this exception
	 * @param returnValue the return value of the method that is intercepted.
	 */
    public InterceptorPivotException(String interceptorName, Object returnValue)
    {
        super();
        this.interceptorName = interceptorName;
        this.returnValue = returnValue;
    }
    
	/**
	 * Constructs a ServiceException object using the specified interceptor, the 
	 * return value for the method that is intercepted and message.
	 * 
	 * @param interceptorName name of the interceptor that generated this exception
	 * @param returnValue the return value of the method that is intercepted.
	 * @param message The message to use.
	 */
    public InterceptorPivotException(String interceptorName, Object returnValue, String message)
    {
        super(message);
        this.interceptorName = interceptorName;
        this.returnValue = returnValue;
    }

	/**
	 * Constructs a ServiceException object using the specified interceptor and 
	 * a message.
	 * 
	 * @param interceptorName name of the interceptor that generated this exception
	 * @param message The message to use.
	 */
    public InterceptorPivotException(String interceptorName, String message)
    {
        super(message);
        this.interceptorName = interceptorName;
    }

    /**
     * @return Returns the interceptorName.
     */
    public String getInterceptorName()
    {
        return interceptorName;
    }
    /**
     * @return Returns the returnValue.
     */
    public Object getReturnValue()
    {
        return returnValue;
    }
    /**
     * @param interceptorName The interceptorName to set.
     */
    public void setInterceptorName(String interceptorName)
    {
        this.interceptorName = interceptorName;
    }
}
