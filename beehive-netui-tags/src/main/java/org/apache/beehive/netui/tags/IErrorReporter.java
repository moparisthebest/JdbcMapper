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

import java.util.ArrayList;

/**
 * An <code>ErrorReporter</code> acts as a container allowing a tag to gather up all errors reported by it's children
 * and report them as a single group.  Typically, there should be a single active <code>ErrorReporter</code>
 * so all the errors on the page are reported in a single place.  All error reporters must look at any parent
 * tags and also the CONTAINER_ERRORS request variable for an instance of <code>ErrorReporter</code> before
 * becoming the primary <code>ErrorReporter</code>.  If another <code>ErrorReporter</code> is defined,
 * the tag should return <code>false</code> from the <code>isReporting()</code> method.  Otherwise, the tag
 * may become the primary <code>ErrorReporter</code>.  If a tag sets the CONTAINER_ERRORS request attribute,
 * it must clear this when processing it's <code>doEndTag()</code> method because it will not be
 * able to report errors after this point.
 */
public interface IErrorReporter
{
    /**
     * This is a request scoped attribute name which may contain an ErrorReporter instance.  If this
     * is defined, then this is the top most error reporter and should be used to report errors.
     */
    final String CONTAINER_ERRORS = "_netui.ErrorReporter";

    /**
     * Add an error to this <code>ErrorReporter</code>.
     * @param ape the page error to add to the container.
     */
    public void addError(AbstractPageError ape);

    /**
     * This boolean indicates if an ErrorReporter is reporting errors
     * or not.  The caller should check this before calling addError
     * because the ErrorReporter may be off.
     * @return a boolean indicating if the tag is reporting errors or not.
     */
    public boolean isReporting();

    /**
     * Return an ArrayList of the errors
     * @return an <code>ArrayList</code> of all errors.
     */
    public ArrayList returnErrors();
}
