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
package org.apache.beehive.netui.pageflow.requeststate;

import java.util.EventListener;
import java.util.Map;

/**
 * The listener interface for receiving naming events from the NameService.  The class that is interested in
 * processing a <code>NamingObject</code> event must implement this interface.  The object is registered with the <code>NameService</code>
 * using the <code>addNamingObjectListener</code> method.  When an object is named, the <code>namingObject</code> method is
 * called.
 */
public interface NamingObjectListener extends EventListener
{
    /**
     * This method is called when an object is registered with the <code>NameService</code> to listen to
     * <code>NamingObject</code> events.  The <code>objectAttributes</code> map is passed allowing the object
     * to store information in the object. The Map is held by a weak reference by the NameService so it
     * will be released when the <code>namedObject</code> is released.
     * @param namedObject The object that will be named.
     * @param objectAttributes A map that associates data with the data.
     */
    void namingObject(INameable namedObject, Map objectAttributes);
}
