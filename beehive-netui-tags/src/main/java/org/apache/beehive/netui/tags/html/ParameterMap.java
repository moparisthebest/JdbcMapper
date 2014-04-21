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
package org.apache.beehive.netui.tags.html;

// java imports

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import java.util.Iterator;
import java.util.Map;

/**
 * Writes each in a map of URL parameters to a URL on its parent tag.
 * The parent tag must implement IUrlParams.
 * @jsptagref.tagdescription <p>Writes a group of name/value pairs to the URL or the parent tag.
 *
 * <p>The &lt;netui:parameterMap> can be nested inside of the
 * {@link org.apache.beehive.netui.tags.html.Anchor},
 * {@link org.apache.beehive.netui.tags.html.Button},
 * {@link org.apache.beehive.netui.tags.html.Form}, and
 * {@link org.apache.beehive.netui.tags.html.Image} tags.
 *
 * <p>You can dynamically determine the value of the &lt;netui:parameterMap> tag by pointing
 * the <code>map</code> attribute at a {@link java.util.HashMap java.util.HashMap} object.
 * @example Assume that there is a java.util.HashMap
 * object in the Controller file.
 *
 * <pre>      public HashMap hashMap = new HashMap();
 *      hashMap.put("q", "Socrates");
 *      hashMap.put("lr", "lang_el");
 *      hashMap.put("as_qdr", "m3");</pre>
 *
 * <p>The following set of tags will read the HashMap object and generate a
 * link with a set of URL parameters.
 *
 * <pre>      &lt;netui:anchor href="http://www.google.com/search">
 *          Search Greek language web sites updated in the last three months with the query "Socrates".
 *          &lt;netui:parameterMap map="${pageFlow.hashMap}"/>
 *      &lt;/netui:anchor></pre>
 *
 * <p>The URL produced appears as follows:
 *
 * <pre>      http://www.google.com/search?lr=lang_el&q=Socrates&as_qdr=m3</pre>
 * @netui:tag name="parameterMap" description="Uses a JSP 2.0 expression that points to a map of parameters. Each entry in the map provides a URL parameter that will be added to the parent tag's URL."
 */
public class ParameterMap
        extends AbstractClassicTag
{
    private Map _map = null;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "ParameterMap";
    }

    /**
     * Sets the map expression.
     * @param map the map expression.
     * @jsptagref.attributedescription A data binding expression pointing to a {@link java.util.Map java.util.Map} of parameters.
     * The expression can point at any implementation of the java.util.Map interface,
     * including {@link java.util.AbstractMap java.util.AbstractMap},
     * {@link java.util.HashMap java.util.HashMap},
     * {@link java.util.Hashtable java.util.Hashtable}, etc.
     * @jsptagref.databindable Read Only
     * @jsptagref.attributesyntaxvalue <i>string_mapObject</i>
     * @netui:attribute required="true" rtexprvalue="true" type="java.util.Map"
     * description="A JSP 2.0 EL expression pointing to a java.util.Map of parameters."
     */
    public void setMap(Map map) throws JspException
    {
        if (map == null) {
            String s = Bundle.getString("Tags_MapAttrValueRequired", new Object[]{"map"});
            registerTagError(s, null);
        }
        _map = map;
    }

    /**
     * Add each parameter in the URL parameter map to the Parameter's parent.
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        if (hasErrors())
            return reportAndExit(SKIP_BODY);

        Tag parentTag = findAncestorWithClass(this, IUrlParams.class);
        if (parentTag != null) {
            // this map shouldn't be null because the attribute is required.
            assert(_map != null);
            IUrlParams parent = (IUrlParams) parentTag;
            Iterator it = _map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry key = (Map.Entry) it.next();
                parent.addParameter(key.getKey().toString(), key.getValue(), null);
            }
        }
        else {
            String msg = Bundle.getString("Tags_InvalidParameterMapParent");
            registerTagError(msg, null);
            reportErrors();
        }

        localRelease();
        return SKIP_BODY;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();
        _map = null;
    }
}
