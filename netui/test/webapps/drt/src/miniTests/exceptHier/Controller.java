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
package miniTests.exceptHier;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * @jpf:catch type="Exception"
 *            method="globalHandler"
 * @jpf:catch type="Controller.ExceptBase"
 *            method="baseHandler"
 * @jpf:catch type="Controller.ExceptSub2"
 *            method="sub2Handler"
 * @jpf:forward name="begin" path="Begin.jsp"
 */
@Jpf.Controller(
    catches = {
        @Jpf.Catch(
            type = Exception.class,
            method = "globalHandler"),
        @Jpf.Catch(
            type = Controller.ExceptBase.class,
            method = "baseHandler"),
        @Jpf.Catch(
            type = Controller.ExceptSub2.class,
            method = "sub2Handler") 
    },
    forwards = {
        @Jpf.Forward(
            name = "begin",
            path = "Begin.jsp") 
    })
public class Controller extends PageFlowController
{
    public static class ExceptBase extends Exception
    {
	public String toString() {
	    return "[ExceptBase]";
	}
    }

    public static class ExceptSub extends ExceptBase
    {
	public String toString() {
	    return "[ExceptSub]";
	}
    }

    public static class ExceptSub2 extends ExceptBase
    {
	public String toString() {
	    return "[ExceptSub2]";
	}
    }

    private String _message;
    public String getMessage()
    {
        return _message;
    }

    /**
     * @jpf:action

     */
    @Jpf.Action(
        )
    protected Forward begin()
    {
        return new Forward("begin");
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward throwBase() throws Exception
    {
        throw new ExceptBase();
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward throwSub() throws Exception
    {
        throw new ExceptSub();
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward throwSub2() throws Exception
    {
        throw new ExceptSub2();
    }


    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward throwRuntime() throws Exception
    {
        throw new RuntimeException();
    }

    /**
     * @jpf:action
     * @jpf:catch type="ExceptSub"
     *            method="localSubHandler"
     */
    @Jpf.Action(
        catches = {
            @Jpf.Catch(
                type = ExceptSub.class,
                method = "localSubHandler") 
        })
    protected Forward throwSubLocal() throws Exception
    {
        throw new ExceptSub();
    }

    /**
     * @jpf:exception-handler
     */
    @Jpf.ExceptionHandler(
        )
    protected Forward globalHandler(Exception ex, String actionName,
					  String message, Object form)
    {
	
	_message = "handled by [global handler] except = " + ex.getClass().getName();
        return new Forward("begin");
    }

    /**
     * @jpf:exception-handler
     */
    @Jpf.ExceptionHandler(
        )
    protected Forward baseHandler(Exception ex, String actionName,
					  String message, Object form)
    {
	_message = "handled by [base handler] except = " + ex;
        return new Forward("begin");
    }

    /**
     * @jpf:exception-handler
     */
    @Jpf.ExceptionHandler(
        )
    protected Forward sub2Handler(Exception ex, String actionName,
					  String message, Object form)
    {
	_message = "handled by [sub2 handler] except = " + ex;
        return new Forward("begin");
    }

    /**
     * @jpf:exception-handler
     */
    @Jpf.ExceptionHandler(
        )
    protected Forward localSubHandler(ExceptBase ex, String actionName,
					  String message, Object form)
    {
	_message = "handled by [local sub handler] except = " + ex;
        return new Forward("begin");
    }
}
