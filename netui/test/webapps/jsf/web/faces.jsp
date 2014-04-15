<%@ page import="java.net.URL,
    org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinitions,
    org.apache.beehive.netui.tools.testrecorder.server.TestRecorderFilter" %>
<% String _title = "JSF Tests"; %>
<%@ include file="dataStructure.jspf"%>
<%
    TestDefinitions testDefs = TestRecorderFilter.instance().getTestDefinitions();
    Info[] _testList = {
	new Info("FacesSmokeAnchors", "/jsfWeb/faces/facesSmoke/Controller.jpf",
	 "Smoke test of the JSF Anchor.",
	 "JSF, commandLink"
		 ),
	new Info("FacesSmokeBundle", "/jsfWeb/faces/facesSmoke/Controller.jpf",
	 "Smoke test of the JSF Bundle.",
	 "JSF, bundle"
		 ),
	new Info("FacesSmokeConversions", "/jsfWeb/faces/facesSmoke/Controller.jpf",
	 "Smoke test of the JSF Conversions.",
	 "JSF, conversions"
		 ),
	new Info("FacesSmokeDataTable", "/jsfWeb/faces/facesSmoke/Controller.jpf",
	 "Smoke test of the JSF DataTable.",
	 "JSF, DataTable"
		 ),
	new Info("FacesSmokeFormOne", "/jsfWeb/faces/facesSmoke/Controller.jpf",
	 "Smoke test of the JSF Form and controls.",
	 "JSF, Form"
		 ),
	new Info("FacesSmokeFormTwo", "/jsfWeb/faces/facesSmoke/Controller.jpf",
	 "Smoke test of the JSF Form and controls.",
	 "JSF, Form"
		 ),
	new Info("FacesSmokeImage", "/jsfWeb/faces/facesSmoke/Controller.jpf",
	 "Smoke test of the JSF Image.",
	 "JSF, GraphicImage"
		 ),
	new Info("FacesSmokePanel", "/jsfWeb/faces/facesSmoke/Controller.jpf",
	 "Smoke test of the JSF Panel.",
	 "JSF, PanelGrid"
		 ),
    };
%>
<%@ include file="page.jspf"%>
