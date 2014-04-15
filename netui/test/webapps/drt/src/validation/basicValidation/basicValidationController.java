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
package validation.basicValidation;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMapping;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;


/**
 * @jpf:controller nested="true"
 * @jpf:message-resources resources="validation.basic.Messages"
 */
@Jpf.Controller(
    nested = true,
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "validation.basic.Messages") 
    })
public class basicValidationController extends PageFlowController
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
     * @jpf:action
     * @jpf:forward name="success" path="success.jsp"
     * @jpf:validation-error-forward name="failure" return-to="currentPage"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "success.jsp") 
        },
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage))
    public Forward submitForm( Form form )
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
     * This form bean does validation manually.
     */
    public static class Form implements Serializable, Validatable
    {
        private String _email;
        private String _zipCode;

        public String getEmail()
        {
            return _email;
        }

        public void setEmail( String email )
        {
            _email = email;
        }

        public String getZipCode()
        {
            return _zipCode;
        }

        public void setZipCode( String zipCode )
        {
            _zipCode = zipCode;
        }

        public void validate(ActionMapping mapping, HttpServletRequest request, ActionMessages errors)
        {
            int at = _email.indexOf( '@' );
            int dot = _email.lastIndexOf( '.' );

            if ( at == -1 || at == 0 || dot == -1 || at > dot )
            {
                errors.add( "email", new ActionError( "badEmail" ) );
            }

            if ( _zipCode.length() != 5 )
            {
                errors.add( "zipCode", new ActionError( "badZip", new Integer( 5 ) ) );
            }
        }
    }
}
