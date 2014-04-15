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

import org.apache.beehive.netui.simpletags.behaviors.formatting.Formatter;

/**
 * Interface provided by Behaviors which support formatting.  A formatter may be
 * added to a behavior which will run before the behavior renders it content.
 */
public interface IFormattable
{
    /**
     * Adds a FormatTag.Formatter instance to the tag's set of formatters.
     * @param formatter the formatter instance to add to the IFormattable tag's set of formatters.
     */
    public void addFormatter(Formatter formatter);

    /**
     * Indicate that a formatter has reported an error so the formattable needs to make sure that
     * the body content is output so the placement of the error is visible in the page.
     */
    public void formatterHasError();
}
