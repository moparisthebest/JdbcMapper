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

import java.util.Collection;
import java.util.Map;

import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.type.AnnotationType;
import com.sun.mirror.type.PrimitiveType;

import org.apache.beehive.controls.api.packaging.FeatureInfo;
import org.apache.beehive.controls.api.packaging.PropertyInfo;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptProperty class represents a control Property where the property attributes
 * are derived using APT metadata
 */
public class AptProperty
{
    /**
     * Constructs a new AptProperty instance
     * from APT metadata
     * @param propertySet the declaring PropertySet
     * @param propDecl the declration of the property annotation type element
     */
    public AptProperty(AptPropertySet propertySet, AnnotationTypeElementDeclaration propDecl,
                       TwoPhaseAnnotationProcessor ap)
    {
        _propertySet = propertySet;
        _propDecl = propDecl;
        _ap = ap;

        //
        // Primitive properties must specify a default value, to provide consistent semantics
        // in cases where no value has been set by annotation, client, or configuration.  Object
        // typed properties have an optional default, and 'null' in this context means that the
        // property value has not been set.
        //
        if (propDecl.getReturnType() instanceof PrimitiveType &&
            propDecl.getDefaultValue() == null)
        {
            _ap.printError( propDecl, "property.primitive.without.default", propDecl.getSimpleName() );
        }
    }

    /**
     * Returns the PropertySet associated with the Property
     */
    public AptPropertySet getPropertySet() { return _propertySet; }

    /**
     * Returns the base property name. The associated accessor methods will have the
     * form set{name} and get{name}
     */
    public String getAccessorName()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(_propertySet.getPrefix());
    
        String name = getName();
        sb.append(Character.toUpperCase(name.charAt(0)));
        if (name.length() > 0)
            sb.append(name.substring(1));
        return sb.toString();
    }

    /**
     * Returns the name of the property reading accessor method
     */
    public String getReadMethod()
    {
        StringBuffer sb = new StringBuffer();
        if (getType().equals("boolean"))
            sb.append("is");
        else
            sb.append("get");
        sb.append(getAccessorName());
        return sb.toString();
    }

    /**
     * Returns the name of the property writing accessor method
     */
    public String getWriteMethod()
    {
        return "set" + getAccessorName();
    }

    /**
     * Returns the name associated with this Property in the PropertySet
     */
    public String getName()
    {
        if ( _propDecl == null )
            return "";

        //
        // Use the member name of the property method in the property set
        //
        return _propDecl.getSimpleName();
    }

    /**
     * Returns the static final field name containing the key for this Property
     */
    public String getKeyName()
    {
        return getAccessorName() + "Key";
    }

    /**
     * Returns the type of the Property
     */
    public String getType()
    {
        if ( _propDecl == null || _propDecl.getReturnType() == null )
            return "";
        
        return _propDecl.getReturnType().toString();
    }

    /**
     * Returns true if the property is an annotation type, false otherwise
     */
    public boolean isAnnotation()
    {
        if ( _propDecl == null )
            return false;

        return _propDecl.getReturnType() instanceof AnnotationType;
    }

    /**
     * Returns any PropertyInfo associated with the property (or null if none)
     */ 
    public PropertyInfo getPropertyInfo()
    {
        if ( _propDecl == null )
            return null;
        
        return _propDecl.getAnnotation(PropertyInfo.class);
    }

    /**
     * Returns any FeatureInfo associated with the property (or null if none)
     */ 
    public FeatureInfo getFeatureInfo()
    {
        if ( _propDecl == null )
            return null;
        
        return _propDecl.getAnnotation(FeatureInfo.class);
    }

    /**
     * Returns 'true' is the property is a bound property that will support registration of
     * a PropertyChangeListener for change notifications.
     */
    public boolean isBound()
    {
        //
        // Constrained properties are implicitly bound.  Refer to section 7.4.3 of the JavaBeans
        // spec for the rationale.
        //
        PropertyInfo propInfo = getPropertyInfo();
        return propInfo != null && (propInfo.bound() || propInfo.constrained());
    }

    /**
     * Returns 'true' is the property is a constrained property that will support registration of
     * a VetoableChangeListener for vetoable change notifications.
     */
    public boolean isConstrained()
    {
        PropertyInfo propInfo = getPropertyInfo();
        return propInfo != null && propInfo.constrained();
    }

    /**
     * Returns the class name of the property editor class, or null
     */
    public String getEditorClass()
    {
        PropertyInfo pi = getPropertyInfo();
        if (pi == null)
            return null;

        //
        // This is trickier, because APT doesn't allow access to Class-valued annotations,
        // because the type may not yet have been compiled.
        //
        Collection<AnnotationMirror> annotMirrors = _propDecl.getAnnotationMirrors();
        for (AnnotationMirror am: annotMirrors)
        {
            if (am.getAnnotationType().toString().equals(
                    "org.apache.beehive.controls.api.packaging.PropertyInfo"))
            {
                Map<AnnotationTypeElementDeclaration,AnnotationValue> avs =
                    am.getElementValues();
                for (AnnotationTypeElementDeclaration ated: avs.keySet())
                {
                    if (ated.toString().equals("editorClass()"))
                    {
                        //
                        // Get the annotation value, and ignore the default value which implies
                        // no editor class (because 'null' cannot be a default value)
                        //
                        String editorClass = avs.get(ated).getValue().toString();
                        if (editorClass.equals("org.apache.beehive.controls.api.packaging.PropertyInfo.NoEditor.class"))
                            return null;

                        return editorClass;
                    }
                }
                break;
            }
        }
        return null;
    }

    AnnotationTypeElementDeclaration _propDecl;
    private AptPropertySet _propertySet;
    TwoPhaseAnnotationProcessor _ap;
}
