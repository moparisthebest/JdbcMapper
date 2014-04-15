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
package miniTests.actionChaining;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;


@Jpf.Controller()
public class ActionChainingController extends PageFlowController
{
    private SomeForm _form;


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

   @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "action1b.do")
        })
    protected Forward action1a( SomeForm form )
    {
        saveForm( form );
        return new Forward( "success" );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "result.jsp")
        })
    protected Forward action1b( SomeForm form )
    {
        return new Forward( "success", "isSameForm", isSameForm( form ) );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "action2b.do")
        })
    protected Forward action2a( SomeForm form )
    {
        saveForm( form );
        return new Forward("success");
    }

    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "result.jsp")
        })
    protected Forward action2b( SomeForm form )
    {
        return new Forward( "success", "isSameForm", isSameForm( form ) );
    }

    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "action3b.do")
        })
    protected Forward action3a( SomeForm form )
    {
        saveForm( form );
        return new Forward( "success", form );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "result.jsp")
        })
    protected Forward action3b( SomeForm form )
    {
        return new Forward( "success", "isSameForm", isSameForm( form ) );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "action4b.do")
        })
    protected Forward action4a( SomeForm form )
    {
        saveForm( form );
        return new Forward( "success", form );
    }

    @Jpf.Action(
        useFormBean = "_form",
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "result.jsp")
        })
    protected Forward action4b( SomeForm form )
    {
        Boolean same = new Boolean( getRequest().getAttribute( "prevForm" ) == _form );
        return new Forward( "success", "isSameForm", same );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "action5b.do")
        })
    protected Forward action5a()
    {
        return new Forward( "success", new SomeForm() );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "result.jsp")
        })
    protected Forward action5b( AnotherForm form )
    {
        return new Forward( "success" );
    }

    public static class SomeForm implements Serializable
    {
    }

    public static class AnotherForm implements Serializable
    {
    }

    private void saveForm( Object form )
    {
        getRequest().setAttribute( "prevForm", form );
    }

    private Boolean isSameForm( Object form )
    {
        return new Boolean( form == getRequest().getAttribute( "prevForm" ) );
    }
}
