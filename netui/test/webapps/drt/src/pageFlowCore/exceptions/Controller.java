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
package pageFlowCore.exceptions;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.UnfulfilledRolesException;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.io.Serializable;


/**
 * @jpf:catch type="Controller.GlobalCatchToMethodException"
 *            method="globalHandler"
 *            message-key="theMessage"
 * @jpf:catch type="pageFlowCore.exceptions.Controller$GlobalCatchToPageException"
 *            path="error.jsp"
 *            message="This happened because of a GlobalCatchToPageException"
 * @jpf:catch type="org.apache.beehive.netui.pageflow.UnfulfilledRolesException"
 *            method="failedRolesHandler"
 *
 * @jpf:message-resources resources="exceptions.Messages"
 */
@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = Controller.GlobalCatchToMethodException.class,
            method = "globalHandler",
            messageKey = "theMessage"),
        @Jpf.Catch(
            type = pageFlowCore.exceptions.Controller.GlobalCatchToPageException.class,
            path = "error.jsp",
            message = "This happened because of a GlobalCatchToPageException"),
        @Jpf.Catch(
            type = org.apache.beehive.netui.pageflow.UnfulfilledRolesException.class,
            method = "failedRolesHandler") 
    },
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "exceptions.Messages") 
    })
public class Controller extends PageFlowController
{
    private String messageFromMethod;

    public String getMessageFromMethod()
    {
        return messageFromMethod;
    }

    /**
     * @jpf:action
     * @jpf:forward name="startPage" path="start.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "startPage",
                path = "start.jsp") 
        })
    protected Forward begin()
    {
        return new Forward( "startPage" );
    }

    /**
     * @jpf:action
     * @jpf:catch type="LocalCatchToMethodException"
     *            method="localHandler"
     */
    @Jpf.Action(
        catches = {
            @Jpf.Catch(
                type = LocalCatchToMethodException.class,
                method = "localHandler") 
        })
    protected Forward catchLocalToMethod()
        throws Exception
    {
        throw new LocalCatchToMethodException( "this is a LocalCatchToMethodException" );
    }

    /**
     * @jpf:action
     * @jpf:catch type="LocalCatchToPageException"
     *            path="/pageFlowCore/exceptions/error.jsp"
     *            message="This happened because of a LocalCatchToPageException"
     */
    @Jpf.Action(
        catches = {
            @Jpf.Catch(
                type = LocalCatchToPageException.class,
                path = "/pageFlowCore/exceptions/error.jsp",
                message = "This happened because of a LocalCatchToPageException") 
        })
    protected Forward catchLocalToPage()
        throws Exception
    {
        throw new LocalCatchToPageException( "this is a LocalCatchToPageException" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward catchGlobalToMethod()
        throws Exception
    {
        throw new GlobalCatchToMethodException( "this is a GlobalCatchToMethodException" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward catchGlobalToPage()
        throws Exception
    {
        throw new GlobalCatchToPageException( "this is a GlobalCatchToPageException" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="startPage" path="start.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "startPage",
                path = "start.jsp") 
        })
    protected Forward goBack( EmptyForm emptyForm )
    {
        return new Forward( "startPage" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward throwUnhandled()
        throws Exception
    {
        throw new UnhandledException( "this is an UnhandledException" );
    }

    /**
     * @jpf:action roles-allowed="coolusers,supercoolusers"
     */
    @Jpf.Action(
        rolesAllowed = {
            "coolusers",
            "supercoolusers"
        })
    protected Forward failRoles()
        throws Exception
    {
        assert false;
        return null;
    }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage" path="error.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "errorPage1",
                path = "error.jsp") 
        })
    protected Forward globalHandler( GlobalCatchToMethodException ex, String actionName,
                                     String message, Object form )
    {
        messageFromMethod = "caught " + ex + " in globalHandler; message = " + message;
        return new Forward( "errorPage1" );
    }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage" path="error.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "errorPage2",
                path = "error.jsp") 
        })
    protected Forward localHandler( LocalCatchToMethodException ex, String actionName,
                                    String message, Object form )
    {
        messageFromMethod = "caught " + ex + " in localHandler";
        return new Forward( "errorPage2" );
    }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage" path="error.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "errorPage3",
                path = "error.jsp") 
        })
    protected Forward failedRolesHandler( UnfulfilledRolesException ex, String actionName,
                                          String message, Object form )
    {
        messageFromMethod = "Caught " + ex + " in globalHandler; none of the roles ";
        for ( int i = 0; i < ex.getRoleNames().length; ++i )
        {
            messageFromMethod += ( i > 0 ? ", " : "" ) + ex.getRoleNames()[i];
        }
        messageFromMethod += " applied.";

        return new Forward( "errorPage3" );
    }

    public static class GlobalCatchToMethodException extends Exception
    {
        public GlobalCatchToMethodException( String msg )
        {
            super( msg );
        }
    }

    public static class GlobalCatchToPageException extends Exception
    {
        public GlobalCatchToPageException( String msg )
        {
            super( msg );
        }
    }

    public static class LocalCatchToMethodException extends Exception
    {
        public LocalCatchToMethodException( String msg )
        {
            super( msg );
        }
    }

    public static class LocalCatchToPageException extends Exception
    {
        public LocalCatchToPageException( String msg )
        {
            super( msg );
        }
    }

    public static class UnhandledException extends Exception
    {
        public UnhandledException( String msg )
        {
            super( msg );
        }
    }

    public static class EmptyForm implements Serializable
    {
    }

    /**
     * We're just using this to clear out messageFromMethod on each request.
     */
    protected void beforeAction()
    {
        messageFromMethod = "";
    }
}
