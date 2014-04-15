package coretags.button.id;

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
    private String _text;
    public String getText() {
        return _text;
    }

    private String _method;
    public String getMethod() {
        return _method;
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
    protected Forward post(coretags.imagebutton.id.Controller.Bean form)
    {
        Forward forward = new Forward("success");
        _text = form.getText();
        _method = "post";
        return forward;
    }

    @Jpf.Action(forwards = { 
        @Jpf.Forward(name = "success",
                     path = "index.jsp")
    })
    protected Forward override(coretags.imagebutton.id.Controller.Bean form)
    {
        Forward forward = new Forward("success");
        _text = form.getText();
        _method = "override";
        return forward;
    }
                    
    public static class Bean implements Serializable
    {
        private String text;
        
        public String getText()
        {
            return text;
        }
        
        public void setText(String value)
        {
            text = value;
        }
    }
}

@Jpf.ViewProperties(value= {"<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
"<view-properties>",
"<pageflow-object id='pageflow:/coretags/imagebutton/id/Controller.jpf'/>",
"<pageflow-object id='page:index.jsp'><property value='220' name='x'/><property value='100' name='y'/></pageflow-object>",
"<pageflow-object id='formbean:Bean'/>",
"<pageflow-object id='action:begin.do'><property value='80' name='x'/><property value='100' name='y'/></pageflow-object>",
"<pageflow-object id='action:post.do#coretags.imagebutton.id.Controller.Bean'><property name='x' value='340'/><property name='y' value='100'/></pageflow-object>",
"<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'><property value='116,140,140,164' name='elbowsX'/><property value='92,92,92,92' name='elbowsY'/><property value='East_1' name='fromPort'/><property value='West_1' name='toPort'/><property value='index' name='label'/></pageflow-object>",
"<pageflow-object id='action-call:@page:index.jsp@#@action:post.do#coretags.imagebutton.id.Controller.Bean@'><property name='elbowsX' value='256,280,280,304'/><property name='elbowsY' value='92,92,92,92'/><property name='fromPort' value='East_1'/><property name='toPort' value='West_1'/></pageflow-object>",
"<pageflow-object id='forward:path#success#index.jsp#@action:post.do#coretags.imagebutton.id.Controller.Bean@'><property name='elbowsX' value='304,280,280,256'/><property name='elbowsY' value='103,103,103,103'/><property name='fromPort' value='West_2'/><property name='toPort' value='East_2'/><property name='label' value='success'/></pageflow-object>",
"</view-properties>"
})
interface VIEW_PROPERTIES { }
