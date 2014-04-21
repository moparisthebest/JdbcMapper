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
import java.util.HashSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.io.IOException;
import java.io.Writer;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.*;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.ClassType;

import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptControlClient class contains metadata about a class that contains nested control
 * references (AptControlField).
 */
public class AptControlClient extends AptType implements Generator
{
    /**
     * Constructs a new ControlClient instance where information is derived
     * from APT metadata
     * @param decl the annotated declaration
     */
    public AptControlClient(Declaration decl, TwoPhaseAnnotationProcessor ap)
    {
        _ap = ap;
        if (! (decl instanceof ClassDeclaration))
        {
            _ap.printError( decl, "control.illegal.usage" );
            return;
        }
        _clientDecl = (ClassDeclaration)decl;
        setDeclaration(_clientDecl);

        _controls = initControls();
        initEventAdaptors();

        //
        // Construct a new initializer class from this implementation class
        //
        _init = new ClientInitializer(this);       
    }

    /**
     * Returns true if this type of client requires that nested controls have unique identifiers
     */
    protected boolean needsUniqueID()
    {
        //
        // BUGBUG:
        // Pageflows need to have a more unique ID generated for fields, because multiple pageflows
        // may be shared within a single ControlContainerContext, and just using the field name could
        // result in collisions.  A better (and less hard-wired) approach is needed than searching for
        // specific annotations.   Perhaps a model that enables particular client types to subclass
        // AptControlClient and override getID() would be much better.
        //
        for (AnnotationMirror annotMirror : _clientDecl.getAnnotationMirrors())
        {
            String annotType = annotMirror.getAnnotationType().toString();
            if (annotType.equals("org.apache.beehive.netui.pageflow.annotations.Jpf.Controller") ||
                annotType.equals("org.apache.beehive.netui.pageflow.annotations.Jpf.Backing"))
                return true;
        }
        return false;
    }

    /**
     * Returns a unique ID for a control field
     */
    public String getID(AptControlField control)
    {
        if (!needsUniqueID())
            return "\"" + control.getName() + "\""; 

        return "client.getClass() + \"@\" + client.hashCode() + \"." + control.getClassName() + "." + control.getName() + "\"";
    }

    /**
     * Returns the list of ControlFields declared directly by this ControlImpl
     */
    public ArrayList<AptControlField> getControls() { return _controls; }

    /**
     * Returns true if the implemenation class contains any nested controls
     */
    public boolean hasControls() { return _controls.size() != 0; }

    /**
     * Returns true if the control client needs field initialization support
     */
    public boolean needsFieldInit()
    {
        return hasControls();
    }

    /**
     * Returns the field with the specified name
     */
    public AptField getField(String name)
    {
        for (AptField field : _controls)
            if (field.getName().equals(name))
                return field;

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
        return null;
    }

    /**
     * Returns the information necessary to generate a ClientInitializer from this control
     */
    public List<GeneratorOutput> getGenerateOutput(Filer filer) throws IOException
    {
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("client", this);                                // control client
        map.put("init", _init);                                 // control client initializer

        Writer writer = new IndentingWriter(filer.createSourceFile(_init.getClassName()));
        GeneratorOutput genOut =
            new GeneratorOutput(writer,"org/apache/beehive/controls/runtime/generator/ClientInitializer.vm",
                                map);
        ArrayList<GeneratorOutput> genList = new ArrayList<GeneratorOutput>(1);
        genList.add(genOut);
        return genList;
    }

    /**
     * Initializes the list of ControlFields declared directly by this ControlClient
     */
    protected ArrayList<AptControlField> initControls()
    {
        ArrayList<AptControlField> controls = new ArrayList<AptControlField>();

        if ( _clientDecl == null || _clientDecl.getFields() == null )
            return controls;

        Collection<FieldDeclaration> declaredFields = _clientDecl.getFields();
        for (FieldDeclaration fieldDecl : declaredFields)
        {
            if (fieldDecl.getAnnotation(org.apache.beehive.controls.api.bean.Control.class) != null)
                controls.add(new AptControlField(this, fieldDecl, _ap));
        }
        return controls;
    }

    public boolean hasSuperClient()
    {
        return ( getSuperClientName() != null );
    }

    /**
     * Returns the fully qualified classname of the closest control client in the inheritance chain.
     * @return class name of the closest control client
     */
    public String getSuperClientName()
    {
        ClassType superType = _clientDecl.getSuperclass();

        while ( superType != null )
        {
            ClassDeclaration superDecl = superType.getDeclaration();

            Collection<FieldDeclaration> declaredFields = superDecl.getFields();
            for (FieldDeclaration fieldDecl : declaredFields)
            {
                if (fieldDecl.getAnnotation(org.apache.beehive.controls.api.bean.Control.class) != null)
                {
                    // Found an @control annotated field, so return this class name
                    return superDecl.getQualifiedName();
                }
            }

            superType = superType.getSuperclass();
        }

        return null;
    }

    /**
     * Returns the super class for this class
     */
    public AptControlClient getSuperClass() { return null; }

    /**
     * Initializes the list of EventAdaptors for this ControlImpl
     */
    protected void initEventAdaptors()
    {
        if ( _clientDecl == null || _clientDecl.getMethods() == null )
            return;
        
        for (MethodDeclaration clientMethod : _clientDecl.getMethods())
        {
            //
            // Do a quick check for the presence of the EventHandler annotation on methods
            //
            if (clientMethod.getAnnotation(EventHandler.class) == null ||
                clientMethod.toString().equals("<clinit>()"))
                continue;

            //
            // EventHandler annotations on private methods cause compilation error.
            //
            if (isPrivateMethod(clientMethod))
            {
                _ap.printError( clientMethod, "eventhandler.method.is.private");
                continue;
            }

            //
            // If found, we must actually read the value using an AnnotationMirror, since it
            // contains a Class element (eventSet) that cannot be loaded
            //
            AnnotationMirror handlerMirror = null;
            for (AnnotationMirror annot : clientMethod.getAnnotationMirrors())
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
                                                  clientMethod);
            }

            AptAnnotationHelper handlerAnnot = new AptAnnotationHelper(handlerMirror);

            //
            // Locate the EventField based upon the field element value
            //
            String fieldName = (String)handlerAnnot.getObjectValue("field");
            AptEventField eventField = (AptEventField)getField(fieldName);
            if (eventField == null)
            {
                // Deliberately not issuing a diagnostic if an event handler specifies
                // a field that isn't a control.  Other annotation processors also
                // handle event handlers, so delegate diagnostic responsibility to them.
                continue;
            }

            //
            // Locate the EventSet based upon the eventSet element value
            //
            Object tmo = handlerAnnot.getObjectValue("eventSet");
            if (!(tmo instanceof TypeMirror))
                continue;
            
            TypeMirror tm = (TypeMirror)tmo;
            String setName = tm.toString();

            AptControlInterface controlIntf = eventField.getControlInterface();
            AptEventSet eventSet = controlIntf.getEventSet(setName);

            // todo: remove workaround once bug has been resolved.
            /* Workaround JIRA issue BEEHIVE-1143, eventset name may
               contain a '$' seperator between the outer class and inner class.
               Should be a '.' seperator. Only applies to Eclipse APT. This
               workaround is also present in AptControlImplementation.initEventAdapters
             */
            if (tm.getClass().getName().startsWith("org.eclipse.")) {
                setName = setName.replace('$', '.');
            }
            // end of workaround

            if (eventSet == null)
            {
                _ap.printError( clientMethod, "eventhandler.eventset.not.found", setName );
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
            AptMethod handlerMethod = new AptMethod(clientMethod, _ap);

            //
            // Will start at the currrent event set and look up through any ones it
            // extends to try and find a matching event
            //
            while (eventSet != null)
            {
                for (AptEvent controlEvent : eventSet.getEvents())
                {
                    if (controlEvent == null || 
                        controlEvent.getName() == null || 
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
                        (controlEvent.getArgTypes().equals(handlerMethod.getArgTypes()) &&
                         controlEvent.getReturnType().equals(handlerMethod.getReturnType())
                        )
                       )
                    {
                        HashSet<String> throwSet = new HashSet<String>(controlEvent.getThrowsList());
                        ArrayList<String> handlerThrows = handlerMethod.getThrowsList();
                        boolean throwsMatches = true;
                        for ( String t : handlerThrows )
                        {
                            if ( !throwSet.contains(t) )
                                throwsMatches = false;
                        }
                    
                        if ( !throwsMatches )
                        {
                            _ap.printError( clientMethod, "eventhandler.throws.mismatch", handlerMethod.getName() );
                        }

                        adaptor.addHandler(controlEvent, 
                                       new AptEventHandler(controlEvent, clientMethod, _ap ));
                        found = true;
                        break;
                    }
                }
                if (found)  // outer loop too
                    break;

                //
                // Look up on the super event set if not found at the current level
                //
                eventSet = eventSet.getSuperEventSet();
            }
            if (!found)
            {
                _ap.printError( clientMethod, "eventhandler.method.not.found", setName );
            }
        } 
    }

    ClassDeclaration _clientDecl;
    TwoPhaseAnnotationProcessor _ap;
    ArrayList<AptControlField> _controls;
    ClientInitializer _init;
}
