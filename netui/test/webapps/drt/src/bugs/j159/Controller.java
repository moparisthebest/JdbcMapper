package bugs.j159;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _action;

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="checkBoxOption.jsp")
        }
    )
    protected Forward begin()
    {
	_action = "begin";
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward post(Bean form)
    {
	_action = "post";
        return new Forward("index");
    }

    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }

    public static class Bean implements Serializable
    {
        private String[] checks;
	private String radios;
        public String[] getChecks()
        {
            return checks;
        }

        public void setChecks(String[] value)
        {
            checks = value;
        }
        public String getRadios()
        {
            return radios;
        }

        public void setRadios(String value)
        {
            radios = value;
        }
    }
}
