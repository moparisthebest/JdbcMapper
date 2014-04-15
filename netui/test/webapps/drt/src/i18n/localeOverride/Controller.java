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
package i18n.localeOverride;

// java imports
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.Globals;

import java.util.Locale;

/**
 * @jpf:forward name="index" path="index.jsp"
 * @jpf:message-resources resources="properties.bundle1"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "index",
            path = "index.jsp") 
    },
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "properties.bundle1") 
    })
public class Controller 
    extends PageFlowController
{
    private Locale defaultLocale = null;

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin()
    {
        defaultLocale = (Locale)getSession().getAttribute(Globals.LOCALE_KEY);

        Locale germany = new Locale("de");
        getSession().setAttribute(Globals.LOCALE_KEY, germany);

        return new Forward("index");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward resetLocale()
    {
        getSession().setAttribute(Globals.LOCALE_KEY, defaultLocale);
        return new Forward("index");
    }
}
