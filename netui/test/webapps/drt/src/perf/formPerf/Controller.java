package perf.formPerf;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Jpf.Controller
public class Controller 
    extends PageFlowController
{
    private String[] selectOptions = {"select one", "select two", "select three",
        "select four", "select five", "select six", "select seven"};

    public String[] getSelectOptions() {
        return selectOptions;
    }
        
     private String[] radioOptions = {"radio one", "radio two", "radio three",
        "radio four", "radio five", "radio six", "radio seven"};

    public String[] getRadioOptions() {
        return radioOptions;
    }


    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="success", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("success");
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
    protected Forward postBean(Bean form)
    {
        Forward forward = new Forward("success");
        return forward;
    }

    public static class Bean implements Serializable
    {
        private String textOne;

        private String textTwo;

        private String textThree;

        private String textFour;

        private boolean boolOne;

        private boolean boolTwo;

        private String[] select;

        private String fullText;

        private boolean[] boolArray = {true, true, false, false, false};


        private String radio;

        public String getTextOne()
        {
            return textOne;
        }

        public void setTextOne(String value)
        {
            textOne = value;
        }

        public String getTextTwo()
        {
            return textTwo;
        }

        public void setTextTwo(String value)
        {
            textTwo = value;
        }

        public String getTextThree()
        {
            return textThree;
        }

        public void setTextThree(String value)
        {
            textThree = value;
        }

        public String getTextFour()
        {
            return textFour;
        }

        public void setTextFour(String value)
        {
            textFour = value;
        }

        public boolean isBoolOne()
        {
            return boolOne;
        }

        public void setBoolOne(boolean value)
        {
            boolOne = value;
        }

        public boolean isBoolTwo()
        {
            return boolTwo;
        }

        public void setBoolTwo(boolean value)
        {
            boolTwo = value;
        }

        public String[] getSelect()
        {
            return select;
        }

        public void setSelect(String[] value)
        {
            select = value;
        }

        public String getFullText()
        {
            return fullText;
        }

        public void setFullText(String value)
        {
            fullText = value;
        }

        public boolean[] getBoolArray()
        {
            return boolArray;
        }

        public void setBoolArray(boolean[] value)
        {
            boolArray = value;
        }

        public String getRadio()
        {
            return radio;
        }

        public void setRadio(String value)
        {
            radio = value;
        }
    }
}@Jpf.ViewProperties(value= {"<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
"<view-properties>",
"<pageflow-object id='pageflow:/Controller.jpf'/>",
"<pageflow-object id='page:error.jsp'><property name='x' value='100'/><property name='y' value='240'/></pageflow-object>",
"<pageflow-object id='page:index.jsp'><property name='x' value='240'/><property name='y' value='80'/></pageflow-object>",
"<pageflow-object id='action:begin.do'><property name='x' value='100'/><property name='y' value='80'/></pageflow-object>",
"<pageflow-object id='forward:path#success#index.jsp#@action:begin.do@'><property name='elbowsX' value='136,170,170,204'/><property name='elbowsY' value='72,72,72,72'/><property name='fromPort' value='East_1'/><property name='toPort' value='West_1'/><property name='label' value='success'/></pageflow-object>",
"<pageflow-object id='formbean:Bean'/>",
"<pageflow-object id='action:postBean.do#Controller.Bean'><property name='x' value='380'/><property name='y' value='80'/></pageflow-object>",
"<pageflow-object id='action-call:@page:index.jsp@#@action:postBean.do#Controller.Bean@'><property name='elbowsX' value='276,310,310,344'/><property name='elbowsY' value='72,72,72,72'/><property name='fromPort' value='East_1'/><property name='toPort' value='West_1'/></pageflow-object>",
"<pageflow-object id='forward:path#success#index.jsp#@action:postBean.do#Controller.Bean@'/>",
"</view-properties>"
})
interface VIEW_PROPERTIES { }
