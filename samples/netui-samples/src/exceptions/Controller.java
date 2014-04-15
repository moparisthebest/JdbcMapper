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
package exceptions;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;

/**
 * Demonstration of Page Flow declarative exception handling.
 */
@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin",path="index.jsp")
    },
    catches={
        // Catch Exception1, and simply forward to caught.jsp.
        @Jpf.Catch(type=Controller.Exception1.class, path="caught.jsp"),

        // Catch Exception2, and specify that the error message should come from key
        // messages.exception2.
        @Jpf.Catch(
            type=Controller.Exception2.class,
            path="caught.jsp",
            messageKey="messages.exception2"
        ),

        // Catch BaseException (which will catch Exception3), and specify a hardcoded message
        // (which can contain JSP 2.0-style expressions).  Handle the exception with the handleIt()
        // method.
        @Jpf.Catch(
            type=Controller.BaseException.class,
            method="handleIt",
            message="This is a hardcoded message from page flow ${pageFlow.URI}."
        )
    },
    messageBundles={
        @Jpf.MessageBundle(
            bundlePath="org.apache.beehive.samples.netui.resources.exceptions.messages"
        )
    }
)
public class Controller extends PageFlowController {
    public static abstract class BaseException
        extends Exception {
        public String getMessage() {
            return "This is the result of calling getMessage() on the exception.";
        }
    }

    public class Exception1 extends BaseException {
    }

    public class Exception2 extends BaseException {
    }

    public class Exception3 extends BaseException {
    }

    @Jpf.Action()
    public Forward throwException1()
        throws Exception1 {
        // Note that there is no need to catch Exception1 *inside* this action method; just put
        // Exception1 in the 'throws' for the method.
        throw new Exception1();
    }

    @Jpf.Action()
    public Forward throwException2()
        throws Exception2 {
        throw new Exception2();
    }

    @Jpf.Action()
    public Forward throwException3()
        throws Exception3 {
        throw new Exception3();
    }

    @Jpf.ExceptionHandler(
        forwards = {
        @Jpf.Forward(name = "caughtPage", path = "caught.jsp")
            }
    )
    public Forward handleIt(BaseException ex, String actionName, String message, Object formBean) {
        return new Forward("caughtPage");
    }
}
