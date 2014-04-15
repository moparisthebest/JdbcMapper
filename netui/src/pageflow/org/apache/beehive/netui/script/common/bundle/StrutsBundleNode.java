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
package org.apache.beehive.netui.script.common.bundle;

import java.util.Locale;
import java.util.Enumeration;

import org.apache.struts.util.MessageResources;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;

/**
 */
class StrutsBundleNode
    extends BundleNode {

    private Locale _locale;
    private MessageResources _messageResource;

    StrutsBundleNode(Locale locale, MessageResources messageResource) {
        _locale = locale;
        _messageResource = messageResource;
    }

    public boolean containsKey(String key) {
        assert _messageResource != null;
        return _messageResource.getMessage(_locale, key) != null;
    }

    public String getString(String key) {
        assert _messageResource != null;
        return _messageResource.getMessage(_locale, key);
    }

    public Enumeration getKeys() {
        throw new UnsupportedOperationException("The getKeys() method is not supported on the MessageResources type.");
    }

    public String toString() {
        InternalStringBuilder sb = new InternalStringBuilder();
        sb.append("StrutsBundleNode ");
        sb.append("messageResource: ");
        sb.append(_messageResource);
        sb.append(" ");
        sb.append("locale: ");
        sb.append(_locale);
        return sb.toString();
    }
}
