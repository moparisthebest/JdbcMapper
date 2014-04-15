To run this sample, following these steps:

(1) Complete the required and optional steps in the topic: http://beehive.apache.org/docs/1.0/tutorial/setup.html

(2) Download the latest version of derby and copy derby.jar to $CATALINA_HOME/common/lib

(3) Configure Tomcat

   [todo: still working on getting a working Tomcat configuration]

(4) Configure the web app

Add the following to your apps WEB-INF/web.xml file.  Position it immediately before the </web-app> tag:

    <resource-ref>
        <description>DB Connection</description>
        <res-ref-name>jdbc/JdbcControlSampleDB</res-ref-name>
        <res-type>javax.sql.DataSource</res-type>
        <res-auth>Container</res-auth>
    </resource-ref>

(5) Edit /system-controls-jdbc/build.properties so that beehive.home points to the top level folder of your beehive installation.

(6) Start Tomcat $CATALINA_HOME/bin/startup.bat

(7) cd to /system-controls-jdbc and run: ant clean build war

(8) cp jdbcControlSample.war $CATALINA_HOME/webapps

(9) visit: http://localhost:8080/jdbcControlSample/Controller.jpf
