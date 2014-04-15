package validation.validateExternalBean;

import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.FormBean(
    messageBundle="validation.validateExternalBean.BeanMessages"
)
public class Bean2
    extends Bean1
{
    private String _baz;

    @Jpf.ValidatableProperty(
        validateMinLength=@Jpf.ValidateMinLength(
            chars=2,
            messageKey="messages.baz",
            messageArgs={@Jpf.MessageArg(argKey="args.baz")}
        )
    )
    public String getBaz()
    {
        return _baz;
    }

    public void setBaz( String baz )
    {
        _baz = baz;
    }
}

