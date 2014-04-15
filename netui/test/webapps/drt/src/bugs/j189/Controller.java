package bugs.j189;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tags.tree.TreeElement;

import java.io.Serializable;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{

    private String _text;
    public String getText() {
        return _text;
    }

    private String _method;
    public String getMethod() {
        return _method;
    }

    private TreeElement _tree;
    
    private TreeElement _tree2;
    
    public TreeElement getTree(){
        return _tree;
    }
    public void setTree(TreeElement t) {
        _tree = t;
    }

    public TreeElement getTree2(){
        return _tree2;
    }
    public void setTree2(TreeElement t) {
        _tree2 = t;
    }

    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="form.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }

    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "form.jsp")
    })
    protected Forward back(Bean form)
    {
        Forward success = new Forward("success");
        return success;
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

        public static class Bean2 implements Serializable
    {
        private String text1;

        public String getText1()
        {
            return text1;
        }

        public void setText1(String value)
        {
            text1 = value;
        }

    }

}
