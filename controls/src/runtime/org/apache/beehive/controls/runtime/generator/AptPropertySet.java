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
import java.util.Collection;

import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Declaration;

import org.apache.beehive.controls.api.properties.PropertySet;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptPropertySet class represents a control PropertySet where the property list
 * is derived using APT metadata
 */
public class AptPropertySet
{
    /**
     * Constructs a new AptPropertySet instance from APT metadata
     * @param controlIntf the declaring bean interface.  May be null (external property set)
     * @param propertySet the PropertySet declaration
     * @param ap the annotation processor
     */
    public AptPropertySet(AptControlInterface controlIntf,
                          Declaration propertySet,
                          TwoPhaseAnnotationProcessor ap)
    {
        _ap = ap;
        _controlIntf = controlIntf;

        if (!(propertySet instanceof AnnotationTypeDeclaration))
        {
            _ap.printError( propertySet, "propertyset.illegal.usage" );
            return;
        }
        _propertySet = (AnnotationTypeDeclaration)propertySet;

        _isOptional = _propertySet.getAnnotation(PropertySet.class).optional();
        _hasSetters = _propertySet.getAnnotation(PropertySet.class).hasSetters();

        _properties = initProperties();
    }

    /**
     * Initializes the list of ControlProperties associated with this ControlPropertySet
     */
    protected ArrayList<AptProperty> initProperties()
    {
        ArrayList<AptProperty> properties = new ArrayList<AptProperty>();

        if (_propertySet == null || _propertySet.getMethods() == null )
            return properties;
        
        // Add all declared method, but ignore the mystery <clinit> methods
        for (MethodDeclaration methodDecl : _propertySet.getMethods())
            if (!methodDecl.toString().equals("<clinit>()"))
                properties.add(
                    new AptProperty(this,(AnnotationTypeElementDeclaration)methodDecl,_ap));

        return properties;
    }

    /**
     * Returns the list of ControlProperties associated with this ControlPropertySet
     */
    public Collection<AptProperty> getProperties() { return _properties; }

    /**
     * Returns the fully qualified package name of this property set
     */
    public String getPackage()
    {
        if (_propertySet == null || _propertySet.getPackage() == null )
            return "";
        
        return _propertySet.getPackage().getQualifiedName();
    }

    /**
     * Returns the unqualified classname of this property set
     */
    public String getShortName()
    {
        if (_propertySet == null )
            return "";
        
        return _propertySet.getSimpleName();
    }

    /**
     * Returns the fully qualified class name of the property set
     */
    public String getClassName()
    {
        if (_propertySet == null )
            return "";

        return _propertySet.getQualifiedName();
    }

    /**
     * Returns the property name prefix for properties in this PropertySet
     */
    public String getPrefix()
    {
        if (_propertySet == null || _propertySet.getAnnotation(PropertySet.class) == null )
            return "";
        
        return _propertySet.getAnnotation(PropertySet.class).prefix();
    }

    /**
     * Returns whether or not this propertyset exposes setters
     */
    public boolean isOptional()
    {
        return _isOptional;
    }

    /**
     * Returns whether or not this propertyset exposes setters
     */
    public boolean hasSetters()
    {
        return _hasSetters;
    }

    private AnnotationTypeDeclaration _propertySet;
    private AptControlInterface _controlIntf;           // may be null if an external property set
    private ArrayList<AptProperty> _properties;
    private TwoPhaseAnnotationProcessor _ap;
    private boolean _isOptional;
    private boolean _hasSetters;
}
