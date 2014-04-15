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
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-template-1.0" prefix="netui-template"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<netui-data:declarePageInput name="categories" type="org.apache.beehive.samples.petstore.model.Category[]"/>
<netui-data:declareBundle bundlePath="org.apache.beehive.samples.petstore.resources.view" name="view"/>

<netui-template:template templatePage="/site/template.jsp">

	<netui-template:setAttribute name="title" value="${bundle.view.homeTitle}" />

    <netui-template:section name="leftnav">
      <table class="tableborder" bgcolor="#eeeeee" width="200">
        <tr>
          <td>
          <c:if test="${sharedFlow.rootSharedFlow.account != null}">
              <netui:span styleClass="meditaliclabel" value="${bundle.view.welcomeLabel} ${sharedFlow.rootSharedFlow.account.firstName}"/>!
          </c:if>
          &nbsp;
          </td>
        </tr>
        <netui-data:repeater dataSource="pageInput.categories">
        <tr>
          <td>
          <netui:anchor action="viewCategory" value="${container.item.name}">
            <netui:parameter name="catId" value="${container.item.catId}"/>
          </netui:anchor>
          <br/>
          <netui:span styleClass="meditaliclabel" value="${container.item.description}"/>
          </td>
        </tr>
        </netui-data:repeater>
      </table>
    </netui-template:section>

    <netui-template:section name="body">
      <netui:errors/>
      <map name="beehivepetstoremap">
        <area alt="Birds" coords="72,2,280,250" href="rootSharedFlow.globalViewCategory.do?catId=BIRDS" shape="RECT" />
        <area alt="Fish" coords="2,180,72,250" href="rootSharedFlow.globalViewCategory.do?catId=FISH" shape="RECT" />
        <area alt="Dogs" coords="60,250,130,320" href="rootSharedFlow.globalViewCategory.do?catId=DOGS" shape="RECT" />
        <area alt="Reptiles" coords="140,270,210,340" href="rootSharedFlow.globalViewCategory.do?catId=REPTILES" shape="RECT" />
        <area alt="Birds" coords="225,240,295,310" href="rootSharedFlow.globalViewCategory.do?catId=BIRDS" shape="RECT" />
        <area alt="Cats" coords="280,180,350,250" href="rootSharedFlow.globalViewCategory.do?catId=CATS" shape="RECT" />
      </map>
      <img vspace="10" border="0" height="347" width="357" src="${pageContext.request.contextPath}/images/splash.gif" 
           align="center" usemap="#beehivepetstoremap"/>
    </netui-template:section>

</netui-template:template>
