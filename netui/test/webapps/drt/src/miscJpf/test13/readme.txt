Here is what I did/do to run this test under tomcat.

1) copy {devRoot}/netui/applications/netuiDRTApp/netuiDRT/qaNetuiWeb/WEB-INF/tomcat-web.xml -> web.xml
2) copy {devRoot}/netui/applications/netuiDRTApp/netuiDRT/qaNetuiWeb/WEB-INF/src/miscJpf/test13/tomcat-struts-config-test13.xml -> struts-config-test13.xml
3) edit {devRoot}/netui/applications/netuiDRTApp/netuiDRT/qaNetuiWeb/WEB-INF/src/miscJpf/test13/bld.bat
   so environment variables "libRoot" & "webAppRoot" point to locations in your dev environment.
4) from a dos shell execute bld.bat
5) edit the Tomcat/conf/server.xml file and add a line similar to the one below.
   put this line within the <Service> attribute.
      <Context path="/test13" docBase="{devRoot}\netui\applications\netuiDRTApp\netuiDRT\qaNetuiWeb" />
6) start tomcat and hit "http://localhost:8080/test13"

