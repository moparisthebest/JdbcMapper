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
package org.apache.beehive.controls.runtime.generator;

import com.sun.mirror.type.InterfaceType;

/**
 * The ControlBean class is an class representing a generated JavaBean class that can host
 * control implementation types associated with a particular control public or extension
 * interface.
 */
public class ControlBean
{
    /**
     * Constructs a new ControlBean class supporting a particular bean interface
     * @param controlIntf the public interface associated with the bean
     */
    protected ControlBean(AptControlInterface controlIntf)
    {
        super();
        _controlIntf = controlIntf;
        if (_controlIntf != null)
        {
            _packageName = _controlIntf.getPackage();
            _shortName = _controlIntf.getShortName() + "Bean";
            _className = isRootPackage() ? _shortName : _packageName + "." + _shortName;

            _superClass = new ControlBean(_controlIntf.getSuperClass());
        } 
        else
        {
            Class c = org.apache.beehive.controls.runtime.bean.ControlBean.class;
            _packageName = c.getPackage().getName();
            _className = c.getName();
            _shortName = _className.substring(_packageName.length() + 1);
        }
    }

    /**
     * Return whether the ControlBean is contained in a package.
     */
    public boolean isRootPackage() {
        return _packageName == null || _packageName.trim().equals("");
    }

    /**
     * Returns the fully qualified package name of the ControlBean
     */
    public String getPackage() { return _packageName; }

    /**
     * Returns the unqualified classname of the ControlBean
     */
    public String getShortName() { return _shortName; }

    /**
     * Returns the fully qualified classname of the ControlBean
     */
    public String getClassName() { return _className; }

    /**
     * Returns the class declaration for the ControlBean
     */
    public String getClassDeclaration() 
    {
        StringBuffer sb = new StringBuffer(_shortName);
        sb.append(_controlIntf.getFormalTypeParameters());
        return sb.toString();
    }

    /**
     * Returns the fully qualified classname of the ControlBean BeanInfo class.  The
     * standard JavaBean naming convention is used to enable automatic location by
     * the JavaBean introspector.
     */
    public String getBeanInfoName() { return _className + "BeanInfo"; }


    /**
     * Returns the class as a Jar Manifest Name attribute
     */
    public String getManifestName() { return _className.replace('.','/') + ".class"; }

    /**
     * Returns the public or extension interface associated with the ControlBean
     */
    public AptControlInterface getControlInterface() { return _controlIntf; }

    /**
     * Returns the super class for this ControlBean
     */
    public ControlBean getSuperClass() { return _superClass; }

    /**
     * Returns any formal type parameters that should be bound for the bean's superclass,
     * based upon any type bindings that occur on the original interface.
     */
    public String getSuperTypeBinding()
    {
        InterfaceType superType = _controlIntf.getSuperType();
        if (superType != null)
        {
            String typeStr = superType.toString();
            int paramIndex = typeStr.indexOf('<');
            if (paramIndex > 0)
                return typeStr.substring(paramIndex);
        }
        return "";
    }

    String _packageName;
    String _shortName;
    String _className;
    AptControlInterface _controlIntf;
    ControlBean _superClass;
}
