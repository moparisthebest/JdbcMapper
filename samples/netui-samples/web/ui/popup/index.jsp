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

<netui-template:template templatePage="/resources/template/template.jsp">
  <netui-template:setAttribute name="sampleTitle" value="Popup Windows"/>
  <netui-template:section name="main">
    <netui:form action="submit">
      <table>
        <tr>
            <td>Name:</td>
            <td>
                <netui:textBox dataSource="actionForm.name"/>
                <netui:error key="name"/>
            </td>
        </tr>
        <tr><td>Address:</td><td><netui:textBox dataSource="actionForm.address"/></td></tr>
        <tr><td>City:</td><td><netui:textBox dataSource="actionForm.city"/></td></tr>
        <tr>
            <td>Favorite Color:</td>
            <td>
                <netui:textBox dataSource="actionForm.color" tagId="colorField" readonly="true"/>

                <%-- The netui:button uses the 'popup' attribute to trigger the popup window. --%>
                <netui:button type="submit" value="Go to the Color Picker" action="getColor" popup="true">
                    <%--
                        The netui:configurePopup tag sets popup window parameters, and also contains
                        netui:retrievePopupOutput tags to map return values from the nested page flow
                        into fields on this page.
                        
                        You can *override* the default field-mapping behavior by setting the 'onPopupDone'
                        attribute on netui:configurePopup.  For instance, if you set that value to
                        "myPopupMapper" and include the function below, then an alert dialog will appear
                        for each field when the popup window closes.

                        function myPopupMapper(map)
                        {
                            for (var i in map)
                            {
                                alert("field-ID=" + i + ", value=" + map[i]);
                            }
                        }
                    --%>
                    <netui:configurePopup location="false" width="170" height="150">
                        <netui:retrievePopupOutput dataSource="outputFormBean.color" tagIdRef="colorField"/>
                    </netui:configurePopup>
                </netui:button>
                <netui:error key="color"/>
            </td>
        </tr>
        </table>
      <netui:button type="submit" value="Submit" action="submit"/>
    </netui:form>

  </netui-template:section>
</netui-template:template>


