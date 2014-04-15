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
package org.apache.beehive.netui.core.chain.impl;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.beehive.netui.core.chain.Catalog;
import org.apache.beehive.netui.core.chain.Command;

/**
 * 
 */
public class CatalogBase
    implements Catalog {

    private ConcurrentHashMap _commands = new ConcurrentHashMap();

    public void addCommand(String name, Command command) {
        _commands.put(name, command);
    }

    public Command getCommand(String name) {
        return (Command)_commands.get(name);
    }

    public Iterator getNames() {
        return _commands.keySet().iterator();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[").append(getClass().getName()).append(": ");
        Iterator names = _commands.keySet().iterator();
        while(names.hasNext()) {
            stringBuilder.append(names.next());
            if(names.hasNext())
                stringBuilder.append(",");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
