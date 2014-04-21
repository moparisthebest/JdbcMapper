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
package org.apache.beehive.netui.databinding.datagrid.api.exceptions;

/**
 * Exception thrown when a {@link org.apache.beehive.netui.databinding.datagrid.api.filter.Filter}
 * object is invalid while being manipulated by the data grid framework.
 */
public class IllegalFilterException
        extends RuntimeException {

    /**
     * Default constructor
     */
    public IllegalFilterException() {
        super();
    }

    /**
     * Constructor with cause message
     * @param message
     */
    public IllegalFilterException(String message) {
        super(message);
    }

    /**
     * Constructor with cause
     * @param cause
     */
    public IllegalFilterException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with cause and message
     * @param message
     * @param cause
     */
    public IllegalFilterException(String message, Throwable cause) {
        super(message, cause);
    }
}
