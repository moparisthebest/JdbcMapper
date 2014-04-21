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
package org.apache.beehive.netui.tags;

import javax.servlet.jsp.JspException;


/**
 * This interface defines the HTML 4.0 core attributes.  The following properties represent the core attributes:
 * id = tagId, class = styleClass, style = style, title = title.  This is a marker interface on the tags
 * ensuring that tags representing core HTML elements have the defined property set.
 */
public interface IHtmlCore
{

    /**
     * Return the ID of the tag.  The id may be rewritten by the container (such
     * as a portal) to make sure it is unique.  JavaScript may lookup the actual id
     * of the element by looking it up in the <code>netui_names</code> table written
     * into the HTML.
     * @return the tagId.
     */
    //String getTagId();

    /**
     * Set the ID of the tag.
     * @param tagId - the tagId.
     */
    void setTagId(String tagId)
            throws JspException;

    /**
     * Returns the Nodes title.
     * @return
     */
    //String getTitle();

    /**
     * Sets the Nodes title.
     * @param title
     */
    void setTitle(String title);

    /**
     * Sets the style of the rendered html tag.
     * @param style - the html style.
     */
    void setStyle(String style);

    /**
     * Gets the style of the rendered html tag.
     * @return the style.
     */
    //String getStyle();

    /**
     * Sets the style class of the rendered html tag.
     * @param styleClass - the html style class.
     */
    void setStyleClass(String styleClass);

    /**
     * Gets the style class of the rendered html tag.
     * @return the style class.
     */
    //String getStyleClass();
}
