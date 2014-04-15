package miniTests.updateFormFromNested;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;


@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="zipCancel", navigateTo=Jpf.NavigateTo.currentPage)
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="success",
                navigateTo=Jpf.NavigateTo.currentPage
            )
        }
    )
    protected Forward zipSuccess( String zip )
    {
        SubmitForm previousForm = ( SubmitForm ) getPreviousFormBean();
        previousForm.setZip( zip );
        Forward success = new Forward( "success", previousForm );
        return success;
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


    /**
     * This action forwards to the nested page flow to gather the zip.  Note that it takes a
     * SubmitForm so we can update the form with the zip code in {@link #zipSuccess}, but we've
     * explicitly turned validation off for this action, since the form may be incomplete.
     */
    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name = "getZipFlow",
                path = "getZip/GetZip.jpf"
            )
        },
        doValidation=false
    )
    protected Forward getZip(SubmitForm form)
    {
        return new Forward("getZipFlow");
    }


    @Jpf.FormBean()
    public static class SubmitForm implements Serializable
    {
        private String _name;
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

    /**
     * For performance reasons, getPreviousFormBean() (used in {@link #zipSuccess}) will not
     * be enabled unless we use <code>Jpf.NavigateTo.previousAction</code> in this page flow
     * flow, <i>or</i> we provide this override.  So here we provide the override.
     */
    protected boolean alwaysTrackPreviousAction()
    {
        return true;
    }
}

