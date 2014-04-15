package validation.validateExternalBean;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp"),
        @Jpf.SimpleAction(name="messagesFromPageFlow", path="messagesFromPageFlow.jsp"),
        @Jpf.SimpleAction(name="messagesFromBean", path="messagesFromBean.jsp")
    },
    messageBundles={
        @Jpf.MessageBundle(bundlePath="validation.validateExternalBean.PageFlowMessages")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward submitBean1( Bean1 bean )
    {
        return new Forward( "success" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="success", path="success.jsp")
        },
        validationErrorForward=@Jpf.Forward(name="failure", navigateTo=Jpf.NavigateTo.currentPage)
    )
    public Forward submitBean2( Bean2 bean )
    {
        return new Forward( "success" );
    }
}
