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
package org.apache.beehive.netui.tags.rendering;

import java.util.HashMap;

abstract public class TagHtmlBase extends TagRenderingBase
{
    /**
     * Render all of the attributes defined in a map and return the string value.  The attributes
     * are rendered with in a name="value" style supported by XML.
     * @param type an integer key indentifying the map
     */
    protected void renderAttributes(int type, AbstractRenderAppender sb, AbstractAttributeState state, boolean doubleQuote)
    {
        HashMap map = null;
        switch (type) {
            case AbstractHtmlState.ATTR_JAVASCRIPT:
                assert(state instanceof AbstractHtmlState) : "ATTR_JAVASCRIPT requires a AbstractHtmlState instance";
                AbstractHtmlState htmlState = (AbstractHtmlState) state;
                map = htmlState.getEventMap();
                break;
            default:
                super.renderAttributes(type, sb, state, doubleQuote);
                return;
        }
        renderGeneral(map, sb, doubleQuote);
    }
}
