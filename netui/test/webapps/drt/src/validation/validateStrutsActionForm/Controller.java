package validation.validateStrutsActionForm;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="validation.validateStrutsActionForm.PageFlowMessages")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validatableProperties={
            @Jpf.ValidatableProperty(
                propertyName="foo",
                validateMinLength=@Jpf.ValidateMinLength(chars=2, messageKey="actionMessage")
            )
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submit( MyForm form )
    {
        return new Forward( "index" );
    }

    @Jpf.FormBean(
        messageBundle="validation.validateStrutsActionForm.FormBeanMessages"
    )
    public static class MyForm extends org.apache.struts.action.ActionForm
    {
        private String _foo;

        @Jpf.ValidatableProperty(
            validateRequired=@Jpf.ValidateRequired(messageKey="beanMessage")
        )
        public String getFoo()
        {
            return _foo;
        }

        public void setFoo( String foo )
        {
            _foo = foo;
        }
    }
}
