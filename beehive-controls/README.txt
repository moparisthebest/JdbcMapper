This directory contains the Beehive Controls source and test files.  This
README provides an overview of the basic directory structure, and describes
some of the useful ant targets for building, generating javadoc, and running
tests for the Controls runtime.

SOURCE DIRECTORY STRUCTURE:

./src:

Contains all of the source files for the Beehive Controls runtime.  When the
runtime is built, all of the generated classes end up in 
build/jars/controls.jar.

./src/api:

Contains all of the source files for Controls public APIs used by Control
authors or clients.   All annotation type, interfaces, and classes in the public 
API set live within the org.apache.beehive.controls.api.* package space.

./src/spi:

Contains a small set of service provider interfaces used to adapt or
extend the Controls runtime for a specific environment.  The audience for
the SPI set is primary system developers who want to integrate the runtime
into a specific container or application server environment, implement a
specific type of interceptor or instantiation factory, etc.   All of the
classes in the SPI set live within the org.apache.beehive.controls.spi.*
package space.

./src/runtime:

Contains the Control runtime implementation classes.  There are several
base classes used for code-generated ControlBeans as well as supporting
runtime classes for properties, contextual services, container integration,
etc.   Control authors or clients should never reference any of these 
runtime classes directly.  All of the runtime classes live within the 
org.apache.beehive.controls.runtime.* package space.

SOURCE DEPENDENCIES:

The dependencies across the various types of Control sources are:

api -> spi : API public factory classes depend upon some SPI interfaces
spi -> api : the SPI classes often consume public classes
runtime -> api, spi : the runtime classes reference both API and SPI types

Note: there are *no* dependencies from public interfaces to runtime classes.
This relationship is enforced by actually having them compile separately
(api + spi first, then runtime).

SOURCE ANT TARGETS

This section describes some of the available ant targets from the Controls
root directory.

ant build:

Compiles all annotation type, interface, and class files in the API, SPI, and
runtime directory and creates build/jars/controls.jar to contain them.

ant docs:

Generates javadoc documention for all API, SPI, and runtime classes and puts
them in build/docs.  After running this target, you can browse to 
file:build/docs/apidocs/classref_controls/index.html to view them.

ant clean:

Removes all generated output files from any of the build targets in the top
level or test directories.

TEST INFRASTRUCTURE:

The Controls runtime test infrastructure lives under the test subdirectory.  It
includes a variety of test for Controls running in different context, from
vanilla Junit/java tests to running Controls inside of the various containers
that are part of the Beehive programming model: JWS, JPF, and nesting inside
of other controls.    More details about the Controls runtime test tools
can be found at http://wiki.apache.org/beehive/Controls/TestingControls. 

CONTROLS TEST TARGETS:

There are two main test targets for running Controls tests.  These should be run
from within the controls/test directory:

ant checkin.tests:

These are a set of checkin tests that do shallow testing of a broad range of
functionality.   These should be run and pass 100% before a committer submits
any Controls runtime changes.

ant detailed.tests:

This runs all control tests.  Since some of them are test cases that are the
basis of open JIRA issues, THESE TESTS ARE NOT EXPECTED TO PASS 100%.   Currently,
there is no good filter for running the detailed tests that are expected to
pass, but it has been suggested that this would be a good thing (to enable
deeper testing of larger changes).
