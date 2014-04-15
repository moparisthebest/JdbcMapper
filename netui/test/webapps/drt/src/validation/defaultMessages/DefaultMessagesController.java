package validation.defaultMessages;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class DefaultMessagesController extends PageFlowController
{
    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward begin()
    {
        return new Forward("success");
    }


    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }


    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "success.jsp")
    },
        validationErrorForward=@Jpf.Forward(name="fail", navigateTo=Jpf.NavigateTo.currentPage)
    )
    protected Forward validate(TheForm form)
    {
        Forward forward = new Forward("success");
        return forward;
    }


    @Jpf.FormBean()
    public static class TheForm implements Serializable
    {
        private String _required;
        private String _minlength;
        private String _maxlength;
        private String _byte;
        private String _short;
        private String _int;
        private String _float;
        private String _long;
        private String _double;
        private String _date;
        private String _range;
        private String _creditcard;
        private String _email;

        @Jpf.ValidatableProperty(displayName="This field", validateRequired = @Jpf.ValidateRequired())
        public String getRequired()
        {
            return _required;
        }

        public void setRequired( String required )
        {
            _required = required;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateMinLength = @Jpf.ValidateMinLength(chars=2))
        public String getMinlength()
        {
            return _minlength;
        }

        public void setMinlength( String minlength )
        {
            _minlength = minlength;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateMaxLength = @Jpf.ValidateMaxLength(chars=2))
        public String getMaxlength()
        {
            return _maxlength;
        }

        public void setMaxlength( String maxlength )
        {
            _maxlength = maxlength;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateType = @Jpf.ValidateType(type=byte.class))
        public String getByte()
        {
            return _byte;
        }

        public void setByte( String aByte )
        {
            _byte = aByte;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateType = @Jpf.ValidateType(type=short.class))
        public String getShort()
        {
            return _short;
        }

        public void setShort( String aShort )
        {
            _short = aShort;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateType = @Jpf.ValidateType(type=int.class))
        public String getInt()
        {
            return _int;
        }

        public void setInt( String anInt )
        {
            _int = anInt;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateType = @Jpf.ValidateType(type=float.class))
        public String getFloat()
        {
            return _float;
        }

        public void setFloat( String aFloat )
        {
            _float = aFloat;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateType = @Jpf.ValidateType(type=long.class))
        public String getLong()
        {
            return _long;
        }

        public void setLong( String aLong )
        {
            _long = aLong;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateType = @Jpf.ValidateType(type=double.class))
        public String getDouble()
        {
            return _double;
        }

        public void setDouble( String aDouble )
        {
            _double = aDouble;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateDate = @Jpf.ValidateDate(pattern="MM/dd/yyyy"))
        public String getDate()
        {
            return _date;
        }

        public void setDate( String date )
        {
            _date = date;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateRange = @Jpf.ValidateRange(minInt=5, maxInt=10))
        public String getRange()
        {
            return _range;
        }

        public void setRange( String range )
        {
            _range = range;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateCreditCard = @Jpf.ValidateCreditCard())
        public String getCreditcard()
        {
            return _creditcard;
        }

        public void setCreditcard( String creditcard )
        {
            _creditcard = creditcard;
        }

        @Jpf.ValidatableProperty(displayName="This field",validateEmail = @Jpf.ValidateEmail())
        public String getEmail()
        {
            return _email;
        }

        public void setEmail( String email )
        {
            _email = email;
        }
    }
}
