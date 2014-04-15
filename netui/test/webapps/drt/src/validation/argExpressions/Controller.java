package validation.argExpressions;

import org.apache.beehive.netui.pageflow.ExpressionMessage;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Validatable;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import javax.servlet.http.HttpServletRequest;

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
    public Forward validateMethod( Form1 form )
    {
        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", path="index.jsp")
    )
    public Forward validateAnnotations( Form2 form )
    {
        return new Forward( "index" );
    }

    public String getMessage()
    {
        return "These are some args.  arg0={0}, arg1={1}";
    }

    public String getArg0()
    {
        return "ARG0!";
    }

    public String getArg1()
    {
        return "ARG1!";
    }

    public static class Form1 implements Validatable
    {
        public void validate( ActionMapping mapping, HttpServletRequest request, ActionMessages errors )
        {
            errors.add( "foo", new ExpressionMessage( "(Form1) this is arg 0: {0}", new Object[]{ "${pageFlow.arg0}" } ) );
            errors.add( "bar", new ExpressionMessage( "(Form1) ${pageFlow.message}", new Object[]{ "${pageFlow.arg0}", "${pageFlow.arg1}" } ) );
        }
    }

    @Jpf.FormBean()
    public static class Form2
    {
        @Jpf.ValidatableProperty(
            validateRequired=@Jpf.ValidateRequired(
                message="(Form2) this is arg0: {0}",
                messageArgs={@Jpf.MessageArg(arg="${pageFlow.arg0}")}
            )
        )
        public String getFoo()
        {
            return "";
        }

        @Jpf.ValidatableProperty(
            validateRequired=@Jpf.ValidateRequired(
                message="(Form2) ${pageFlow.message}",
                messageArgs={@Jpf.MessageArg(arg="${pageFlow.arg0}"), @Jpf.MessageArg(arg="${pageFlow.arg1}")}
            )
        )
        public String getBar()
        {
            return "";
        }
    }
}
