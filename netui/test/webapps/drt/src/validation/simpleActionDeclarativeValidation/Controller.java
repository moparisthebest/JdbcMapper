package validation.simpleActionDeclarativeValidation;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(
            name="submit",
            path="index.jsp",
            useFormBean="_form",
            validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp"),
            validatableProperties={
                @Jpf.ValidatableProperty(
                    displayName="Bar",
                    propertyName="bar",
                    validateRequired=@Jpf.ValidateRequired()
                )
            }
        )
    }
)
public class Controller extends PageFlowController
{
    private MyForm _form;

    public static class MyForm implements java.io.Serializable
    {
        private String _bar;

        public String getBar()
        {
            return _bar;
        }

        public void setBar( String bar )
        {
            _bar = bar;
        }
    }
}
