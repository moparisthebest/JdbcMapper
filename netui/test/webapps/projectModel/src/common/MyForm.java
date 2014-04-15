package common;

import org.apache.beehive.netui.pageflow.annotations.Jpf;

@Jpf.FormBean(messageBundle="common.Messages")
public class MyForm
{
    private String _foo;

    @Jpf.ValidatableProperty(
        validateRequired=@Jpf.ValidateRequired(messageKey="error.required")
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
