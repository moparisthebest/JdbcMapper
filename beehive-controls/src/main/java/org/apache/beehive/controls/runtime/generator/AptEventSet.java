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
import java.util.HashSet;

import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.InterfaceType;

import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.packaging.EventSetInfo;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptEventSet class represents a control EventSet where the events
 * are derived using APT metadata.
 */
public class AptEventSet extends AptType
{
    /**
     * Constructs a new AptEventSet instance from APT metadata
     * @param controlIntf the declaring control interface
     * @param eventSet the EventSet class
     * @param ap the associated AnnotationProcessor
     */
    public AptEventSet(AptControlInterface controlIntf, InterfaceDeclaration eventSet,
                       TwoPhaseAnnotationProcessor ap)
    {
        _controlIntf = controlIntf;
        _eventSet = eventSet;
        _ap = ap;
        setDeclaration(eventSet);

        EventSet eventSetAnnot = eventSet.getAnnotation(EventSet.class);
        if (eventSetAnnot != null)
            _unicast = eventSetAnnot.unicast();

        //
        // If an EventSet interface has formal type parameters, they must be a subset of
        // the original formal type parameters declared on the original control interface.
        // This is required because it must be possible to bind the types of events immediately
        // upon construction of the bean... there is no opportunity to separately specify 
        // parameterization for the event set for the purpose of creating listeners, client
        // notifiers, etc.
        //
        TypeDeclaration intfDecl = controlIntf.getTypeDeclaration();
        for (TypeParameterDeclaration estpd : _eventSet.getFormalTypeParameters())
        {
            boolean found = false;
            for (TypeParameterDeclaration citpd : intfDecl.getFormalTypeParameters())
            {
                if (estpd.getSimpleName().equals(citpd.getSimpleName()))
                {
                    found = true;
                    break;
                }
            }
            if (! found)
            {
                //
                // BUGBUG: Ideally, this would be estpd.getPosition, but this seems to return
                // 0,0 for the current APT implementation, so we use the event set position
                // instead.
                // Once this works, the 'break' below can also be removed to present errors
                // for multiple invalid parameters
                //
                _ap.printError( eventSet, "eventset.formal.parameter.mismatch" );
                break;
            }
        }

        _superEventSet = initSuperEventSet();

        _events = initEvents();
    }

    /**
     * Checks to see if this EventSet extends an EventSet declared on a parent control interface.  If
     * found it will return the parent EventSet, or return null if not found. 
     */
    public AptEventSet initSuperEventSet()
    {
        // This will be common, so short circuit quickly
        AptControlInterface superControl = _controlIntf.getSuperClass();
        if (superControl == null)
            return null;

        // Compute a hash set containing the qualified names of all super interfaces
        // for this EventSet
        HashSet<String> extendNames = new HashSet<String>();
        for (InterfaceType superType: _eventSet.getSuperinterfaces())
        {
            InterfaceDeclaration superDecl = superType.getDeclaration();
            if (superDecl != null)
                extendNames.add(superDecl.getQualifiedName());
        }

        // Starting with the parent of the ControlInterface declaring this EventSet, look
        // for a parent interface that declares ones of these super interfaces as an event
        // set
        while (superControl != null)
        {
            Collection<AptEventSet> superEventSets = superControl.getEventSets();
            for (AptEventSet superEventSet : superEventSets)
            {
                if (extendNames.contains(superEventSet.getClassName()))
                    return superEventSet;
            }

            superControl = superControl.getSuperClass();
        }

        // Nothing found, so no super event set
        return null;
    }

    /**
     * Returns any EventSet from which this event set derives (or null if none)
     */
    public AptEventSet getSuperEventSet() { return _superEventSet; }

    /**
     * Initializes the list of Events associated with this EventSet
     */
    protected AptMethodSet<AptEvent> initEvents()
    {
        AptMethodSet<AptEvent> events = new AptMethodSet<AptEvent>();
        if ( _eventSet == null || _eventSet.getMethods() == null )
            return events;

        //
        // Add all of the public methods directly declared and inherited from extended
        // interfaces, except for the EventSet super interface (if any)
        //
        ArrayList<InterfaceDeclaration> intfList = new ArrayList<InterfaceDeclaration>();
        intfList.add(_eventSet);
        for (int i = 0; i < intfList.size(); i++)
        {
            InterfaceDeclaration intfDecl = intfList.get(i);

            //
            // Don't add events that are derived from a super event set.  These are not added because
            // this class picks a single super interface to extend from when building a hierarchy
            // of callback notifiers (etc).  So, the super event set that was chosen first is left out
            // of the list of event methods since they're captured in superclasses in the Control's implementation
            //
            if (_superEventSet != null && _superEventSet.getClassName().equals(intfDecl.getQualifiedName()))
                continue;

            // Add all declared methods, but ignore the mystery <clinit> methods
            for (MethodDeclaration methodDecl : intfDecl.getMethods())
                if (!methodDecl.toString().equals("<clinit>()"))
                    events.add(new AptEvent(this, methodDecl, _ap));

            //
            // Add all superinterfaces of the target interface to the list
            //
            for (InterfaceType superType: intfDecl.getSuperinterfaces())
            {
                InterfaceDeclaration superDecl = superType.getDeclaration();
                if (superDecl != null && !intfList.contains(superDecl))
                    intfList.add(superDecl);
            }
        }

        return events;
    }

    /**
     * Returns the list of Events associated with this EventSet
     */
    public Collection<AptEvent> getEvents() { return _events.getMethods(); }

    /**
     * Returns 'true' if the event set support only unicast (single listener) events,
     * false otherwise.
     */
    public boolean isUnicast()
    {
        return _unicast;
    }

    /**
     * Returns the number of Events for this EventSet and any super event set
     */
    public int getEventCount()
    {
        int count = _events.size();
        if (_superEventSet != null)
            count += _superEventSet.getEventCount();
        return count;
    }

    /**
     * Returns the programmatic descriptor name to be returned by the EventDescriptor
     * for the event set.
     */
    public String getDescriptorName()
    {
        //
        // The javadocs for java.beans.EventSetDescriptor suggest that the programmatic name
        // should start w/ a lowercase letter.   So we use the unqualified event set interface
        // name w/ the first character lowercased.
        //
        String name = getShortName();
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Returns the name of the generated notifier class for this ControlEventSet
     */
    public String getNotifierClass()
    {
        StringBuffer sb = new StringBuffer(getShortName());
        sb.append("Notifier");

        //
        // If the event set declaration has any parameterized types, then include them on
        // the notifier class as well.  Currently, these can only be parameterized types
        // from the outer (control interface), since there is no other mechanism for specifying
        // type values at notifier construction (other than propagation from the outer type).
        // 
        sb.append(getFormalTypeParameterNames());
        return sb.toString();
    }

    /**
     * Returns any 'extends' clause that should be placed on the generated notifier class
     */
    public String getNotifierExtends()
    {
        //
        // All EventNotifiers are rooted from a common utility class, so if there is no
        // super event set, then extend the utility notifier class.
        //
        if (_superEventSet == null)
        {
            if (_unicast)
                return "org.apache.beehive.controls.runtime.bean.UnicastEventNotifier";
            else
                return "org.apache.beehive.controls.runtime.bean.EventNotifier";
        }

        //
        // Otherwise, a generated notifier will extend the notifier of any parent event set
        //
        return _superEventSet.getNotifierClass();
    }

    /**
     * Returns the short name for this notifier's base class.
     */
    public String getNotifierExtendsShortName() {

        if (_superEventSet == null)
        {
            if (_unicast)
                return "UnicastEventNotifier";
            else
                return "EventNotifier";
        }

        return _superEventSet.getNotifierClass();
    }

    /**
     * Return true if this notifier extends the UnicastEventNotifier or EventNotifier base class.
     */
    public boolean isExtendsNotifierBase() {
        return _superEventSet == null;
    }

    /**
     * Returns the name of the method used to register a new EventSet listener
     */
    public String getAddListenerMethod()
    {
        return "add" + getShortName() + "Listener";
    }

    /**
     * Returns the name of the method used to register a new EventSet listener
     */
    public String getRemoveListenerMethod()
    {
        return "remove" + getShortName() + "Listener";
    }

    /**
     * Returns the name of the method used to retrieve the (unicast) EventSet listener
     */
    public String getGetListenersMethod()
    {
        return "get" + getShortName() + "Listeners";
    }

    /**
     * Returns the name of a custom-generated method to initialize MethodDescriptor bean
     * info for the events in this EventSet
     */
    public String getInfoInitializer()
    {
       return "init" + getShortName() + "Events";
    }

    /**
     * Returns any EventSetInfo associated with the event set (or null if none)
     */
    public EventSetInfo getEventSetInfo()
    {
        if ( _eventSet == null )
            return null;

        return _eventSet.getAnnotation(EventSetInfo.class);
    }

    /**
     * Returns the underlying APT InterfaceDeclaration associated with this event set
     */
    public InterfaceDeclaration getDeclaration()
    {
        return _eventSet;
    }

    private TwoPhaseAnnotationProcessor     _ap;
    private InterfaceDeclaration            _eventSet;
    private AptEventSet                     _superEventSet;
    private AptControlInterface             _controlIntf;
    private AptMethodSet<AptEvent>          _events;
    private boolean                         _unicast;
}
