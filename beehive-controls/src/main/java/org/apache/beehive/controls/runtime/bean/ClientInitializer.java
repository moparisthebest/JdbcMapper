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

import java.lang.reflect.AnnotatedElement;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.properties.PropertyMap;
import org.apache.beehive.controls.api.properties.AnnotatedElementMap;
import org.apache.beehive.controls.api.versioning.VersionRequired;
import org.apache.beehive.controls.api.versioning.Version;

/**
 * The ClientInitializer class is an abstract base class that all generated Control
 * client initializer classes will extend.  It provides common utilities and supporting code
 * for initialization, and has a shared package relationship with the base ControlBean
 * class providing access to internals not available in a more general context.
 */
abstract public class ClientInitializer
{
    /**
     * Enforces the VersionRequired annotation at runtime (called when an instance of a control is annotated
     * with VersionRequired).  Throws a ControlException if enforcement fails.
     *
     * @param versionRequired the value of the VersionRequired annotation on a control field
     */
    protected static void enforceVersionRequired( ControlBean control, VersionRequired versionRequired )
    {
        Class controlIntf = ControlUtils.getMostDerivedInterface( control.getControlInterface() );

        Version versionPresent = (Version) controlIntf.getAnnotation( Version.class );
        if ( versionPresent != null )
        {
            ControlBean.enforceVersionRequired( controlIntf.getCanonicalName(), versionPresent, versionRequired );
        }
    }

    /**
     * Returns the annotation map for the specified element.
     */
    public static PropertyMap getAnnotationMap(ControlBeanContext cbc, AnnotatedElement annotElem)
    {
        if ( cbc == null )
            return new AnnotatedElementMap(annotElem);

        return cbc.getAnnotationMap(annotElem);
    }
}
