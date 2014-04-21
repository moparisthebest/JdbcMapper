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
import java.util.HashMap;
import java.util.Collection;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.type.AnnotationType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.WildcardType;

import org.apache.beehive.controls.api.packaging.FeatureInfo;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;


/**
 * The AptMethod class defines a base set of utility methods for acessing method attributes
 * based upon an APT method declaration.
 */ 
public class AptMethod
{
    //
    // Maps primitive type names to a default value string
    //
    private static HashMap<String, String> _defaultReturnValues = new HashMap<String,String>();

    static final HashMap<PrimitiveType.Kind,String> _primToObject = 
                        new HashMap<PrimitiveType.Kind,String>();

    static
    {
        _defaultReturnValues.put("void", ""); 
        _defaultReturnValues.put("boolean", "false"); 
        _defaultReturnValues.put("char", "'\0'"); 
        _defaultReturnValues.put("byte", "0");
        _defaultReturnValues.put("short", "0");
        _defaultReturnValues.put("int", "0");
        _defaultReturnValues.put("long", "0");
        _defaultReturnValues.put("float", "0.0f");
        _defaultReturnValues.put("double", "0.0d");

        _primToObject.put(PrimitiveType.Kind.BOOLEAN, "Boolean");
        _primToObject.put(PrimitiveType.Kind.BYTE, "Byte");
        _primToObject.put(PrimitiveType.Kind.CHAR, "Character");
        _primToObject.put(PrimitiveType.Kind.DOUBLE, "Double");
        _primToObject.put(PrimitiveType.Kind.FLOAT, "Float");
        _primToObject.put(PrimitiveType.Kind.INT, "Integer");
        _primToObject.put(PrimitiveType.Kind.LONG, "Long");
        _primToObject.put(PrimitiveType.Kind.SHORT, "Short");
    }


    /**
     * Constructs a new AptMethod instance associated with a specific method declaration
     */
    public AptMethod(MethodDeclaration methodDecl, TwoPhaseAnnotationProcessor ap )
    {
        _methodDecl = methodDecl;
        _interceptorServiceNames = initInterceptorServiceNames();
        _ap = ap;
    }

    /**
     * Returns the name of the method
     */
    public String getName()
    {
        if ( _methodDecl == null )
            return "";
        
        return _methodDecl.getSimpleName();
    }

    /**
     * Returns the argument declaration of the method, applying the bindings in the provided
     * type map to any parameter types
     */
    public String getArgDecl(HashMap<String,TypeMirror> bindingMap)
    {
        StringBuffer sb = new StringBuffer();

        if ( _methodDecl.getParameters() == null )
            return "";
        
        int i = 0;
        for (ParameterDeclaration paramDecl : _methodDecl.getParameters())
        {
            TypeMirror paramType = paramDecl.getType();
            if ( paramType == null )
                return "";
                
            if (bindingMap != null && bindingMap.containsKey(paramType.toString()))
                paramType = bindingMap.get(paramType.toString());

            if (i != 0)
                sb.append(", ");
            
            sb.append(paramType.toString());

            sb.append(' ');

            // BUGBUG: when the MethodDeclaration is derived from Reflection, this seems
            // to return 'arg0' for all arguments!
            String argName = paramDecl.getSimpleName();
            if (argName.equals("arg0"))
                sb.append("arg" + i);
            else
                sb.append(argName);

            i++;
        }
        return sb.toString();
    }

    /**
     * Returns the arguments declarations for the method, with no formal parameter binding applied
     */
    public String getArgDecl()
    {
        return getArgDecl(null);
    }

    /**
     * Returns the the method argument names, in a comma separated list
     */
    public String getArgList(boolean quoteDelimit)
    {
        StringBuffer sb = new StringBuffer();
        int i = 0;

        if ( _methodDecl.getParameters() == null )
            return "";
        
        for (ParameterDeclaration paramDecl : _methodDecl.getParameters())
        {
            if (i != 0)
                sb.append(", ");

            // BUGBUG: when the MethodDeclaration is derived from Reflection, this seems
            // to return 'arg0' for all arguments!
            String argName = paramDecl.getSimpleName();
            if (quoteDelimit) sb.append('"');
            if (argName.equals("arg0"))
                sb.append("arg" + i);
            else
                sb.append(argName);
            if (quoteDelimit) sb.append('"');
            i++;
        }
        return sb.toString();
    }

    /**
     * Default form of getArgList, that does not quote delimit arguments
     */
    public String getArgList() { return getArgList(false); }

    /**
     * Returns the the method argument classes, in a comma separated list
     */
    public String getArgTypes()
    {
        StringBuffer sb = new StringBuffer();
        int i = 0;

        if ( _methodDecl == null || _methodDecl.getParameters() == null )
            return "";
        
        for (ParameterDeclaration paramDecl : _methodDecl.getParameters())
        {
            if (i++ != 0)
                sb.append(", ");

            TypeMirror paramType = paramDecl.getType();
            if (paramType == null)
                return "";

            //
            // Use the erasure here, because we only want the raw type, not the reference
            // type
            //
            sb.append(_ap.getAnnotationProcessorEnvironment().getTypeUtils().getErasure(paramType));
            sb.append(".class");
        }
        return sb.toString();
    }

    /**
     * Returns 'true' if the method uses any parameterized types as parameters
     */
    public boolean hasParameterizedArguments()
    {
        for (ParameterDeclaration paramDecl : _methodDecl.getParameters())
        {
            TypeMirror paramType = paramDecl.getType();
            if (paramType instanceof ReferenceType || paramType instanceof WildcardType)
                return true;
        }
        return false;
    }

    /**
     * Returns the declaration of any generic formal types associated with the method
     */
    public String getFormalTypes()
    {
        if ( _methodDecl == null || _methodDecl.getReturnType() == null )
            return "";

        Collection<TypeParameterDeclaration> formalTypes = _methodDecl.getFormalTypeParameters();
        if (formalTypes.size() == 0)
            return "";

        StringBuffer sb = new StringBuffer("<");
        boolean isFirst = true;
        for (TypeParameterDeclaration tpd: formalTypes)
        {
            if (isFirst)
                isFirst = false;
            else
                sb.append(", ");

            sb.append(tpd.toString());
        }
        sb.append(">");
        return sb.toString();
    }

    /**
     * Returns the method return type, applying any formal type parameter bindings defined
     * by the provided map.
     */
    public String getReturnType(HashMap<String,TypeMirror> bindingMap)
    {
        if ( _methodDecl == null || _methodDecl.getReturnType() == null )
            return "";
        
        String returnType = _methodDecl.getReturnType().toString();
        if (bindingMap != null && bindingMap.containsKey(returnType))
            return bindingMap.get(returnType).toString();

        return returnType;
    }

    /**
     * Returns the method return type with no type bindings applied
     */
    public String getReturnType()
    {
        return getReturnType(null);
    }

    /**
     * Returns the throws clause of the operation
     */
    public String getThrowsClause()
    {
        if ( _methodDecl == null || _methodDecl.getThrownTypes() == null )
            return "";
        
        Collection<ReferenceType> thrownTypes = _methodDecl.getThrownTypes();
        if (thrownTypes.size() == 0)
            return "";

        StringBuffer sb = new StringBuffer("throws ");
        int i = 0;
        for (ReferenceType exceptType : thrownTypes)
        {
            if (i++ != 0)
                sb.append(", ");
            sb.append(exceptType.toString());
        }
        return sb.toString();
    }

    /**
     * Returns an ArrayList of thrown exceptions
     */
    public ArrayList<String> getThrowsList()
    {
        ArrayList<String> throwsList = new ArrayList<String>();

        if ( _methodDecl == null ||
             _methodDecl.getThrownTypes() == null ||
             _methodDecl.getThrownTypes().size() == 0 )
            return throwsList;
        
        Collection<ReferenceType> thrownTypes = _methodDecl.getThrownTypes();
        for (ReferenceType exceptType : thrownTypes)
            throwsList.add(exceptType.toString());

        return throwsList;
    }

    /**
     * Returns a default return value string for the method, based upon bound return type
     */
    public String getDefaultReturnValue(HashMap<String,TypeMirror> typeBinding)
    {
        String returnType = getReturnType(typeBinding);
        if (_defaultReturnValues.containsKey(returnType))
            return _defaultReturnValues.get(returnType);
        return "null";
    }

    /**
     * Returns a default return value string for the method, with no type binding applied
     */
    public String getDefaultReturnValue()
    {
        return getDefaultReturnValue(null);
    }

    /**
     * Returns any FeatureInfo associated with the method (or null if none)
     */ 
    public FeatureInfo getFeatureInfo()
    {
        if ( _methodDecl == null )
            return null;
        
        return _methodDecl.getAnnotation(FeatureInfo.class);
    }

    /**
     *  Sets the unique index value for this method.  If a particular method is overloaded,
     * then each associated AptMethod will have a unique index;  otherwise, the index is -1.
     */
    public void setIndex(int index)
    {
        _index = index;
    }

    /**
     * Returns the unique index value for this method.
     */
    public int getIndex() { return _index; }

    /**
     * Is this a public method?
     * @return true if public
     */
    protected boolean isPublic() {
        Collection<Modifier> modifiers = _methodDecl.getModifiers();
        return modifiers.contains(Modifier.PUBLIC);
    }

    MethodDeclaration _methodDecl;
    int _index = -1;
    TwoPhaseAnnotationProcessor _ap;

    /**
     * Returns the names of interceptor service interfaces associated with this operation
     * @return the names of the interceptor service interfaces associated with this operation
     */
    public Collection<String> getInterceptorServiceNames()
    {
        return _interceptorServiceNames;
    }

    /**
     * Returns the names of interceptor service interfaces associated with this operation, formatted as a
     * constant initializer string.
     * @return the names of the interceptor service interfaces associated with this operation
     */
    public String getInterceptorDecl()
    {
        Collection<String> names = getInterceptorServiceNames();
        if ( names == null || names.size() == 0 )
            return null;

        StringBuffer ret = new StringBuffer("{");

        String [] n = names.toArray(new String[0]);
        for (int i=0 ; i < n.length ; ++i)
        {
            ret.append('"'); ret.append( n[i] ); ret.append('"');
            if ( i != n.length-1 )
                ret.append(", ");
        }
        ret.append( "}" );

        return ret.toString();
    }

    private Collection<String> initInterceptorServiceNames()
    {
        ArrayList<String> ret = new ArrayList<String>();

        if (_methodDecl == null)
            return ret;

        // Iterate over annotations on operation, looking for interceptor-based ones
        Collection<AnnotationMirror> annotations = _methodDecl.getAnnotationMirrors();
        for ( AnnotationMirror a : annotations )
        {
            AnnotationType at = a.getAnnotationType();
            AnnotationTypeDeclaration atd = at.getDeclaration();

            /*
            When performing annotation processing, the ATD here might be null if the apt.exe runtime
            is unable to resolve the type to something specific.  This will happen when a type referenced
            in a source file is invalid because of a bad / missing import, mis-spelling, etc.  When
            this happens, annotation processing should merilly continue and let javac.exe report
            the type errors.  Better to do that than to throw an NPE here.
            */
            if (atd == null)
                continue;

            Collection<AnnotationMirror> metaAnnotations = atd.getAnnotationMirrors();

            /*
            Look for annotations that are meta-annotated with @InterceptorAnnotation
            */
            for ( AnnotationMirror ma : metaAnnotations )
            {
                if ( ma.getAnnotationType().getDeclaration().getQualifiedName().
                        equals( "org.apache.beehive.controls.spi.svc.InterceptorAnnotation" ) )
                {
                    /*
                    found an interceptor-based annotation, add it!
                    */
                    AptAnnotationHelper ia = new AptAnnotationHelper( ma );
                    DeclaredType serviceType = (DeclaredType) ia.getObjectValue("service");
                    String intf = serviceType.toString();
                    ret.add( intf );

                    break;
                }
            }
        }

        return ret;
    }

    Collection<String> _interceptorServiceNames;

}
