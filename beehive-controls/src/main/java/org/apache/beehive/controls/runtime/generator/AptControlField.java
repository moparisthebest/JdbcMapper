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

import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.MirroredTypeException;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.versioning.VersionRequired;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptControlField class contains information about a field that refers to a nested control.
 */
public class AptControlField extends AptEventField
{
    /**
     * Base constructor, protected so only a custom subclass can invoke
     * @param controlClient the declaring AptType
     */
    public AptControlField(AptType controlClient, FieldDeclaration controlDecl,
                           TwoPhaseAnnotationProcessor ap)
    {
        super( controlDecl );
        _controlClient = controlClient;
        _ap = ap;
        _controlBean = new ControlBean(getControlInterface());
    }

    /**
     * Does this control field have a VersionRequired annotation?
     * @return <code>true</code> if there is a version required annotation; <code>false</code> otherwise
     */
    public boolean hasVersionRequired()
    {
        return ( _fieldDecl.getAnnotation( VersionRequired.class ) != null );
    }

    /**
     * Initializes the ControlInterface associated with this ControlField
     */
    protected AptControlInterface initControlInterface()
    {
        TypeMirror controlType = _fieldDecl.getType();
        if (! (controlType instanceof DeclaredType))
        {
            _ap.printError( _fieldDecl, "control.field.bad.type" );
            return null;
        }

        //
        // The field can either be declared as the bean type or the public interface type.
        // If it is the bean type, then we need to reflect to find the public interface
        // type it implements.
        //
        TypeDeclaration typeDecl = ((DeclaredType)controlType).getDeclaration();
        InterfaceDeclaration controlIntf = null;

        //
        // It is possible that the declared type is associated with a to-be-generated
        // bean type.  In this case, look for the associated control interface on the
        // processor input list.
        //
        if ( typeDecl == null )
        {
            String className = controlType.toString();
            String intfName = className.substring(0, className.length() - 4);
            String interfaceHint = getControlInterfaceHint();
            controlIntf = (InterfaceDeclaration)_ap.getAnnotationProcessorEnvironment().getTypeDeclaration(intfName);

            if (controlIntf == null)
            {
                // The specified class name may not be fully qualified.  In this case, the
                // best we can do is look for a best fit match against the input types
                for (TypeDeclaration td :_ap.getAnnotationProcessorEnvironment().getSpecifiedTypeDeclarations())
                {
                    // if an interface hint was provided, use it to find the control interface,
                    // if not provided try to find the control interface by matching simple names.
                    if (interfaceHint != null) {
                        if (td instanceof InterfaceDeclaration &&
                                td.getQualifiedName().equals(interfaceHint))
                        {
                            controlIntf = (InterfaceDeclaration)td;
                            break;
                        }
                    }
                    else {
                        if (td instanceof InterfaceDeclaration &&
                            td.getSimpleName().equals(intfName))
                        {
                            controlIntf = (InterfaceDeclaration)td;
                            break;
                        }
                    }
                }
            }
        }
        else if (typeDecl instanceof ClassDeclaration)
        {
            Collection<InterfaceType> implIntfs = ((ClassDeclaration)typeDecl).getSuperinterfaces();
            for (InterfaceType intfType : implIntfs)
            {
                InterfaceDeclaration intfDecl = intfType.getDeclaration();

                if ( intfDecl == null )
                    return null;
                
                if (intfDecl.getAnnotation(ControlInterface.class) != null||
                    intfDecl.getAnnotation(ControlExtension.class) != null)
                {
                    controlIntf = intfDecl;
                    break;
                }
            }
        }
        else if (typeDecl instanceof InterfaceDeclaration)
        {
            controlIntf = (InterfaceDeclaration)typeDecl;
        }

        if (controlIntf == null)
        {
            _ap.printError( _fieldDecl, "control.field.bad.type.2" );
            return null;
        }

        return new AptControlInterface(controlIntf, _ap);
    }

    /**
     * Get the interface hint attribute value (as a string) from the Control annotation,
     * if it wasn't specified return null.
     */
    private String getControlInterfaceHint() {

        Control controlAnnotation = _fieldDecl.getAnnotation(Control.class);
        String interfaceHint = null;
        try {
            // always excepts
            controlAnnotation.interfaceHint();
        } catch (MirroredTypeException mte) {
            interfaceHint = ("java.lang.Object".equals(mte.getQualifiedName())) ? null : mte.getQualifiedName();
        }
        return interfaceHint;
    }

    /**
     * Returns the ControlBean associated with this ControlField
     */
    public ControlBean getControlBean() { return _controlBean; }

    private TwoPhaseAnnotationProcessor _ap;
    private AptType _controlClient;
    private ControlBean _controlBean;
}
