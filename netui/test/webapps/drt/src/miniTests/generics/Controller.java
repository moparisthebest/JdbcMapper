package miniTests.generics;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller(
    simpleActions={@Jpf.SimpleAction(name="begin", path="index.jsp")}
)
public class Controller< T extends BaseFormBean > extends PageFlowController
{
    private T _form;

    @Jpf.Action(
        useFormBean="_form",
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    ) 
    protected Forward submit( T form )
    {
        return new Forward("index");
    }

    public String getFoo()
    {
        return _form != null ? _form.getFoo() : "[empty]";
    }
}
