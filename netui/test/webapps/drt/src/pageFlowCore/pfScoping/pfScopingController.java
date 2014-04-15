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
package pageFlowCore.pfScoping;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

/**
 * @jpf:controller
 */
@Jpf.Controller()
public class pfScopingController 
    extends PageFlowController
{
    /**
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="frames.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "frames.jsp") 
        })
    protected Forward doFrames()
    {
        return new Forward( "success" );
    }


    private TreeElement _root = null;
  
    public TreeElement getTree()
    {
        if ( _root == null )
        {
             
            _root = new TreeElement("root", true );
            TreeElement child1 = new TreeElement( "launch A", true);
            child1.setHref("a/FlowA.jpf?jpfScopeID=treeA");
            TreeElement child2 = new TreeElement("launch B",  true );
            child2.setTarget("_treeWindowB");
            _root.addChild( child1 );
            _root.addChild( child2 );
        }
                
        return _root;
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="success" path="windows.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "windows.jsp") 
        })
    protected Forward updateTree()
    {
       String expanded = getRequest().getParameter( TreeElement.EXPAND_NODE );
        
        if ( expanded != null )
        {
            String[] elements;
            elements = expanded.split( "\\." );
            TreeElement node = _root;
            
            for ( int i = 1; i < elements.length; ++i )
            {
                int n = Integer.parseInt( elements[i] );
                node = ( TreeElement ) node.getChild( n );
            }
            
            if ( node != null )
            {
                node.setExpanded( ! node.isExpanded() );
            }
        }
        
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" path="b/FlowB.jpf" redirect="true"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "b/FlowB.jpf",
                redirect = true) 
        })
    protected Forward launchTreeB()
    {
        Forward fwd = new Forward( "success" );
        fwd.addQueryParam( "jpfScopeID", "treeScopeB" );
        return fwd;
    }
}
