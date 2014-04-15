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
package bugs.j971;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction(name="begin", path="index.jsp"),
      @Jpf.SimpleAction(name="postback", navigateTo=Jpf.NavigateTo.currentPage)
   }
)
public class Controller extends PageFlowController
{
   
    private TreeElement productTree;

    public TreeElement getProductTree(){
        return this.productTree;
    }
    
    public void setProductTree(TreeElement productTree){
        this.productTree = productTree;
    }

    protected void onCreate() {
       makeTree();
    }

    @Jpf.Action( forwards={
       @Jpf.Forward( name="success", navigateTo=Jpf.NavigateTo.currentPage )
    })
    public Forward select() {
       return new Forward( "success" );
    }

    public void makeTree() {
        productTree = new TreeElement("Root Node",true);
        productTree.addChild(0, new TreeElement("CHILDNODE - A",false));
        productTree.addChild(1, new TreeElement("CHILDNODE - B",false));
    }


    public void reset(){
        makeTree();
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward resetTree()
    {
        reset();
        return new Forward("success");
    }

    public void removeaKid(){
        productTree.removeChild(0);
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward removeKid()
    {
        removeaKid();
        return new Forward("success");
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward clearFromLeaf()
    {
        TreeElement[] children = productTree.getChildren();
        if (children != null && children.length > 0) {
            // before fix for J971 this caused an NPE in clearChildren()
            children[0].clearChildren();
        }
        return new Forward("success");
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward clearFromRoot()
    {
        productTree.clearChildren();
        return new Forward("success");
    }
}

