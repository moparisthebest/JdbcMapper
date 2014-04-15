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
package bugs.cr137279;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

@Jpf.Controller()
public class Controller extends PageFlowController
{
    private TreeElement tree;
    public TreeElement getTree() {
        return tree;
    }

    public void setTree(TreeElement tree) {
        this.tree = tree;
    }
    
    private TreeElement treeTwo;
    public TreeElement getTreeTwo() {
        return treeTwo;
    }

    public void setTreeTwo(TreeElement treeTwo) {
        this.treeTwo = treeTwo;
    }

    private TreeElement treeThree;
    public TreeElement getTreeThree() {
        return treeThree;
    }
    
    public void setTreeThree(TreeElement treeThree) {
        this.treeThree = treeThree;
    }

    @Jpf.Action(
        forwards={
        @Jpf.Forward(name = "index", path = "index.jsp")}
    )
    protected Forward begin()
    {
        return new Forward("index");
    }


}
