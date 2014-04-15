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
package miniTests.lifecycleExceptions;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.ArrayList;

@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = lifecycleExceptionsController.OnCreateException.class,
            method = "handleIt",
            message = "onCreate"),
        @Jpf.Catch(
            type = lifecycleExceptionsController.BeforeActionException.class,
            method = "handleIt",
            message = "beforeAction"),
        @Jpf.Catch(
            type = lifecycleExceptionsController.AfterActionException.class,
            method = "handleIt",
            message = "afterAction"),
        @Jpf.Catch(
            type = lifecycleExceptionsController.ActionMethodException.class,
            method = "handleIt",
            message = "action method") 
    }
)
public class lifecycleExceptionsController extends PageFlowController
{
    private ArrayList history = new ArrayList();
    
    public ArrayList getHistory() {
        return history;
    }
    
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward throwInBeforeAction()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward throwInAfterAction()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward throwInAction()
        throws Exception
    {
        throw new ActionMethodException();
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward noThrow()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                navigateTo = Jpf.NavigateTo.currentPage) 
        })
    protected Forward throwEverywhere()
        throws Exception
    {
        throw new ActionMethodException();
    }

    public static class HistoryItem implements Serializable
    {
        private String _actionName;
        private Exception _exception;
        private String _message;

        public HistoryItem( String actionName, Exception exception, String message )
        {
            _actionName = actionName;
            _exception = exception;
            _message = message;
        }

        public String getActionName()
        {
            return _actionName;
        }

        public void setActionName( String actionName )
        {
            _actionName = actionName;
        }

        public Exception getException()
        {
            return _exception;
        }

        public void setException( Exception exception )
        {
            _exception = exception;
        }

        public String getExceptionName()
        {
            return _exception.getClass().getName();
        }

        public String getMessage()
        {
            return _message;
        }

        public void setMessage( String message )
        {
            _message = message;
        }
    }
    
    public static class OnCreateException extends Exception
    {
    }
    
    public static class BeforeActionException extends Exception
    {
    }
    
    public static class AfterActionException extends Exception
    {
    }
    
    public static class ActionMethodException extends Exception
    {
    }
    

    protected void onCreate()
        throws Exception
    {
        throw new OnCreateException();
    }
    
    protected void beforeAction()
        throws Exception
    {
        if ( getCurrentActionName().equals( "throwInBeforeAction" ) )
        {
            throw new BeforeActionException();
        }
    }
    
    protected void afterAction()
        throws Exception
    {
        if ( getCurrentActionName().equals( "throwInAfterAction" )
             || getCurrentActionName().equals( "throwEverywhere" ) )
        {
            throw new AfterActionException();
        }
    }
    
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
    
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    protected Forward handleIt( Exception e, String actionName, String message, Object form )
    {
        history.add( new HistoryItem( actionName, e, message ) );
        return new Forward( "index" );
    }
}
