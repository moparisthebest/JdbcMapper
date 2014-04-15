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
package org.apache.beehive.netui.simpletags.rendering;

import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractAttributeState extends AbstractTagState
{
    private static final Logger logger = Logger.getInstance(AbstractAttributeState.class);

    /**
     * String constant for the empty string.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The integer type constant indentifying the General Attributes  <code>AbstractBaseTag</code>
     * reserves 0-9 for indentification.
     */
    public static final int ATTR_GENERAL = 0;

    private HashMap generalMap = null;              // the map of general attributes

    public HashMap getGeneralAttributeMap()
    {
        return generalMap;
    }

    /**
     * This method will return the map that represents the passed in <code>type</code>.  The boolean flag
     * </code>createIfNull</code> indicates that the map should be created or not if it's null. This
     * class defines two maps defined by  <code>@see #ATTR_GENERAL</code> and <code>ATTR_GENERAL_EXPRESSION</code>
     * @param type         <code>integer</code> type indentifying the map to be created.
     * @param createIfNull <code>boolean</code> flag indicating if the map should be created if it doesn't exist.
     * @return The map or null
     * @see #ATTR_GENERAL
     */
    public Map selectMap(int type, boolean createIfNull)
    {
        Map ret = null;
        if (type == ATTR_GENERAL) {
            if (generalMap == null && createIfNull)
                generalMap = new HashMap();
            ret = generalMap;
        }

        return ret;
    }

    public void clear()
    {
        if (generalMap != null)
            generalMap.clear();
    }

    /**
     * Register a name/value pair into a named attribute map.  The base type
     * supports the <code>ATTR_GENERAL<code> named map.  Subclasses may add additional maps
     * enabling attributes to be treated with different behavior.
     * @param type     an integer key identifying the map.
     * @param attrName the name of the attribute
     * @param value    the value of the attribute
     */
    public void registerAttribute(int type, String attrName, String value, boolean ignoreEmpty)
    {
        assert(attrName != null);
        
        // If the value is null or the empty string ignore the expression.
        if (value == null)
            return;
        if (ignoreEmpty && "".equals(value))
            return;

        Map map = selectMap(type, true);
        if (map == null) {
            String s = Bundle.getString("Tags_ParameterAccessError",
                    new Object[]{new Integer(type), attrName});
            logger.error(s);
            return;
        }
        map.put(attrName, value);
    }

    public void registerAttribute(int type, String attrName, String value)
    {
        registerAttribute(type, attrName, value, true);
    }

    /**
     * Remove a previously registered attribute value from map.
     * @param type     an integer key indentifying the map
     * @param attrName the name of the attribute to remove from the specified map
     */
    public void removeAttribute(int type, String attrName)
    {
        Map map = selectMap(type, false);
        if (map == null) {
            String s = Bundle.getString("Tags_ParameterAccessError",
                    new Object[]{new Integer(type), attrName});
            logger.error(s);
            return;
        }

        map.remove(attrName);
    }

    /**
     * Return a named attribute value from the specified attribute map.
     * @param type     an integer key indentifying the map
     * @param attrName the name of the attribute we will get the value from.
     * @return a string value of the attribute if set or null.
     */
    public String getAttribute(int type, String attrName)
    {
        Map map = selectMap(type, false);
        if (map == null)
            return null;
        return (String) map.get(attrName);
    }
}
