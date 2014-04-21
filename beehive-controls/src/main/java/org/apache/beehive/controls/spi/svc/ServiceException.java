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
 * The ServiceException class declares an checked exception that is thrown by the Service API
 * runtime under certain failure conditions.
 */
public class ServiceException
    extends Exception
{
    /**
	 * Constructs a ServiceException object with the specified String as a message.
	 * 
	 * @param message The message to use.
	 */
    public ServiceException(String message)
    {
        super(message);
    }

	/**
	 * Constructs a ServiceException object using the specified String as a message, and the 
     * specified Throwable as a nested exception.
	 * 
	 * @param message The message to use.
	 * @param t The exception to nest within this 
	 * exception.
	 */
    public ServiceException(String message, Throwable t)
    {
        super(message + "[" + t.getMessage() + "]", t);
    }

	/* Private Constant(s) */
    private static final long serialVersionUID = 8818197331269164527L;
}
