package miniTests.sameBeanDifferentScope;

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
    private MyForm _myForm = new MyForm( "init val" );

    @Jpf.Action(
        useFormBean="_myForm",
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward withPageFlowScopedForm( MyForm form )
    {
        return new Forward( "index" );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward withoutPageFlowScopedForm( MyForm form )
    {
        return new Forward( "index" );
    }

    public static class MyForm implements java.io.Serializable
    {
        private String _foo;
        public MyForm() {}
        public MyForm( String foo ) { _foo = foo; }
        public void setFoo( String foo ) { _foo = foo; }
        public String getFoo() { return _foo; }
    }
}
