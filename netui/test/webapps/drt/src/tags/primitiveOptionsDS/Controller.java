/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package tags.primitiveOptionsDS;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.io.Serializable;

/**
 * This is the default controller for a blank web application.
 *
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/primitiveOptionsDS/Controller.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property name="x" value="420"/>
 *   <property name="y" value="300"/>
 * </pageflow-object>
 * <pageflow-object id="action:testAction.do#primitiveOptionsDS.Controller.TestActionForm">
 *   <property value="80" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:index.jsp">
 *   <property name="x" value="220"/>
 *   <property name="y" value="80"/>
 * </pageflow-object>
 * <pageflow-object id="page:dumpBoxes.jsp">
 *   <property value="100" name="x"/>
 *   <property value="340" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#index.jsp#@action:begin.do@">
 *   <property name="elbowsY" value="292,292,72,72"/>
 *   <property name="elbowsX" value="384,320,320,256"/>
 *   <property name="toPort" value="East_1"/>
 *   <property name="fromPort" value="West_1"/>
 *   <property name="label" value="index"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#dumpBoxes.jsp#@action:testAction.do#primitiveOptionsDS.Controller.TestActionForm@">
 *   <property value="116,116,100,100" name="elbowsX"/>
 *   <property value="83,206,206,296" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="formbeanprop:primitiveOptionsDS.Controller.TestActionForm#lName#java.lang.String"/>
 * <pageflow-object id="formbean:primitiveOptionsDS.Controller.TestActionForm"/>
 * <pageflow-object id="action-call:@page:index.jsp@#@action:testAction.do#primitiveOptionsDS.Controller.TestActionForm@">
 *   <property value="184,150,150,116" name="elbowsX"/>
 *   <property value="72,72,72,72" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */
@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/primitiveOptionsDS/Controller.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property name='x' value='420'/>",
        "  <property name='y' value='300'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:testAction.do#primitiveOptionsDS.Controller.TestActionForm'>",
        "  <property value='80' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property name='x' value='220'/>",
        "  <property name='y' value='80'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:dumpBoxes.jsp'>",
        "  <property value='100' name='x'/>",
        "  <property value='340' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'>",
        "  <property name='elbowsY' value='292,292,72,72'/>",
        "  <property name='elbowsX' value='384,320,320,256'/>",
        "  <property name='toPort' value='East_1'/>",
        "  <property name='fromPort' value='West_1'/>",
        "  <property name='label' value='index'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#success#dumpBoxes.jsp#@action:testAction.do#primitiveOptionsDS.Controller.TestActionForm@'>",
        "  <property value='116,116,100,100' name='elbowsX'/>",
        "  <property value='83,206,206,296' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='North_1' name='toPort'/>",
        "  <property value='success' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='formbeanprop:primitiveOptionsDS.Controller.TestActionForm#lName#java.lang.String'/>",
        "<pageflow-object id='formbean:primitiveOptionsDS.Controller.TestActionForm'/>",
        "<pageflow-object id='action-call:@page:index.jsp@#@action:testAction.do#primitiveOptionsDS.Controller.TestActionForm@'>",
        "  <property value='184,150,150,116' name='elbowsX'/>",
        "  <property value='72,72,72,72' name='elbowsY'/>",
        "  <property value='West_1' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Controller extends PageFlowController
{
    private Long[] list1 = new Long[] { new Long(56), null, new Long(27), new Long(32) };
    private Long[] boxes = new Long[] { new Long(56), new Long(32) };

    public Long[] getList1()
    {
        return list1;
    }

    public void setList1(Long[] list1)
    {
        this.list1 = list1;
    }

    public Long[] getBoxes()
    {
        return boxes;
    }

    public void setBoxes(Long[] boxes)
    {
        this.boxes = boxes;
    }
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    protected Forward begin() {
        return new Forward("index");
    }


    /**
     * @jpf:action
     * @jpf:forward name="success" path="dumpBoxes.jsp" 
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "dumpBoxes.jsp") 
        })
    protected Forward testAction(TestActionForm form)
    {
        return new Forward("success");
    }

    /**
     * FormData get and set methods may be overwritten by the Form Bean editor.
     */
    public static class TestActionForm implements Serializable
    {
        private String lName;

        public void setlName(String lName)
        {
            this.lName = lName;
        }

        public String getlName()
        {
            return this.lName;
        }
    }
}
