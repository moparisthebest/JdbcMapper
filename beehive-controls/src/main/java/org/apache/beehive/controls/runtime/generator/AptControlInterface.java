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

import java.io.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.net.*;
import java.util.*;

import com.sun.mirror.apt.Filer;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.MirroredTypesException;

import org.apache.beehive.controls.api.bean.ControlChecker;
import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.ExternalPropertySets;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.packaging.FeatureInfo;
import org.apache.beehive.controls.api.packaging.ManifestAttribute;
import org.apache.beehive.controls.api.packaging.ManifestAttributes;
import org.apache.beehive.controls.api.properties.PropertySet;
import org.apache.beehive.controls.api.versioning.Version;
import org.apache.beehive.controls.api.versioning.VersionRequired;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;
import org.apache.beehive.controls.runtime.generator.apt.CheckerAnnotationProcessorEnvironmentImpl;

/**
 * The AptControlInterface provides validation and metadata management for a ControlInterface
 * or ControlExtension class during APT processing.  It is also used to model the interface
 * to contextual services, since they parallel the conventions of control interfaces.
 */
public class AptControlInterface extends AptType implements Generator
{
    /**
     * Constructs a new AptControlInterface instance where interface information is derived
     * from an APT interface declaration
     * @param decl the annotated Declaration
     * @param ap the top-level annotation processor
     */
    public AptControlInterface(Declaration decl, TwoPhaseAnnotationProcessor ap)
    {
        _ap = ap;

        //
        // Verify that the @ControlInterface/@ControlExtension annotations are only used on an
        // interface.
        // Note: AptControlInterface is also used to construct the operation and event model
        // for contextual services (see AptContextField).  Becaue contextual sevices can actually
        // be classes as well as interfaces, the test below has to be specific to the annotated
        // use cases
        //
        if (! (decl instanceof InterfaceDeclaration) &&
              (decl.getAnnotation(ControlExtension.class) != null ||
               decl.getAnnotation(ControlInterface.class) != null))
        {
            _ap.printError(decl, "control.interface.annotation.badlocation" );
            return;
        }

        _intfDecl = (InterfaceDeclaration)decl;
        setDeclaration(_intfDecl);

        _isExtension = initIsExtension();

        _superClass = initSuperClass();

        _operations = initOperations();

        _intfProps = initIntfProperties();

        _propertySets = initPropertySets();

        _eventSets = initEventSets();

        _featureInfo = initFeatureInfo();

        _version = initVersion();
        _versionRequired = initVersionRequired();

        //
        // Construct a bean instance for this interface
        //
        _bean = new ControlBean(this);

        //
        // Enforce VersionRequired semantics
        //
        enforceVersionRequired();

        //
        // Do work specific to control extensions
        //

        if (isExtension())
        {
            //
            // If this is an control extension, run the control-author-specified
            // checker class to perform additional validation.
            //
            check();
        }
    }

    /**
     * Returns the parent control interface or extension type from which the control
     * interface is derived (or null, if it is at the root of the interface hierarchy)
     */
    public InterfaceType getSuperType()
    {
        if ( _intfDecl.getSuperinterfaces() == null )
            return null;

        for (InterfaceType intfType : _intfDecl.getSuperinterfaces())
        {
            InterfaceDeclaration superDecl = intfType.getDeclaration();
            if ( superDecl != null )
            {
                if (superDecl.getAnnotation(ControlExtension.class) != null ||
                    superDecl.getAnnotation(ControlInterface.class) != null)
                {
                    _superDecl = superDecl;
                    return intfType;
                }
            }
        }

        return null;
    }

    /**
     * Initializes the super interface that this ControlInterface extends (or sets it to null
     * if a base interface)
     */
    private AptControlInterface initSuperClass()
    {
        //
        // Look for a super interface that is either a control interface or extension.
        // If found, return it.
        //
        InterfaceType superType = getSuperType();
        if (superType == null)
        {
            // At this point, we're processing the root of the interface heirarchy,
            // which is not permitted to be a ControlExtension (that would imply a
            // ControlExtension that wasn't actually extending a ControlInterface).
            if ( isExtension() )
            {
                _ap.printError( _intfDecl, "control.extension.badinterface");
            }

            return null;
        }

        InterfaceDeclaration superDecl = superType.getDeclaration();
        if ( superDecl != null )
        {
            if (superDecl.getAnnotation(ControlExtension.class) != null ||
                superDecl.getAnnotation(ControlInterface.class) != null)
            {
                _superDecl = superDecl;
                AptControlInterface superIntf = new AptControlInterface(_superDecl, _ap);

                if (!isExtension() && superIntf.isExtension())
                {
                    _ap.printError( _intfDecl, "control.interface.badinterface");
                }
                return superIntf;
            }
        }

        return null;
    }

    /**
     * Returns the super interface for this interface
     */
    public AptControlInterface getSuperClass() { return _superClass; }

    /**
     * Initializes the list of operations declared by this AptControlInterface
     */
    private AptMethodSet<AptOperation> initOperations()
    {
        AptMethodSet<AptOperation> operList = new AptMethodSet<AptOperation>();

        if ( _intfDecl == null )
            return operList;

        //
        // Add the methods from the current interface and all super interfaces *other*
        // than the one from which control inheritance or extension is defined.  These
        // exceptions are handled on the super ControlInterface (the return value
        // of AptControlInterface.initSuperClass())
        //
        // Do this by:
        //  - initially populate the check vector with the control interface
        //  - iterate through the check vector, examining each interface to:
        //      * ignore the super interface
        //      * add all declared interface methods to the operations list
        //      * add any super interfaces to the Vector (avoiding recursion)
        //  - the iteration continues until all superinterfaces have been processed
        //
        Vector<InterfaceDeclaration> checkIntfs = new Vector<InterfaceDeclaration>();
        checkIntfs.add(_intfDecl);

        for (int i = 0; i < checkIntfs.size(); i++)
        {
            InterfaceDeclaration intfDecl = checkIntfs.elementAt(i);
            if (intfDecl.equals(_superDecl))
                continue;

            if ( intfDecl.getMethods() == null )
                continue;

            // Add all declared methods, but ignore the mystery <clinit> methods
            for (MethodDeclaration methodDecl : intfDecl.getMethods())
                if (!methodDecl.toString().equals("<clinit>()"))
                    operList.add(new AptOperation(this, methodDecl, _ap));

            if ( intfDecl.getSuperinterfaces() == null )
                continue;

            for (InterfaceType superType : intfDecl.getSuperinterfaces())
            {
                InterfaceDeclaration superDecl = superType.getDeclaration();
                if (superDecl != null && !checkIntfs.contains(superDecl))
                    checkIntfs.add(superDecl);
            }
        }

        return operList;
    }

    /**
     * Returns the list of ControlOperations declared directly by this AptControlInterface
     */
    public Collection<AptOperation> getOperations() { return _operations.getMethods(); }

    /**
     * Returns the total number of operations for this control interface
     */
    public int getOperationCount()
    {
        int count = _operations.size();
        if (_superClass != null)
            count += _superClass.getOperationCount();

        return count;
    }

    /**
     * Initializes the list of PropertySets declared or referenced by this AptControlInterface
     */
    private ArrayList<AptPropertySet> initPropertySets()
    {
        ArrayList<AptPropertySet> propSets = new ArrayList<AptPropertySet>();

        if ( _intfDecl == null )
            return propSets;

        // TODO: enforce presence of prefixes when multiple property sets w/ the same
        // property name exist

        //
        // Add the intrinsic/base property set
        //

        TypeDeclaration basePropsDecl =
            _ap.getAnnotationProcessorEnvironment().getTypeDeclaration( "org.apache.beehive.controls.api.properties.BaseProperties" );
        if ( basePropsDecl != null )
        {
            propSets.add( new AptPropertySet( null, basePropsDecl, _ap ) );
        }

        //
        // Add external property sets
        //
        ExternalPropertySets extPropsAnnotation = _intfDecl.getAnnotation(ExternalPropertySets.class);
        if ( extPropsAnnotation != null )
        {
            if (isExtension())
            {
                _ap.printError( _intfDecl, "extpropertyset.illegal.usage" );
            }

            try
            {
                Class[] extProps = extPropsAnnotation.value();
            }
            catch ( MirroredTypesException mte )
            {
                Collection<String> extProps = mte.getQualifiedNames();
                for ( String extPropName : extProps )
                {
                    TypeDeclaration extPropDecl = _ap.getAnnotationProcessorEnvironment().getTypeDeclaration( extPropName );
                    if ( extPropDecl != null )
                    {
                        AptPropertySet extPropSet = new AptPropertySet( null, extPropDecl, _ap );
                        propSets.add( extPropSet );
                    }
                    else
                    {
                        _ap.printError( _intfDecl, "extpropertyset.type.not.found", extPropName );
                    }
                }
            }
        }

        //
        // Add nested property sets
        //

        if ( _intfDecl.getNestedTypes() == null )
            return propSets;

        for (TypeDeclaration innerDecl : _intfDecl.getNestedTypes())
        {
            boolean fError = false;
            if (innerDecl.getAnnotation(PropertySet.class) != null)
            {
                if (! (innerDecl instanceof AnnotationTypeDeclaration))
                {
                    _ap.printError( innerDecl, "propertyset.not.annotation.type" );
                    fError = true;
                }

                Retention ret = innerDecl.getAnnotation(Retention.class);
                if (ret == null || ret.value() != RetentionPolicy.RUNTIME)
                {
                    _ap.printError( innerDecl, "propertyset.missing.retention" );
                    fError = true;
                }

                if (isExtension())
                {
                    _ap.printError( innerDecl, "propertyset.illegal.usage.2" );
                    fError = true;
                }

                if ( !fError )
                    propSets.add(
                        new AptPropertySet(this, (AnnotationTypeDeclaration)innerDecl, _ap));
            }
        }

        //
        // Detect the presence of locally declared bound or constrained properties
        // Enforce property name (including prefix) uniqueness across all propertysets on this interface.
        //

        Set<String> propertyNames = new HashSet<String>();

        for (AptPropertySet propSet : propSets)
        {
            for (AptProperty prop : propSet.getProperties())
            {
                if (prop.isBound())
                    _hasBoundProperties = true;

                if (prop.isConstrained())
                    _hasConstrainedProperties = true;

                String propName = prop.getAccessorName();

                if ( propertyNames.contains( propName ) )
                {
                    _ap.printError( _intfDecl, "propertyset.duplicate.property.names", propName, propSet.getShortName() );
                }
                else
                {
                    propertyNames.add( propName );
                }
            }
        }

        return propSets;
    }

    /**
     * Returns the list of PropertySets declared directly by this AptControlInterface
     */
    public Collection<AptPropertySet> getPropertySets() { return _propertySets; }

    /**
     * Returns the total number of properties for this control interface
     */
    public int getPropertyCount()
    {
        int count;
        if (_superClass == null)
            count = 0;
        else
            count = _superClass.getPropertyCount();

        for (AptPropertySet propertySet : _propertySets) {
            // if a property set is set to optional and hasSetters is set to false,
            // there isn't a getter or setter available for that property
            if (propertySet.hasSetters() || !propertySet.isOptional()) {
                count += propertySet.getProperties().size();
            }
        }

        count += _intfProps.size();
        return count;
    }

    /**
     * Returns the list of properties defined by getter and setter methods in this control interface.
     */
    public Collection<AptControlInterfaceProperty> getInterfaceProperties() { return _intfProps; }

    /**
     * Returns true if the interface has any bound properties associated with it.
     */
    public boolean hasBoundProperties()
    {
        if (_superClass != null && _superClass.hasBoundProperties())
            return true;

        return _hasBoundProperties;
    }

    /**
     * Returns true if this interface is the first interface in the inheritance hierarchy
     * to declare support for bound properties.  This is used to declared PropertyChangeListener
     * registration methods for the bean once (and only once).
     */
    public boolean addsBoundPropertySupport()
    {
        //
        // If a super interface has already added support, then not added here
        //
        if (_superClass != null && _superClass.addsBoundPropertySupport())
            return false;

        return hasBoundProperties();
    }

    /**
     * Returns true if any properties declared directly by this control interface are constrained
     * properties.  This <b>will not</b> reflect the attributes of properties declared on
     * an interface from which this interface derives.
     */
    public boolean hasConstrainedProperties()
    {
        if (_superClass != null && _superClass.hasConstrainedProperties())
            return true;

        return _hasConstrainedProperties;
    }

    /**
     * Returns true if this interface is the first interface in the inheritance hierarchy
     * to declare support for constrained properties.  This is used to declared 
     * VetoableChangeListener registration methods for the bean once (and only once).
     */
    public boolean addsConstrainedPropertySupport()
    {
        //
        // If a super interface has already added support, then not added here
        //
        if (_superClass != null && _superClass.addsConstrainedPropertySupport())
            return false;

        return hasConstrainedProperties();
    }

    /**
     * Initializes the list of EventSets declared by this AptControlInterface
     */
    private ArrayList<AptEventSet> initEventSets()
    {
        ArrayList<AptEventSet> eventSets = new ArrayList<AptEventSet>();

        if ( _intfDecl == null || _intfDecl.getNestedTypes() == null )
            return eventSets;

        for (TypeDeclaration innerDecl : _intfDecl.getNestedTypes())
        {
            // HACKHACK: There appear to be mirror API bugs where calling getAnnotation()
            // on certain entity types will result in an endless loop.  For now, work around
            // this by a priori filtering... but this mechanism will drop errors that appear
            // on an inapropriate type (see check below)
            if (! (innerDecl instanceof InterfaceDeclaration))
                continue;

            if (innerDecl.getAnnotation(EventSet.class) != null)
            {

                if (! (innerDecl instanceof InterfaceDeclaration))
                {
                    _ap.printError( innerDecl, "eventset.illegal.usage" );
                }
                else
                {
                    eventSets.add(
                        new AptEventSet(this, (InterfaceDeclaration)innerDecl, _ap));
                }
            }
        }
        return eventSets;
    }

    /**
     * Returns the list of AptEventSet declared directly by this AptControlInterface
     */
    public Collection<AptEventSet> getEventSets() { return _eventSets; }

    /**
     * Returns the total number of operations for this control interface
     */
    public int getEventSetCount()
    {
        int count = _eventSets.size();
        if (_superClass != null)
            count += _superClass.getEventSetCount();

        return count;
    }

    /**
     * Returns the number of event sets declared in this control interface.
     * Does not include eventset's declared in super class(es).
     */
    public int getLocalEventSetCount()
    {
        return _eventSets.size();
    }

    /**
     * Returns the AptEventSet with the specified name
     */
    public AptEventSet getEventSet(String name)
    {
        for (AptEventSet eventSet: getEventSets())
            if (eventSet.getClassName().equals(name))
                return eventSet;

        if (_superClass != null)
            return _superClass.getEventSet(name);

        return null;
    }

    /**
     * Returns the FeatureInfo attributes for this control interface
     */
    public FeatureInfo getFeatureInfo() { return _featureInfo; }

    /**
     * Returns the list of fully qualified class names for types that are derived
     * from this Generator
     */
    public String [] getGeneratedTypes()
    {
        return new String [] { _bean.getClassName() };
    }

    /**
     * Returns the Version annotation, if any.
     */
    public Version getVersion()
    {
        return _version;
    }

    /**
     * Returns the VersionRequired annotation, if any.
     */
    public VersionRequired getVersionRequired()
    {
        return _versionRequired;
    }

    /**
     * Returns the information necessary to generate a ControlBean from this AptControlInterface
     */
    public List<GeneratorOutput> getCheckOutput(Filer filer) throws IOException
    {
        HashMap<String,Object> map = new HashMap<String,Object>();

        map.put("intf", this);                  // the control interface
        map.put("bean", _bean);

        ArrayList<GeneratorOutput> genList = new ArrayList<GeneratorOutput>();

        //
        // the ControlBean class
        //
        Writer beanWriter = new IndentingWriter(filer.createSourceFile(_bean.getClassName()));
        GeneratorOutput beanSource =
            new GeneratorOutput(beanWriter,
                               "org/apache/beehive/controls/runtime/generator/ControlBean.vm", map);
        genList.add(beanSource);

        //
        // the ControlBean BeanInfo class
        //
        Writer beanInfoWriter = new IndentingWriter(filer.createSourceFile(_bean.getBeanInfoName()));
        GeneratorOutput beanInfoSource =
            new GeneratorOutput(beanInfoWriter,
                "org/apache/beehive/controls/runtime/generator/ControlBeanInfo.vm", map);
        genList.add(beanInfoSource);

        return genList;
    }

    /**
     * Returns the information necessary to generate a packaging information from this 
     * AptControlInterface.  Since this information is not needed during type validation,
     * it can be delated until the generate phase.
     */
    public List<GeneratorOutput> getGenerateOutput(Filer filer) throws IOException
    {
        HashMap<String,Object> map = new HashMap<String,Object>();

        map.put("intf", this);                  // the control interface
        map.put("bean", _bean);

        ArrayList<GeneratorOutput> genList = new ArrayList<GeneratorOutput>();

        //
        // the ControlBean MANIFEST.MF section
        //
        Writer manifestWriter = filer.createTextFile(Filer.Location.CLASS_TREE, _bean.getPackage(),
                                          new File(_bean.getShortName() + ".class.manifest"),
                                          null);
        GeneratorOutput beanManifest =
            new GeneratorOutput(manifestWriter,
                                "org/apache/beehive/controls/runtime/generator/ControlManifest.vm",
                                map);
        genList.add(beanManifest);

        return genList;
    }

    /**
     * Returns true if this interface is a ControlExtension (jcx) interface, false
     * otherwise.
     */
    public boolean isExtension()
    {
        return _isExtension;
    }

    /**
     * Returns the most-derived interface in the inheritance chain that is annotated
     * with @ControlInterface.  It represents the point in the inheritance chain
     * where @ControlInterface becomes @ControlExtension (i.e., anything interface derived from
     * the 'most-derived interface' is annotated with @ControlExtension).  May return
     * null if the inheritance chain is malformed.
     */
    public AptControlInterface getMostDerivedInterface()
    {
        //
        // Walk up ControlInterface chain looking for the 1st instance annotated
        // w/ @ControlInterface (as opposed to @ControlExtension)
        //
        // REVIEW: TBD rules for inheritance of @ControlInterface will affect this.
        // Probably need to keep walking and examine each @ControlInterface in the chain.
        // Run all checkers in chain?  Make checkers responsible for invoking their base
        // class-defined checkers?
        //

        AptControlInterface ancestor = getSuperClass();
        while (ancestor != null)
        {
            if (!ancestor.isExtension())
                break;

            ancestor = ancestor.getSuperClass();
        }

        return ancestor;
    }

    /**
     * Returns a classloader that can be used to load external classes
     */
    public ClassLoader getExternalClassLoader()
    {
        Map<String,String> opts = _ap.getAnnotationProcessorEnvironment().getOptions();
        String classpath = opts.get("-classpath");

        if ( classpath != null )
        {
            String [] cpEntries = classpath.split( File.pathSeparator );
            ArrayList a = new ArrayList();
            for ( String e : cpEntries )
            {
                try
                {
                    File f = (new File(e)).getCanonicalFile();
                    URL u = f.toURL();
                    a.add(u);
                }
                catch (Exception ex)
                {
                    System.err.println( "getExternalClassLoader(): bad cp entry=" + e );
                    System.err.println( "Exception processing e=" + ex );
                }
            }
            URL [] urls = new URL[a.size()];
            urls = (URL[]) a.toArray(urls);

            return new URLClassLoader( urls, ControlChecker.class.getClassLoader() );
        }

        return null;
    }

    //
    // These are defined by the JAR spec, Name-value pair section
    //
    static final String alphaNum = "ABCDEFGHIJKLMNOPQRSUVWXYZabcdefghijklmnopqrstuvwyz0123456789";
    static final String headerChar = alphaNum + "_-";

    /**
     *  Validates a manifest attribute.  If the attribute is invalid, it will generate
     *  appropriate APT messager entries and return false, else return true.
     */
    private boolean isValidManifestAttribute(ManifestAttribute attr)
    {
        String name = attr.name();
        String value = attr.value();
        boolean isValid = true;

        /*
        Note, the null-check for "name" is necessary when the annotation processor is hosted inside
        of an IDE where the name attribute of the ManifestAttribute metadata can be null
        temporarily.  In order to "keep going", just report a warning and proceed.
        */
        if (name == null || name.length() == 0)
        {
            _ap.printError( _intfDecl, "manifestattribute.illegal.name.1" );
            isValid = false;
        }
        else
        {
            if (alphaNum.indexOf(name.charAt(0)) < 0)
            {
                _ap.printError( _intfDecl, "manifestattribute.illegal.name.2" );
                isValid = false;
            }
            for (int i = 1; i < name.length(); i++)
            {
                if (headerChar.indexOf(name.charAt(i)) < 0)
                {
                    _ap.printError( _intfDecl, "manifestattribute.illegal.name.3", name.charAt(i) );
                    isValid = false;
                    break;
                }
            }
        }

        /*
        Note, the null-check for "value" is necessary when the annotation processor is hosted inside
        of an IDE where the value attribute of the ManifestAttribute metadata can be null
        temporarily.  In order to "keep going", just report a warning and proceed.
        */
        if (value == null || value.length() == 0)
        {
            _ap.printError( _intfDecl, "manifestattribute.illegal.name.4" );
            isValid = false;
        }
        else
        {
            // TODO: validate string contents are valid UTF-8?
        }

        return isValid;
    }

    /**
     * Returns the array of ManifestAttributes associated with the AptControlInterface
     */
    public HashMap<String, String> getManifestAttributes()
    {
        HashMap<String,String> attributes = new HashMap<String,String>();

        if ( _intfDecl == null )
            return attributes;

        try
        {
            ManifestAttributes annotAttrs =_intfDecl.getAnnotation(ManifestAttributes.class);
            if (annotAttrs != null)
            {
                ManifestAttribute [] attrs = (ManifestAttribute [])annotAttrs.value();
                for (int i = 0; i < attrs.length; i++)
                {
                    if (isValidManifestAttribute(attrs[i]))
                        attributes.put(attrs[i].name(), attrs[i].value());
                }
            }
            ManifestAttribute annotAttr = _intfDecl.getAnnotation(ManifestAttribute.class);
            if (annotAttr != null)
            {
                if (isValidManifestAttribute(annotAttr))
                    attributes.put(annotAttr.name(), annotAttr.value());
            }
            return attributes;
        }
        catch (Exception e) { e.printStackTrace(); return attributes; }
    }

    /**
     * Computes whether this interface is a ControlInterface or a ControlExtension
     */
    private boolean initIsExtension()
    {
        if ( _intfDecl == null )
            return false;

        return _intfDecl.getAnnotation(ControlExtension.class) != null;
    }

    /**
     * Returns the FeatureInfo annotation for this control interface, or null if there is none.
     */
    private FeatureInfo initFeatureInfo()
    {
        if ( _intfDecl == null )
            return null;
        return _intfDecl.getAnnotation(FeatureInfo.class);
    }

    /**
     * Returns the Version annotation for this control interface, or null if there is none.
     */
    private Version initVersion()
    {
        if ( _intfDecl == null )
            return null;
        return _intfDecl.getAnnotation(Version.class);
    }

    /**
     * Returns the VersionRequired annotation for this control interface, or null if there is none.
     */
    private VersionRequired initVersionRequired()
    {
        if ( _intfDecl == null )
            return null;
        return _intfDecl.getAnnotation(VersionRequired.class);
    }

    /**
     * Enforces the VersionRequired annotation for control extensions.
     */
    private void enforceVersionRequired()
    {
        if ( _versionRequired != null )
        {
            if ( !isExtension() )
            {
                _ap.printError( _intfDecl, "versionrequired.illegal.usage" );
                return;
            }

            int majorRequired = _versionRequired.major();
            int minorRequired = _versionRequired.minor();

            if ( majorRequired < 0 )    // no real version requirement
                return;

            AptControlInterface ci = getMostDerivedInterface();
            if ( ci == null )
                return;

            int majorPresent = -1;
            int minorPresent = -1;
            Version ciVersion = ci._version;
            if ( ciVersion != null )
            {
                majorPresent = ciVersion.major();
                minorPresent = ciVersion.minor();

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
            _ap.printError( _intfDecl, "versionrequired.failed", _intfDecl.getSimpleName(),
                    majorRequired, minorRequired, majorPresent, minorPresent );
        }
    }

    /**
     * Runs control-specific checker class (if specified)
     */
    public void check()
    {
        //
        // Find the nearest @ControlInterface, which is where the relevant control checker
        // annotation will be found.
        //

        AptControlInterface mostDerived = (AptControlInterface) getMostDerivedInterface();
        if ( mostDerived == null )
            return;

        InterfaceDeclaration intfDecl = mostDerived._intfDecl;

        if ( intfDecl == null )
            return;

        AnnotationMirror controlMirror = null;

        for (AnnotationMirror annot : intfDecl.getAnnotationMirrors())
        {
            if (annot.getAnnotationType().getDeclaration().getQualifiedName().equals(
                    "org.apache.beehive.controls.api.bean.ControlInterface"))
            {
                controlMirror = annot;
                break;
            }
        }

        assert ( controlMirror != null ) : "Found a control interface that isn't annotated properly: " + intfDecl;

        AptAnnotationHelper controlAnnot = new AptAnnotationHelper(controlMirror);

        //
        // Read the name of the checker class from the @ControlInterface annotation,
        // dynamically load and run it.
        //

        DeclaredType checkerMirror = (DeclaredType)controlAnnot.getObjectValue("checker");
        if ( checkerMirror == null )
        {
            // try the deprecated 'checkerClass' attribute
            checkerMirror = (DeclaredType)controlAnnot.getObjectValue("checkerClass");
        }

        if ( checkerMirror != null && checkerMirror.getDeclaration() != null )
        {
            // TODO: optimize to not invoke default checker?
            String checkerName = checkerMirror.toString();

            try
            {
                ClassLoader loader = getExternalClassLoader();

                Class checkerClass = loader.loadClass( checkerName );
                if ( !ControlChecker.class.isAssignableFrom(checkerClass) )
                {
                    _ap.printError( intfDecl, "control.interface.illegal.checker", intfDecl.getSimpleName(), checkerName );
                }
                else
                {

                    Constructor ctor = checkerClass.getConstructor();

                    ControlChecker checker = (ControlChecker) ctor.newInstance();
                    CheckerAnnotationProcessorEnvironmentImpl ape =
                            new CheckerAnnotationProcessorEnvironmentImpl(_ap);
                    checker.check( _intfDecl, ape );
                }
            }
            catch ( Exception e ) {
                _ap.printError( intfDecl, "control.interface.checker.load.failed", intfDecl.getSimpleName(), checkerName );
            }
        }
    }

    /**
     * Build a list of properties defined by getter/setter methods on this control interface.
     */
    private ArrayList<AptControlInterfaceProperty> initIntfProperties() {

        HashMap<String, AptControlInterfaceProperty> intfPropMap = new HashMap<String, AptControlInterfaceProperty>();

        Collection<AptOperation> ops = getOperations();
        for (AptOperation op : ops) {
            String opName = op.getName();
            if (!op.isPublic()) {
                continue;
            }

            if (isGetter(op)) {
                String propertyName = getIntfPropertyName(op);
                if (intfPropMap.containsKey(propertyName)) {
                    intfPropMap.get(propertyName).setGetterName(opName);
                }
                else {
                    intfPropMap.put(propertyName, new AptControlInterfaceProperty(propertyName, opName, null));
                }
            }
            else if (isSetter(op)) {
                String propertyName = getIntfPropertyName(op);
                if (intfPropMap.containsKey(propertyName)) {
                    intfPropMap.get(propertyName).setSetterName(opName);
                }
                else {
                    intfPropMap.put(propertyName, new AptControlInterfaceProperty(propertyName, null, opName));
                }
            }
            else if (isIsGetter(op)) {
                String propertyName = getIntfPropertyName(op);
                if (intfPropMap.containsKey(propertyName)) {
                    intfPropMap.get(propertyName).setGetterName(opName);
                }
                else {
                    intfPropMap.put(propertyName, new AptControlInterfaceProperty(propertyName, opName, null));
                }
            }
        }
        return new ArrayList<AptControlInterfaceProperty>(intfPropMap.values());
    }

    /**
     * Does the method have a Java Beans getter method signature.
     * @param method AptMethod instance
     * @return true if getter
     */
    private boolean isGetter(AptMethod method) {
        String methodName = method.getName();

        if (methodName.length() < 4)
            return false;

        if (!methodName.startsWith("get"))
            return false;

        if (method.getArgList().length() > 0)
            return false;

        if ("void".equals(method.getReturnType()))
            return false;

        return true;
    }

    /**
     * Does the method have a Java Beans getter method signature (is varient).
     * @param method AptMethod instance.
     * @return true if 'is' getter.
     */
    private boolean isIsGetter(AptMethod method) {
        String methodName = method.getName();

        if (methodName.length() < 3)
            return false;

        if (!methodName.startsWith("is"))
            return false;

        if (method.getArgList().length() > 0)
            return false;

        if (!"boolean".equals(method.getReturnType()))
            return false;

        return true;
    }

    /**
     * Does the method have a Java Beans setter method signature (is varient).
     * @param method AptMethod instance.
     * @return true if setter.
     */
    private boolean isSetter(AptMethod method) {
        String methodName = method.getName();

        if (methodName.length() < 4)
            return false;

        if (!methodName.startsWith("set"))
            return false;

        String argList = method.getArgList();
        if (argList.length() == 0)
            return false;

        if (argList.indexOf(',') > -1)
            return false;

        if (!"void".equals(method.getReturnType()))
            return false;

        return true;
    }

    /**
     * Generate a property name from a method name.
     * @param method AptMethod instance.
     * @return property name.
     */
    private String getIntfPropertyName(AptMethod method) {
        String opName = method.getName();

        int prefixIdx = 3;
        if (opName.startsWith("is"))
            prefixIdx = 2;

        if (opName.length() == prefixIdx + 1)
            return "" + Character.toLowerCase(opName.charAt(prefixIdx));

        return Character.toLowerCase(opName.charAt(prefixIdx)) + opName.substring(prefixIdx+1);
    }

    private ArrayList<AptControlInterfaceProperty>  _intfProps;
    private AptControlInterface                     _superClass;
    private AptMethodSet<AptOperation>              _operations;
    private ArrayList<AptPropertySet>               _propertySets;
    private boolean                                 _isExtension;        // true if ControlExtension, else ControlInterface
    private boolean                                 _hasBoundProperties;
    private boolean                                 _hasConstrainedProperties;
    private ArrayList<AptEventSet>                  _eventSets;
    private ControlBean                             _bean;
    private FeatureInfo                             _featureInfo;
    private Version                                 _version;
    private VersionRequired                         _versionRequired;
    private InterfaceDeclaration                    _intfDecl;
    private InterfaceDeclaration                    _superDecl;
    private TwoPhaseAnnotationProcessor             _ap;
}
