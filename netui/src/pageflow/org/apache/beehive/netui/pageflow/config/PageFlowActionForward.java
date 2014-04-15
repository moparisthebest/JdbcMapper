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
package org.apache.beehive.netui.pageflow.config;

import org.apache.struts.action.ActionForward;

import java.util.ArrayList;
import java.io.Serializable;


/**
 * Bean class to handle our extensions to the Struts &lt;forward&gt; element.
 */
public class PageFlowActionForward extends ActionForward
{
    private boolean _isNestedReturn = false;
    private boolean _isReturnToPage = false;
    private boolean _isReturnToAction = false;
    private String _returnFormType;
    private String _returnFormMember;
    private boolean _hasExplicitRedirectValue = false;
    private ArrayList _actionOutputs;
    private boolean _restoreQueryString;
    private boolean _externalRedirect = false;
    private String _relativeTo;


    public boolean isNestedReturn()
    {
        return _isNestedReturn;
    }

    public void setNestedReturn( boolean nestedReturn )
    {
        _isNestedReturn = nestedReturn;
    }

    public boolean isReturnToPage()
    {
        return _isReturnToPage;
    }

    public void setReturnToPage( boolean returnToPage )
    {
        _isReturnToPage = returnToPage;
    }

    public boolean isReturnToAction()
    {
        return _isReturnToAction;
    }

    public void setReturnToAction( boolean returnToAction )
    {
        _isReturnToAction = returnToAction;
    }

    public String getReturnFormType()
    {
        return _returnFormType;
    }

    public void setReturnFormType( String returnFormType )
    {
        _returnFormType = returnFormType;
    }

    public String getReturnFormMember()
    {
        return _returnFormMember;
    }

    public void setReturnFormMember( String returnFormMember )
    {
        _returnFormMember = returnFormMember;
    }

    public boolean hasExplicitRedirectValue()
    {
        return _hasExplicitRedirectValue;
    }

    public void setHasExplicitRedirectValue( boolean hasExplicitRedirectValue )
    {
        _hasExplicitRedirectValue = hasExplicitRedirectValue;
    }

    public boolean isRestoreQueryString()
    {
        return _restoreQueryString;
    }

    public void setRestoreQueryString( boolean restoreQueryString )
    {
        _restoreQueryString = restoreQueryString;
    }

    public boolean isExternalRedirect()
    {
        return _externalRedirect;
    }

    public void setExternalRedirect(boolean externalRedirect)
    {
        _externalRedirect = externalRedirect;
    }

    public String getRelativeTo()
    {
        return _relativeTo;
    }

    public void setRelativeTo( String relativeTo )
    {
        _relativeTo = relativeTo;
    }

    public static class ActionOutput implements Serializable
    {
        private String _name;
        private String _type;
        private boolean _isNullable;
        
        public ActionOutput( String name, String type, boolean isNullable )
        {
            _name = name;
            _type = type;
            _isNullable = isNullable;
        }

        public String getName()
        {
            return _name;
        }

        public String getType()
        {
            return _type;
        }

        public boolean getNullable()
        {
            return _isNullable;
        }
    }

    public void setActionOutputCount( String count )
    {
        setActionOutputCount( Integer.parseInt( count ) );
    }
    
    public String getActionOutputCount()
    {
        return new Integer( _actionOutputs != null ? _actionOutputs.size() : 0 ).toString();
    }
    
    public void setActionOutputCount( int count )
    {
        if ( _actionOutputs == null )
        {
            _actionOutputs = new ArrayList( count );
        }
        else
        {
            _actionOutputs.ensureCapacity( count );
        }
        
        while ( _actionOutputs.size() < count )
        {
            _actionOutputs.add( null );
        }
    }
    
    protected void setActionOutput( int n, String concatenatedVals )
    {
        setActionOutputCount( n + 1 );
        String[] vals = concatenatedVals.split( "\\|" );
        assert vals.length == 3 : vals.length;
        _actionOutputs.set( n, new ActionOutput( vals[2], vals[0], Boolean.valueOf( vals[1] ).booleanValue() ) );
    }
    
    public ActionOutput[] getActionOutputs()
    {
        if ( _actionOutputs == null ) return new ActionOutput[0];
        return ( ActionOutput[] ) _actionOutputs.toArray( new ActionOutput[ _actionOutputs.size() ] );
    }
    
    public void setActionOutput0( String str )
    {
        setActionOutput( 0, str );
    }
    
    public void setActionOutput1( String str )
    {
        setActionOutput( 1, str );
    }
    
    public void setActionOutput2( String str )
    {
        setActionOutput( 2, str );
    }
    
    public void setActionOutput3( String str )
    {
        setActionOutput( 3, str );
    }
    
    public void setActionOutput4( String str )
    {
        setActionOutput( 4, str );
    }
    
    public void setActionOutput5( String str )
    {
        setActionOutput( 5, str );
    }
    
    public void setActionOutput6( String str )
    {
        setActionOutput( 6, str );
    }
    
    public void setActionOutput7( String str )
    {
        setActionOutput( 7, str );
    }
    
    public void setActionOutput8( String str )
    {
        setActionOutput( 8, str );
    }
    
    public void setActionOutput9( String str )
    {
        setActionOutput( 9, str );
    }
    
    public void setActionOutput10( String str )
    {
        setActionOutput( 10, str );
    }
    
    public void setActionOutput11( String str )
    {
        setActionOutput( 11, str );
    }
    
    public void setActionOutput12( String str )
    {
        setActionOutput( 12, str );
    }
    
    public void setActionOutput13( String str )
    {
        setActionOutput( 13, str );
    }
    
    public void setActionOutput14( String str )
    {
        setActionOutput( 14, str );
    }
    
    public void setActionOutput15( String str )
    {
        setActionOutput( 15, str );
    }
    
    public void setActionOutput16( String str )
    {
        setActionOutput( 16, str );
    }
    
    public void setActionOutput17( String str )
    {
        setActionOutput( 17, str );
    }
    
    public void setActionOutput18( String str )
    {
        setActionOutput( 18, str );
    }
    
    public void setActionOutput19( String str )
    {
        setActionOutput( 19, str );
    }
}
