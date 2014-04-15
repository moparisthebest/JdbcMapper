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
package org.apache.beehive.netui.tags.divpanel;

import org.apache.beehive.netui.pageflow.requeststate.INameable;

import java.io.Serializable;

public class DivPanelState implements INameable, Serializable
{
    private String _name;
    private String _firstPage;

    /**
     * Set the ObjectName of the INameable object.  This should only
     * be set once.  If it is called a second time an IllegalStateException
     * should be thrown
     * @param name the Object's name.
     * @throws IllegalStateException if this method is called more than once for an object
     */
    public void setObjectName(String name)
    {
        _name = name;
    }

    /**
     * Returns the ObjectName of the INameable object.
     * @return the ObjectName.
     */
    public String getObjectName()
    {
        return _name;
    }

    public String getFirstPage()
    {
        return _firstPage;
    }

    public void setFirstPage(String firstPage)
    {
        _firstPage = firstPage;
    }
}
