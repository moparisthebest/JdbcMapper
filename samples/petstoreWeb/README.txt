Welcome to the Beehive Petstore
===============================

Requirements
============
The following software is required to build and deploy the pet store:
  - Java 5
  - Ant 1.6.2
  - Servlet or J2EE Container
  - Apache Derby 10.0.x JAR

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
Install Ant 1.6.2 and set the ANT_HOME environment variable to reference the Ant 
install directory.  Ensure that $ANT_HOME/bin is available in your $PATH.

3) J2EE Container

  A Servlet or J2EE container is required.  The petstoreWeb will run well on 
Tomcat 5.x.

4) Derby 10.0.x

   Download: http://db.apache.org/derby/derby_downloads.html

Once Derby has been downloaded, set the "derby.jar" property in build.properties
so that derby.jar can be located by the build file and copied to the webapp's 
WEB-INF/lib directory.
   
5) Specifying the Derby database location

Modify petstoreWeb/src/org/apache/beehive/samples/petstore/controls/data/DBProperties.java
in the petstoreWeb to set the location for the Derby petstore database. By default, 
this location is: "d:/Apache/Derby/petDB". Most users will need to change this.

Building
========
The samples/petstoreWeb is structured as:

samples/
  petstoreWeb/
    build.xml
    build.properties
    src/
      Contains the .java code for Beehive controls, form beans, and other sources / properties files
    web/
      Contains JSPs, .java Page Flows, images, and other web content.

When the webapp is built, a build/ directory is created and the Beehive runtime
is added to build/WEB-INF/lib.  Then, web content is copied from web/ to build/
and the Beehive source artifacts (controls and Page Flows) are built into WEB-INF/classes.

Once the webapp is built, the build/ directory contains the complete, exploded webapp 
and can be deployed to a Servlet container.

The Ant build files for the petstore are stored in the petstoreWeb/ directory.  In the 
instructions below, "$>" represents the command prompt in the samples/petstoreWeb/.

The first time you build the Petstore web application, the Beehive runtime libraries
will need to be copied into the webapp with:

  $> ant deploy-beehive

Then, the webapp can be built with:

  $> ant build

To clean the webapp, run:

  $> ant clean

To build a .war file for the webapp, run:

  $> ant war

Deploying and Running
=====================
Use your container's standard deployment practice to deploy the Web
application found in samples/petstoreWeb.

The default context path for petstoreWeb is "petstoreWeb".  When deployed to the server, 
the web appliciation should be available at the URL:

  http://<host>:<port>/petstoreWeb/

If you are using Tomcat 5.0.x for you J2EE container, you can deploy using the following:

  $> cd <BeehiveRoot>\samples\petstoreWeb
  $> ant war
  $> cp petstoreWeb.war $CATALINA_HOME\webapps

Check the Beehive Wiki or your application container's documentation for information about
deploying to other application containers.

Once deployed, if you have not created the tables in your Derby database before, you must do 
this now. Simply go to the home page of the petstore app and click on the link that says
"Initialize the DB".

Testing
=======
The petstore webapp includes a set of HTTPUnit tests that can be used to verify 
the webapp works during development.  These tests require HTTPUnit 1.6 which can be
downloaded from:

  http://httpunit.sourceforge.net/

Once HTTPUnit has been downloaded, set the "httpunit.dir" property in build.properties
so that the JARs can be located by the build file. Also, you must copy junit.jar into 
the $ANT_HOME/lib.

To build the tests, run:

  $> ant test.build

To run the tests, deploy the petstore webapp then run:

  $> ant test.run

To clean the tests, run:

  $> ant test.clean


