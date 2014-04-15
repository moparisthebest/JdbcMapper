<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://beehive.apache.org/netui/tags-html-1.0" prefix="netui" %>
<html>
<head>
<title>Other Escaped Character Test</title>
<netui:base />
</head>
<body>
<h1>Other Escaped Character Test</h1>

<h3>anchor tag</h3>
<pre>
mark = '-' | '_' | '.' | '!' | '~' | '*' | "'" | '(' | ')'
</pre>
<netui:anchor href="a-b_c.d!e~f*g'h(i)j.html?mark=a-b_c.d!e~f*g'h(i)j#a-b_c.d!e~f*g'h(i)j">
    mark - test href
</netui:anchor>
<br>
<netui:anchor href="a-b_c.d!e~f*g'h(i)j.html" location="a-b_c.d!e~f*g'h(i)j">
    mark - test href and parameter
    <netui:parameter name="mark" value="a-b_c.d!e~f*g'h(i)j"/>
</netui:anchor>
<br>

<pre>
reserved = ';' | '/' | '?' | ':' | '@' | '&' | '=' | '+' | '$' | ','
note: path also has ';' | '/' | '='| '?' reserved
</pre>
<netui:anchor href="b/d:e@f&h+i$j,k.html?reserved=a;b/c?d:e@f&g=h+i$j,k#a;b/c?d:e@f&g=h+i$j,k">
    reserved - test href
</netui:anchor>
<br>
<netui:anchor href="b/d:e@f&h+i$j,k.html" location="a;b/c?d:e@f&g=h+i$j,k">
    reserved - test href and parameter
    <netui:parameter name="reserved" value="a;b/c?d:e@f&g=h+i$j,k"/>
</netui:anchor>
<br>

<pre>
unwise = '{' | '}' | '|' | '\' | '^' | '[' | ']' | "`"
</pre>
<netui:anchor href="a{b}c|d\e^f[g]h`i.html?unwise=a{b}c|d\e^f[g]h`i#a{b}c|d\e^f[g]h`i">
    unwise - test href
</netui:anchor>
<br>
<netui:anchor href="a{b}c|d\e^f[g]h`i.html" location="a{b}c|d\e^f[g]h`i">
    unwise - test href and parameter
    <netui:parameter name="unwise" value="a{b}c|d\e^f[g]h`i"/>
</netui:anchor>
<br>

<pre>
delim = '<' | '>' | '#' | '%' | '"'
</pre>
<netui:anchor href="a<b>d%e\"f.html?delim=a<b>d%e\"f#a<b>c#d%e\"f">
    delim - test href
</netui:anchor>
<br>
<netui:anchor href="a<b>d%e\"f.html" location="a<b>c#d%e\"f">
    delim - test href and parameter
    <netui:parameter name="delim" value="a<b>d%e\"f"/>
</netui:anchor>
<br>

<pre>
space = ' '
</pre>
<netui:anchor href="a b.html?spaces=a b#a b">
    spaces - test href
</netui:anchor>
<br>
<netui:anchor href="a b.html" location="a b">
    spaces - test href and parameter
    <netui:parameter name="spaces" value="a b"/>
</netui:anchor>
<br>

<h3>rewriteURL tag</h3>
<netui:rewriteURL URL="b/d:e@f&h+i$j,k.html?reserved=a;b/c?d:e@f&g=h+i$j,k#a;b/c?d:e@f&g=h+i$j,k"/>
<br>
<netui:rewriteURL URL="a{b}c|d\e^f[g]h`i.html?unwise=a{b}c|d\e^f[g]h`i#a{b}c|d\e^f[g]h`i"/>
<br>
<netui:rewriteURL URL="space test.html">
    <netui:parameter name="excluded" value="space a<b>d%e\"f"/>
</netui:rewriteURL>
<br>

<h3>imageAnchor tag</h3>
<netui:imageAnchor href="space test.gif" alt="space test">
    <netui:parameter name="excluded" value="space a<b>d%e\"f"/>
</netui:imageAnchor>

<p>
Return <netui:anchor action="begin">home</netui:anchor>
</p>

</body>
</html>

