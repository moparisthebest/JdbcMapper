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
package miniTests.forwardToAction;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller()
public class ForwardToActionController extends PageFlowController
{
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "withoutForm",
                path = "actionWithoutForm.do"),
            @Jpf.Forward(
                name = "withForm",
                path = "actionWithForm.do") 
        })
    protected Forward someAction(SomeActionForm form)
    {
        if ( getRequest().getParameter( "withForm" ) != null )
        {
            return new Forward( "withForm" );
        }
        else
        {
            return new Forward( "withoutForm" );
        }
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "result.jsp") 
        })
    protected Forward actionWithoutForm()
    {
        return new Forward("success");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "result.jsp") 
        })
    protected Forward actionWithForm(AnotherForm form)
    {
        return new Forward("success");
    }

    public static class SomeActionForm implements Serializable
    {
        private String foo;

        public void setFoo(String foo)
        {
            this.foo = foo;
        }

        public String getFoo()
        {
            return this.foo;
        }
    }

    public static class AnotherForm implements Serializable
    {
        private String bar;

        public void setBar(String bar)
        {
            this.bar = bar;
        }

        public String getBar()
        {
            return this.bar;
        }
    }
}
