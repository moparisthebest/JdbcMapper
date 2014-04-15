package org.apache.beehive.netui.test.pageflow.validation;

import org.apache.beehive.netui.pageflow.*;
import org.apache.beehive.netui.pageflow.annotations.*;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="org.apache.beehive.netui.test.pageflow.validation.Messages")
    }
)
public class Controller extends PageFlowController
{
    /**
     * Validate a non-FormData-derived bean.
     */ 
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submitAnyBean( AnyBeanForm form )
    {
        return new Forward( "index" );
    }
    
    
    /**
     * Validate a FormData-derived bean.
     */ 
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submitFormData( FormDataForm form )
    {
        return new Forward( "index" );
    }

    /**
     * Validate a FormData-derived bean.
     */ 
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submitValidatableFormData( ValidatableFormDataForm form )
    {
        return new Forward( "index" );
    }

    @Jpf.FormBean()
    public static class AnyBeanForm
            implements Serializable, Validatable
    {
        private String _foo;
        
        @Jpf.ValidatableProperty(
                validateMinLength=@Jpf.ValidateMinLength(chars=10, messageKey="message2")
                )
                public String getFoo()
        {
            return _foo;
        }
        
        public void setFoo( String foo )
        {
            _foo = foo;
        }
        
        public void validate( ActionMapping mapping, HttpServletRequest request, ActionMessages errors )
        {
            errors.add( "foo", new ActionMessage( "message1" ) );
        }
    }

    @Jpf.FormBean()
    public static class FormDataForm
            extends FormData
    {
        private String _foo;
        
        @Jpf.ValidatableProperty(
                validateMinLength=@Jpf.ValidateMinLength(chars=10, messageKey="message2")
                )
                public String getFoo()
        {
            return _foo;
        }
        
        public void setFoo( String foo )
        {
            _foo = foo;
        }
        
        public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
        {
            ActionErrors errors = super.validate( mapping, request );
            if ( errors == null ) errors = new ActionErrors();
            errors.add( "foo", new ActionMessage( "message1" ) );
            return errors;
        }
    }

    @Jpf.FormBean()
    public static class ValidatableFormDataForm
        extends FormData
        implements Validatable
    {
        private String _foo;

        @Jpf.ValidatableProperty(
            validateMinLength=@Jpf.ValidateMinLength(chars=10, messageKey="message2")
        )
        public String getFoo()
        {
            return _foo;
        }

        public void setFoo( String foo )
        {
            _foo = foo;
        }

        public void validate( ActionMapping mapping, HttpServletRequest request, ActionMessages errors )
        {
            errors.add( "foo", new ActionMessage( "message1" ) );
        }
    }
}
