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
package miscJpf.bug30448;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import shared.FormA;
import shared.QaTrace;

/**
 * @jpf:controller
 * @jpf:forward name="gotoDone" redirect="true" path="/resources/jsp/done.jsp"
 * @jpf:forward name="gotoError" path="/resources/jsp/error.jsp"
 * @jpf:forward name="gotoTraceResults" redirect="true" path="/resources/jsp/qaTraceResults.jsp"
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/miscJpf/bug30448/Jpf1.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="100" name="x"/>
 *   <property value="860" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:jpfResults.do">
 *   <property value="100" name="x"/>
 *   <property value="1020" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:jpfAction1.do">
 *   <property value="940" name="x"/>
 *   <property value="800" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:jpfReturn1.do">
 *   <property value="200" name="x"/>
 *   <property value="180" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:jpfReturn2.do">
 *   <property value="225" name="x"/>
 *   <property value="205" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:jpfReturn2.do#shared.FormA">
 *   <property value="160" name="x"/>
 *   <property value="140" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:jpfReturn3.do#shared.FormA">
 *   <property value="110" name="x"/>
 *   <property value="90" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:finish.do">
 *   <property value="1060" name="x"/>
 *   <property value="920" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/done.jsp">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/error.jsp">
 *   <property value="85" name="x"/>
 *   <property value="65" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/resources/jsp/qaTraceResults.jsp">
 *   <property value="250" name="x"/>
 *   <property value="230" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@page:Jsp1.jsp@#@action:jpfAction1.do@">
 *   <property value="276,590,590,904" name="elbowsX"/>
 *   <property value="852,852,792,792" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="page:Jsp1.jsp">
 *   <property value="240" name="x"/>
 *   <property value="860" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:jpfReturn3.do">
 *   <property value="340" name="x"/>
 *   <property value="740" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:jpfReturn1.do#shared.FormA">
 *   <property value="135" name="x"/>
 *   <property value="115" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn3.do@">
 *   <property value="504,312,312,121" name="elbowsX"/>
 *   <property value="521,521,46,46" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_0" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn1.do@">
 *   <property value="504,370,370,236" name="elbowsX"/>
 *   <property value="521,521,183,183" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_2" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn2.do@">
 *   <property value="504,382,382,261" name="elbowsX"/>
 *   <property value="521,521,208,208" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_2" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn3.do#shared.FormA@">
 *   <property value="504,325,325,146" name="elbowsX"/>
 *   <property value="521,521,82,82" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn1.do#shared.FormA@">
 *   <property value="504,337,337,171" name="elbowsX"/>
 *   <property value="521,521,107,107" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn2.do#shared.FormA@">
 *   <property value="504,350,350,196" name="elbowsX"/>
 *   <property value="521,521,132,132" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_1" name="toPort"/>
 * </pageflow-object>
 * <pageflow-object id="external-jpf:subJpf/SubJpf1.jpf">
 *   <property value="540" name="x"/>
 *   <property value="540" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:StartTest.jsp">
 *   <property value="100" name="x"/>
 *   <property value="380" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoPg1#Jsp1.jsp#@action:begin.do@">
 *   <property value="136,170,170,204" name="elbowsX"/>
 *   <property value="841,841,841,841" name="elbowsY"/>
 *   <property value="East_0" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="gotoPg1" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoResults#Jsp1.jsp#@action:jpfResults.do@">
 *   <property value="136,170,170,204" name="elbowsX"/>
 *   <property value="1012,1012,852,852" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="gotoResults" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoSubJpf1#subJpf/SubJpf1.jpf#@action:jpfAction1.do@">
 *   <property value="904,740,740,576" name="elbowsX"/>
 *   <property value="781,781,543,543" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="East_2" name="toPort"/>
 *   <property value="gotoSubJpf1" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoResults#Jsp1.jsp#@action:jpfReturn1.do@">
 *   <property value="200,200,202,204" name="elbowsX"/>
 *   <property value="224,841,841,841" name="elbowsY"/>
 *   <property value="South_1" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="gotoResults" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoResults#Jsp1.jsp#@action:jpfReturn2.do@">
 *   <property value="225,225,204,204" name="elbowsX"/>
 *   <property value="249,536,536,841" name="elbowsY"/>
 *   <property value="South_1" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="gotoResults" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoResults#Jsp1.jsp#@action:jpfReturn2.do#shared.FormA@">
 *   <property value="196,200,200,204" name="elbowsX"/>
 *   <property value="143,143,841,841" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="gotoResults" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoResults#Jsp1.jsp#@action:jpfReturn3.do#shared.FormA@">
 *   <property value="146,175,175,204" name="elbowsX"/>
 *   <property value="93,93,841,841" name="elbowsY"/>
 *   <property value="East_2" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="gotoResults" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#gotoDone#/resources/jsp/done.jsp"/>
 * <pageflow-object id="forward:path#gotoError#/resources/jsp/error.jsp"/>
 * <pageflow-object id="forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp"/>
 * <pageflow-object id="action-call:@page:Jsp1.jsp@#@action:finish.do@">
 *   <property value="276,650,650,1024" name="elbowsX"/>
 *   <property value="852,852,901,901" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 *
 */
@Jpf.Controller(
    forwards = {
        @Jpf.Forward(
            name = "gotoDone",
            redirect = true,
            path = "/resources/jsp/done.jsp"),
        @Jpf.Forward(
            name = "gotoError",
            path = "/resources/jsp/error.jsp"),
        @Jpf.Forward(
            name = "gotoTraceResults",
            redirect = true,
            path = "/resources/jsp/qaTraceResults.jsp") 
    })
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/miscJpf/bug30448/Jpf1.jpf'/>",
        "<pageflow-object id='action:begin.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='860' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:jpfResults.do'>",
        "  <property value='100' name='x'/>",
        "  <property value='1020' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:jpfAction1.do'>",
        "  <property value='940' name='x'/>",
        "  <property value='800' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:jpfReturn1.do'>",
        "  <property value='200' name='x'/>",
        "  <property value='180' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:jpfReturn2.do'>",
        "  <property value='225' name='x'/>",
        "  <property value='205' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:jpfReturn2.do#shared.FormA'>",
        "  <property value='160' name='x'/>",
        "  <property value='140' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:jpfReturn3.do#shared.FormA'>",
        "  <property value='110' name='x'/>",
        "  <property value='90' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:finish.do'>",
        "  <property value='1060' name='x'/>",
        "  <property value='920' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/done.jsp'>",
        "  <property value='60' name='x'/>",
        "  <property value='40' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/error.jsp'>",
        "  <property value='85' name='x'/>",
        "  <property value='65' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:/resources/jsp/qaTraceResults.jsp'>",
        "  <property value='250' name='x'/>",
        "  <property value='230' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:jpfAction1.do@'>",
        "  <property value='276,590,590,904' name='elbowsX'/>",
        "  <property value='852,852,792,792' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:Jsp1.jsp'>",
        "  <property value='240' name='x'/>",
        "  <property value='860' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:jpfReturn3.do'>",
        "  <property value='340' name='x'/>",
        "  <property value='740' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:jpfReturn1.do#shared.FormA'>",
        "  <property value='135' name='x'/>",
        "  <property value='115' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn3.do@'>",
        "  <property value='504,312,312,121' name='elbowsX'/>",
        "  <property value='521,521,46,46' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_0' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn1.do@'>",
        "  <property value='504,370,370,236' name='elbowsX'/>",
        "  <property value='521,521,183,183' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_2' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn2.do@'>",
        "  <property value='504,382,382,261' name='elbowsX'/>",
        "  <property value='521,521,208,208' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_2' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn3.do#shared.FormA@'>",
        "  <property value='504,325,325,146' name='elbowsX'/>",
        "  <property value='521,521,82,82' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn1.do#shared.FormA@'>",
        "  <property value='504,337,337,171' name='elbowsX'/>",
        "  <property value='521,521,107,107' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='action-call:@external-jpf:subJpf/SubJpf1.jpf@#@action:jpfReturn2.do#shared.FormA@'>",
        "  <property value='504,350,350,196' name='elbowsX'/>",
        "  <property value='521,521,132,132' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_1' name='toPort'/>",
        "</pageflow-object>",
        "<pageflow-object id='external-jpf:subJpf/SubJpf1.jpf'>",
        "  <property value='540' name='x'/>",
        "  <property value='540' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:StartTest.jsp'>",
        "  <property value='100' name='x'/>",
        "  <property value='380' name='y'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoPg1#Jsp1.jsp#@action:begin.do@'>",
        "  <property value='136,170,170,204' name='elbowsX'/>",
        "  <property value='841,841,841,841' name='elbowsY'/>",
        "  <property value='East_0' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "  <property value='gotoPg1' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoResults#Jsp1.jsp#@action:jpfResults.do@'>",
        "  <property value='136,170,170,204' name='elbowsX'/>",
        "  <property value='1012,1012,852,852' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_1' name='toPort'/>",
        "  <property value='gotoResults' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoSubJpf1#subJpf/SubJpf1.jpf#@action:jpfAction1.do@'>",
        "  <property value='904,740,740,576' name='elbowsX'/>",
        "  <property value='781,781,543,543' name='elbowsY'/>",
        "  <property value='West_0' name='fromPort'/>",
        "  <property value='East_2' name='toPort'/>",
        "  <property value='gotoSubJpf1' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoResults#Jsp1.jsp#@action:jpfReturn1.do@'>",
        "  <property value='200,200,202,204' name='elbowsX'/>",
        "  <property value='224,841,841,841' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "  <property value='gotoResults' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoResults#Jsp1.jsp#@action:jpfReturn2.do@'>",
        "  <property value='225,225,204,204' name='elbowsX'/>",
        "  <property value='249,536,536,841' name='elbowsY'/>",
        "  <property value='South_1' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "  <property value='gotoResults' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoResults#Jsp1.jsp#@action:jpfReturn2.do#shared.FormA@'>",
        "  <property value='196,200,200,204' name='elbowsX'/>",
        "  <property value='143,143,841,841' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "  <property value='gotoResults' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoResults#Jsp1.jsp#@action:jpfReturn3.do#shared.FormA@'>",
        "  <property value='146,175,175,204' name='elbowsX'/>",
        "  <property value='93,93,841,841' name='elbowsY'/>",
        "  <property value='East_2' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "  <property value='gotoResults' name='label'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#gotoDone#/resources/jsp/done.jsp'/>",
        "<pageflow-object id='forward:path#gotoError#/resources/jsp/error.jsp'/>",
        "<pageflow-object id='forward:path#gotoTraceResults#/resources/jsp/qaTraceResults.jsp'/>",
        "<pageflow-object id='action-call:@page:Jsp1.jsp@#@action:finish.do@'>",
        "  <property value='276,650,650,1024' name='elbowsX'/>",
        "  <property value='852,852,901,901' name='elbowsY'/>",
        "  <property value='East_1' name='fromPort'/>",
        "  <property value='West_0' name='toPort'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Jpf1 extends PageFlowController
    {
    public static final  String   FLD1_VALUE  = "Jpf1 Fld1 Value";
    public static final  String   FLD2_VALUE  = "Jpf1 Fld2 Value";

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
    * @jpf:forward name="gotoPg1" path="Jsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoPg1",
                path = "Jsp1.jsp") 
        })
   protected Forward begin()
      {
      _log.tracePoint("Jpf1.begin():" + _cnt);
      return new Forward("gotoPg1");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoSubJpf1" path="subJpf/SubJpf1.jpf"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoSubJpf1",
                path = "subJpf/SubJpf1.jpf") 
        })
   protected Forward jpfAction1()
      {
      _log.tracePoint("Jpf1.jpfAction1():" + _cnt);
      _log.tracePoint("-------------------------------------------------------------");
      return new Forward("gotoSubJpf1");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoResults" path="Jsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoResults",
                path = "Jsp1.jsp") 
        })
   protected Forward jpfReturn1()
      {
      _log.tracePoint("Jpf1.jpfReturn1():" + _cnt);
      return new Forward("gotoResults");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoResults" path="Jsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoResults",
                path = "Jsp1.jsp") 
        })
   protected Forward jpfReturn2()
      {
      _log.tracePoint("Jpf1.jpfReturn2():" + _cnt);
      return new Forward("gotoResults");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoResults" path="Jsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoResults",
                path = "Jsp1.jsp") 
        })
   protected Forward jpfReturn2(FormA inForm)
      {
      _log.tracePoint("Jpf1.jpfReturn2(FormA):" + _cnt);
      return new Forward("gotoResults");
      }

   /**
    * @jpf:action
    * @jpf:forward name="gotoResults" path="Jsp1.jsp"
    */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "gotoResults",
                path = "Jsp1.jsp") 
        })
   protected Forward jpfReturn3(FormA inForm)
      {
      _log.tracePoint("Jpf1.jpfReturn3(FormA):" + _cnt);
      return new Forward("gotoResults");
      }

    /**
     * @jpf:action
     */
    @Jpf.Action(
        )
    protected Forward finish()
        {
        _log.tracePoint("Jpf1.finish():" + _cnt);
        return new Forward("gotoTraceResults");
        }
    }
