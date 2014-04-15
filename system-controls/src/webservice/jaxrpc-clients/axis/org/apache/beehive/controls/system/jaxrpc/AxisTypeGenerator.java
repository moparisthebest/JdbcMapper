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

package org.apache.beehive.controls.system.jaxrpc;

import org.apache.axis.wsdl.gen.Generator;
import org.apache.axis.wsdl.gen.NoopGenerator;
import org.apache.axis.wsdl.symbolTable.SymbolTable;
import org.apache.axis.wsdl.toJava.Emitter;
import org.apache.axis.wsdl.toJava.JavaGeneratorFactory;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.PortType;
import javax.wsdl.Service;

/**
 * Type generator class for the AxisTypeGeneratory ant task.
 */
public class AxisTypeGenerator extends JavaGeneratorFactory {

    /**
     * Constructor.
     */
    public AxisTypeGenerator() {
        super();
        Emitter e = new Emitter();
        setEmitter(e);
        emitter.setFactory(this);
    }

    /**
     * Generate types from a wsdl.
     *
     * @param wsdl      Wsdl
     * @param outputDir Output dir.
     * @throws Exception on error.
     */
    public void generateTypes(String wsdl, String outputDir)
            throws Exception {
        emitter.setOutputDir(outputDir);
        emitter.run(wsdl);
    }

    /**
     * Noop.
     *
     * @param message
     * @param symbolTable
     * @return noop
     */
    public Generator getGenerator(Message message, SymbolTable symbolTable) {
        return new NoopGenerator();
    }

    /**
     * Noop.
     *
     * @param portType
     * @param symbolTable
     * @return noop
     */
    public Generator getGenerator(PortType portType, SymbolTable symbolTable) {
        return new NoopGenerator();
    }

    /**
     * Noop.
     *
     * @param binding
     * @param symbolTable
     * @return noop
     */
    public Generator getGenerator(Binding binding, SymbolTable symbolTable) {
        return new NoopGenerator();
    }

    /**
     * Noop.
     *
     * @param service
     * @param symbolTable
     * @return noop
     */
    public Generator getGenerator(Service service, SymbolTable symbolTable) {
        return new NoopGenerator();
    }

    /**
     * Noop.
     *
     * @param definition
     * @param symbolTable
     * @return noop.
     */
    public Generator getGenerator(Definition definition, SymbolTable symbolTable) {
        return new NoopGenerator();
    }
}
