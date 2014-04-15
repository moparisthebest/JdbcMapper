package validation.declarativeValidation;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Jpf.Controller(
    validatableBeans={
        @Jpf.ValidatableBean(
            type=Controller.UnvalidatedForm2.class,
            validatableProperties={
                @Jpf.ValidatableProperty(
                    propertyName="validWhenPageFlowProp",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateValidWhen=@Jpf.ValidateValidWhen(condition="${pageFlow.str==actionForm.validWhenPageFlowProp}", message="pageFlow.str must be the same as this")
                ),
                @Jpf.ValidatableProperty(
                    propertyName="minLength",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateMinLength=@Jpf.ValidateMinLength(chars=2)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="maxLength",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateMaxLength=@Jpf.ValidateMaxLength(chars=2)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="mask",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateMask=@Jpf.ValidateMask(regex="a*b")
                ),
                @Jpf.ValidatableProperty(
                    propertyName="typeInt",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateType=@Jpf.ValidateType(type=int.class)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="typeFloat",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateType=@Jpf.ValidateType(type=float.class)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="typeShort",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateType=@Jpf.ValidateType(type=short.class)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="typeLong",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateType=@Jpf.ValidateType(type=long.class)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="typeDouble",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateType=@Jpf.ValidateType(type=double.class)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="typeByte",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateType=@Jpf.ValidateType(type=byte.class)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="date",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateDate=@Jpf.ValidateDate(pattern="M-d-y")
                ),
                @Jpf.ValidatableProperty(
                    propertyName="range",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateRange=@Jpf.ValidateRange(minInt=1, maxInt=10)
                ),
                @Jpf.ValidatableProperty(
                    propertyName="creditCard",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateCreditCard=@Jpf.ValidateCreditCard()
                ),
                @Jpf.ValidatableProperty(
                    propertyName="email",
                    displayName="This field",
                    validateRequired=@Jpf.ValidateRequired(),
                    validateEmail=@Jpf.ValidateEmail()
                )
            }
        )
    }
)
public class Controller extends PageFlowController
{
    private String _str;
    private String _selectedAction = "formLevelValidate";

    public void setStr( String str )
    {
        _str = str;
    }

    public String getStr()
    {
        return _str;
    }

    public Map getActionChoices()
    {
        HashMap ret = new HashMap();
        ret.put( "formLevelValidate", "form-level validation" );
        ret.put( "actionLevelValidate", "action-level validation" );
        ret.put( "classLevelValidate", "class-level validation" );
        return ret;
    }

    public String getSelectedAction()
    {
        return _selectedAction;
    }

    public void setSelectedAction( String actionName )
    {
        _selectedAction = actionName;
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward begin()
    {
        //
        // This is a hack to ensure that running several DRTs against this page flow won't cause
        // failures due to state being left over from the previous test.
        //
        if ( "GET".equals( getRequest().getMethod() ) )
        {
            _str = null;
            _selectedAction = "formLevelValidate";
        }

        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="fail", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward formLevelValidate( ValidatedForm form )
    {
        return new Forward( "success" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.jsp")
        },
        validatableProperties={
            @Jpf.ValidatableProperty(
                propertyName="validWhenPageFlowProp",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateValidWhen=@Jpf.ValidateValidWhen(condition="${pageFlow.str==actionForm.validWhenPageFlowProp}", message="pageFlow.str must be the same as this")
            ),
            @Jpf.ValidatableProperty(
                propertyName="minLength",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateMinLength=@Jpf.ValidateMinLength(chars=2)
            ),
            @Jpf.ValidatableProperty(
                propertyName="maxLength",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateMaxLength=@Jpf.ValidateMaxLength(chars=2)
            ),
            @Jpf.ValidatableProperty(
                propertyName="mask",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateMask=@Jpf.ValidateMask(regex="a*b")
            ),
            @Jpf.ValidatableProperty(
                propertyName="typeInt",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateType=@Jpf.ValidateType(type=int.class)
            ),
            @Jpf.ValidatableProperty(
                propertyName="typeFloat",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateType=@Jpf.ValidateType(type=float.class)
            ),
            @Jpf.ValidatableProperty(
                propertyName="typeShort",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateType=@Jpf.ValidateType(type=short.class)
            ),
            @Jpf.ValidatableProperty(
                propertyName="typeLong",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateType=@Jpf.ValidateType(type=long.class)
            ),
            @Jpf.ValidatableProperty(
                propertyName="typeDouble",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateType=@Jpf.ValidateType(type=double.class)
            ),
            @Jpf.ValidatableProperty(
                propertyName="typeByte",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateType=@Jpf.ValidateType(type=byte.class)
            ),
            @Jpf.ValidatableProperty(
                propertyName="date",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateDate=@Jpf.ValidateDate(pattern="M-d-y")
            ),
            @Jpf.ValidatableProperty(
                propertyName="range",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateRange=@Jpf.ValidateRange(minInt=1, maxInt=10)
            ),
            @Jpf.ValidatableProperty(
                propertyName="creditCard",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateCreditCard=@Jpf.ValidateCreditCard()
            ),
            @Jpf.ValidatableProperty(
                propertyName="email",
                displayName="This field",
                validateRequired=@Jpf.ValidateRequired(),
                validateEmail=@Jpf.ValidateEmail()
            )
        },
        validationErrorForward=@Jpf.Forward(name="fail", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward actionLevelValidate( UnvalidatedForm form )
    {
        return new Forward( "success" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="fail", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward classLevelValidate( UnvalidatedForm2 form )
    {
        return new Forward( "success" );
    }

    public static class UnvalidatedForm implements Serializable
    {
        private String _validWhenPageFlowProp;

        public String getValidWhenPageFlowProp()
        {
            return _validWhenPageFlowProp;
        }

        public void setValidWhenPageFlowProp( String str )
        {
            _validWhenPageFlowProp = str;
        }

        private String _minLength;

        public String getMinLength()
        {
            return _minLength;
        }

        public void setMinLength( String str )
        {
            _minLength = str;
        }

        private String _maxLength;

        public String getMaxLength()
        {
            return _maxLength;
        }

        public void setMaxLength( String str )
        {
            _maxLength = str;
        }

        private String _mask;

        public String getMask()
        {
            return _mask;
        }

        public void setMask( String str )
        {
            _mask = str;
        }

        private String _typeInt;

        public String getTypeInt()
        {
            return _typeInt;
        }

        public void setTypeInt( String str )
        {
            _typeInt = str;
        }

        private String _typeFloat;

        public String getTypeFloat()
        {
            return _typeFloat;
        }

        public void setTypeFloat( String str )
        {
            _typeFloat = str;
        }

        private String _typeShort;

        public String getTypeShort()
        {
            return _typeShort;
        }

        public void setTypeShort( String str )
        {
            _typeShort = str;
        }

        private String _typeLong;

        public String getTypeLong()
        {
            return _typeLong;
        }

        public void setTypeLong( String str )
        {
            _typeLong = str;
        }

        private String _typeDouble;

        public String getTypeDouble()
        {
            return _typeDouble;
        }

        public void setTypeDouble( String str )
        {
            _typeDouble = str;
        }

        private String _typeByte;

        public String getTypeByte()
        {
            return _typeByte;
        }

        public void setTypeByte( String str )
        {
            _typeByte = str;
        }

        private String _date;

        public String getDate()
        {
            return _date;
        }

        public void setDate( String str )
        {
            _date = str;
        }

        private String _range;

        public String getRange()
        {
            return _range;
        }

        public void setRange( String str )
        {
            _range = str;
        }

        private String _creditCard;

        public String getCreditCard()
        {
            return _creditCard;
        }

        public void setCreditCard( String str )
        {
            _creditCard = str;
        }

        private String _email;

        public String getEmail()
        {
            return _email;
        }

        public void setEmail( String str )
        {
            _email = str;
        }
    }
    
    @Jpf.FormBean()
    public static class ValidatedForm implements Serializable
    {
        private String _validWhenPageFlowProp;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateValidWhen=@Jpf.ValidateValidWhen(condition="${pageFlow.str==actionForm.validWhenPageFlowProp}", message="pageFlow.str must be the same as this")
        )
        public String getValidWhenPageFlowProp()
        {
            return _validWhenPageFlowProp;
        }

        public void setValidWhenPageFlowProp( String str )
        {
            _validWhenPageFlowProp = str;
        }

        private String _minLength;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateMinLength=@Jpf.ValidateMinLength(chars=2)
        )
        public String getMinLength()
        {
            return _minLength;
        }

        public void setMinLength( String str )
        {
            _minLength = str;
        }

        private String _maxLength;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateMaxLength=@Jpf.ValidateMaxLength(chars=2)
        )
        public String getMaxLength()
        {
            return _maxLength;
        }

        public void setMaxLength( String str )
        {
            _maxLength = str;
        }

        private String _mask;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateMask=@Jpf.ValidateMask(regex="a*b")
        )
        public String getMask()
        {
            return _mask;
        }

        public void setMask( String str )
        {
            _mask = str;
        }

        private String _typeInt;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateType=@Jpf.ValidateType(type=int.class)
        )
        public String getTypeInt()
        {
            return _typeInt;
        }

        public void setTypeInt( String str )
        {
            _typeInt = str;
        }

        private String _typeFloat;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateType=@Jpf.ValidateType(type=float.class)
        )
        public String getTypeFloat()
        {
            return _typeFloat;
        }

        public void setTypeFloat( String str )
        {
            _typeFloat = str;
        }

        private String _typeShort;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateType=@Jpf.ValidateType(type=short.class)
        )
        public String getTypeShort()
        {
            return _typeShort;
        }

        public void setTypeShort( String str )
        {
            _typeShort = str;
        }

        private String _typeLong;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateType=@Jpf.ValidateType(type=long.class)
        )
        public String getTypeLong()
        {
            return _typeLong;
        }

        public void setTypeLong( String str )
        {
            _typeLong = str;
        }

        private String _typeDouble;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateType=@Jpf.ValidateType(type=double.class)
        )
        public String getTypeDouble()
        {
            return _typeDouble;
        }

        public void setTypeDouble( String str )
        {
            _typeDouble = str;
        }

        private String _typeByte;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateType=@Jpf.ValidateType(type=byte.class)
        )
        public String getTypeByte()
        {
            return _typeByte;
        }

        public void setTypeByte( String str )
        {
            _typeByte = str;
        }

        private String _date;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateDate=@Jpf.ValidateDate(pattern="M-d-y")
        )
        public String getDate()
        {
            return _date;
        }

        public void setDate( String str )
        {
            _date = str;
        }

        private String _range;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateRange=@Jpf.ValidateRange(minInt=1, maxInt=10)
        )
        public String getRange()
        {
            return _range;
        }

        public void setRange( String str )
        {
            _range = str;
        }

        private String _creditCard;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateCreditCard=@Jpf.ValidateCreditCard()
        )
        public String getCreditCard()
        {
            return _creditCard;
        }

        public void setCreditCard( String str )
        {
            _creditCard = str;
        }

        private String _email;

        @Jpf.ValidatableProperty(
            displayName="This field",
            validateRequired=@Jpf.ValidateRequired(),
            validateEmail=@Jpf.ValidateEmail()
        )
        public String getEmail()
        {
            return _email;
        }

        public void setEmail( String str )
        {
            _email = str;
        }
    }

    public static class UnvalidatedForm2 extends UnvalidatedForm
    {
    }
}
