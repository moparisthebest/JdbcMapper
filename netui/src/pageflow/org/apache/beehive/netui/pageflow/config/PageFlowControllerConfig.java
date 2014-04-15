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

import org.apache.struts.config.ControllerConfig;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.beehive.netui.pageflow.internal.InternalUtils;
import org.apache.beehive.netui.util.config.bean.MultipartHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bean class to handle our extensions to the Struts &lt;controller&gt; element.
 */
public class PageFlowControllerConfig extends ControllerConfig
{
    private static final String COMMONS_MULTIPART_HANDLER_CLASS = CommonsMultipartRequestHandler.class.getName();

    private boolean _isNestedPageFlow;
    private boolean _isLongLivedPageFlow;
    private boolean _isReturnToPageDisabled;
    private boolean _isReturnToActionDisabled;
    private boolean _isMissingDefaultMessages;
    private LinkedHashMap/*< String, String >*/ _sharedFlowTypes;
    private String _controllerClass;
    private boolean _isSharedFlow;
    private boolean _isAbstract = false;
    private String _overrideMultipartClass = null;
    private String _overrideMemFileSize = null;
    private boolean _forceMultipartDisabled = false;


    public boolean isNestedPageFlow()
    {
        return _isNestedPageFlow;
    }

    public void setIsNestedPageFlow( boolean nestedPageFlow )
    {
        _isNestedPageFlow = nestedPageFlow;
    }

    public boolean isLongLivedPageFlow()
    {
        return _isLongLivedPageFlow;
    }

    public void setIsLongLivedPageFlow( boolean longLivedPageFlow )
    {
        _isLongLivedPageFlow = longLivedPageFlow;
    }

    public boolean isReturnToPageDisabled()
    {
        return _isReturnToPageDisabled;
    }

    public void setIsReturnToPageDisabled( boolean returnToPageDisabled )
    {
        _isReturnToPageDisabled = returnToPageDisabled;
    }

    public boolean isReturnToActionDisabled()
    {
        return _isReturnToActionDisabled;
    }

    public void setIsReturnToActionDisabled( boolean returnToActionDisabled )
    {
        _isReturnToActionDisabled = returnToActionDisabled;
    }

    public boolean isMissingDefaultMessages()
    {
        return _isMissingDefaultMessages;
    }

    public void setIsMissingDefaultMessages( boolean missingDefaultMessages )
    {
        _isMissingDefaultMessages = missingDefaultMessages;
    }

    public void setSharedFlows( String sharedFlows )
    {
        if ( sharedFlows == null || sharedFlows.length() == 0 )
        {
            _sharedFlowTypes = null;
            return;
        }

        String[] keyValues = sharedFlows.split( "," );
        _sharedFlowTypes = new LinkedHashMap/*< String, String >*/();

        for ( int i = 0; i < keyValues.length; i++ )
        {
            String keyValue = keyValues[i];
            int delim = keyValue.indexOf( '=' );
            assert delim != -1 : "no delimiter in " + keyValue;
            assert delim < keyValue.length() - 1 : "missing value in " + keyValue;
            _sharedFlowTypes.put( keyValue.substring( 0, delim ), keyValue.substring( delim + 1 ) );
        }
    }

    public String getSharedFlows()
    {
        throw new UnsupportedOperationException( "not implemented; uses getSharedFlowTypes" );
    }

    public Map/*< String, String >*/ getSharedFlowTypes()
    {
        return _sharedFlowTypes;
    }

    public String getControllerClass()
    {
        return _controllerClass;
    }

    public void setControllerClass( String controllerClass )
    {
        _controllerClass = controllerClass;
    }

    public boolean isSharedFlow()
    {
        return _isSharedFlow;
    }

    public void setIsSharedFlow( boolean sharedFlow )
    {
        _isSharedFlow = sharedFlow;
    }

    /**
     * This gets the multipart class.  If it was explicitly set on the associated <controller> element, just use that;
     * otherwise, get the value from WEB-INF/beehive-netui-config.xml.
     */
    public String getMultipartClass()
    {
        if ( _forceMultipartDisabled ) return null;
        if ( _overrideMultipartClass != null ) return _overrideMultipartClass;

        MultipartHandler mpHandler = InternalUtils.getMultipartHandlerType();
        
        if ( mpHandler != null )
        {
            switch ( mpHandler.getValue() )
            {
                case MultipartHandler.INT_DISABLED: return null;
                case MultipartHandler.INT_MEMORY: return COMMONS_MULTIPART_HANDLER_CLASS;
                case MultipartHandler.INT_DISK: return COMMONS_MULTIPART_HANDLER_CLASS;
                default: assert false : "unknown value for multipart handler: " + mpHandler.toString();
            }
        }
        
        return null;
    }
    
    public String getMemFileSize()
    {
        if ( _overrideMemFileSize != null ) return _overrideMemFileSize;
        
        MultipartHandler mpHandler = InternalUtils.getMultipartHandlerType();
        
        if ( mpHandler != null )
        {
            switch ( mpHandler.getValue() )
            {
                case MultipartHandler.INT_DISABLED: return super.getMemFileSize();
                case MultipartHandler.INT_MEMORY: return super.getMemFileSize();
                case MultipartHandler.INT_DISK: return "0K"; // memory filesize threshold of zero.
                default: assert false : "unknown value for multipart handler: " + mpHandler.toString();
            }
        }
        
        return super.getMemFileSize();
    }

    public void setMultipartClass( String overrideMultipartClass )
    {
        if ( overrideMultipartClass.equals( "none" ) )
        {
            _forceMultipartDisabled = true;
        }
        else
        {
            _overrideMultipartClass = overrideMultipartClass;
        }
    }
    
    public void setMemFileSize( String fileSize )
    {
        _overrideMemFileSize = fileSize;
    }

    public boolean isAbstract() {
        return _isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        _isAbstract = isAbstract;
    }
}
