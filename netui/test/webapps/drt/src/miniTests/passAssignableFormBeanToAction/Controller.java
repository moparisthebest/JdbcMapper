package miniTests.passAssignableFormBeanToAction;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

@Jpf.Controller(
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    }
)
public class Controller extends PageFlowController
{
    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="action2", action="action2")
        }
    )
    public Forward passBaseToAction2()
    {
        return new Forward( "action2", new BaseFormBean() );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="action2", action="action2")
        }
    )
    public Forward passSubclassToAction2()
    {
        return new Forward( "action2", new SubclassFormBean() );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward action2( BaseFormBean form )
    {
        return new Forward( "index", "formValue", "action2: " + form.getValue() );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="action3", action="action3")
        }
    )
    public Forward passInterfaceImplementorToAction3()
    {
        return new Forward( "action3", new SubclassFormBean() );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    public Forward action3( SomeInterface si )
    {
        return new Forward( "index", "formValue", "action3: " + si.getValue() );
    }

    public interface SomeInterface
    {
        public String getValue();
    }

    public static class BaseFormBean implements Serializable
    {
        public String getValue() { return "base"; }
    }

    public static class SubclassFormBean extends BaseFormBean implements SomeInterface
    {
        public String getValue() { return "subclass"; }
    }
}
