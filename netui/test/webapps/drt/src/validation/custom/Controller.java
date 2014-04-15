package validation.custom;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.Resources;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    customValidatorConfigs={
        "/validation/custom/customrules.xml"
    }
)
// Note: normally, the customrules.xml file would go in WEB-INF.  It's here to encapsulate the test.
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submit( MyForm form )
    {
        return new Forward( "success" );
    }

    @Jpf.FormBean()
    public static class MyForm
    {
        private String _val;

        // for internationalization, use 'displayNameKey', or an expression in 'displayName'
        @Jpf.ValidatableProperty(
            displayName="The value",
            validateRequired=@Jpf.ValidateRequired(),
            validateCustomRules={
                @Jpf.ValidateCustomRule(
                    rule="customRuleDivisibleBy",
                    message="{0} must be divisible by {1}.",
                    variables={@Jpf.ValidateCustomVariable(name="factor", value="3")}
                ),
                @Jpf.ValidateCustomRule(rule="customRulePalindrome", message="{0} must be a palindrome.")
            }
        )
        public String getVal()
        {
            return _val;
        }

        public void setVal( String val )
        {
            _val = val;
        }
    }

    /**
     * This is the method for the 'customRulePalindrome' rule.  Normally it would go in a separate class.
     */
    public static boolean validatePalindrome( Object bean, ValidatorAction va, Field field, ActionMessages errors,
                                              HttpServletRequest request, ServletContext servletContext )
    {
        String value;

        if ( bean == null || bean instanceof String )       
        {
            value = ( String ) bean;
        }
        else
        {
            value = ValidatorUtil.getValueAsString( bean, field.getProperty() );
        }

        if ( ! GenericValidator.isBlankOrNull( value ) )
        {
            for ( int i = 0, len = value.length(); i < len; ++i )
            {
                if ( value.charAt( i ) != value.charAt( len - i - 1 ) )
                {
                    errors.add( field.getKey(), Resources.getActionError( request, va, field ) );
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * This is the method for the 'customRuleDivisibleBy' rule.  Normally it would go in a separate class.
     */
    public static boolean validateDivisibleBy( Object bean, ValidatorAction va, Field field, ActionMessages errors,
                                               HttpServletRequest request, ServletContext servletContext )
    {
        String value;

        if ( bean == null || bean instanceof String )       
        {
            value = ( String ) bean;
        }
        else
        {
            value = ValidatorUtil.getValueAsString( bean, field.getProperty() );
        }

        if ( ! GenericValidator.isBlankOrNull( value ) )
        {

            try
            {
                String factor = field.getVarValue( "factor" );
                if ( Integer.parseInt( value ) % Integer.parseInt( factor ) == 0 ) return true;
            }
            catch ( NumberFormatException e )
            {
                // error will be returned below
            }

            errors.add( field.getKey(), Resources.getActionError( request, va, field ) );
            return false;
        }

        return true;
    }

}
