package pageFlowCore.passToFromNested;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import pageFlowCore.passToFromNested.nested.NestedController;
import pageFlowCore.passToFromNested.nested.NestedController.DataIn;


@Jpf.Controller
public class PassToFromNestedController extends PageFlowController
{
    @Jpf.Action(
        forwards={
           @Jpf.Forward(name="index", path="index.jsp")
        }
    )
    protected Forward begin()
    {
        return new Forward("index");
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "nested/NestedController.jpf"
            )
        }
    )
    protected Forward passDataIn()
    {
        DataIn input = new DataIn(){ public String getMessage() { return "DataIn:hi"; } };
        return new Forward( "success", input );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "string_done.jsp",
                actionOutputs = {
                    @Jpf.ActionOutput(
                        name = "string",
                        type = java.lang.String.class)
                }
            )
        }
    )
    protected Forward string_done( String string )
    {
        return new Forward( "success", "string", string );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "dataIn_done.jsp",
                actionOutputs = {
                    @Jpf.ActionOutput(
                        name = "message",
                        type = java.lang.String.class
                    )
                }
            )
        }
    )
    protected Forward dataIn_done( NestedController.DataOut dataOut )
    {
        return new Forward( "success", "message", dataOut.getMessage() );
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(name = "success", path = "nested/NestedController.jpf")
        }
    )
    protected Forward passString()
    {
        Forward forward = new Forward("success", "String:hi");
        return forward;
    }
}

@Jpf.ViewProperties(value = {
    "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
    "<view-properties>",
    "<pageflow-object id='pageflow:/pageFlowCore/passToFromNested/PassToFromNestedController.jpf'/>",
    "<pageflow-object id='page:dataIn_done.jsp'><property name='x' value='240'/><property name='y' value='680'/></pageflow-object>",
    "<pageflow-object id='page:index.jsp'><property value='400' name='x'/><property value='180' name='y'/></pageflow-object>",
    "<pageflow-object id='page:string_done.jsp'><property name='x' value='560'/><property name='y' value='680'/></pageflow-object>",
    "<pageflow-object id='action:begin.do'><property value='400' name='x'/><property value='60' name='y'/></pageflow-object>",
    "<pageflow-object id='action:passDataIn.do'><property name='x' value='240'/><property name='y' value='180'/></pageflow-object>",
    "<pageflow-object id='action:string_done.do#java.lang.String'><property name='x' value='560'/><property name='y' value='540'/></pageflow-object>",
    "<pageflow-object id='action:dataIn_done.do#pageFlowCore.passToFromNested.nested.NestedController.DataOut'><property name='x' value='240'/><property name='y' value='540'/></pageflow-object>",
    "<pageflow-object id='action:passString.do'><property name='x' value='560'/><property name='y' value='180'/></pageflow-object>",
    "<pageflow-object id='formbean:java.lang.String'/>",
    "<pageflow-object id='formbean:pageFlowCore.passToFromNested.nested.NestedController.DataOut'/>",
    "<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'><property value='400,400,400,400' name='elbowsX'/><property value='104,120,120,136' name='elbowsY'/><property value='South_1' name='fromPort'/><property value='North_1' name='toPort'/><property value='index' name='label'/></pageflow-object>",
    "<pageflow-object id='external-jpf:nested/NestedController.jpf'><property name='x' value='240'/><property name='y' value='380'/></pageflow-object>",
    "<pageflow-object id='action-call:@external-jpf:nested/NestedController.jpf@#@action:string_done.do#java.lang.String@'><property name='elbowsX' value='276,400,400,524'/><property name='elbowsY' value='372,372,532,532'/><property name='fromPort' value='East_1'/><property name='toPort' value='West_1'/></pageflow-object>",
    "<pageflow-object id='action-call:@external-jpf:nested/NestedController.jpf@#@action:dataIn_done.do#pageFlowCore.passToFromNested.nested.NestedController.DataOut@'><property name='elbowsX' value='240,240,240,240'/><property name='elbowsY' value='424,460,460,496'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_1'/></pageflow-object>",
    "<pageflow-object id='forward:path#success#nested/NestedController.jpf#@action:passDataIn.do@'><property name='elbowsX' value='240,240,240,240'/><property name='elbowsY' value='224,280,280,336'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_1'/><property name='label' value='success'/></pageflow-object>",
    "<pageflow-object id='forward:path#success#string_done.jsp#@action:string_done.do#java.lang.String@'><property name='elbowsX' value='560,560,560,560'/><property name='elbowsY' value='584,610,610,636'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_1'/><property name='label' value='success'/></pageflow-object>",
    "<pageflow-object id='forward:path#success#dataIn_done.jsp#@action:dataIn_done.do#pageFlowCore.passToFromNested.nested.NestedController.DataOut@'><property name='elbowsX' value='240,240,240,240'/><property name='elbowsY' value='584,610,610,636'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_1'/><property name='label' value='success'/></pageflow-object>",
    "<pageflow-object id='forward:path#success#nested/NestedController.jpf#@action:passString.do@'><property name='elbowsX' value='524,400,400,276'/><property name='elbowsY' value='183,183,361,361'/><property name='fromPort' value='West_2'/><property name='toPort' value='East_0'/><property name='label' value='success'/></pageflow-object>",
    "<pageflow-object id='action-output:string#@forward:path#success#string_done.jsp#@action:string_done.do#java.lang.String@@'/>",
    "<pageflow-object id='action-output:message#@forward:path#success#dataIn_done.jsp#@action:dataIn_done.do#pageFlowCore.passToFromNested.nested.NestedController.DataOut@@'/>",
    "<pageflow-object id='external-jpf:/pageFlowCore/passToFromNested/nested/NestedController.jpf'><property name='x' value='560'/><property name='y' value='360'/></pageflow-object>",
    "<pageflow-object id='action-call:@external-jpf:/pageFlowCore/passToFromNested/nested/NestedController.jpf@#@action:string_done.do#java.lang.String@'><property name='elbowsX' value='560,560,560,560'/><property name='elbowsY' value='404,450,450,496'/><property name='fromPort' value='South_1'/><property name='toPort' value='North_1'/></pageflow-object>",
    "<pageflow-object id='action-call:@external-jpf:/pageFlowCore/passToFromNested/nested/NestedController.jpf@#@action:dataIn_done.do#pageFlowCore.passToFromNested.nested.NestedController.DataOut@'><property name='elbowsX' value='524,400,400,276'/><property name='elbowsY' value='363,363,532,532'/><property name='fromPort' value='West_2'/><property name='toPort' value='East_1'/></pageflow-object>",
    "<pageflow-object id='action-call:@page:dataIn_done.jsp@#@action:begin.do@'><property name='elbowsX' value='276,320,320,364'/><property name='elbowsY' value='672,672,52,52'/><property name='fromPort' value='East_1'/><property name='toPort' value='West_1'/></pageflow-object>",
    "<pageflow-object id='action-call:@page:index.jsp@#@action:passDataIn.do@'><property name='elbowsX' value='364,320,320,276'/><property name='elbowsY' value='172,172,172,172'/><property name='fromPort' value='West_1'/><property name='toPort' value='East_1'/></pageflow-object>",
    "<pageflow-object id='action-call:@page:index.jsp@#@action:passString.do@'><property name='elbowsX' value='436,480,480,524'/><property name='elbowsY' value='172,172,172,172'/><property name='fromPort' value='East_1'/><property name='toPort' value='West_1'/></pageflow-object>",
    "<pageflow-object id='action-call:@page:string_done.jsp@#@action:begin.do@'><property name='elbowsX' value='524,480,480,436'/><property name='elbowsY' value='672,672,52,52'/><property name='fromPort' value='West_1'/><property name='toPort' value='East_1'/></pageflow-object>",
    "</view-properties>"
})
interface VIEW_PROPERTIES { }
