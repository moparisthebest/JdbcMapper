<%@ page language="java" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-databinding-1.0" prefix="netui-data"%>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui-html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>Simple Pad Test</title>
  </head>
  <body>
<br/>Note, this data set is a 10 item, non-null dataset.<br/>
<b>max=10</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="10" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=10, min=10</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="10" minRepeat="10" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=11, min=10</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="11" minRepeat="10" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=22, min=10</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="22" minRepeat="10" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>min=11</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad minRepeat="11" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>min=15</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad minRepeat="15" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>min=15, bodyContent pad</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad minRepeat="15">FOO<br/></netui-data:pad>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>min=15, empty pad</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad minRepeat="15"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=8, min=5</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="8" minRepeat="5" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=5, min=5</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="5" minRepeat="5" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=2, min=2</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="2" minRepeat="2" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=2, min=1</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="2" minRepeat="1" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=1, min=1</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="1" minRepeat="1" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>min=1</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad minRepeat="1" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>max=1</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad maxRepeat="1" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<b>min=0, max=5</b><br/>
<netui-data:repeater dataSource="pageFlow.simpleJavaBeans">
    <netui-data:pad minRepeat="0" maxRepeat="5" padText="FOO<br/>"/>
    <netui-data:repeaterItem>
        <b>array[<netui-html:span value="${container.item.index}"/>] = <netui-html:span value="${container.item.textProperty}"/></b><br/>
    </netui-data:repeaterItem>
</netui-data:repeater>
<br/>
<hr/>
  </body>
</html>
