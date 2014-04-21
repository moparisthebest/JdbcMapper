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

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CommandConfig {

    private String _id;
    private String _classname;
    private LinkedList _parameters;

    public CommandConfig() {
        _parameters = new LinkedList();
    }

    public void addParameter(CustomPropertyConfig customParameterConfig) {
        _parameters.add(customParameterConfig);
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getClassname() {
        return _classname;
    }

    public void setClassname(String classname) {
        _classname = classname;
    }

    public List getParameters() {
        return _parameters != null ? _parameters : Collections.EMPTY_LIST;
    }

    public void setParameters(List parameters) {
        _parameters.clear();
        _parameters.addAll(parameters);
    }
}