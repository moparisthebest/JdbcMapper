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
package org.apache.beehive.controls.runtime.generator;

/**
 * A property derived from a getter/setter method of the control interface.
 */
public final class AptControlInterfaceProperty {
    private final String _name;
    private String _setterName;
    private String _getterName;

    /**
     * Constructs a new AptControlInterfaceProperty instance.
     *
     * @param name       Property name, may not be null.
     * @param getterName Getter method name, may be null.
     * @param setterName Setter method name, may be null.
     */
    public AptControlInterfaceProperty(String name, String getterName, String setterName) {
        assert name != null;
        _name = name;
        _getterName = getterName;
        _setterName = setterName;
    }

    /**
     * Set the setter method name.
     *
     * @param setterName
     */
    protected void setSetterName(String setterName) {
        _setterName = setterName;
    }

    /**
     * Set the getter method name.
     *
     * @param getterName
     */
    protected void setGetterName(String getterName) {
        _getterName = getterName;
    }

    /**
     * Get the setter method name.
     *
     * @return setter method name, may be null.
     */
    public String getSetterName() {
        return _setterName;
    }

    /**
     * Get the getter method name.
     *
     * @return getter method name, may be null.
     */
    public String getGetterName() {
        return _getterName;
    }

    /**
     * Get the property name.
     *
     * @return Property name.
     */
    public String getName() {
        return _name;
    }
}
