package miniTests.generics;

public class BaseFormBean implements java.io.Serializable
{
    private String _foo;
    public void setFoo( String foo ) { _foo = foo; }
    public String getFoo() { return _foo; }
}
