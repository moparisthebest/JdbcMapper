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
package org.apache.beehive.netui.simpletags.html;

/**
 * This is an interface that defines the accessability properties defined by HTML accessability. All tags
 * producing HTML elements that require accessability properties should implement this interface.
 * The interface defines two primary properties that will be output with the HTML.  The AccessKey
 * usually results in an <code>accesskey</code> attribute.  This allows for keyboard navigation to an element
 * on an HTML page.  The Alt property usually results in an <code>alt</code. attribute.  This is a text description
 * of the HTML element.
 */
public interface IHtmlAccessable extends IHtmlCore
{
    /**
     * Gets the accessKey attribute value.
     * @return the accessKey value.
     */
    //String getAccessKey();

    /**
     * Sets the accessKey attribute value.  This should key value of the
     * keyboard navigation key.  It is recommended not to use the following
     * values because there are often used by browsers <code>A, C, E, F, G,
     * H, V, left arrow, and right arrow</code>.
     * @param accessKey - the accessKey value.
     */
    void setAccessKey(char accessKey);

    /**
     * Gets the alt attribute on the generate &lt;input tag.
     * @return the alt value.
     */
    //String getAlt();

    /**
     * Sets the alt attribute value.
     * @param alt - the alt value.
     */
    void setAlt(String alt);
}
