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
package org.apache.beehive.netui.tags.databinding.invoke;

/**
 * An exception thrown when an object on which to invoke a method can not
 * be found.  The {@link CallMethod#resolveObject()} call throws this exception.
 */
public class ObjectNotFoundException
        extends Exception {

    private String _objectName = null;

    /**
     * Construct an ObjectNotFoundException.
     */
    public ObjectNotFoundException() {
        super();
    }

    /**
     * Construct an ObjectNotFoundException with the given message.
     *
     * @param message a String containing the text of the exception message
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }

    /**
     * Construct an ObjectNotFoundException with the given cause
     *
     * @param cause a <code>Throwable</code> that interfered with the normal lookup of an object.
     */
    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct an ObjectNotFoundException with the given <code>message</code> and <code>cause</code>.
     *
     * @param message a String containing the text of the exception message
     * @param cause   a <code>Throwable</code> that interfered with the normal lookup of an object.
     */
    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct an ObjectNotFoundException with the given <code>message</code>, <code>cause</code>, and <code>objectName</code>.
     *
     * @param message    a String containing the text of the exception message
     * @param cause      a <code>Throwable</code> that interfered with the normal lookup of an object.
     * @param objectName the identifier of the object which could not be looked-up.
     */
    public ObjectNotFoundException(String message, Throwable cause, String objectName) {
        super(message, cause);
        _objectName = objectName;
    }

    /**
     * Get the name of the object whose lookup failed.
     *
     * @return the String name
     */
    public String getObjectName() {
        return _objectName;
    }
}
