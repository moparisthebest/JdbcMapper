Welcome to the NetUI Samples
============================

Requirements
============
The following software is required to build and deploy this application:
  - Java 5
  - Ant 1.6.2
  - Servlet or J2EE Container

Installing Dependencies
=======================
1) Java 5

  Download: http://java.sun.com/j2se/1.5.0/download.jsp

  Install Java 5 and set the JAVA_HOME environment variable to
  refernce the JDK install directory.  Ensure that $JAVA_HOME/bin is
  available in your $PATH.

2) Ant 1.6.2

  Download: http://ant.apache.org/bindownload.cgi

  Ant 1.6.2 is the minimum version required by the Beehive build infrastructure.
  Install Ant 1.6.2 and set the ANT_HOME environment variable to reference the
  Ant install directory.  Ensure that $ANT_HOME/bin is available in your $PATH.

3) J2EE Container

  A Servlet or J2EE container is required.  The web application will run well
  on Tomcat 5.x.  If you are using Tomcat, make sure that the CATALINA_HOME
  environment variable is set to the root of the Tomcat installation.

Building
========
The application is set up to create an exploded web application directory in
build/ by copying the web/ directory, building Beehive source artifacts, and 
compiling the source files in the src/ directory.

The first time you build the web application, the Beehive runtime libraries will
need to be copied into the webapp with:

  $> ant deploy-beehive

Then, the webapp can be built with:

  $> ant build

To clean the webapp, run:

  $> ant clean

To build a .war file for the webapp, run:

  $> ant war

Deploying and Running
=====================
Use your container's standard deployment practice to deploy the web
application found in samples/netui-samples.

The default context path for the webapp is "netui-samples".  When deployed to
the server, the web appliciation should be available at the URL:

  http://<host>:<port>/netui-samples/

If you are using Tomcat 5.0.x for you J2EE container, you can deploy using the
following:

  $> ant build war
  $> cp netui-samples.war $CATALINA_HOME/webapps

Check the Beehive Wiki or your application container's documentation for
information about deploying to other application containers.
