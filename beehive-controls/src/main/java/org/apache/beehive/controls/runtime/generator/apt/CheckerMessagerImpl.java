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

import com.sun.mirror.apt.Messager;
import com.sun.mirror.util.SourcePosition;

/**
 * Wrap a com.sun.mirror.apt.Messager instance to track errors and warnings.
 */
public final class CheckerMessagerImpl implements Messager {
    private final Messager _messager;
    private final Diagnostics _aptDiagnostics;
    private int _errorCount;
    private int _warningCount;


    /**
     * Constructor.
     *
     * @param messager Messager to wrap.
     */
    CheckerMessagerImpl(Messager messager, Diagnostics aptDiagnostics) {
        _messager = messager;
        _aptDiagnostics = aptDiagnostics;
        _errorCount = _warningCount = 0;
    }

    /**
     * Get the number of errors sent to this messager.
     *
     * @return Number of errors.
     */
    int getErrorCount() {
        return _errorCount;
    }

    /**
     * Get the number of warnings sent to this messager.
     *
     * @return Number of warnings.
     */
    int getWarningCount() {
        return _warningCount;
    }

    public void printError(String string) {
        _errorCount++;
        _aptDiagnostics.setHasErrors(true);
        _messager.printError(string);
    }

    public void printError(SourcePosition sourcePosition, String string) {
        _errorCount++;
        _aptDiagnostics.setHasErrors(true);
        _messager.printError(sourcePosition, string);
    }

    public void printWarning(String string) {
        _warningCount++;
        _messager.printWarning(string);
    }

    public void printWarning(SourcePosition sourcePosition, String string) {
        _warningCount++;
        _messager.printWarning(sourcePosition, string);
    }

    public void printNotice(String string) {
        _messager.printNotice(string);
    }

    public void printNotice(SourcePosition sourcePosition, String string) {
        _messager.printNotice(sourcePosition, string);
    }
}
