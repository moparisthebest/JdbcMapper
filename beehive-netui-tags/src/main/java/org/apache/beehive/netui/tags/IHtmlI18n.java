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

/**
 * Interface which inforces that the HTML i18n attributes are found on an HTML element as defined by
 * the HTML 4.0 specification.
 */
public interface IHtmlI18n
{
    /**
     * Gets the dir value of the html.
     * @return "LTR" or "RTL"
     */
    //public String getDir();

    /**
     * Sets the dir value of the html.
     * @param dir - "LTR" or "RTL"
     * @netui:attribute required="false"
     */
    public void setDir(String dir);

    /**
     * Return the language Code of the HTML element.
     * @return a language code.
     */
    //public String getLang();

    /**
     * Set the language code of an HTML element.
     * @param lang
     */
    public void setLang(String lang);
}
