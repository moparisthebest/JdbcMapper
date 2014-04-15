/*
 *
 */
package databinding.controls.methodTest;

import org.apache.beehive.controls.api.bean.ControlImplementation;

@ControlImplementation(isTransient=true)
public class MethodTestImpl implements MethodTest
{
    public String publicMethod() {return "public method";}
    public String publicMethodZeroArg() {return "public method: zero arg";}
    public String publicMethodOneArg(int value1) {return "public method: value1: " + value1;}
    public String publicMethodTwoArg(int value1, int value2) {return "public method: value1: " + value1 + " value2: " + value2;}
}
