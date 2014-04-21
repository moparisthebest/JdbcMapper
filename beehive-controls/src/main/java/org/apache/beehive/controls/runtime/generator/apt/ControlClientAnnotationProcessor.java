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
package org.apache.beehive.controls.runtime.generator.apt;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;
import java.io.File;
import java.io.IOException;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.ClassType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.DeclaredType;

import org.apache.beehive.controls.runtime.bean.ControlUtils;
import org.apache.beehive.controls.runtime.generator.CodeGenerationException;
import org.apache.beehive.controls.runtime.generator.AptAnnotationHelper;
import org.apache.beehive.controls.runtime.generator.AptControlClient;
import org.apache.beehive.controls.runtime.generator.GeneratorOutput;
import org.apache.beehive.controls.runtime.generator.Generator;
import org.apache.beehive.controls.runtime.generator.VelocityGenerator;
import org.apache.beehive.controls.runtime.generator.CodeGenerator;
import org.apache.beehive.controls.api.versioning.Version;
import org.apache.beehive.controls.api.versioning.VersionRequired;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.ControlExtension;

public class ControlClientAnnotationProcessor
    extends TwoPhaseAnnotationProcessor {

    public ControlClientAnnotationProcessor(Set<AnnotationTypeDeclaration> atds, AnnotationProcessorEnvironment env ) {
        super( atds,env );
    }

    @Override
    public void check( Declaration d )
    {
        if ( d instanceof FieldDeclaration )
            checkControlField( (FieldDeclaration)d );

        // if @Control is used on something other than a field, the Java lang
        // checker should produce an error due to the @Target violation.

        if ( d instanceof TypeDeclaration )
            checkControlClientType( (TypeDeclaration)d );

        // When a control is instantiated declaratively, values may be assigned to 
        // the control's properties declaratively as well.  The property constraint
        // validator is called here to ensure all values assigned satisfy any 
        // constraints declared in the properties.
        try
        {
            AnnotationConstraintAptValidator.validate(d);
        }
        catch (IllegalArgumentException iae)
        {
            printError(d, "propertyset.illegal.argument.error", iae.getMessage());
        }
    }

    private static void addControlType(Map<TypeDeclaration,Set<TypeMirror>> clientsMap, TypeDeclaration clientType,
                                       TypeMirror controlFieldType)
    {
        Set<TypeMirror> controlTypes = clientsMap.get( clientType );

        if ( controlTypes == null )
        {
            controlTypes = new HashSet<TypeMirror>();
            clientsMap.put( clientType, controlTypes );
        }

        controlTypes.add( controlFieldType );
    }

    /**
     * Each control client requires a manifest that documents the controls that it references.
     *
     * @throws CodeGenerationException
     */
    @Override
    public void generate() throws CodeGenerationException
    {
        super.generate();

        /*
        The annotation processor may be passed multiple control client types.  Build a map that
        links each control client type with the set of control types that it uses.
        */

        Map<TypeDeclaration,Set<TypeMirror>> clientsMap = new HashMap<TypeDeclaration,Set<TypeMirror>>();

        for (AnnotationTypeDeclaration atd : _atds)
        {
            if (atd.getSimpleName().equals("Control") )
            {
                AnnotationProcessorEnvironment env = getAnnotationProcessorEnvironment();
                Collection<Declaration> decls = env.getDeclarationsAnnotatedWith(atd);
                for (Declaration decl : decls)
                {
                    if ( decl instanceof FieldDeclaration )
                    {
                        FieldDeclaration fd = (FieldDeclaration)decl;
                        TypeDeclaration clientType = fd.getDeclaringType();
                        TypeMirror controlFieldType = fd.getType();
                        addControlType( clientsMap, clientType, controlFieldType );

                        /*
                        Add the control type to any derived class.  Fields with
                        private and default (package) access are also included
                        here as the controls in superclasses may be exposed
                        through public or protected methods to subclasses.
                        */
                        Collection<TypeDeclaration> specifiedTypeDeclartions = env.getSpecifiedTypeDeclarations();
                        for (TypeDeclaration td : specifiedTypeDeclartions)
                        {
                            if (td instanceof ClassDeclaration)
                            {
                                ClassType superclass = ((ClassDeclaration) td).getSuperclass();
                                while (superclass != null)
                                {
                                    if (superclass.getDeclaration().equals(clientType))
                                    {
                                        addControlType(clientsMap, td, controlFieldType);
                                        break;
                                    }

                                    superclass = superclass.getSuperclass();
                                }
                            }
                        }
                    }
                }
            }
            else if (atd.getSimpleName().equals("ControlReferences"))
            {
                Collection<Declaration> decls = getAnnotationProcessorEnvironment().getDeclarationsAnnotatedWith(atd);
                for (Declaration decl : decls)
                {
                    if ( decl instanceof TypeDeclaration )
                    {
                        TypeDeclaration clientType = (TypeDeclaration)decl;
                        Set<TypeMirror> controlTypes = clientsMap.get( clientType );
                        if ( controlTypes == null )
                        {
                            controlTypes = new HashSet<TypeMirror>();
                            clientsMap.put( clientType, controlTypes );
                        }

                        // Read ControlReferences annotation
                        AnnotationMirror controlMirror = null;
                        for (AnnotationMirror annot : clientType.getAnnotationMirrors())
                        {
                            if (annot.getAnnotationType().getDeclaration().getQualifiedName().equals(
                                    "org.apache.beehive.controls.api.bean.ControlReferences"))
                            {
                                controlMirror = annot;
                                break;
                            }
                        }

                        assert( controlMirror != null );

                        // Add each control type listed in the ControlReferences annotation
                        AptAnnotationHelper controlAnnot = new AptAnnotationHelper(controlMirror);
                        Collection<AnnotationValue> references = (Collection<AnnotationValue>)controlAnnot.getObjectValue("value");
                        if ( references != null )
                        {
                            for ( AnnotationValue av : references )
                            {
                                TypeMirror crType = (TypeMirror)av.getValue();
                                controlTypes.add( crType );
                            }
                        }
                    }
                }
            }
        }

        // For each client type:
        //   1 - emit a controls client manifest in the same dir as the client type's class.
        //   2 - emit a controls client initializer class in the same pkg/dir as the client type's class

        Filer f = getAnnotationProcessorEnvironment().getFiler();
        Set<TypeDeclaration> clientTypes = clientsMap.keySet();
        for ( TypeDeclaration clientType : clientTypes )
        {
            // Emit manifest

            String clientPkg = clientType.getPackage().getQualifiedName();
            File clientManifestName =
                new File( clientType.getSimpleName() + ControlClientManifest.FILE_EXTENSION );

            ControlClientManifest mf = new ControlClientManifest( clientType.getQualifiedName() );

            try
            {
                Set<TypeMirror> controlTypes = clientsMap.get( clientType );
                for ( TypeMirror controlType : controlTypes )
                {
                    InterfaceDeclaration controlIntfOrExt = getControlInterfaceOrExtension(controlType);
                    InterfaceDeclaration controlIntf = getMostDerivedControlInterface( controlIntfOrExt );

                    assert controlIntf != null : "Can't find most derived control intf for=" + controlIntfOrExt;

                    ControlInterface annot = controlIntf.getAnnotation(ControlInterface.class);
                    String defBinding = annot.defaultBinding();

                    defBinding = ControlUtils.resolveDefaultBinding( defBinding, controlIntf.getQualifiedName() );

                    mf.addControlType( controlIntfOrExt.getQualifiedName(), defBinding );
                }

                mf.emit( f, clientPkg, clientManifestName, null );
            }
            catch ( IOException ie )
            {
                printError( clientType, "controls.client.manifest.ioerror" );
                ie.printStackTrace( );
            }

            // Emit initializer

            AnnotationProcessorEnvironment env = getAnnotationProcessorEnvironment();
            Generator genClass = new AptControlClient( clientType, this );

            if ( genClass != null )
            {
                try
                {
                    List<GeneratorOutput> genList = genClass.getGenerateOutput(env.getFiler());
                    if (genList == null || genList.size() == 0)
                        return;

                    for (GeneratorOutput genOut : genList)
                    {
                        getGenerator().generate(genOut);
                    }
                }
                catch (IOException ioe)
                {
                    throw new CodeGenerationException("Code generation failure: ", ioe);
                }
            }
        }
    }

    @Override
    public void generate(Declaration decl)
    {
    }

    private void checkControlField( FieldDeclaration f )
    {
        TypeMirror fieldType = f.getType();

        // Make sure that this field doesn't try to override another that's inherited.
        String fieldName = f.getSimpleName();
        TypeDeclaration declaringType = f.getDeclaringType();

        if ( declaringType instanceof ClassDeclaration )
        {
            for ( ClassType i = ( ( ClassDeclaration ) declaringType ).getSuperclass(); i != null; i = i.getSuperclass() )
            {
                ClassDeclaration decl = i.getDeclaration();

                if ( decl != null )
                {
                    for ( FieldDeclaration baseClassField : decl.getFields() )
                    {
                        if ( fieldName.equals( baseClassField.getSimpleName() ) )
                        {
                            Collection<Modifier> modifiers = baseClassField.getModifiers();

                            if ( modifiers.contains( Modifier.PROTECTED ) || modifiers.contains( Modifier.PUBLIC ) )
                            {
                                printError( f, "control.field.override", decl.getQualifiedName() );
                            }
                        }
                    }
                }
            }
        }

        // Valid control field instances can be of an interface type
        // or a class type.
        if ( fieldType instanceof InterfaceType )
        {
            // Valid interface type decls must be annotated w/ @ControlInterface
            // or @ControlExtension.
            Declaration fieldTypeDecl = ((InterfaceType)fieldType).getDeclaration();
            if ( fieldTypeDecl.getAnnotation(ControlInterface.class) == null &&
                 fieldTypeDecl.getAnnotation(ControlExtension.class) == null )
                printError( f, "control.field.bad.interfacetype" );
        }
        else if ( fieldType instanceof ClassType )
        {
            // Valid class type decls must implements the ControlBean API.

            // Walk the implementation inheritance hierarchy, seeing if one of the
            // classes implements ControlBean.  
            //
            // REVIEW: Does NOT check if the interfaces might implement ControlBean!
            // This is unnecessary for our impl, since our generated bean class directly
            // implements ControlBean, but other impls may choose to do otherwise.
            boolean foundControlBean = false;
            ClassType classType = (ClassType)fieldType;

            if (classType.getDeclaration() != null)
            {
                outer: while ( classType != null )
                {
                    Collection<InterfaceType> intfs = classType.getSuperinterfaces();
                    for ( InterfaceType intfType : intfs )
                    {
                        if ( intfType.getDeclaration().getQualifiedName().equals( "org.apache.beehive.controls.api.bean.ControlBean" ) )
                        {
                            foundControlBean = true;
                            break outer;
                        }
                    }
                    classType = classType.getSuperclass();
                }
                if ( !foundControlBean )
                    printError( f, "control.field.bad.classtype" );

                // Valid generated beans should only "implement" the control interface/extension, and no others
                classType = (ClassType)fieldType;
                Collection<InterfaceType> intfs = classType.getSuperinterfaces();
                if ( intfs.size() != 1 )
                {
                    printError( f, "control.field.bad.classtype.badinterface" );
                }

                for ( InterfaceType intfType : intfs )
                {
                    if ( intfType.getDeclaration().getAnnotation(ControlExtension.class) == null &&
                         intfType.getDeclaration().getAnnotation(ControlInterface.class) == null)
                    {
                        printError( f, "control.field.bad.classtype.badinterface");
                    }
                }
            }
            else
            {
                // TODO: This could be a ControlBean type that is going to be generated by
                // the current APT processing iteration.  It should be possible to do more
                // specific verification here using the getTypeDeclaration API on
                // AnnotationProcessorEnvironment.  In any event, the implementation of
                // getControlInterface will properly handle this case, and if it cannot a
                // malformed type error will be generated.
            }
         }
         else
         {
             printError( f, "control.field.bad.type" );
         }

         // Enforce any versioning requirements this control field has.
         //
         // Since our generate() does some detailed grovelling of control types, make sure that
         // will not result in an error by doing that grovelling now.  Control types may be
         // malformed if the source for those types has errors (yet the apt type may still exist!).
         try
         {
             InterfaceDeclaration controlIntfOrExt = getControlInterfaceOrExtension(fieldType);
             InterfaceDeclaration controlIntf = getMostDerivedControlInterface( controlIntfOrExt );

             if ( controlIntf != null )
             {
                 enforceVersionRequired( f, controlIntf );
             }
             else
             {
                 printError( f, "control.field.type.malformed" );
             }
         }
         catch ( CodeGenerationException cge )
         {
             printError( f, "control.field.type.malformed" );
         }

         assert declaringType != null : "Field " + f + " has no declaring type!";

         if ( declaringType.getDeclaringType() != null )
             printError( f, "control.field.in.inner.class" );

        Collection<Modifier> mods = f.getModifiers();

         if ( mods.contains( Modifier.TRANSIENT ))
             printError( f, "transient.control.field" );

         if ( mods.contains( Modifier.STATIC ))
             printError( f, "static.control.field" );

    }

    private void checkControlClientType( TypeDeclaration t )
    {
        // validate @ControlReferences
        AnnotationMirror controlMirror = null;

        for (AnnotationMirror annot : t.getAnnotationMirrors())
        {
            if (annot.getAnnotationType().getDeclaration().getQualifiedName().equals(
                    "org.apache.beehive.controls.api.bean.ControlReferences"))
            {
                controlMirror = annot;
                break;
            }
        }

        // Bail out if no @ControlReferences annotation found
        if ( controlMirror == null )
            return;

        AptAnnotationHelper controlAnnot = new AptAnnotationHelper(controlMirror);

        //
        // Validate that the types listed in the ControlReferences annotations are actually
        // control types.
        //

        Collection<AnnotationValue> references = (Collection<AnnotationValue>)controlAnnot.getObjectValue("value");

        if ( references != null )
        {
            for ( AnnotationValue av : references )
            {
                DeclaredType crType = (DeclaredType)av.getValue();
                if ( crType instanceof InterfaceType )
                {
                    // Valid interface type decls must be annotated w/ @ControlInterface
                    // or @ControlExtension.
                    Declaration typeDecl = crType.getDeclaration();
                    if ( typeDecl.getAnnotation(ControlInterface.class) == null &&
                         typeDecl.getAnnotation(ControlExtension.class) == null )
                         printError( t, "control.reference.bad.interfacetype" );
                }
                else {
                    printError( t, "control.reference.bad.interfacetype" );
                }
            }
        }
    }

    /**
     * Given a InterfaceType or ClassType, returns the InterfaceType for the control type's
     * public interface/extension.
     * @param intfOrBeanClass
     * @return The InterfaceType for the control type's public interface/extension.
     */
    private InterfaceDeclaration getControlInterfaceOrExtension( TypeMirror intfOrBeanClass )
    {
        if (intfOrBeanClass instanceof InterfaceType)
        {
            return ((InterfaceType)intfOrBeanClass).getDeclaration();
        }
        else if (intfOrBeanClass instanceof ClassType)
        {
            ClassType classType = (ClassType)intfOrBeanClass;

            // If the bean type declaration cannot be found, then the only (valid) possibility
            // is that it is a generated type from the current processor pass.   See if a base
            // interface type can be determined from the current processor input list.
            if (classType.getDeclaration() == null)
            {
                //
                // Compute the bean type name, and the associated interface name by stripping
                // the "Bean" suffix
                //
                String className = classType.toString();
                AnnotationProcessorEnvironment ape = getAnnotationProcessorEnvironment();
                InterfaceDeclaration id = null;
                String intfName = null;
                if (className.length() > 4) {
                    intfName = className.substring(0, className.length() - 4);
                    id = (InterfaceDeclaration)ape.getTypeDeclaration(intfName);
                }

                if (id == null && intfName != null)
                {
                    // The specified class name may not be fully qualified.  In this case, the
                    // best we can do is look for a best fit match against the input types
                    for (TypeDeclaration td :ape.getSpecifiedTypeDeclarations())
                    {
                        if (td instanceof InterfaceDeclaration &&
                            td.getSimpleName().equals(intfName))
                        {
                            return (InterfaceDeclaration)td;
                        }
                    }
                }
                return id;
            }
            else
            {
                // direct supers only
                Collection<InterfaceType> intfs = classType.getSuperinterfaces();

                // per the code in checkControlField, this set must be of size 1
                // and the 1 super interface must be a control interface/extension
                // a value of zero may be valid if the control field is not referencing
                // a control -- for this case fall through and return null.
                assert ( intfs.size() <= 1 );
                for ( InterfaceType intfType : intfs )
                    return intfType.getDeclaration();
            }
        }
        else
        {
            throw new CodeGenerationException( "Param not a interface or class type");
        }

        return null;
    }

    /**
     * Given a control interface or extension, do a BFS of its inheritance heirarchy for
     * the first one marked with @ControlInterface.  This represents the point in the
     * heirarchy where use of @ControlExtension changes to use of @ControlInterface.
     *
     * @param controlIntfOrExt an interface annotated with @ControlInterface or @ControlExtension.
     * @return most derived interface in the heirarchy annotated with @ControlInterface, null
     *         if no such interface found.
     */
    private InterfaceDeclaration getMostDerivedControlInterface( InterfaceDeclaration controlIntfOrExt )
    {
        Queue<InterfaceDeclaration> q = new LinkedList<InterfaceDeclaration>();

        InterfaceDeclaration id = controlIntfOrExt;
        while ( id != null )
        {
            if ( id.getAnnotation(ControlInterface.class) != null )
                break;

            Collection<InterfaceType> supers = id.getSuperinterfaces();
            for ( InterfaceType s : supers )
                q.offer( s.getDeclaration() );

            id = q.poll();
        }

        return id;
    }

    /**
     * Enforces the VersionRequired annotation for control fields.
     */
    private void enforceVersionRequired( FieldDeclaration f, InterfaceDeclaration controlIntf )
    {
        VersionRequired versionRequired = f.getAnnotation(VersionRequired.class);
        Version versionPresent = controlIntf.getAnnotation(Version.class);

        if (versionRequired != null) {
            int majorRequired = -1;
            try {
                majorRequired = versionRequired.major();
            }
            catch(NullPointerException ignore) {
                /*
                the major version annotation is required and if unspecified, will
                throw an NPE when it is quereid but not provided.  this error will
                be caught during syntactic validation perfoemed by javac, so ignore
                it if an NPE is caught here
                 */
                return;
            }

            int minorRequired = versionRequired.minor();

            /* no version requirement, so return */
            if(majorRequired < 0)
                return;

            int majorPresent = -1;
            int minorPresent = -1;
            if ( versionPresent != null )
            {
                try {
                    majorPresent = versionPresent.major();
                }
                catch(NullPointerException ignore) {
                    /*
                    the major version annotation is required and if unspecified, will
                    throw an NPE when it is quereid but not provided.  this error will
                    be caught during syntactic validation perfoemed by javac, so ignore
                    it if an NPE is caught here
                     */
                }

                minorPresent = versionPresent.minor();

                if ( majorRequired <= majorPresent &&
                     (minorRequired < 0 || minorRequired <= minorPresent) )
                {
                    // Version requirement is satisfied
                    return;
                }
            }

            //
            // Version requirement failed
            //
            printError( f, "control.field.bad.version", f.getSimpleName(), majorRequired, minorRequired,
                        majorPresent, minorPresent  );
        }
    }

    /**
     * Returns the CodeGenerator instance supporting this processor, instantiating a new
     * generator instance if necessary.
     */
    protected CodeGenerator getGenerator() {

        if (_generator == null) {
            /* Locate the class that wraps the Velocity code generation process */
            AnnotationProcessorEnvironment env = getAnnotationProcessorEnvironment();

            try {
                _generator = new VelocityGenerator(env);
            }
            catch (Exception e) {
                throw new CodeGenerationException("Unable to create code generator", e);
            }
        }

        return _generator;
    }

    CodeGenerator _generator;
}
