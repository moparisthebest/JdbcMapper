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
package org.apache.beehive.netui.pageflow;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.config.ModuleConfig;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.logging.Logger;

import javax.servlet.ServletContext;

/**
 * Default event reporter.  Logs every event when the log level is set to "debug" or "trace".
 */ 
public class DefaultPageFlowEventReporter
    extends PageFlowEventReporter
{
    private static final Logger _log = Logger.getInstance( DefaultPageFlowEventReporter.class );
    
    protected DefaultPageFlowEventReporter( ServletContext servletContext )
    {
        super( servletContext );
    }

    public void actionRaised( RequestContext requestContext, FlowController flowController, ActionMapping mapping,
                              ActionForm form )
                              
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "actionRaised" );
            msg.addParam( "FlowController", flowController );
            msg.addParam( "ActionMapping", mapping );
            msg.addParam( "ActionForm", form );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            _log.debug( msg );
        }
    }

    public void actionSuccess( RequestContext requestContext, FlowController flowController, ActionMapping mapping,
                               ActionForm form, ActionForward result, long timeTakenMillis )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "actionSuccess" );
            msg.addParam( "FlowController", flowController );
            msg.addParam( "ActionMapping", mapping );
            msg.addParam( "ActionForm", form );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            msg.addParam( "ActionForward", result );
            msg.addParam( "TimeTakenMillis", new Long( timeTakenMillis ) );
            _log.debug( msg );
        }
    }

    public void exceptionRaised( RequestContext requestContext, Throwable ex, ActionMapping actionMapping,
                                 ActionForm form, FlowController flowController )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "exceptionRaised" );
            msg.addParam( "Throwable", ex );
            msg.addParam( "ActionMapping", actionMapping );
            msg.addParam( "ActionForm", form );
            msg.addParam( "FlowController", flowController );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            _log.debug( msg );
        }
    }

    public void exceptionHandled( RequestContext requestContext, Throwable ex, ActionMapping actionMapping,
                                  ActionForm form, FlowController flowController, ActionForward result,
                                  long timeTakenMillis )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "exceptionHandled" );
            msg.addParam( "Throwable", ex );
            msg.addParam( "ActionMapping", actionMapping );
            msg.addParam( "ActionForm", form );
            msg.addParam( "FlowController", flowController );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            msg.addParam( "ActionForward", result );
            msg.addParam( "TimeTakenMillis", new Long( timeTakenMillis ) );
            _log.debug( msg );
        }
    }

    public void flowControllerCreated( RequestContext requestContext, FlowController flowController )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "flowControllerCreated" );
            msg.addParam( "FlowController", flowController );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            _log.debug( msg );
        }
    }

    public void flowControllerDestroyed( FlowController flowController, Object storageLocation )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "flowControllerDestroyed" );
            msg.addParam( "FlowController", flowController );
            msg.addParam( "StorageLocation", storageLocation );
            _log.debug( msg );
        }
    }

    public void beginActionRequest( RequestContext requestContext )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "beginActionRequest" );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            _log.debug( msg );
        }
    }

    public void endActionRequest( RequestContext requestContext, long timeTakenMillis )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "endActionRequest" );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            msg.addParam( "TimeTakenMillis", new Long( timeTakenMillis ) );
            _log.debug( msg );
        }
    }

    public void beginPageRequest( RequestContext requestContext )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "beginPageRequest" );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            _log.debug( msg );
        }
    }

    public void endPageRequest( RequestContext requestContext, long timeTakenMillis )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "endPageRequest" );
            msg.addParam( "Request", requestContext.getRequest() );
            msg.addParam( "Response", requestContext.getResponse() );
            msg.addParam( "TimeTakenMillis", new Long( timeTakenMillis ) );
            _log.debug( msg );
        }
    }

    public void flowControllerRegistered( String modulePath, String controllerClassName, ModuleConfig moduleConfig )
    {
        if ( _log.isDebugEnabled() )
        {
            LogMsg msg = new LogMsg( "flowControllerRegistered" );
            msg.addParam( "ModulePath", modulePath );
            msg.addParam( "ControllerClassName", controllerClassName );
            msg.addParam( "ModuleConfig", moduleConfig );
            _log.debug( msg );
        }
    }

    protected static class LogMsg
    {
        private String _eventName;
        private InternalStringBuilder _logMessage;
            
        public LogMsg( String eventName )
        {
            _eventName = eventName;
        }
            
        public void addParam( String name, Object value )
        {
            if ( _logMessage == null )
            {
                _logMessage = new InternalStringBuilder( _eventName ).append( ": " );
            }
            else
            {
                _logMessage.append( ", " );
            }
                
            _logMessage.append( name ).append( '=' ).append( value );
        }
            
        public String toString()
        {
            return _logMessage == null ? _eventName : _logMessage.toString();
        }
    }
}
