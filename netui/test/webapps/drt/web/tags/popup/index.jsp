<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="netui" uri="http://beehive.apache.org/netui/tags-html-1.0"%>
<%@ taglib prefix="netui-data" uri="http://beehive.apache.org/netui/tags-databinding-1.0"%>
<%@ taglib prefix="netui-template" uri="http://beehive.apache.org/netui/tags-template-1.0"%>


<netui:html>
    <head>
        <netui:base/>
    </head>
    <netui:body>
        <h3>${pageFlow.URI}</h3>

        anchor:
        <netui:anchor action="goNested" popup="true">
            <netui:configurePopup width="200" height="100"/>
            goNested
        </netui:anchor>

        <br/>
        imageAnchor: 
        <netui:imageAnchor action="goNested" src="cool.gif" popup="true">
            <netui:configurePopup width="200" height="100"/>
        </netui:imageAnchor>

        <br/>
        area: 
        <img src="cool.gif" usemap="#map1"/>
        <map name="map1">
            <netui:area shape="rect" coords="0,0,25,25" action="goNested"  popup="true">
                <netui:configurePopup width="200" height="100"/>
            </netui:area>
        </map>

        <netui:form action="goNested">
            button (uses form's action):
            <netui:button value="goNested" popup="true">
                <netui:configurePopup width="200" height="100"/>
            </netui:button>

            <br/>
            button (overrides action):
            <netui:button action="goNested2" value="goNested2" popup="true">
                <netui:configurePopup width="200" height="100"/>
            </netui:button>
        </netui:form>
    </netui:body>
</netui:html>

  

