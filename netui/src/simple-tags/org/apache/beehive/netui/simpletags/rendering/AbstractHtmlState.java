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

import java.util.HashMap;
import java.util.Map;

/**
 * This class extends the <code>AbstractAttributeState</code> to add support for HTML.
 * This class add support for the <code>id</code>, <code>style</code>, and <code>class</code>
 * attributes.  In addition, there is a <code>Map</code> that supports the JavaScript
 * event attributes.
 */
public class AbstractHtmlState extends AbstractAttributeState
{
    /**
     * Define the Attribute Map for the JavaScript event handler attributes.
     */
    public static final int ATTR_JAVASCRIPT = 12;

    /**
     * The HTML <code>id</code> attribute.
     */
    public String id;

    /**
     * The HTML <code>style</code> attribute.
     */
    public String style;

    /**
     * The HTML <code>class</code> attribute.
     */
    public String styleClass;

    private HashMap _jsMap = null;       // Map used to hold the registered JavaScript attributes.

    /**
     * Return the Map Containing the JavaScript entries.
     * @return a <code>Map</code> of the JavaScript attributes.
     */
    public HashMap getEventMap()
    {
        return _jsMap;
    }

    /**
     * Initialize the state.
     */
    public void clear()
    {
        super.clear();

        if (_jsMap != null)
            _jsMap.clear();

        id = null;
        style = null;
        styleClass = null;
    }

    /**
     * This method will return the map that represents the passed in <code>type</code>.  The boolean flag
     * </code>createIfNull</code> indicates that the map should be created or not if it's null. This
     * class defines two maps defined by  <code>@see #ATTR_STYLE</code> and <code>ATTR_JAVASCRIPT</code>
     * @param type         <code>integer</code> type indentifying the map to be created.
     * @param createIfNull <code>boolean</code> flag indicating if the map should be created if it doesn't exist.
     * @return The map or null
     * @see #ATTR_JAVASCRIPT
     */
    public Map selectMap(int type, boolean createIfNull)
    {
        if (type == ATTR_JAVASCRIPT) {
            if (_jsMap == null && createIfNull)
                _jsMap = new HashMap();
            return _jsMap;
        }
        return super.selectMap(type, createIfNull);
    }
}
