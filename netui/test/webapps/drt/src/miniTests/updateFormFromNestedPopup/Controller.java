package miniTests.updateFormFromNestedPopup;

import miniTests.updateFormFromNestedPopup.getZip.GetZip;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;


@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="getZip", path="getZip/GetZip.jpf")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(doValidation=false)
    protected Forward zipSuccess( GetZip.ZipForm zip )
    {
        return new Forward( "_auto" );
    }

    @Jpf.Action()
    protected Forward zipCancel()
    {
        return new Forward( "_auto" );  // THIS IS THE MAGIC GLOBAL FORWARD
    }


    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name = "success",
                path = "results.jsp"
            )
        },
        validationErrorForward=@Jpf.Forward(name="fail", navigateTo=Jpf.NavigateTo.currentPage)
    )
    protected Forward submit(SubmitForm form)
    {
        return new Forward("success", "form", form );
    }


    @Jpf.FormBean()
    public static class SubmitForm
        implements Serializable
    {
        private String _name;
        private String _address;
        private String _city;
        private String _state;
        private String _zip;

        @Jpf.ValidatableProperty(
            displayName="The name",
            validateRequired=@Jpf.ValidateRequired()
        )
        public String getName()
        {
            return _name;
        }

        public void setName(String value)
        {
            _name = value;
        }

        public String getAddress()
        {
            return _address;
        }

        public void setAddress( String address )
        {
            _address = address;
        }

        public String getCity()
        {
            return _city;
        }

        public void setCity( String city )
        {
            _city = city;
        }

        @Jpf.ValidatableProperty(
            displayName="The state",
            validateRequired=@Jpf.ValidateRequired()
        )
        public String getState()
        {
            return _state;
        }

        public void setState( String state )
        {
            _state = state;
        }

        @Jpf.ValidatableProperty(
            displayName="The zip code",
            validateRequired=@Jpf.ValidateRequired()
        )
        public String getZip()
        {
            return _zip;
        }

        public void setZip(String value)
        {
            _zip = value;
        }
    }
}
