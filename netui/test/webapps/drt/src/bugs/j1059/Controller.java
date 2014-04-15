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
package bugs.j1059;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(bundlePath="bugs.j1059.messages")
    },
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    public static class MyFormBean implements java.io.Serializable {
        String name;
        public String getName() {
            return name;
        }
        public void setName(String value) {
            name = value;
        }
    }

    public static class BogusException extends Exception {
        Object[] messageArgs = null;
        public BogusException(String msg) {
            super(msg);
        }
        public void setMessageArgs(Object[] args) {
            messageArgs = args;
        }
        public Object[] getMessageArgs() {
            return messageArgs;
        }
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="success", path="index.jsp")
        },
        catches = {
            @Jpf.Catch(
                type = Controller.BogusException.class,
                method = "bogusExceptionHandler",
                messageKey="messageKey_1")
        }
    )
    protected Forward testAction(MyFormBean bean)
            throws BogusException {
        String name = bean.getName();
        if (name != null && name.indexOf('X') != -1) {
            BogusException ex2 =
                new BogusException("testAction failed... BogusException msg.");
            ex2.setMessageArgs(new Object[] {"name", "contains the character, 'X'"});
            throw ex2;
        }
        return new Forward("success");
    }

    @Jpf.ExceptionHandler(
        forwards = {
            @Jpf.Forward(
                name = "handleError",
                path = "index.jsp")
        })
    protected Forward bogusExceptionHandler( Controller.BogusException ex,
                                           String actionName,
                                           String message, Object form ) {
        Object[] args = ex.getMessageArgs();
        addActionError("name", "messageKey_2", args);
        return new Forward( "handleError" );
    }
}
