<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="db"%>
<html>
<head>
<title>Binding To Structures</title>
</head>
<body>
<h4>Binding To Structures and fields</h4>
HashMap["foo"]: <b><netui:span value='${pageFlow.hashValue["foo"]}' /></b><br />
HashMap."Baz": <b><netui:span value="${pageFlow.hashValue.baz}" /></b><br />
Hash.Array[3]: <b><netui:span value="${pageFlow.hashValue.array[3]}"/></b><br />
Array [2]: <b><netui:span value="${pageFlow.array[2]}"/></b><br />
public.field: <b><netui:span value="${pageFlow.pubField}"/></b><br />
public.Array [1]: <b><netui:span value="${pageFlow.pubArray[1]}"/></b><br />
class.field: <b><netui:span value="${pageFlow.info.value}"/></b><br />
Hash.class.field: <b><netui:span value="${pageFlow.hashValue.info.value}"/></b><br />
public.class.field: <b><netui:span value="${pageFlow.pubInfo.value}"/></b><br />
public Hash["foo"]: <b><netui:span value='${pageFlow.pubHash["foo"]}'/></b><br />
public Hash "Baz" with ".": <b><netui:span value="${pageFlow.pubHash.baz}"/></b><br />
public Hash <null> with ".": <b><netui:span value="${pageFlow.pubHash.nullValue}"/></b><br />
public Hash <null> with ["nullValue"]: <b><netui:span value='${pageFlow.pubHash["nullValue"]}'/></b><br />
public, existing null field: <b><netui:span value='${pageFlow.nullField}'/></b><br />
</body>
</html>

	


			   
