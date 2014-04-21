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

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorListener;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.util.Declarations;
import com.sun.mirror.util.Types;

import java.util.Collection;
import java.util.Map;

/**
 * Wrapper for an AnnotationProcessorEnvironment instance.
 * Keeps track of errors / warnings logged.
 */
public final class CheckerAnnotationProcessorEnvironmentImpl implements AnnotationProcessorEnvironment {
    private final AnnotationProcessorEnvironment _aptEnv;
    private final CheckerMessagerImpl _messager;

    /**
     * @param aptDiagnostics
     */
    public CheckerAnnotationProcessorEnvironmentImpl(Diagnostics aptDiagnostics) {
        _aptEnv = aptDiagnostics.getAnnotationProcessorEnvironment();
        _messager = new CheckerMessagerImpl(_aptEnv.getMessager(), aptDiagnostics);
    }

    /**
     * Get the number of errors sent to the messager.
     *
     * @return Number of errors.
     */
    public int getErrorCount() {
        return _messager.getErrorCount();
    }

    /**
     * Get the number of warnings sent to the messager.
     *
     * @return Number of warnings.
     */
    public int getWarningCount() {
        return _messager.getWarningCount();
    }

    public Messager getMessager() {
        return _messager;
    }

    public Filer getFiler() {
        return _aptEnv.getFiler();
    }

    public Map<String, String> getOptions() {
        return _aptEnv.getOptions();
    }

    public Collection<TypeDeclaration> getSpecifiedTypeDeclarations() {
        return _aptEnv.getSpecifiedTypeDeclarations();
    }

    public PackageDeclaration getPackage(String string) {
        return _aptEnv.getPackage(string);
    }

    public TypeDeclaration getTypeDeclaration(String string) {
        return _aptEnv.getTypeDeclaration(string);
    }

    public Collection<TypeDeclaration> getTypeDeclarations() {
        return _aptEnv.getTypeDeclarations();
    }

    public Declarations getDeclarationUtils() {
        return _aptEnv.getDeclarationUtils();
    }

    public Types getTypeUtils() {
        return _aptEnv.getTypeUtils();
    }

    public void addListener(AnnotationProcessorListener listener) {
        _aptEnv.addListener(listener);
    }

    public void removeListener(AnnotationProcessorListener listener) {
        _aptEnv.removeListener(listener);
    }

    public Collection<Declaration> getDeclarationsAnnotatedWith(AnnotationTypeDeclaration typeDeclaration) {
        return _aptEnv.getDeclarationsAnnotatedWith(typeDeclaration);
    }
}
