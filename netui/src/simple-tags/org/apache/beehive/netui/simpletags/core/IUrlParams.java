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
package org.apache.beehive.netui.simpletags.core;

/**
 * This interfaces defines the contract allowing tags to add parameters which will
 * be added to URLs which they generate.  The Parameter and ParameterMap tags both
 * use this method to add parameters to ancestor tags.
 */
public interface IUrlParams
{
    /**
     * This method will allow a tag that produces one or more Urls to have parameters set
     * on the tag.  The name and value should be required.  The facet is optional, and
     * allows tags producing more than one URL to have parameters set on different URLs.
     * @param name  The name of the parameter to be added to the URL.
     * @param value The value of the parameter.
     * @param facet The name of a facet for which the parameter should be added.
     */
    public void addParameter(String name, Object value, String facet) ;
}
