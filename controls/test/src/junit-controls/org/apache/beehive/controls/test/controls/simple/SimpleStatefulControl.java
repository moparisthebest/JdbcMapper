/**
 * Created by IntelliJ IDEA.
 * User: ekoneil
 * Date: Apr 27, 2006
 * Time: 12:47:00 PM
 * To change this template use File | Settings | File Templates.
 */
package org.apache.beehive.controls.test.controls.simple;

import org.apache.beehive.controls.api.bean.ControlInterface;

@ControlInterface
public interface SimpleStatefulControl {

    public int getValue();

    public void setValue(int value);
}
 