Overview
---------

axis-client is a web service control (WSC) sample application.  This sample
contains a simple AXIS web service and a web client which uses a WSC.


External Dependencies
----------------------

In addition to a Beehive distribution the following must be present to run
this  sample:

*) Jakarta Tomcat
*) AXIS 1.3.x or later


Running
--------

1) Edit the build.properties file to reflect the Beehive and AXIS installation
   directories.
2) Build the web service war, 'ant build.webservice', wsc-sample.service.war
   will be generated.
3) Deploy the web service to your application server.
4) Build the client, 'ant clean build war', wsc-client-sample.war will
   be generated.
5) Deploy the client war file to your application server.
6) Sample can be accessed at <http://localhost:8080>/wsc-client-sample/index.jsp

