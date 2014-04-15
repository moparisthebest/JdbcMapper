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
package miscJpf.bug30448.subJpf;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;
import shared.QaTrace;

/**
 * @jpf:controller nested="true"
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/miscJpf/bug30448/subJpf/SubJpf1.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="100" name="x"/>
 *   <property value="680" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test1.do">
 *   <property value="400" name="x"/>
 *   <property value="240" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test2.do">
 *   <property value="400" name="x"/>
 *   <property value="80" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test3.do">
 *   <property value="400" name="x"/>
 *   <property value="1140" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test4.do">
 *   <property value="400" name="x"/>
 *   <property value="1280" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test5.do">
 *   <property value="400" name="x"/>
 *   <property value="680" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test6.do">
 *   <property value="400" name="x"/>
 *   <property value="980" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test7.do">
 *   <property value="400" name="x"/>
 *   <property value="380" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test8.do">
 *   <property value="400" name="x"/>
 *   <property value="840" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:test9.do">
 *   <property value="400" name="x"/>
 *   <property value="540" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test9.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="683,683,521,521" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test5.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="672,672,672,672" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test6.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="683,683,972,972" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test7.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="661,661,372,372" name="elbowsY"/>
 *   <property value="East_0" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test8.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="683,683,821,821" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test3.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="683,683,1132,1132" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test4.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="683,683,1261,1261" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test1.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="661,661,232,232" name="elbowsY"/>
 *   <property value="East_0" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:SubJsp1.jsp@#@action:test2.do@">
 *   <property value="276,320,320,364" name="elbowsX"/>
 *   <property value="661,661,72,72" name="elbowsY"/>
 *   <property value="East_0" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:SubJsp1.jsp">
 *   <property value="240" name="x"/>
 *   <property value="680" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoPg1#SubJsp1.jsp#@action:begin.do@">
 *   <property value="136,170,170,204" name="elbowsX"/>
 *   <property value="672,672,672,672" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="gotoPg1" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="exit:jpfReturn1">
 *   <property value="540" name="x"/>
 *   <property value="120" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn1#@action:test1.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="232,232,112,112" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn1#@action:test2.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="72,72,101,101" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="exit:jpfReturn2">
 *   <property value="540" name="x"/>
 *   <property value="1200" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn2#@action:test3.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="1132,1132,1192,1192" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn2#@action:test4.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="1272,1272,1203,1203" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_2" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="exit:jpfReturn3">
 *   <property value="540" name="x"/>
 *   <property value="660" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn3#@action:test5.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="672,672,652,652" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn3#@action:test6.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="972,972,663,663" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_2" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn3#@action:test7.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="372,372,663,663" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_2" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn3#@action:test8.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="832,832,663,663" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_2" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:return-action#return#jpfReturn3#@action:test9.do@">
 *   <property value="436,470,470,504" name="elbowsX"/>
 *   <property value="532,532,663,663" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_2" name="toPort"/>
 *   <property value="return" name="label"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 *
 */
@Jpf.Controller(
    nested = true)
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/miscJpf/bug30448/subJpf/SubJpf1.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='680' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test1.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='240' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test2.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='80' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test3.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='1140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test4.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='1280' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test5.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='680' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test6.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='980' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test7.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='380' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test8.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='840' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:test9.do'>",
        "  <property value='400' name='x'/>",
        "  <property value='540' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test9.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='683,683,521,521' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test5.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='672,672,672,672' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test6.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='683,683,972,972' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test7.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='661,661,372,372' name='elbowsY'/>",
        "  <property value='East_0' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test8.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='683,683,821,821' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test3.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='683,683,1132,1132' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test4.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='683,683,1261,1261' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test1.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='661,661,232,232' name='elbowsY'/>",
        "  <property value='East_0' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:SubJsp1.jsp@#@action:test2.do@'>",
        "  <property value='276,320,320,364' name='elbowsX'/>",
        "  <property value='661,661,72,72' name='elbowsY'/>",
        "  <property value='East_0' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:SubJsp1.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='680' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoPg1#SubJsp1.jsp#@action:begin.do@'>",
        "  <property value='136,170,170,204' name='elbowsX'/>",
        "  <property value='672,672,672,672' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='gotoPg1' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='exit:jpfReturn1'>",
        "  <property value='540' name='x'/>",
        "  <property value='120' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn1#@action:test1.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='232,232,112,112' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn1#@action:test2.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='72,72,101,101' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='exit:jpfReturn2'>",
        "  <property value='540' name='x'/>",
        "  <property value='1200' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn2#@action:test3.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='1132,1132,1192,1192' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn2#@action:test4.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='1272,1272,1203,1203' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='exit:jpfReturn3'>",
        "  <property value='540' name='x'/>",
        "  <property value='660' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn3#@action:test5.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='672,672,652,652' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn3#@action:test6.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='972,972,663,663' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn3#@action:test7.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='372,372,663,663' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn3#@action:test8.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='832,832,663,663' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:return-action#return#jpfReturn3#@action:test9.do@'>",
        "  <property value='436,470,470,504' name='elbowsX'/>",
        "  <property value='532,532,663,663' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_2' name='toPort'/>",
        "  <property value='return' name='label'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class SubJpf1 extends PageFlowController
   {
   public static final String FLD1_VALUE  = "SubJpf1 Fld1 Value";
   public static final String FLD2_VALUE  = "SubJpf1 Fld2 Value";
   private FormA _theForm  = new FormA();

    private QaTrace _log = null;
    private int     _cnt = 0;

    /**
     * onCreate
     */
    public void onCreate() throws Exception
        {
        _log = QaTrace.getTrace(getSession(), true);
        _cnt = _log.newClass(this);
        _log.tracePoint("Jpf1.onCreate():" + _cnt);
        }

   /**
    * @jpf:action
    * @jpf:forward name="gotoPg1" path="SubJsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "SubJsp1.jsp") 
        })
   protected Forward begin()
       {
       _log.tracePoint("SubJpf1.begin():" + _cnt);
       return new Forward("gotoPg1");
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn1"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn1") 
        })
   protected Forward test1()
       {
       _log.tracePoint("SubJpf1.test1():" + _cnt);
       return new Forward("return");
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn1" return-form-type="FormA"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn1",
                outputFormBeanType = FormA.class) 
        })
   protected Forward test2()
       {
       _log.tracePoint("SubJpf1.test2():" + _cnt);
       return new Forward("return", new FormA());
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn2"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn2") 
        })
   protected Forward test3()
       {
       _log.tracePoint("SubJpf1.test3():" + _cnt);
       return new Forward("return");
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn2" return-form-type="FormA"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn2",
                outputFormBeanType = FormA.class) 
        })
   protected Forward test4()
       {
       _log.tracePoint("SubJpf1.test4():" + _cnt);
       return new Forward("return", new FormA());
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn3"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn3") 
        })
   protected Forward test5()
       {
       _log.tracePoint("SubJpf1.test5():" + _cnt);
       return new Forward("return");
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn3" return-form-type="FormA"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn3",
                outputFormBeanType = FormA.class) 
        })
   protected Forward test6()
       {
       _log.tracePoint("SubJpf1.test6():" + _cnt);
       return new Forward("return", new FormA());
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn3"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn3") 
        })
   protected Forward test7()
       {
       _log.tracePoint("SubJpf1.test7():" + _cnt);
       return new Forward("return", new FormA());
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn3" return-form-type="FormA"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn3",
                outputFormBeanType = FormA.class) 
        })
   protected Forward test8()
       {
       _log.tracePoint("SubJpf1.test8():" + _cnt);
       return new Forward("return");
       }

   /**
    * @jpf:action
    * @jpf:forward name="return" return-action="jpfReturn3" return-form="_theForm"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "return",
                returnAction = "jpfReturn3",
                outputFormBean = "_theForm") 
        })
   protected Forward test9()
       {
       _log.tracePoint("SubJpf1.test9():" + _cnt);
       return new Forward("return");
       }
   }
