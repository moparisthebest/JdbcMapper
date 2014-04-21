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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.events.Client;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.api.versioning.VersionSupported;
import org.apache.beehive.controls.api.versioning.Version;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptControlImplementation class provides validation and metadata management when
 * processing a ControlImplementation class.
 */
public class AptControlImplementation extends AptType implements Generator
{
    /**
     * Constructs a new AptControlImplementation instance where information is derived
     * from APT metadata
     * @param decl the annotated declaration
     */
    public AptControlImplementation(Declaration decl, TwoPhaseAnnotationProcessor ap)
    {
        _ap = ap;
        if (! (decl instanceof ClassDeclaration))
        {
            _ap.printError( decl, "control.implementation.badclass" );
            return;
        }
        _implDecl = (ClassDeclaration)decl;
        setDeclaration(_implDecl);

        _superClass = initSuperClass();

        _contexts = initContexts();

        _controls = initControls();

        _clients = initClients();

        initEventAdaptors();

        //
        // Check serializability of the implementation class.  Any non-transient implementation
        // must implement the java.io.Serializable marker interface to indicate that the author
        // has considered serializability.
        //
        ControlImplementation implAnnot = _implDecl.getAnnotation(ControlImplementation.class);
        if (!implAnnot.isTransient())
        {
            if (!isSerializable())
            {
                _ap.printError( decl, "control.implementation.unserializable" );
            }
        }

        //
        // Construct a new initializer class from this implementation class
        //
        _init = new ImplInitializer(this);

        if ( getControlInterface() == null )
        {
            _ap.printError( decl, "control.implementation.missing.interface" );
            return;
        }

        _versionSupported = initVersionSupported();

        enforceVersionSupported();
    }

    /**
     * Initializes the super interface that this ControlImpl extends (or null if a
     * base class)
     */
    private AptControlImplementation initSuperClass()
    {
        if ( _implDecl == null || _implDecl.getSuperclass() == null )
            return null;
        
        ClassDeclaration superDecl = _implDecl.getSuperclass().getDeclaration();
        if (superDecl != null && 
            superDecl.getAnnotation(org.apache.beehive.controls.api.bean.ControlImplementation.class) != null)
        {
            return new AptControlImplementation(superDecl, _ap);
        }
        
        return null; 
    }

    /**
     * Returns the super interface for this interface
     */
    public AptControlImplementation getSuperClass() { return _superClass; }

    /**
     * Initializes the list of ContextField declared directly by this ControlImpl
     */
    private ArrayList<AptContextField> initContexts()
    {
        ArrayList<AptContextField> contexts = new ArrayList<AptContextField>();
        
        if ( _implDecl == null || _implDecl.getFields() == null )
            return contexts;

        Collection<FieldDeclaration> declaredFields = _implDecl.getFields();
        for (FieldDeclaration fieldDecl : declaredFields)
        {
            if (fieldDecl.getAnnotation(org.apache.beehive.controls.api.context.Context.class) != null)
                contexts.add(new AptContextField(this, fieldDecl, _ap));
        }
        return contexts;
    }

    /**
     * Returns the list of ContextFields declared directly by this ControlImplementation
     */
    public ArrayList<AptContextField> getContexts() { return _contexts; }

    /**
     * Returns true if the implemenation class contains any nested services
     */
    public boolean hasContexts() { return _contexts.size() != 0; }

    /**
     * Initializes the list of ControlFields for this ControlImpl
     */
    private ArrayList<AptControlField> initControls()
    {
        ArrayList<AptControlField> fields = new ArrayList<AptControlField>();

        if ( _implDecl == null || _implDecl.getFields() == null )
            return fields;

        Collection<FieldDeclaration> declaredFields = _implDecl.getFields();
        for (FieldDeclaration fieldDecl : declaredFields)
        {
            if (fieldDecl.getAnnotation(org.apache.beehive.controls.api.bean.Control.class) != null)
                fields.add(new AptControlField(this, fieldDecl, _ap));
        }
        return fields;
    }

    /**
     * Returns true if the implemenation class contains any nested controls
     */
    public boolean hasControls() { return _controls.size() != 0; }

    /**
     * Initializes the list of ClientFields declared directly by this ControlImpl
     */
    protected ArrayList<AptClientField> initClients()
    {
        ArrayList<AptClientField> clients = new ArrayList<AptClientField>();
        
        if ( _implDecl == null || _implDecl.getFields() == null )
            return clients;

        Collection<FieldDeclaration> declaredFields = _implDecl.getFields();
        for (FieldDeclaration fieldDecl : declaredFields)
        {
            if (fieldDecl.getAnnotation(Client.class) != null)
                clients.add(new AptClientField(this, fieldDecl));
        }
        return clients;
    }

    /**
     * Returns the list of ClientFields declared directly by this ControlImplementation
     */
    public ArrayList<AptClientField> getClients() { return _clients; }

    /**
     * Returns the VersionSupported annotation, if any.
     */
    public VersionSupported getVersionSupported() { return _versionSupported; }

    /**
     * Returns true if the implemenation class contains any nested event proxies
     */
    public boolean hasClients() { return _clients.size() != 0; }

    /**
     * Returns the field with the specified name
     */
    public AptField getField(String name)
    {
        for (AptField genField : _contexts)
            if (genField.getName().equals(name))
                return genField;
        for (AptField genField : _clients)
            if (genField.getName().equals(name))
                return genField;

        return null;
    }

    public AptEventField getControlField(String name)
    {
        for (AptControlField controlField : _controls)
            if (controlField.getName().equals(name))
                return controlField;
        
        return null;
    }

    /**
     * Returns the list of fully qualified class names for types that are derived
     * from this Generator
     */
    public String [] getGeneratedTypes()
    {
        return new String [] { _init.getClassName() };
    }

    /**
     * Returns the information necessary to generate a ImplInitializer from this
     * ControlImplementation.
     */
    public List<GeneratorOutput> getCheckOutput(Filer filer) throws IOException
    {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("impl", this);                                  // control implementation
        map.put("init", _init);                                  // control impl initializer

        Writer writer = new IndentingWriter(filer.createSourceFile(_init.getClassName()));
        GeneratorOutput genOut = 
            new GeneratorOutput(writer,"org/apache/beehive/controls/runtime/generator/ImplInitializer.vm",
                                map);
        ArrayList<GeneratorOutput> genList = new ArrayList<GeneratorOutput>(1);
        genList.add(genOut);
        return genList;
    }

    /**
     * Returns the list of generated files derived from this Generator during the
     * generate phase of annotation processing.
     */
    public List<GeneratorOutput> getGenerateOutput(Filer filer) throws IOException
    {
        return null;
    }

    /**
     * Returns the ControlInterface implemented by this ControlImpl.
     */
    public AptControlInterface getControlInterface()
    {
        if ( _implDecl == null || _implDecl.getSuperinterfaces() == null )
            return null;
        
        Collection<InterfaceType> superInterfaces = _implDecl.getSuperinterfaces();
        for (InterfaceType intfType : superInterfaces)
        {
            InterfaceDeclaration intfDecl = intfType.getDeclaration();
            if (intfDecl != null &&
                intfDecl.getAnnotation(org.apache.beehive.controls.api.bean.ControlInterface.class) != null)
                return new AptControlInterface(intfDecl, _ap);
        }

        return null;
    }

    /**
     * Initializes the list of EventAdaptors for this ControlImpl
     */
    protected void initEventAdaptors()
    {
        if ( _implDecl == null || _implDecl.getMethods() == null )
            return;
        
        for (MethodDeclaration implMethod : _implDecl.getMethods())
        {
            //
            // Do a quick check for the presence of the EventHandler annotation on methods
            //
            if (implMethod.getAnnotation(EventHandler.class) == null ||
                implMethod.toString().equals("<clinit>()"))
                continue;
            
            //
            // EventHandler annotations on private methods cause compilation error.
            //
            if (isPrivateMethod(implMethod))
            {
                _ap.printError(implMethod, "eventhandler.method.is.private");
                continue;
            }

            //
            // If found, we must actually read the value using an AnnotationMirror, since it
            // contains a Class element (eventSet) that cannot be loaded
            //
            AnnotationMirror handlerMirror = null;
            for (AnnotationMirror annot : implMethod.getAnnotationMirrors())
            {
                if ( annot == null ||
                    annot.getAnnotationType() == null ||
                    annot.getAnnotationType().getDeclaration() == null ||
                    annot.getAnnotationType().getDeclaration().getQualifiedName() == null )
                    return;

                if ( annot.getAnnotationType().getDeclaration().getQualifiedName().equals(
                        "org.apache.beehive.controls.api.events.EventHandler"))
                {
                    handlerMirror = annot;
                    break;
                }
            }
            if (handlerMirror == null)
            {
                throw new CodeGenerationException("Unable to find EventHandler annotation on " +
                                                  implMethod);
            }

            AptAnnotationHelper handlerAnnot = new AptAnnotationHelper(handlerMirror);

            //
            // Locate the EventField based upon the field element value
            //
            String fieldName = (String)handlerAnnot.getObjectValue("field");
            AptEventField eventField = (AptEventField)getField(fieldName);
            if (eventField == null)
            {
                // eventField == null means this field isn't interesting for the purposes
                // of this processor (control impls).  However, only emit an error message
                // if the field isn't on a nested control
                if ( getControlField(fieldName) == null )
                    _ap.printError( implMethod, "eventhandler.field.not.found", fieldName );

                continue;
            }

            //
            // Locate the EventSet based upon the eventSet element value
            //
            TypeMirror tm = (TypeMirror)( handlerAnnot.getObjectValue("eventSet") );
            if ( tm == null )
                continue;
            String setName = tm.toString();
            AptControlInterface controlIntf = eventField.getControlInterface();

            // todo: remove workaround once bug has been resolved.
            /* Workaround for JIRA issue BEEHIVE-1143, eventset name may
               contain a '$' seperator between the outer class and inner class.
               Should be a '.' seperator. Only applies to Eclipse APT. This
               workaround is also present in AptControlClient.initEventAdapters
             */
            if (tm.getClass().getName().startsWith("org.eclipse.")) {
                setName = setName.replace('$', '.');
            }
            // end of workaround

            AptEventSet eventSet = controlIntf.getEventSet(setName);
            if (eventSet == null)
            {
                _ap.printError( implMethod, "eventhandler.eventset.not.found", setName );
                continue;
            }

            //
            // Register a new EventAdaptor for the EventSet, if none exists already
            //
            EventAdaptor adaptor = eventField.getEventAdaptor(eventSet);
            if (adaptor == null)
            {
                adaptor = new EventAdaptor(eventField, eventSet);
                eventField.addEventAdaptor(eventSet, adaptor);
            }

            //
            // Locate the EventSet method based upon the eventName element value.  Once
            // found, add a new AptEventHandler to the adaptor for this event.
            //
            boolean found = false;
            String eventName = (String)handlerAnnot.getObjectValue("eventName");
            AptMethod handlerMethod = new AptMethod(implMethod, _ap);
            for (AptEvent controlEvent : eventSet.getEvents())
            {
                if (controlEvent == null || controlEvent.getName() == null || 
                    !controlEvent.getName().equals(eventName))
                    continue;
                if ( controlEvent.getArgTypes() == null )
                    continue;

                //
                // BUGBUG: If the arguments are parameterized, then the event handler
                // might declare a specific bound version of the type, so a direct
                // comparison will fail.  If parameterized, we don't validate.
                //
                if (controlEvent.hasParameterizedArguments() ||
                    controlEvent.getArgTypes().equals(handlerMethod.getArgTypes()))
                {
                    adaptor.addHandler(controlEvent, 
                                       new AptEventHandler(controlEvent, implMethod, _ap));
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                _ap.printError( implMethod, "eventhandler.method.not.found", setName );
            }
        } 
    }

    private VersionSupported initVersionSupported()
    {
        if ( _implDecl == null )
            return null;
        return _implDecl.getAnnotation(VersionSupported.class);
    }

    /**
     * Enforces the VersionRequired annotation for control extensions.
     */
    private void enforceVersionSupported()
    {
        if ( _versionSupported != null )
        {
            int majorSupported = _versionSupported.major();
            int minorSupported = _versionSupported.minor();

            if ( majorSupported < 0 )    // no real version support requirement
                return;

            AptControlInterface ci = getControlInterface();
            if ( ci == null )
                return;

            int majorPresent = -1;
            int minorPresent = -1;
            Version ciVersion = ci.getVersion();
            if ( ciVersion != null )
            {
                majorPresent = ciVersion.major();
                minorPresent = ciVersion.minor();

                if ( majorSupported >= majorPresent &&
                     (minorSupported < 0 || minorSupported >= minorPresent) )
                {
                    // Version requirement is satisfied
                    return;
                }
            }

            //
            // Version requirement failed
            //

            _ap.printError( _implDecl, "versionsupported.failed", _implDecl.getSimpleName(), majorSupported, minorSupported,
                            majorPresent, minorPresent );
        }
    }

    /**
     * Does this control impl on one of it superclasses implement java.io.Serializable?
     * @return true if this control impl or one of its superclasses implements java.io.Serializable.
     */
    protected boolean isSerializable() {

        for (InterfaceType superIntf: _implDecl.getSuperinterfaces()) {
            if (superIntf.toString().equals("java.io.Serializable")) {
                return true;
            }
        }

        // check to see if the superclass is serializable
        return _superClass != null && _superClass.isSerializable();
    }

    private ClassDeclaration                _implDecl;
    private TwoPhaseAnnotationProcessor     _ap;
    private AptControlImplementation        _superClass;
    private ArrayList<AptContextField>      _contexts;
    private ArrayList<AptClientField>       _clients;
    private ArrayList<AptControlField>      _controls;
    private ImplInitializer                 _init;
    private VersionSupported                _versionSupported;
}
