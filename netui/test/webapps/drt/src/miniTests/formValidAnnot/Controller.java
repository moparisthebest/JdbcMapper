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
package miniTests.formValidAnnot;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * PageFlow class generated from control DbStuff
 * @jpf:message-resources resources="error.Errors"
 */
@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "error.Errors")
    })
public class Controller extends org.apache.beehive.netui.pageflow.PageFlowController
{
    private EmptyForm form;

    public EmptyForm getForm()
    {
        return form;
    }

    public void setForm(EmptyForm form)
    {
        this.form = form;
    }
    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="Begin.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Begin.jsp")
        })
    protected Forward begin()
    {
        return new Forward( "success" );
    }

    /**
     * @jpf:action validation-error-page="Begin.jsp"
     * @jpf:forward name="success" path="Results.jsp"
     */
    @Jpf.Action(
        validationErrorForward = @Jpf.Forward(
            name="validationFailure",
            path="Begin.jsp"),
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "Results.jsp")
        })
    protected Forward formAction(EmptyForm form)
    {
        this.form = form;
        return new Forward("success");
    }

    public static class EmptyForm implements Serializable, Validatable
    {
        private java.lang.String type;

        private java.lang.String name;


        public void setName(java.lang.String name)
        {
            this.name = name;
        }

        public java.lang.String getName()
        {
            return this.name;
        }

        public void setType(java.lang.String type)
        {
            this.type = type;
        }

        public java.lang.String getType()
        {
            return this.type;
        }

        public void validate(ActionMapping mapping, HttpServletRequest request, ActionMessages errors)
        {
            if (name == null) {
                errors.add("name", new ActionError("NameError"));
            }
            else {
                if (Character.toUpperCase(name.charAt(0)) != name.charAt(0)) {
                    errors.add("name", new ActionError("NameError"));
                }
            }
            if (type == null || (!type.equals("bar") && !type.equals("foo"))) {
                errors.add("te", new ActionError("TypeError"));
            }
        }
    }
}
