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
package org.apache.beehive.netui.simpletags.util;

import org.apache.beehive.netui.simpletags.core.TagContext;
import org.apache.beehive.netui.pageflow.PageFlowContext;
import org.apache.beehive.netui.pageflow.requeststate.NameService;

public class ContextUtils
{
    private final static String TAG_CONTEXT_NAME = "netui.velocity.tags";
    public static PageFlowContext getPageFlowContext()
    {
        PageFlowContext pfCtxt = PageFlowContext.getContext();
        assert(pfCtxt != null) : "the PageFlowContext returned as null";
        return pfCtxt;
    }

    public static TagContext getTagContext()
    {
        PageFlowContext pfCtxt = getPageFlowContext();
        TagContext tagCtxt = (TagContext) pfCtxt.get(TAG_CONTEXT_NAME,TagContext.ACTIVATOR);
        assert(tagCtxt != null) : "the TagContext returned as null";
        return tagCtxt;
    }

    /**
     * Return the name service
     * @return
     */
    public static NameService getNameService() {
        PageFlowContext pfCtxt = PageFlowContext.getContext();
        assert(pfCtxt != null) : "the PageFlowContext returned as null";
        return NameService.instance(pfCtxt.getRequest().getSession());
    }
}
