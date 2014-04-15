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

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.beehive.controls.api.bean.AnnotationMemberTypes;
import org.apache.beehive.controls.api.bean.AnnotationConstraints.MembershipRule;
import org.apache.beehive.controls.api.bean.AnnotationConstraints.MembershipRuleValues;
import org.apache.beehive.controls.api.properties.PropertyKey;
import org.apache.beehive.controls.api.properties.PropertySet;
import org.apache.beehive.controls.runtime.bean.AnnotationConstraintValidator;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.AnnotationType;

/**
 * This class is for validating control property values at build time
 * It calls {@link org.apache.beehive.controls.runtime.bean.AnnotationConstraintValidator
 *  AnnotationConstraintValidator} to do the validation.   
 */
public class AnnotationConstraintAptValidator extends AnnotationConstraintValidator
{

    public AnnotationConstraintAptValidator()
    {
        super();
    }

    /**
     * This method ensures that any control property value assignment satisfies
     * all property constraints. This method should be called from an annotation
     * processor to ensure declarative control property assignment using
     * annotations are validated at build time. This method is currently called
     * from ControlAnnotationProcessor and ControlClientAnnotationProcessor.
     * 
     * @param d
     *            a declaration which may contain a control property value
     *            assignment
     * @throws IllegalArgumentException
     *             when the declaration contains a control property value
     *             assignment that does not satisfy a property constraint.
     */
    public static void validate(Declaration d) throws IllegalArgumentException
    {
        Collection<AnnotationMirror> mirrors = d.getAnnotationMirrors();

        // for each annotations defined on the declaration, if the annotation
        // is a PropertySet, ensure the values assigned to the properties
        // satisfy all PropertySet and PropertyType constraints.
        for (AnnotationMirror m : mirrors)
        {
            AnnotationTypeDeclaration decl = m.getAnnotationType().getDeclaration();
            /*
            when embedding this annotation processor in an IDE, the declaration
            could be null when it doesn't resolve to a valid type.  In this case,
            just continue processing declarations.
            */
            if(decl == null) {
                continue;
            }
            else if (decl.getAnnotation(PropertySet.class) != null)
            {
                Iterator<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>> i =
                    m.getElementValues().entrySet().iterator();
                while (i.hasNext())
                {
                    Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> entry =
                        i.next();
                    Collection<Annotation> annotations = getMemberTypeAnnotations(entry.getKey());
                    if (annotations.size() > 0)
                    {
                        Annotation[] anArray = new Annotation[annotations.size()];
                        annotations.toArray(anArray);
                        validate(anArray, entry.getValue().getValue());
                    }
                }

                //If a membership rule is defined on the property set, ensure the rule is satisfied. 
                if (decl.getAnnotation(MembershipRule.class) != null)
                {
                    try 
                    {
                        String declClassName = decl.getQualifiedName();

                        TypeDeclaration owningClass = decl.getDeclaringType();
                        if (owningClass != null)
                            declClassName = owningClass.getQualifiedName() + "$" + decl.getSimpleName();
                            
	                    Class clazz = Class.forName(declClassName);
	                    Annotation a = d.getAnnotation(clazz);
	                    validateMembership(a);
                    }
                    catch (ClassNotFoundException cnfe)
                    {
                        //should not happen
                    }
                }
            }
        }

        // If the declaration is a class or interface, validate its methods
        // and fields.
        if (d instanceof TypeDeclaration)
        {
            TypeDeclaration td = ((TypeDeclaration) d);
            for (Declaration md : td.getMethods())
                validate(md);
            for (Declaration fd : td.getFields())
                validate(fd);
        }
        // If the delcaration is a method, validate its parameters.
        else if (d instanceof MethodDeclaration)
        {
            for (Declaration pd : ((MethodDeclaration) d).getParameters())
                validate(pd);
        }

    }

    private static Collection<Annotation> getMemberTypeAnnotations(Declaration decl)
    {
        Collection<Annotation> annotations = new ArrayList<Annotation>();
        for (AnnotationMirror am : decl.getAnnotationMirrors())
        {
            AnnotationType at = am.getAnnotationType();
            try
            {
                if (at.getContainingType() == null)
                    continue;
                String containingClassName = at.getContainingType()
                        .getDeclaration().getQualifiedName();
                if (containingClassName.equals(AnnotationMemberTypes.class
                        .getName()))
                {
                    String memberTypeName = at.getDeclaration().getSimpleName();
                    Class clazz = Class.forName(containingClassName + "$"
                            + memberTypeName);
                    Annotation a = decl.getAnnotation(clazz);
                    if (null != a)
                    {
                        annotations.add(a);
                    }
                }
            }
            catch (ClassNotFoundException e)
            {
            }
        }
        return annotations;
    }
}
