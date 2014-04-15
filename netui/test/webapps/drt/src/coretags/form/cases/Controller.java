package coretags.form.cases;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Enumeration;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private String _action;
    private String _text;
    private String _textPage;
    public BeanThree useBean;
    private Bean _direct;

    public String getAction()
    {
        return _action;
    }

    public String getText()
    {
        return _text;
    }

    public void setTextPage(String text) {
        _textPage = text;
	_text = text;
    }

    public String getTextPage() {
        return _textPage;
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
            forwards = {
            @Jpf.Forward(name = "index", path = "html.jsp")
            }
            )
    protected Forward html()
    {
        _action = "html";
        return new Forward("index");
    }

    @Jpf.Action(
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward post(Bean postForm)
    {
        dumpRequest();
        _action = "post";
        _text = postForm.getText();
        postForm.setText("");
        return new Forward("index");
    }

    @Jpf.Action(
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward postNoForm()
    {
        dumpRequest();
        _action = "postNoForm";
        return new Forward("index");
    }

    @Jpf.Action(
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward postOverride(Bean postOverForm)
    {
        dumpRequest();
        _action = "postOverride";
        _text = postOverForm.getText();
        postOverForm.setText("");
        return new Forward("index");
    }

    @Jpf.Action(
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward postOverrideNewForm(BeanTwo overNewForm)
    {
        dumpRequest();
        _action = "postOverrideNewForm";
        _text = overNewForm.getText();
        overNewForm.setText("");
        return new Forward("index");
    }

    @Jpf.Action(
        useFormBean="useBean",
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward postUseFormBean(BeanThree useForm)
    {
        dumpRequest();
        _action = "useFormBean";
        _text = useForm.getText();
        return new Forward("index");
    }

    @Jpf.Action(
        forwards = { @Jpf.Forward(name = "index", navigateTo = Jpf.NavigateTo.currentPage)})
    protected Forward postDirect(Bean directForm)
    {
        dumpRequest();
        _action = "direct";
        _text = directForm.getText();
        directForm.setText("");
        return new Forward("index");
    }

    /**
     * Callback that is invoked when this controller instance is created.
     */
    protected void onCreate()
    {
	useBean = new BeanThree();
        useBean.setText("Bean Defined");
	
	_direct = new Bean();
        _direct.setText("Direct Defined");
        getRequest().getSession().setAttribute("FormCases",_direct);
    }

    private void dumpRequest() {
	// this is just for test purposes...so exit now
	if (true)
	    return;

        System.err.println("**************************DUMP**************************");
        ServletRequest req = getRequest();
        Enumeration e = req.getParameterNames();
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            System.err.println("Parameter:" + o);
            String[] params = req.getParameterValues(o.toString());
            for (int i=0;i<params.length;i++) {
                System.err.println("\tValue: '" + params[i] + "'");
            }
        }
    }

    /**
     * Callback that is invoked when this controller instance is destroyed.
     */
    protected void onDestroy(HttpSession session)
    {
	session.removeAttribute("FormCases");
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

    public static class BeanThree implements Serializable
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
