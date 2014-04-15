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
package except;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:catch type="except.Controller$MyException"
 *            method="globalHandler"
 */
@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = except.Controller.MyException.class,
            method = "globalHandler") 
    })
public class Controller extends PageFlowController
{
    private String _message;
    public String getMessage()
    {
        return _message;
    }

    /**
     * @jpf:action
     * @jpf:forward name="begin" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    protected Forward begin()
    {
	_message = "";
        return new Forward( "begin" );
    }

    /**
     * @jpf:action
     * @jpf:catch type="except.Controller$MyException"
     *            method="localHandler"
     */
    @Jpf.Action(
        catches = {
            @Jpf.Catch(
                type = except.Controller.MyException.class,
                method = "localHandler") 
        })
    protected Forward throwLocal()
        throws Exception
    {
        throw new MyException();
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward throwGlobal()
        throws Exception
    {
        throw new MyException();
    }

    /**
     * @jpf:action
     * @jpf:forward name="begin" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "begin",
                path = "Begin.jsp") 
        })
    protected Forward goHome()
    {
        return new Forward( "begin" );
    }


    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage" path="ErrorPage.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "errorPage",
                path = "ErrorPage.jsp") 
        })
    protected Forward globalHandler(MyException ex, String actionName,
					  String message, Object form)
    {
        _message = "caught " + ex + " in globalHandler";
        return new Forward( "errorPage" );
    }

    /**
     * @jpf:exception-handler
     * @jpf:forward name="errorPage" path="ErrorPage.jsp"
     */
    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "errorPage",
                path = "ErrorPage.jsp") 
        })
    protected Forward localHandler(MyException ex, String actionName,
                                           String message, Object form)
    {
        _message = "caught " + ex + " in localHandler";
        return new Forward( "errorPage" );
    }

    public static class MyException extends Exception
    {
	public String toString() {
	    return "[MyException]";
	}
    }
}
