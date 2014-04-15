package bugs.j522;

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
    private String _text;
    public String getAction() {
	return _action;
    }

    public String getText() {
        return _text;
}


    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
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
    protected Forward red()
    {
	_action = "red";
        return new Forward("index");
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward blue()
    {
	_action = "blue";
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
	_text = form.getText1();
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
        private String text1;

        private String text2;

        public String getText1()
        {
            return text1;
        }

        public void setText1(String value)
        {
            text1 = value;
        }

        public String getText2()
        {
            return text2;
        }

        public void setText2(String value)
        {
            text2 = value;
        }
    }
}
