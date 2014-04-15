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
package tree.treeAttributeBits;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeHtmlAttributeInfo;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private TreeHtmlAttributeInfo _attr1;
    private TreeHtmlAttributeInfo _attr2;
    
    public TreeHtmlAttributeInfo getAttributeInfo1() {
        return _attr1;
    }
     public TreeHtmlAttributeInfo getAttributeInfo2() {
        return _attr2;
    }
    
    protected void onCreate()
    {
        _attr1 = new TreeHtmlAttributeInfo();
        _attr2 = new TreeHtmlAttributeInfo();
        
        _attr1.setOnIcon(true);
        _attr1.setOnSelectionLink(false);
        
        _attr2.setOnIcon(false);
        _attr2.setOnSelectionLink(true);
        
    }
    
    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }



    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "index.jsp")
})
    protected Forward flip()        {
        Forward forward = new Forward("success");
        _attr1.setOnIcon(!_attr1.isOnIcon());
        _attr1.setOnSelectionLink(!_attr1.isOnSelectionLink());
        
        _attr2.setOnIcon(!_attr2.isOnIcon());
        _attr2.setOnSelectionLink(!_attr2.isOnSelectionLink());
         
        return forward;
    }
                
              
    
    @Jpf.Action(forwards = { 
    @Jpf.Forward(name = "success", path = "index.jsp")
})
        protected Forward alternate()        {
        Forward forward = new Forward("success");
        _attr1.setOnIcon(!_attr1.isOnIcon());
        return forward;
    }
}
