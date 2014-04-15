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
 * This interfaces is implemented by tags that allow attributes to be set
 * externally to the tag.  The HTML tags for example, allow certain attributes to
 * be set by children tags through the use of the <code>attribute</code> tag.
 */
public interface IAttributeConsumer
{
    /**
     * Set an attribute value on the implementing class.  The <code>name</code> represents
     * the name of the attribute.  The <code>value</code> represents the value and may contains
     * an expression.  The <code>facet</code> is optional and may be used by complex types to
     * target the attribute to a sub part of the generated markup. This method may result in errors
     * being generated.
     * @param name  The name of the attribute.  This value may not be null or the empty string.
     * @param value The value of the attribute.  This may contain an expression.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    void setAttribute(String name, String value, String facet) throws JspException;
}
