package bugs.j631;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward submit( MyForm form )
    {
        return new Forward( "index" );
    }

    @Jpf.FormBean(messageBundle="bugs.j631.FormBeanMessages")
    public static class MyForm implements java.io.Serializable
    {
        private String _foo;

        @Jpf.ValidatableProperty(
            validateRequired=@Jpf.ValidateRequired(messageKey="required")
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
