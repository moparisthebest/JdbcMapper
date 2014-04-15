This file describes what the various directories within control/test/src
are for and what to put where.

Controls test are made up of four parts.

* junit-controls
  
   This are examples of controls used for excercising different features 
   of the controls framework. For each new junit test a new control should
   be created.
   
* junit-tests
   These are the Junit files for the test suite.  When adding a new Junit test
   file categorize into existing or new package group based on the functionality
   being tests as necessary.

* auxilaries

   Auxilary files are things like Checkers.  Checkers must be compiled *before*
   the clients that will need them are built.  Files in 'aux' should not
   contain any dependencies on files which are outside of the aux src tree.

NOTE:
   Tests which require the JPF context should be added to the controlsWeb tests
   in the netui test suite.  The controls DRTs do not have a dependency on netui
   and should remain that way.
