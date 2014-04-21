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
package org.apache.beehive.netui.util.config.bean;

import java.util.List;
import java.util.LinkedList;

/**
 *
 */
public class CatalogConfig {

    private String _name = null;
    private LinkedList _chains;

    public CatalogConfig() {
        _chains = new LinkedList();
    }

    public void addCommand(CommandConfig commandConfig) {
        _chains.add(commandConfig);
    }

    public List getCommands() {
        return _chains;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
}
