package pageFlowCore.returnToTest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class InputForm implements Serializable
{
    private String bar;
    private String foo;


    public void setFoo(String foo)
    {
        this.foo = foo;
    }

    public String getFoo()
    {
        return this.foo;
    }

    public void setBar(String bar)
    {
        this.bar = bar;
    }

    public String getBar()
    {
        return this.bar;
    }

    public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
    {
        if ( foo.length() == 0 )
        {
            ActionErrors errs = new ActionErrors();
            errs.add( "foo", new ActionError( "anError" ) );
            return errs;
        }

        return null;
    }
}
