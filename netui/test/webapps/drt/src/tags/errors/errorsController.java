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
package tags.errors;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Jpf.Controller(
    messageBundles = {
        @Jpf.MessageBundle(
            bundlePath = "errors.Messages1"),
        @Jpf.MessageBundle(
            bundlePath = "errors.Messages2",
            bundleName = "anotherBundle") 
    }
)
public class errorsController extends PageFlowController
{
    /**
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     * @jpf:forward name="success" return-to="currentPage" 
     * @jpf:validation-error-forward name="current" return-to="currentPage"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                navigateTo = Jpf.NavigateTo.currentPage) 
        },
        validationErrorForward=@Jpf.Forward(name="current", navigateTo=Jpf.NavigateTo.currentPage))
    protected Forward errorAction( SomeActionForm form )
    {
        return new Forward( "success" );
    }

    /**
     * @jpf:action
     * @jpf:validation-error-forward name="success" path="errors1.jsp"
     */
    @Jpf.Action(
        validationErrorForward=@Jpf.Forward(name="success", path="errors1.jsp"))
    protected Forward errorsAction1( SomeActionForm form )
    {
        return null;
    }

    /**
     * @jpf:action
     * @jpf:validation-error-forward name="success" path="errors4.jsp"
     */
    @Jpf.Action(
        validationErrorForward=@Jpf.Forward(name="success", path="errors4.jsp"))
    protected Forward errorsAction4( SomeActionForm form )
    {
        return null;
    }

    /**
     * @jpf:action
     * @jpf:forward name="prev" return-to="previousPage"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "prev",
                navigateTo = Jpf.NavigateTo.previousPage) 
        })
    protected Forward goPrev()
    {
        return new Forward( "prev" );
    }

    /**
     * @jpf:action
     * @jpf:validation-error-forward name="success" path="errors2.jsp"
     */
    @Jpf.Action(
        validationErrorForward=@Jpf.Forward(name="success", path="errors2.jsp"))
    protected Forward errorsAction2( SomeActionForm form )
    {
        return null;
    }

    /**
     * @jpf:action
     * @jpf:validation-error-forward name="success" path="errors3.jsp"
     */
    @Jpf.Action(
        validationErrorForward=@Jpf.Forward(name="success", path="errors3.jsp"))
    protected Forward errorsAction3( SomeActionForm form )
    {
        return null;
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class SomeActionForm implements Serializable, Validatable
    {
        public void validate(ActionMapping mapping, HttpServletRequest request, ActionMessages errors)
        {
            if ( request.getParameter( "showDefaultError" ) != null )
            {
                errors.add( "defaultProperty", new ActionError( "defaultPropertyMessage" ) );
            }
            
            if ( request.getParameter( "showBundleError1" ) != null )
            {
                errors.add( "bundleProperty1", new ActionError( "bundlePropertyMessage1", "ARG1" ) );
            }
            
            if ( request.getParameter( "showBundleError2" ) != null )
            {
                errors.add( "bundleProperty2", new ActionError( "bundlePropertyMessage2", "ARG1", "ARG2" ) );
            }
            
            if ( request.getParameter( "showRootError" ) != null )
            {
                errors.add( "rootProperty", new ActionError( "rootPropertyMessage", new Object[]{ "ARG1", "ARG2", "ARG3" } ) );
            }
        }
    }
}
