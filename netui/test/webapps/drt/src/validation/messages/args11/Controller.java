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
package validation.messages.args11;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import validation.messages.ValidatableFormData;

import javax.servlet.http.HttpSession;

@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(bundlePath = "validation.messages.messages")
    },
    validatorVersion = Jpf.ValidatorVersion.oneOne,
    validatableBeans = {
        @Jpf.ValidatableBean(
            type = validation.messages.ValidatableFormData.class,
            validatableProperties = {
                @Jpf.ValidatableProperty(
                    propertyName = "item1",
                    validateMinLength = 
                        @Jpf.ValidateMinLength(
                            chars = 2,
                            message = "minimum length for {0} is {1} chars",
                            messageArgs = {
                                @Jpf.MessageArg(
                                    arg = "item1"
                                ),
                                @Jpf.MessageArg(
                                    arg = "two"
                                )
                            }
                        )
                ),
                @Jpf.ValidatableProperty(
                    propertyName = "item2",
                    validateMinLength = 
                        @Jpf.ValidateMinLength(
                            chars = 4,
                            messageKey = "minlength.msg",
                            messageArgs = {
                                @Jpf.MessageArg(
                                    argKey = "item2.minlength.name"
                                ),
                                @Jpf.MessageArg(
                                    argKey = "item2.minlength.length"
                                )
                            }
                        )
                ),
                @Jpf.ValidatableProperty(
                    propertyName = "item3",
                    validateMinLength = 
                        @Jpf.ValidateMinLength(
                            chars = 6,
                            message = "{0} {1} for {2} is {3} chars",
                            messageArgs = {
                                @Jpf.MessageArg(
                                    arg = "Min",
                                    position = 0
                                ),
                                @Jpf.MessageArg(
                                    arg = "length"
                                ),
                                @Jpf.MessageArg(
                                    arg = "item3"
                                ),
                                @Jpf.MessageArg(
                                    arg = "six"
                                )
                            }
                        )
                ),
                @Jpf.ValidatableProperty(
                    propertyName = "item4",
                    validateMinLength =
                        @Jpf.ValidateMinLength(
                            chars = 8,
                            messageKey = "minlength.msg",
                            messageArgs = {
                                @Jpf.MessageArg(
                                    argKey = "item4.minlength.length",
                                    position = 1
                                ),
                                @Jpf.MessageArg(
                                    argKey = "item4.minlength.name",
                                    position = 0
                                )
                            }
                        )
                )
            }
        )
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }


    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }


    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "index.jsp")
        },
        validatableProperties={
            @Jpf.ValidatableProperty(
                propertyName = "item1",
                validateMaxLength = 
                    @Jpf.ValidateMaxLength(
                        chars = 4,
                        message = "maximum length for {0} is {1} chars",
                        messageArgs = {
                            @Jpf.MessageArg(
                                arg = "item1"
                            ),
                            @Jpf.MessageArg(
                                arg = "four"
                            )
                        }
                    )
            )
        },
        validationErrorForward = @Jpf.Forward(name = "errors",
                                              path = "index.jsp")
    )
    protected Forward validate(ValidatableFormData form)
    {
        Forward forward = new Forward( "success" );
        return forward;
    }
}

