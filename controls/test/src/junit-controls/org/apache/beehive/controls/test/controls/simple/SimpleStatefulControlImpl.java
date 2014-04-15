/**
 * Created by IntelliJ IDEA.
 * User: ekoneil
 * Date: Apr 27, 2006
 * Time: 12:47:32 PM
 * To change this template use File | Settings | File Templates.
 */
package org.apache.beehive.controls.test.controls.simple;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;

@ControlImplementation(isTransient = true)
public class SimpleStatefulControlImpl
    implements SimpleStatefulControl {

    @Context
    private ControlBeanContext _context;

    private int _value;

    public int getValue() {
        return _value;
    }

    public void setValue(int value) {
        _value = value;
    }
}
