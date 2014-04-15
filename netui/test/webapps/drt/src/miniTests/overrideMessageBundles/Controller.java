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
package miniTests.overrideMessageBundles;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="miniTests.overrideMessageBundles.Messages")
    }
)
public class Controller extends miniTests.overrideMessageBundles.base.Controller
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submit(MyForm form)
    {
        return new Forward("index");
    }

    @Jpf.FormBean()
    public static class MyForm implements java.io.Serializable
    {
        private String _foo;

        @Jpf.ValidatableProperty(
            validateRequired=@Jpf.ValidateRequired(messageKey="errRequired")
        )
        public String getFoo()
        {
            return _foo;
        }

        public void setFoo(String foo)
        {
            _foo = foo;
        }
    }
}
