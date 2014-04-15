Beehive Web Service Control Tests
=================================

The directory structure is as follows:

  build/
    - build directory generated while running the tests
  jcxgen-tests/
    - standalone JUnit tests used to test the generation of service controls
  schemas/
    - WSDLs used to generate web services
  servers/
    - webapps used to host web services
  tests/
    - unit tests run against the web service webapp

Adding New Tests
================

Depending on what you are testing:

  * add new jcx generation tests to the jcxgen-tests area, these tests are run standalone without tomcat

  * For new web services control runtime tests:

    - Junit tests go in the 'tests' subdirectory, maintain the existing package structure
      'org.apache.beehive.controls.system.webservice.units' (even though its alot of package names) and
      add you JUnit test to a new subpackage of 'units' or add to an existing subpackage of 'units' if
      that seems more appropriate.  NOTE: All JUnit test class names must end with 'Test' (ex. MyJunitTest.class)
      in order to be run by the ant scripts.

    - Add new WebService controls (if not generating from a wsdl) to the 'webservice-controls' subdirectory - this is
      where web service controls go that have been created from hand (are not wsdl generated).

    -  Add new wsdl's to the 'schemas' subdirectory.  Wsdl files placed here will be used to automatically generate
       web service controls at build time.

    -  Add new test web services to the 'servers' subdirectory.

Additional Notes
=================

  * Web service control attachment tests are disabled.  Modify the JUnit portion of the build.xml file to enable.
    These tests require the J2EE activation and mailapi's.