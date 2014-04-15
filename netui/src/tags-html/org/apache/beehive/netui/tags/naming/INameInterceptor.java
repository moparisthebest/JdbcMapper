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
package org.apache.beehive.netui.tags.naming;

import org.apache.beehive.netui.script.ExpressionEvaluationException;

import javax.servlet.jsp.tagext.Tag;

/**
 * An interface that is used to allow interceptors to
 * modify attributes that are going to be written to a
 * client from a NetUI JSP tag.
 * <br/>
 * Several concrete implementations of this interface are
 * provided in order to rewrite names on form tags as they
 * are rendered to the browser.
 * @see FormDataNameInterceptor
 * @see IndexedNameInterceptor
 * @see PrefixNameInterceptor
 */
public interface INameInterceptor
{

    /**
     * @param name
     * @param currentTag
     * @return String
     * @throws ExpressionEvaluationException
     */
    public String rewriteName(String name, Tag currentTag) throws ExpressionEvaluationException;
}
