<%--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at
   
       http://www.apache.org/licenses/LICENSE-2.0
   
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
  
   $Header:$
--%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>

<netui-template:template templatePage="/resources/template/template.jsp">

    <netui-template:setAttribute name="sampleTitle" value="Nesting"/>

    <netui-template:section name="main">
        Clicking the link below launches the ChooseAirport nested page flow, which does one of two
        things:
        <ul>
            <li>
                Returns the 'chooseAirportDone' action, which carries a Results bean with it.  In
                this case, we forward to a results page to display the returned data.
            </li>
            <li>
                Returns the 'chooseAirportCancelled' action, in which case we just go back to the
                current page.
            </li>
        </ul>
        The "Your Name" field below is used to show that this page flow's state is preserved when
        you return from the nested flow.
        <br/>
        <br/>
        <hr/>
        <netui:form action="chooseAirport">
            Your Name: <netui:textBox dataSource="pageFlow.yourName"/>
            <br/>
            <netui:button value="Run the Choose Airport wizard"/>
        </netui:form>

    </netui-template:section>

</netui-template:template>
