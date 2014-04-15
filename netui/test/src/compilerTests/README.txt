 * ===============================================================================================
 * How is a page flow compiled, and how do these tests work?
 * ===============================================================================================
 The Beehive compiler test framework is a JUnit TestCase which will attempt to compile pageflow 
 controller files and any controls files in the test directory and then analyze the compiler output 
 results.

 Each compilation of a test directory which is laid out as a normal Beehive webapp directory 
 structure is considered a junit test.

 You may put as many page flow directories as you want in each test directory . All the test 
 directories should reside in one testsuite directory. The testsuite location should be specified 
 throught an system viriable "testsuite.dir".

 Each test directory should contain an expectedOutput directory that has all the expected compiler 
 outputs. All the expected generated struts config files should be recorded there with the extention 
 ".expected" instead of ".xml". Any compiler warning or error messages are piped into a file named
 warningorerror.actual. The expected warning or error output should also be recorded into the 
 expectedOutput directory with the extention of ".expected" instead of ".actual".

 The test results are based on the line comparison between the actual and expected ouputs.

 Each of the test will be first copied into a build area which can be specified through a system 
 viriable "test.output.dir". You can specify a list of tests you want to include or exclude by the 
 test directory name throught the system viriables "included.test.list" and "excluded.test.list". 
 This two lists can work together to get the list of tests you want to include. 

 A class path that is required to run the beehive compiler needs to be passed though a system varialble 
 "compiler-tests.classpath". The class path should include beehive-netui-compiler.jar, 
 beehive-controls.jar, a servletAPI jar file and velocity jar files. The beehive-netui-core.jar is also 
 needed when needing to run javac after the apt compiler.

 To run the compiler Junit TestCase itselt, you will need the junit.jar, commons-logging and log4j in 
 your your classpath.

 The compiler test automation in beehive and beehive test distribution is built in through 
 netui/test/ant/junitCore.xml and netui/test/ant/compiler-test.properties 
 (or netui/test/dist-test/files/ant/compiler-test.properties for the distribution test). 


 * ================================================================================================
 * How to creat a compiler test
 * ================================================================================================
 1. Create a test directory and give it a meaningful name, such as PF_Forward. PF stands for Page Flow 
    and Forward means this is to compile a particular Forward annotation.

 2. You will then need to create page flow directories inside your test directory. Put any non-pageflow 
    source files, such as any controls under WEB-INF/src inside your test directory.

 3. Copy your test directory into the testsuite directory.

 4. Use the compiler-test.properties file to incude your only test into the included tests and then run 
    the compiler.bvt

 5. The test will fail for the first time since the expected compiler output is not being recirded yet.

 6. Examin the actual compiler out put, the struts config files under WEB-INF/classes/_pageflow and the
    warningsorerros.actual under your test directory. 

 7. Create a expectedOutput directory in your test directory that resides in the testsuite directory and copy the 
    correct struts config files from WEB-INF/classes/_pageflow and rename them with the extention of 
    ".expected".

 8. Copy the warningorerros.actual to the expectedOutput directory when it shows up as expected. Change 
    any path to the begining of WEB-INF into [LOCAL_PATH] for easy handling of file comparison in 
    different environments. For example, change "D:\directory1\directory2\directory3\WEB-INF\.tmpbeansrc
    \PI_ControlsOverride\ControlsController.java:17:" into "[LOCAL_PATH]\WEB-INF\.tmpbeansrc\
    PI_ControlsOverride\ControlsController.java:17:" and then rename the file with extention ".expected". 


 9. Run the test again and your test should PASS.


 * =================================================================================================
 * HowTo: Debug/Fix a PageFlowCompilerTest failure.
 * =================================================================================================

 1. The junit failure message should show what file comparison failed.
 2. Use your favorite diff tool to compare the .actual and .expected files.
 3. Is this change in behavior valid?
 4. Should the resulting files be created?
 5. Are the resulting files correct?
 6. Are any files not created, that should be created?
 7. Are the warnings or errors correctly reported, or not reported?
 8. Has the compiler changed in the way it works: arguments or compiler extensions?
 9. Have the test targets changed the classpath, or the way tests are run?
 10. Have the annotations changed for your particular file extension?
 11. Either update your page flow and/or update your expected files with the .actual files.
