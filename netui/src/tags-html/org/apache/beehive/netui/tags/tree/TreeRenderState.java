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
package org.apache.beehive.netui.tags.tree;

public class TreeRenderState implements java.io.Serializable
{
    public String selectedStyle;          // selection style
    public String selectedStyleClass;     // selection class
    public String unselectedStyle;        // unselect style
    public String unselectedStyleClass;   // unselect class
    public String disabledStyle;          // disabled style
    public String disabledStyleClass;     // disabled class

    public String tagId;                  // tag that should uniquely indentify a tree.  Required for multiple tree using auto expand

    public boolean runAtClient;
    public boolean escapeContent = false;  // esapce the content of labels
}
