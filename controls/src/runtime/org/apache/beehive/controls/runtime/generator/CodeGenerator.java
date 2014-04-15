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

/**
 * The CodeGenerator class is an abstract base class that encapsulates the invocation
 * Apache Velocity or other code generation tools that generate source artifacts.
 * <p>
 * This primary motivation for this abstraction is to decouple the loading and invocation
 * of Velocity from the mainline control generation process.
 */
abstract public class CodeGenerator
{
    public CodeGenerator() {
    }

    abstract public void generate(GeneratorOutput genOut) throws CodeGenerationException;
}
