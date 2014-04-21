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
import java.util.List;

import com.sun.mirror.apt.Filer;

/**
 * The Generator interface will be implemented by APT data types that result in the generation
 * of new source or text artifacts.
 * for template usage on class-type objects
 * <p>
 * This is done with an abstract class (instead of an interface) so derived abstract classes
 * can be subclassed from it w/out requiring all of the methods to be declared there.
 */
public interface Generator
{
    /**
     * Returns the list of fully qualified class names for types that are derived
     * from this Generator
     */
    public String [] getGeneratedTypes();
    
    /**
     * Returns the list of generated files derived from this Generator during the
     * check phase of annotation processing.
     */
    public List<GeneratorOutput> getCheckOutput(Filer filer) throws IOException;

    /**
     * Returns the list of generated files derived from this Generator during the
     * generate phase of annotation processing.
     */
    public List<GeneratorOutput> getGenerateOutput(Filer filer) throws IOException;
}
