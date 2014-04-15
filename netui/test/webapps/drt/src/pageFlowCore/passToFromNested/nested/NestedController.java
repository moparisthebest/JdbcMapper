package pageFlowCore.passToFromNested.nested;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;


@Jpf.Controller( nested=true )
public class NestedController extends PageFlowController
{
    public interface DataIn
    {
        public String getMessage();
    }

    public interface DataOut
    {
        public String getMessage();
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="page",
                path="dataIn.jsp",
                actionOutputs = {
                    @Jpf.ActionOutput(name = "message", type = String.class)
                }
            )
        }
    )
    protected Forward begin( DataIn dataIn )
    {
        return new Forward( "page", "message", dataIn.getMessage() );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="page",
                path="string.jsp",
                actionOutputs = {
                    @Jpf.ActionOutput(name = "string", type = String.class)
                }
            )
        }
    )
    protected Forward begin( String string )
    {
        return new Forward( "page", "string", string );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="done",
                returnAction="dataIn_done",
                outputFormBeanType = DataOut.class
            )
        }
    )
    protected Forward doneDataIn()
    {
        DataOut out = new DataOut(){ public String getMessage(){ return "DataOut:bye"; } };
    	return new Forward( "done", out );
    }

    @Jpf.Action(
        forwards={
            @Jpf.Forward(
                name="done",
                returnAction="string_done",
                outputFormBeanType = String.class
            )
        }
    )
    protected Forward doneString()
    {
    	return new Forward( "done", "String:bye" );
    }

}

@Jpf.ViewProperties(value = {
    "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
    "<view-properties>",
    "<pageflow-object id='pageflow:/pageFlowCore/passToFromNested/nested/NestedController.jpf'/>",
    "<pageflow-object id='page:dataIn.jsp'><property name='x' value='120'/><property name='y' value='160'/></pageflow-object>",
    "<pageflow-object id='page:string.jsp'><property name='x' value='220'/><property name='y' value='160'/></pageflow-object>",
    "<pageflow-object id='action:begin.do#pageFlowCore.passToFromNested.nested.NestedController.DataIn'><property name='x' value='80'/><property name='y' value='60'/></pageflow-object>",
    "<pageflow-object id='action:begin.do#java.lang.String'><property name='x' value='260'/><property name='y' value='60'/></pageflow-object>",
    "<pageflow-object id='action:doneDataIn.do'><property name='x' value='100'/><property name='y' value='260'/></pageflow-object>",
    "<pageflow-object id='action:doneString.do'><property name='x' value='240'/><property name='y' value='260'/></pageflow-object>",
    "<pageflow-object id='formbean:pageFlowCore.passToFromNested.nested.NestedController.DataIn'/>",
    "<pageflow-object id='formbean:java.lang.String'/>",
    "<pageflow-object id='forward:path#page#dataIn.jsp#@action:begin.do#pageFlowCore.passToFromNested.nested.NestedController.DataIn@'><property name='elbowsX' value='80,80,120,120'/><property name='elbowsY' value='104,110,110,116'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_1'/><property name='label' value='page'/></pageflow-object>",
    "<pageflow-object id='forward:path#page#string.jsp#@action:begin.do#java.lang.String@'><property name='elbowsX' value='224,220,220,220'/><property name='elbowsY' value='52,52,84,116'/><property name='fromPort' value='West_1'/><property name='toPort' value='North_1'/><property name='label' value='page'/></pageflow-object>",
    "<pageflow-object id='exit:dataIn_done'><property name='x' value='60'/><property name='y' value='360'/></pageflow-object>",
    "<pageflow-object id='forward:returnAction#done#dataIn_done#@action:doneDataIn.do@'><property name='elbowsX' value='64,60,60,60'/><property name='elbowsY' value='252,252,284,316'/><property name='fromPort' value='West_1'/><property name='toPort' value='North_1'/><property name='label' value='done'/></pageflow-object>",
    "<pageflow-object id='exit:string_done'><property name='x' value='260'/><property name='y' value='360'/></pageflow-object>",
    "<pageflow-object id='forward:returnAction#done#string_done#@action:doneString.do@'><property name='elbowsX' value='240,240,260,260'/><property name='elbowsY' value='304,310,310,316'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_1'/><property name='label' value='done'/></pageflow-object>",
    "<pageflow-object id='action-output:message#@forward:path#page#dataIn.jsp#@action:begin.do#pageFlowCore.passToFromNested.nested.NestedController.DataIn@@'/>",
    "<pageflow-object id='action-output:string#@forward:path#page#string.jsp#@action:begin.do#java.lang.String@@'/>",
    "<pageflow-object id='exit:pageFlowCore_passToFromNested_nestedDone'><property value='460' name='x'/><property value='360' name='y'/></pageflow-object>",
    "<pageflow-object id='action-call:@page:string.jsp@#@action:doneString.do@'><property name='elbowsX' value='220,220,229,229'/><property name='elbowsY' value='204,210,210,216'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_0'/></pageflow-object>",
    "<pageflow-object id='action-call:@page:dataIn.jsp@#@action:doneDataIn.do@'><property name='elbowsX' value='120,120,111,111'/><property name='elbowsY' value='204,210,210,216'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_2'/></pageflow-object>",
    "</view-properties>"
})
interface VIEW_PROPERTIES { }
