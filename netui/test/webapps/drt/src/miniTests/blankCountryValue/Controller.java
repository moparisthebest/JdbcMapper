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
package miniTests.blankCountryValue;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Locale;

@Jpf.Controller(
    validatableBeans = {
        @Jpf.ValidatableBean(
            type = miniTests.blankCountryValue.Controller.ValidateForm.class,
            validatableProperties = {
                @Jpf.ValidatableProperty(
                    propertyName = "item",
                    validateMinLength = 
                        @Jpf.ValidateMinLength(
                            chars = 2,
                            message = "minimum length is 2 characters - All locales"),
                    localeRules = {
                        @Jpf.ValidationLocaleRules(
                            language = "fr",
                            country = "  ",
                            variant = "",
                            validateMinLength = 
                                @Jpf.ValidateMinLength(
                                    chars = 8,
                                    message = "minimum length is 8 characters - fr locale")
                        )
                    }
                )
            }
        )
    }
)
public class Controller extends PageFlowController
{
    private Locale savedLocale = null;

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


    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    },
                validationErrorForward = @Jpf.Forward(name = "errors",
                                                      path = "index.jsp"))
    protected Forward validate(ValidateForm form)
    {
        Forward forward = new Forward( "success" );
        return forward;
    }


    public static class ValidateForm implements Serializable
    {
        private String _item;

        public String getItem()
        { return this._item; }

        public void setItem(String item)
        { this._item = item; }
    }


    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward saveLocale()
    {
        savedLocale = getLocale();
        Forward forward = new Forward( "success" );
        return forward;
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward changeLocale()
    {
        setLocale( new Locale( "fr" ) );
        Forward forward = new Forward( "success" );
        return forward;
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward resetLocale()
    {
        setLocale( savedLocale );
        Forward forward = new Forward( "success" );
        return forward;
    }
}

