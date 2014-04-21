/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */
package org.apache.beehive.controls.runtime.bean;

import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.api.ControlException;

/**
 * Utilities used by the Controls runtime.
 */
public final class ControlUtils {

    private ControlUtils() {}

    /**
     * Implements the default control implementation binding algorithm ( <InterfaceName> + "Impl" ).  See
     * documentation for the org.apache.beehive.controls.api.bean.ControlInterface annotation.
     *
     * @param implBinding the value of the defaultBinding attribute returned from a ControlInterface annotation
     * @param controlClass the actual name of the interface decorated by the ControlInterface annotation
     * @return the resolved defaultBinding value
     */
    public static String resolveDefaultBinding( String implBinding, String controlClass )
    {
        int intfIndex = implBinding.indexOf(ControlInterface.INTERFACE_NAME);
        if (intfIndex >= 0)
        {
            implBinding = implBinding.substring(0,intfIndex) + controlClass +
                          implBinding.substring(intfIndex +
                                                ControlInterface.INTERFACE_NAME.length());
        }
        return implBinding;
    }

    /**
     * Returns the default binding based entirely upon annotations or naming conventions.
     * @param controlIntf the control interface class
     * @return the class name of the default control implementation binding
     */
    static String getDefaultControlBinding(Class controlIntf)
    {
        controlIntf = getMostDerivedInterface(controlIntf);

        ControlInterface intfAnnot =
                    (ControlInterface)controlIntf.getAnnotation(ControlInterface.class);
        String implBinding = intfAnnot.defaultBinding();
        implBinding = resolveDefaultBinding( implBinding, controlIntf.getName() );

        return implBinding;
    }

    /**
     * Computes the most derived ControlInterface for the specified ControlExtension.
     * @param controlIntf
     * @return the most derived ControlInterface
     */
    static Class getMostDerivedInterface(Class controlIntf)
    {
        while (controlIntf.isAnnotationPresent(ControlExtension.class))
        {
            Class [] intfs = controlIntf.getInterfaces();
            boolean found = false;
            for (int i = 0; i < intfs.length; i++)
            {
                if (intfs[i].isAnnotationPresent(ControlExtension.class) ||
                    intfs[i].isAnnotationPresent(ControlInterface.class))
                {
                    controlIntf = intfs[i];
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                throw new ControlException("Can't find base control interface for " + controlIntf);
            }
        }
        return controlIntf;
    }
}
