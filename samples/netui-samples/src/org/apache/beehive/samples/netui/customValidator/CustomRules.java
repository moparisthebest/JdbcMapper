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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.Resources;

/**
 * This class contains custom rules for the "Custom Validator" sample.
 */
public class CustomRules {
    /**
     * This is the method for the 'customRulePalindrome' rule.  It passes validation only if the
     * value is a palindrome String (reads the same backwards and forwards).
     */
    public static boolean validatePalindrome(Object bean,
                                             ValidatorAction va,
                                             Field field,
                                             ActionMessages errors,
                                             HttpServletRequest request,
                                             ServletContext servletContext) {
        String value;

        if (bean == null || bean instanceof String) {
            value = (String) bean;
        }
        else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (! GenericValidator.isBlankOrNull(value)) {
            for (int i = 0, len = value.length(); i < len; ++i) {
                if (value.charAt(i) != value.charAt(len - i - 1)) {
                    errors.add(field.getKey(), Resources.getActionError(request, va, field));
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * This is the method for the 'customRuleDivisibleBy' rule.  It passes validation only if the
     * value is a number divisible by the value of variable <code>factor</code>.
     */
    public static boolean validateDivisibleBy(Object bean,
                                              ValidatorAction va,
                                              Field field,
                                              ActionMessages errors,
                                              HttpServletRequest request,
                                              ServletContext servletContext) {
        String value;

        if (bean == null || bean instanceof String) {
            value = (String) bean;
        }
        else {
            value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        }

        if (! GenericValidator.isBlankOrNull(value)) {

            try {
                String factor = field.getVarValue("factor");
                if (Integer.parseInt(value) % Integer.parseInt(factor) == 0) return true;
            }
            catch (NumberFormatException e) {
                // error will be returned below
            }

            errors.add(field.getKey(), Resources.getActionError(request, va, field));
            return false;
        }

        return true;
    }
}
