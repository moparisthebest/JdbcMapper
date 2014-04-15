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
package org.apache.beehive.samples.netui.customValidator;

import org.apache.beehive.netui.pageflow.annotations.Jpf;

/**
 * This is the form bean for the "Custom Validator" sample.  It defines its own message bundle, but
 * you could also just use messages in the page flow's default message bundle.
 */
@Jpf.FormBean(messageBundle = "org.apache.beehive.samples.netui.resources.customvalidator.messages")
public class MyFormBean {
    private String _palindrome;
    private String _divisibleBy3;
    private String _palindromeDivisibleBy5;

    @Jpf.ValidatableProperty(
        displayNameKey = "displayname.palindrome",
        validateRequired = @Jpf.ValidateRequired(),
        validateCustomRules = {
            @Jpf.ValidateCustomRule(rule = "customRulePalindrome")
        }
    )
    public String getPalindrome() {
        return _palindrome;
    }

    public void setPalindrome(String palindrome) {
        _palindrome = palindrome;
    }

    @Jpf.ValidatableProperty(
        displayNameKey = "displayname.divisibleBy3",
        validateRequired = @Jpf.ValidateRequired(),
        validateCustomRules = {
            @Jpf.ValidateCustomRule(
                rule = "customRuleDivisibleBy",
                variables = {
                    @Jpf.ValidateCustomVariable(name = "factor", value = "3")
                }
            )
        }
    )
    public String getDivisibleBy3() {
        return _divisibleBy3;
    }

    public void setDivisibleBy3(String divisibleBy3) {
        _divisibleBy3 = divisibleBy3;
    }

    @Jpf.ValidatableProperty(
        displayNameKey = "displayname.palindromeDivisibleBy5",
        validateRequired = @Jpf.ValidateRequired(),
        validateCustomRules = {
            @Jpf.ValidateCustomRule(
                rule = "customRuleDivisibleBy",
                variables = {
                    @Jpf.ValidateCustomVariable(name = "factor", value = "5")
                }
            ),
            @Jpf.ValidateCustomRule(rule = "customRulePalindrome")
        }
    )
    public String getPalindromeDivisibleBy5() {
        return _palindromeDivisibleBy5;
    }

    public void setPalindromeDivisibleBy5(String palindromeDivisibleBy5) {
        _palindromeDivisibleBy5 = palindromeDivisibleBy5;
    }
}

