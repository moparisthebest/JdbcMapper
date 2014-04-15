Follow the following steps to test your Beehive Distribution.  This assumes you have created the build.dist and
build.test.dist using the scripts found under trunk.

For Windows run buildTestDistro.cmd
For Unix/Linux run buildTestDistro.sh

These shell scripts will produce the binaries you can then use to test the Beehive distribution you created.

Once the buildTestDistro.cmd or buildTestDistro.sh script has completed successfully, follow the steps below
to test your distribution:

1. Locate the distribution and test distribution binaries, which can be found in the directory

 <SVN TRUNK>/build/dist/apache-beehive-svn-snapshot  (note, not yet compressed bug pending)
and
 <SVN TRUNK>/build/testDistArchives/apache-beehive-svn-snapshot-test.zip
 (For unix/linux, see file apache-beehive-svn-snapshot-test.tar.gz)

2. Move dist and test dist to shared location on your disk where you would like tests to run.  Suggest a path 
like the following: (referred to from here on out as <dist_home> in these notes.
  c:\beehiveDist

After copy and extraction the directory structure will look something like this.
<dist_home>\apache-beehive-svn-snapshot
<dist_home>\beehive-test-dist

3. Make sure the following environment variables are set for the shell,
         %CATALINA_HOME% ($CATALINA_HOME for unix)
         %ANT_HOME% ($ANT_HOME for unix)
         %JAVA_HOME% {$JAVA_HOME for unix}

    also make sure that you %JAVA_HOME%\bin and %ANT_HOME%\bin is in your path.

  Here is an example of the script that you might want to use to set your environment,
  *********************
    @echo off
    REM
    REM Customize this file based on where you install various 3rd party components
    REM such as the JDK, Ant and Tomcat, and also the location of the distribution 
    REM you are going to test against.
    REM

    REM location of a JDK
    set JAVA_HOME=C:\jdk1.5.0-B64

    REM location of Ant
    set ANT_HOME=C:\apache-ant-1.6.2

    REM location of Tomcat
    set CATALINA_HOME=C:\jakarta-tomcat-5.0.25

    set PATH=%PATH%;%JAVA_HOME%\bin;%ANT_HOME%\bin
  **************************

4. Change directory to beehive-test-dist
5. run 'ant run.tests' to test the default distribution that come with the package

   If you get a deployment error around coreWeb already existing.  This needs to 
   be undeployed manually.  To do this, start Tomcat and undeploy using the following:
     ant -f netui/ant/tomcat-import.xml undeploy -Dcontext.path=coreWeb
   This will only occur is running the source tree and distribution tree with the same
   tomcat instance.

6. Or run 'ant -Ddist.home=d:\beehive-distibution run.tests' to run tests on an alternative beehive distribution.

You will find the test reports in /beehive-test-dist/build/testResults directory. For example,
                       testResults/netui/drt.testResults/html/junit-noframes.html (Junit drt test results)
                       testResults/netui/bvt.testResults/html/junit-noframes.html (Junit bvt test results)
                       testResults/netui/testRecorder/drt/html/junit-noframes.html (Test Recorder drt test results)
                       testResults/netui/bvt.testResults/html/junit-noframes.html (Junit bvt test results)

