package miniTests.flowControllerFactory;

import org.apache.beehive.netui.pageflow.FlowControllerFactory;
import org.apache.beehive.netui.pageflow.FlowController;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Used in the FlowControllerFactory test.  Enabled in beehive-netui-config.xml.
 */
public class TestFactory
    extends FlowControllerFactory
{
    public FlowController getFlowControllerInstance(Class flowControllerClass)
        throws IllegalAccessException, InstantiationException
    {
        if (flowControllerClass.getName().equals("miniTests.flowControllerFactory.Controller"))
        {
            try
            {
                Constructor ctor = flowControllerClass.getConstructor(new Class[]{ String.class });
                String initVal = getConfig().getCustomProperty( "initVal" );
                return (FlowController) ctor.newInstance(new Object[]{ initVal });
            }
            catch (InvocationTargetException e)
            {
                throw new RuntimeException(e);   // won't happen in this test.
            }
            catch (NoSuchMethodException e)
            {
                throw new RuntimeException(e);   // won't happen in this test.
            }
        }

        return super.getFlowControllerInstance(flowControllerClass);
    }
}
