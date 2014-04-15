package tags.selectEmptyOption;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;

/**
 * This is the default controller for a blank web application.
 */
@Jpf.Controller
public class Controller extends PageFlowController
{
    private String[] _opts = new String[0];
    private String[] _select;
    
    public String[] getOptions() {
        return _opts;
    }
    
    public String[] getSelect() {
        return _select;
    }
    public void setSelect(String[] select) {
        _select = select;
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
}

@Jpf.ViewProperties(value = { 
    "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->", 
    "<view-properties>", 
    "<pageflow-object id='pageflow:/bug/Controller.jpf'/>", 
    "<pageflow-object id='page:index.jsp'><property value='220' name='x'/><property value='100' name='y'/></pageflow-object>", 
    "<pageflow-object id='action:begin.do'><property value='80' name='x'/><property value='100' name='y'/></pageflow-object>", 
    "<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'><property value='116,140,140,164' name='elbowsX'/><property value='92,92,92,92' name='elbowsY'/><property value='East_1' name='fromPort'/><property value='West_1' name='toPort'/><property value='index' name='label'/></pageflow-object>", 
    "</view-properties>"
})
interface VIEW_PROPERTIES { }
