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

import java.util.ArrayList;

/**
 * The ImplInitializer class is a generated class that contains the code necessary to initialize
 * a ControlBean implementation instance.
 */
public class ImplInitializer
{
    /**
     * Constructs a new ImplInitializer class supporting a particular control bean implementation
     * @param controlImpl the control implementation to be initialized
     */
    protected ImplInitializer(AptControlImplementation controlImpl)
    {
        super();
        _controlImpl = controlImpl;
        _controlIntf = _controlImpl.getControlInterface();
        if (_controlImpl != null)
        {
            _packageName = _controlImpl.getPackage();
            _shortName = _controlImpl.getShortName() + "Initializer";
            _className = _packageName + "." + _shortName;
            if (_controlImpl.getSuperClass() != null)
                _superClass = new ImplInitializer(_controlImpl.getSuperClass());
        } 
        else
        {
            Class c = org.apache.beehive.controls.runtime.bean.ImplInitializer.class;
            _packageName = c.getPackage().getName();
            _className = c.getName();
            _shortName = _className.substring(_packageName.length() + 1);
        }

        //
        // Compute the list of impl fields that will require reflected Fields.
        //
        _reflectFields = new ArrayList<AptField>();
        for (AptField genField : _controlImpl.getContexts())
            if (needsReflection(genField))
                _reflectFields.add(genField);
        for (AptField genField : _controlImpl.getClients())
            if (needsReflection(genField))
                _reflectFields.add(genField);
    }

    /*
     * Return whether the ControlBean is contained in a package.
     */
    public boolean isRootPackage() {
        return _packageName == null || _packageName.trim().equals("");
    }

    /**
     * Returns the package name of the ImplInitializer
     */
    public String getPackage() { return _packageName; }

    /**
     * Returns the unqualified classname of the ImplInitializer
     */
    public String getShortName() { return _shortName; }

    /**
     * Returns the fully qualfied classname of the ImplInitializer
     */
    public String getClassName() { return _className; }

    /**
     * Returns the fully qualified classname of any associated ClientInitializer
     */
    public String getClientInitializerName()
    {
        return _controlImpl.getClassName() + "ClientInitializer";
    }

    /**
     * Returns the ControlBean implementation instance
     */
    public AptControlImplementation getControlImplementation() { return _controlImpl; }

    /**
     * Returns the public or extension interface associated with the ControlBean implementation
     */
    public AptControlInterface getControlInterface() { return _controlIntf; }

    /**
     * Returns the ImplInitializer super class for this ImplInitializer
     */
    public ImplInitializer getSuperClass() { return _superClass; }

    /**
     * Returns true if the ImplInitializer has a super class
     */
    public boolean hasSuperClass() { return _superClass != null; }

    /**
     * Returns true if the initializer will use Reflection to initialize the field, false
     * otherwise.
     */
    static public boolean needsReflection(AptField genField)
    {
        //
        // Since initializers are generated into the same package as the initialized class,
        // only private access fields require reflection
        //
        String accessModifier = genField.getAccessModifier();
        if (accessModifier.equals("private"))
            return true;

        return false;
    }

    /**
     * Returns the list of impl class fields that must be initialized using Reflection
     */
    public ArrayList<AptField> getReflectFields()
    {
        return _reflectFields;
    }

    String _packageName;
    String _shortName;
    String _className;
    AptControlImplementation _controlImpl;
    AptControlInterface _controlIntf;
    ImplInitializer _superClass;
    ArrayList<AptField> _reflectFields;
}
