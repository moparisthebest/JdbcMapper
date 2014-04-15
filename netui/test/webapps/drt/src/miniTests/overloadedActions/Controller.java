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
package miniTests.overloadedActions;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.io.Serializable;

import java.util.Arrays;


/**
 * @jpf:forward name="page1" path="page1.jsp"
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "page1",
            path = "page1.jsp") 
    })
public class Controller extends PageFlowController
{
    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward begin()
    {
        return new Forward( "page1" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward conflictsWithNonAction()
    {
        return new Forward( "page1" );
    }

    private void conflictsWithNonAction( String s )
    {
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="nest" path="nested/Controller.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nest",
                path = "nested/Controller.jpf") 
        })
    public Forward nestReturnNoForm()
    {
        return new Forward( "nest" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="nest" path="nested/Controller.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nest",
                path = "nested/Controller.jpf") 
        })
    public Forward nestReturnForm1()
    {
        return new Forward( "nest", new Form1() );
    }

    /**
     * @jpf:action
     * @jpf:forward name="nest" path="nested/Controller.jpf"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nest",
                path = "nested/Controller.jpf") 
        })
    public Forward nestReturnForm2()
    {
        return new Forward( "nest", new Form2() );
    }

    /**
     * @jpf:action
     * @jpf:forward name="nest" path="nested/returnUnknownForm.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nest",
                path = "nested/returnUnknownForm.do") 
        })
    public Forward nestReturnUnknownForm()
    {
        return new Forward( "nest" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="nest" path="nested/returnNormalWithForm.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nest",
                path = "nested/returnNormalWithForm.do") 
        })
    public Forward nestReturnNormalWithForm()
    {
        return new Forward( "nest" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="nest" path="nested/returnNormalWithoutForm.do"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "nest",
                path = "nested/returnNormalWithoutForm.do") 
        })
    public Forward nestReturnNormalWithoutForm()
    {
        return new Forward( "nest" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward overloaded( Form2 form )
    {
        getRequest().setAttribute( "message", "overloaded( Form2 )" );
        return new Forward( "page1" );
    }


    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward overloaded()
    {
        getRequest().setAttribute( "message", "overloaded()" );
        return new Forward( "page1" );
    }


    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward overloaded( Form1 form )
    {
        getRequest().setAttribute( "message", "overloaded( Form1 )" );
        return new Forward( "page1" );
    }

    /**
     * Note that we should get in here even though the nested pageflow returned
     * an action without a form.  This is because in that case it's just like
     * a page raising an action -- the form is inferred from the action.
     *
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward normalReturnWithoutForm( Form2 form )
    {
        getRequest().setAttribute( "message", "normalReturnWithoutForm()" );
        return new Forward( "page1" );
    }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    public Forward normalReturnWithForm( Form1 form )
    {
        getRequest().setAttribute( "message", "normalReturnWithForm( Form1 )" );
        return new Forward( "page1" );
    }

    public String[] getSortedActions()
    {
        String[] actions = getActions();
        Arrays.sort( actions );
        return actions;
    }

    public static class Form1 implements Serializable {}
    public static class Form2 implements Serializable {}
}
