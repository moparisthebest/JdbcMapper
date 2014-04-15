Some brief notes on this test:

This test is setup so that the BarControl is processed last by APT so that the FooControlBean field must be resolved
to either a.FooControlBean or b.FooControlBean (this is the right choice).  In order for APT to correctly determine
which FooControlBean to use it must correctly process the @Control annotation's control interface hint.

If working properly the BarControlClientInitializer should have a reference to the correct FooControlBean, otherwise
a compilation error occurs.
