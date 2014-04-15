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
package org.apache.beehive.netui.compiler.model;

import org.w3c.dom.Element;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


abstract class AbstractForwardContainer
        extends StrutsElementSupport
        implements ForwardContainer
{
    private LinkedHashMap _forwards = new LinkedHashMap();
    
    
    public AbstractForwardContainer( StrutsApp parentApp )
    {
        super( parentApp );
    }

    public AbstractForwardContainer( AbstractForwardContainer src )
    {
        super( src.getParentApp() );
        _forwards = ( LinkedHashMap ) src._forwards.clone();
    }
    
    /**
      * Implemented for {@link ForwardContainer}.
      */
     public void addForward( ForwardModel newActionForward )
     {
         if ( _forwards.containsKey( newActionForward.getName() ) )
         {
                 // TODO: Rich - replace this with something other than the knex logger so that the xdoclet compiler
                 // won't require knex
//                if ( ! fwd.getPath().equals( newActionForward.getPath() ) )
//                {
//                    logger.warn( "Could not add forward \"" + newActionForward.getName() + "\", path=\""
//                                 + newActionForward.getPath() + "\" because there is already a forward with"
//                                 + " the same name (path=\"" + fwd.getPath() + "\")." );
//                }
                
                 return;
         }
        
         _forwards.put( newActionForward.getName(), newActionForward );
     }
    
    public ForwardModel findForward( String forwardName )
    {
        return ( ForwardModel ) _forwards.get( forwardName );
    }
    
    public void writeForwards( XmlModelWriter xw, Element forwardsParentElement )
    {
        for ( Iterator i = _forwards.values().iterator(); i.hasNext(); )
        {
            ForwardModel fwd = ( ForwardModel ) i.next();
            Element fwdToEdit = findChildElement(xw, forwardsParentElement, "forward", "name", fwd.getName(), true, null);
            fwd.writeXML( xw, fwdToEdit );
        }
    }
    
    public ForwardModel[] getForwards()
    {
        return ( ForwardModel[] ) _forwards.values().toArray( new ForwardModel[ _forwards.size() ] );
    }

    public List getForwardsAsList()
    {
        List ret = new ArrayList();
        ret.addAll( _forwards.values() );
        return ret;
    }

    public void deleteForward( ForwardModel forward )
    {
        _forwards.remove( forward.getName() );
    }

}
