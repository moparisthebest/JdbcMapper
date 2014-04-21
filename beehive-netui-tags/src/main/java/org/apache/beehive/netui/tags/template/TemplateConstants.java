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
package org.apache.beehive.netui.tags.template;

/**
 * Define the constants used by the template tags.  Mostly these are the
 * names used to store objects into the <code>ServletRequest</code>.  These
 * objects are used during the process of generating a page with a template.
 */
public interface TemplateConstants
{
    /**
     * This is the name of a <code>HashMap</code> containing all of the
     * attributes. The <code>HashMap</code> will be stored in the
     * <code>ServletRequest</code>.
     */
    final String TEMPLATE_ATTRIBUTES = "_netui.template.attributes";

    /**
     * This is the name of a <code>HashMap</code> containing all of the
     * sections.  The <code>HashMap</code> will be stored in the
     * <code>ServletRequest</code>.
     */
    final String TEMPLATE_SECTIONS = "_netui.template.sections";

}
