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
package validation.strutsValidator;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller(
    nested = true,
    strutsMerge = "/WEB-INF/strutsValidator-merge-config.xml",
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "validation.validator.Messages") 
    })
public class strutsValidatorController extends PageFlowController
{ 
    /** 
     * @jpf:action
     * @jpf:forward name="formPage" path="formPage.jsp" 
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "formPage",
                path = "formPage.jsp") 
        })
    public Forward begin()
    {
        return new Forward( "formPage" );
    }

    /**
     * @jpf:action validation-error-page="formPage.jsp"
     * @jpf:forward name="success" path="success.jsp"
     */
    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="formPage.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "success.jsp") 
        })
    public Forward submitForm( MyForm form ) 
    {
        return new Forward( "success" );
    }


    /**
     * @jpf:action
     * @jpf:forward name="previousPageFlowBegin" return-action="begin"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "previousPageFlowBegin",
                returnAction = "begin") 
        })
    public Forward exit()
    {
        return new Forward( "previousPageFlowBegin" );
    }

    /**
     * This form bean uses the ValidatorPlugIn (rules are in /WEB-INF/strutsValidator-validation.xml).
     */
    public static class MyForm extends org.apache.struts.validator.ValidatorForm
    {
        private String _email;
        private String _age;

        public String getEmail()
        {
            return _email;
        }

        public void setEmail( String email )
        {
            _email = email;
        }

        public String getAge()
        {
            return _age;
        }

        public void setAge( String age )
        {
            _age = age;
        }
    }
}
