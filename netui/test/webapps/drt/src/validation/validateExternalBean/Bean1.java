package validation.validateExternalBean;

import org.apache.beehive.netui.pageflow.annotations.Jpf;
import java.io.Serializable;

@Jpf.FormBean()
public class Bean1
    implements Serializable
{
    private String _foo;
    private String _bar;

    @Jpf.ValidatableProperty(
        validateMinLength=@Jpf.ValidateMinLength(
            chars=2,
            messageKey="messages.foo",
            messageArgs={@Jpf.MessageArg(argKey="args.foo")}
        )
    )
    public String getFoo()
    {
        return _foo;
    }

    public void setFoo( String foo )
    {
        _foo = foo;
    }

    @Jpf.ValidatableProperty(
        validateMinLength=@Jpf.ValidateMinLength(
            chars=2,
            message="${actionForm.message}",
            messageArgs={@Jpf.MessageArg(arg="${actionForm.arg}")}
        )
    )
    public String getBar()
    {
        return _bar;
    }

    public void setBar( String bar )
    {
        _bar = bar;
    }

    public String getMessage()
    {
        return "message from " + getClass().getName() + ".getMessage(), with arg: {0}";
    }

    public String getArg()
    {
        return "ARGH!";
    }
}
