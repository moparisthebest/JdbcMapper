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
 * This interfaces is implemented by tags that allow a behavior to
 * added and/or modified through an open ended set of name/value pairs.
 */
public interface IBehaviorConsumer
{
    /**
     * Set a behavior value on the implementing class.  The <code>name</code> represents
     * the name of the behavior.  The <code>value</code> represents the value.
     * The <code>facet</code> is optional and may be used by complex types to
     * target the behavior.
     * @param name  The name of the behavior.  This value may not be null or the empty string.
     * @param value The value of the behavior.
     * @param facet The name of a facet to which the attribute will be applied.  This is optional.
     * @throws JspException A JspException may be thrown if there is an error setting the attribute.
     */
    void setBehavior(String name, Object value, String facet) throws JspException;
}
