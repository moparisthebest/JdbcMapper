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

    <netui-template:setAttribute name="sampleTitle" value="File Upload"/>

    <netui-template:section name="main">
        <p>
            The <code>&lt;netui:fileUpload&gt;</code> tag below binds to the <code>file</code>
            property on the action's form bean.  The property is of type
            <code>org.apache.struts.upload.FormFile</code>, which contains information about the
            submitted file as well as the file data itself.
        </p>
        <p>
            The <code>enctype</code> on this <code>&lt;netui:form&gt;</code> tag is
            "multipart/form-data", which is required for uploading files.
        </p>
        <p>
            Note that for security reasons, multipart request handing is <i>disabled</i> by
            default.  To enable it (and to choose whether the temporary uploaded file lives in
            memory or on disk), there are two options:
            <ul>
                <li>
                    Set the <code>multipartHandler</code> attribute on the page flow's
                    <code>@Jpf.Controller</code> annotation.  This enables/disables multipart
                    handling for this page flow only.
                </li>
                <li>
                    Set the <code>&lt;multipart-handler&gt;</code> within the
                    <code>&lt;pageflow-config&gt;</code> element in
                    WEB-INF/beehive-netui-config.xml.  This enables/disables multipart handling
                    for all page flows in the webapp (and can be overridden by the
                    <code>multipartHandler</code> annotation attribute as above).
                </li>
            </ul>
        </p>
        <h3>Upload a File</h3>
        <netui:form action="upload" enctype="multipart/form-data">
            <table>
                <tr>
                    <td>File:</td>
                    <td>
                        <netui:fileUpload dataSource="actionForm.file" readonly="false" size="40"/>
                        <netui:error key="file"/>
                    </td>
                </tr>
                <tr>
                    <td>Label:</td>
                    <td>
                        <netui:textBox dataSource="actionForm.label"/>
                    </td>
                </tr>
            </table>
            <netui:button value="submit"/>
        </netui:form>
    </netui-template:section>

</netui-template:template>
