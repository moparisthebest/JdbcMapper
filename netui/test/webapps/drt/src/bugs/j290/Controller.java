package bugs.j290;

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
    public Bean hiddenBean;
    public BeanTwo beanTwo;

    public String getAction()
    {
        return _action;
    }

    public String getText()
    {
        return _text;
    }

    @Jpf.Action(
            forwards = {
            @Jpf.Forward(name = "index", path = "index.jsp")
            }
            )
    protected Forward begin()
    {
        _action = "begin";
        return new Forward("index");
    }

    @Jpf.Action(
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward post(Bean postForm)
    {
        _action = "post";
        _text = postForm.getText();
        postForm.setText("");
        return new Forward("index");
    }

    @Jpf.Action(
        useFormBean="hiddenBean",
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward post2(Bean postForm)
    {
        _action = "post2";
        _text = postForm.getText();
        return new Forward("index");
    }

    @Jpf.Action(
        useFormBean="beanTwo",
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward post3(BeanTwo postForm)
    {
        _action = "post3";
        _text = postForm.getText();
        return new Forward("index");
    }

    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
	hiddenBean = new Bean();
        hiddenBean.setText("hiddenBean Defined");
	
	beanTwo = new BeanTwo();
        beanTwo.setText("beanTwo Defined");
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
    }

    public static class Bean implements Serializable
    {
        private String _text;

        public String getText()
        {
            return _text;
        }

        public void setText(String value)
        {
            _text = value;
        }
    }

    public static class BeanTwo implements Serializable
    {
        private String _text;

        public String getText()
        {
            return _text;
        }

        public void setText(String value)
        {
            _text = value;
        }
    }
}
