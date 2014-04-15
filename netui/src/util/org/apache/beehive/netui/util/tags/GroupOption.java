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
package org.apache.beehive.netui.util.tags;

/**
 * GroupOption is a simple JavaBean that can be used to fully specify
 * the name, value, alt text and accesskey of either a <code>CheckBoxGroup</code>
 * or a <code>RadioButtonGroup</code>.  The name will appear in the HTML after 
 * the checkbox or radio button.  The name attribute is required for outpu
 * by the tags.  The value can either be set separately or  will default to
 * the name.  Optionally and alt text attribute and accesskey value can be
 * provided.
 */
public class GroupOption
    implements java.io.Serializable
{
    private String _name;
    private String _value;
    private String _alt;
    private char _accessKey;


    /**
     * Default Constructor.
     */
    public GroupOption() {}

    /**
     * Construct a GroupOption setting the name.
     */
    public GroupOption(String name)
    {
        _name = name;
    }

    /**
     * Construct a GroupOption setting the name and value.
     */
    public GroupOption(String name,String value)
    {
        _name = name;
        _value = value;
    }

    /**
     * Construct a GroupOption setting all the values.
     */
    public GroupOption(String name,String value,String alt, char accessKey)
    {
        _name = name;
        _value = value;
        _alt = alt;
        _accessKey = accessKey;
    }

    /**
     * Set the name of the option which will appear next to the option.
     * @param name The name of the created option.
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Get the name of the option.
     * @return The name of the option that was set.
     */
    public String getName() {
        return _name;
    }

    /**
     * Set the value of the option.  Thie value will be written out as
     * the <code>value</code> attribute.
     * @param value The name of the created option.
     */
    public void setValue(String value) {
        _value = value;
    }

    /**
     * Get the value of the option.  If no value has been set
     * this method will return value assigned to the <code>name</code>
     * property.
     * @return The value of the option.
     */
    public String getValue() {
        return (_value != null) ? _value : _name;
    }


    /**
     * Set the alt text of the option.
     * @param alt The text that will be set for the <code>alt</code> attribute.
     */
    public void setAlt(String alt) {
        _alt = alt;
    }

    /**
     * Get the alt text of the option.
     * @return The alt text of the option.
     */
    public String getAlt() {
        return _alt;
    }

    /**
     * Set the alt text of the option.
     * @param accessKey The value that will be set for the
     * <code>accesskey</code> attribute.
     */
    public void setAccessKey(char accessKey) {
        _accessKey = accessKey;
    }

    /**
     * Get the accesskey of the option.
     * @return The accessKey of the option.
     */
    public char getAccessKey() {
        return _accessKey;
    }
}
