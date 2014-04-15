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

import com.sun.mirror.declaration.MethodDeclaration;

import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptEventHandler class represents a control EventHandler where the event attributes
 * are derived using APT metadata
 */
public class AptEventHandler extends AptMethod
{
    /**
     * Constructs a new AptEventHandler instance
     * from APT metadata
     * @param event the handled ControlEvent
     * @param handlerDecl the handler method declaration
     */
    public AptEventHandler(AptEvent event, MethodDeclaration handlerDecl, TwoPhaseAnnotationProcessor ap)
    {
        super(handlerDecl, ap);
        _event = event;
        _handlerDecl = handlerDecl;
    }

    /**
     * Returns the ControlEvent associated with the ControlEventHandler
     */
    public AptEvent getEvent() { return _event; }

    MethodDeclaration _handlerDecl;
    private AptEvent _event;
}
