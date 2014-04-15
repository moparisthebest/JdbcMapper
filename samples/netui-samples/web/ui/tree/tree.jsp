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
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>

<netui-template:template templatePage="/resources/template/template.jsp">
  <netui-template:setAttribute name="sampleTitle" value="Tree"/>
  <netui-template:section name="main">
    <div style="border: thin solid;height: 300px;">
      <netui:tree dataSource="pageFlow.root"
        selectionAction="selectFrame"
		expansionAction="selectTree"
		selectionTarget="contentFrame"
        tagId="tree"
        selectedStyle="background-color: #FFD185; font-color: #FFFFFF; text-decoration: none;"
	    unselectedStyle="text-decoration: none">
        <netui:treeItem title="0" expanded="true">
        <netui:treeLabel>0</netui:treeLabel>
          <netui:treeItem title="0.0" expanded="false">
          <netui:treeLabel>0.0</netui:treeLabel>
            <netui:treeItem title="0.0.0" expanded="false">
            <netui:treeLabel>0.0.0</netui:treeLabel>
              <netui:treeItem title="0.0.0.0">0.0.0.0</netui:treeItem>
              <netui:treeItem title="0.0.0.1">0.0.0.1</netui:treeItem>
            </netui:treeItem>
          </netui:treeItem>
          <netui:treeItem title="0.1" expanded="false">
          <netui:treeLabel>0.1</netui:treeLabel>
            <netui:treeItem title="0.1.0">0.1.0</netui:treeItem>
            <netui:treeItem title="0.1.1">0.1.1</netui:treeItem>
          </netui:treeItem>
          <netui:treeItem title="0.2" expanded="false">
          <netui:treeLabel>0.2</netui:treeLabel>
            <netui:treeItem title="0.2.0">0.2.0</netui:treeItem>
            <netui:treeItem title="0.2.1">0.2.1</netui:treeItem>
	    </netui:treeItem>
        </netui:treeItem>
      </netui:tree>
    </div>
  </netui-template:section>
</netui-template:template>
