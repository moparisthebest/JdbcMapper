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
package bugs.j533;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;
import org.apache.beehive.netui.tags.tree.TreeHtmlAttributeInfo;
import org.apache.beehive.netui.tags.tree.TreeRootElement;

@Jpf.Controller (
   simpleActions={
      @Jpf.SimpleAction(name="begin", path="index.jsp"),
      @Jpf.SimpleAction(name="postback", navigateTo=Jpf.NavigateTo.currentPage)
   }
)

public class Controller extends PageFlowController
{

    //Attributes
    TreeElement attrJspTree1;

    TreeRootElement attrDynTree1;

    //Methods

    //Tree Creation
    public TreeRootElement getAttrDynTree1()
    {
        return this.attrDynTree1;
    }
    public void setAttrDynTree1(TreeRootElement attrDynTree1)
    {
        this.attrDynTree1= attrDynTree1;
    }

    //Attributes
    public TreeElement getAttrJspTree1()
    {
        return this.attrJspTree1;
    }
    public void setAttrJspTree1(TreeElement attrJspTree1)
    { 
        this.attrJspTree1= attrJspTree1; 
    }

   

    protected void onCreate() {
        attrDynTree1 = new TreeRootElement( "Dynamic Tree 1", true );
        attrDynTree1.setTitle("Title in the HTML source.");

        TreeElement te = new TreeElement("TreeElement1", false);
        TreeHtmlAttributeInfo thai = 
                new TreeHtmlAttributeInfo("attrall", "TE1.DescendAttr");
        thai.setOnSelectionLink(false);
        thai.setOnDiv(true);
        thai.setOnIcon(false);
        thai.setApplyToDescendents(true);
        te.addAttribute(thai); 
        thai = new TreeHtmlAttributeInfo("attrone", "TE1.AttrA");
        thai.setOnSelectionLink(false);
        thai.setOnDiv(true);
        thai.setOnIcon(true);
        thai.setApplyToDescendents(false);
        te.addAttribute(thai); 
        attrDynTree1.addChild( te );

        te = new TreeElement("TreeElement2", false);
        attrDynTree1.getChild(0).addChild( te );

        te = new TreeElement("TreeElement3", false);
        thai = new TreeHtmlAttributeInfo("attrthree", "TE3.AttrB");
        thai.setOnSelectionLink(true);
        thai.setOnDiv(true);
        thai.setOnIcon(false);
        thai.setApplyToDescendents(false);
        te.addAttribute(thai); 
        attrDynTree1.getChild(0).getChild(0).addChild( te );

        te = new TreeElement("TreeElement4", false);
        attrDynTree1.getChild(0).getChild(0).addChild( te );

    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success", navigateTo=Jpf.NavigateTo.currentPage)
    })
    protected Forward resetTrees()        {
        Forward forward = new Forward("success");

        onCreate();
        
        //Attributes
        attrJspTree1 = null;
                  
        return forward;
    }

}

