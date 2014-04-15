package validation.addExpressionMessages;

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
    },
    sharedFlowRefs={
        @Jpf.SharedFlowRef(name="sf", type=SharedFlow.class)
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="validation.addExpressionMessages.Messages")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward addMessages( MyBean bean )
    {
        getRequest().setAttribute( "requestMessage", "a message in the request with two arguments: {0} and {1}" );
        getSession().setAttribute( "sessionMessage", "a message in the session" );

        addActionErrorExpression( "prop1", "${pageFlow.pageFlowMessage}", null );
        addActionErrorExpression( "prop2", "${requestScope.requestMessage}", new Object[]{ "ARG1", "ARG2" } );
        addActionErrorExpression( "prop3", "${sessionScope.sessionMessage}", null );

        return new Forward( "index" );
    }

    @Jpf.Action(
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward submitMyBean( MyBean bean )
    {
        assert false;  // should never get here -- validation will always fail.
        return null;
    }

    public String getPageFlowMessage()
    {
        return "a message in page flow " + getURI();
    }

    public static class MyBean
        implements java.io.Serializable, Validatable
    {
        public void validate( ActionMapping mapping, HttpServletRequest request, ActionMessages errors )
        {
            errors.add( "prop4", new ExpressionMessage( "explicit string", null ) );
            errors.add( "prop5", new ExpressionMessage( "${bundle.default.bundleMessage}", null ) );
            errors.add( "prop6", new ExpressionMessage( "${sharedFlow.sf.sharedFlowMessage}", null ) );

            // The following is just to make sure that the "sessionMessage" string doesn't get left
            // hanging around in the session while the other tests run.
            request.getSession().removeAttribute( "sessionMessage" );
        }
    }
}
