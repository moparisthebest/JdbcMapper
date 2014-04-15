package idmap.formNoHtml;

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
    private String[] _selectOptions = {"Option 1", "Option 2", "Option 3", "Option 4"};
    private Bean _bean = new Bean();

    public Bean getBean() {
        return _bean;
    }

    public String[] getSelectOptions() {
        return _selectOptions;
    }
    
    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
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


    @Jpf.Action(forwards = {
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward postForm(Bean form)
    {
        _bean = form;
        Forward forward = new Forward("success");
        return forward;
    }

    public static class Bean implements Serializable
    {
        private String text1;

        private String text2;

        private String text3;

        private boolean check1;

        private boolean check2;

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

        public String getText3()
        {
            return text3;
        }

        public void setText3(String value)
        {
            text3 = value;
        }

        public boolean isCheck1()
        {
            return check1;
        }

        public void setCheck1(boolean value)
        {
            check1 = value;
        }

        public boolean isCheck2()
        {
            return check2;
        }

        public void setCheck2(boolean value)
        {
            check2 = value;
        }
    }
}

@Jpf.ViewProperties(value= {"<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
"<view-properties>",
"<pageflow-object id='pageflow:/idmap/simple/Controller.jpf'/>",
"<pageflow-object id='page:index.jsp'><property value='220' name='x'/><property value='100' name='y'/></pageflow-object>",
"<pageflow-object id='formbean:Bean'/>",
"<pageflow-object id='action:postForm.do#idmap.simple.Controller.Bean'><property name='x' value='340'/><property name='y' value='100'/></pageflow-object>",
"<pageflow-object id='forward:path#success#index.jsp#@action:postForm.do#idmap.simple.Controller.Bean@'/>",
"<pageflow-object id='action-call:@page:index.jsp@#@action:postForm.do#idmap.simple.Controller.Bean@'><property name='elbowsX' value='256,280,280,304'/><property name='elbowsY' value='92,92,92,92'/><property name='fromPort' value='East_1'/><property name='toPort' value='West_1'/></pageflow-object>",
"<pageflow-object id='action:begin.do'><property value='80' name='x'/><property value='100' name='y'/></pageflow-object>",
"<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'><property value='116,140,140,164' name='elbowsX'/><property value='92,92,92,92' name='elbowsY'/><property value='East_1' name='fromPort'/><property value='West_1' name='toPort'/><property value='index' name='label'/></pageflow-object>",
"</view-properties>"
})
interface VIEW_PROPERTIES { }
