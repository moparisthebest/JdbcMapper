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

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogSystem;

import com.sun.mirror.apt.Messager;

/**
 * The VelocityAptLogSystem implements the <code>org.apache.velocity.runtime.LogSystem</code>
 * interface for logging messages from Velocity and routes warnings and errors to the log
 * system of APT.
 */
public class VelocityAptLogSystem implements LogSystem
{
    /**
     * This is the name of the Velocity configuration property that will be used to pass
     * the APT environment from the execution environment into the logger instance.  This
     * property must be set on the VelocityEngine instance, and the value should be the
     * <code>com.sun.mirror.apt.Messager</apt> instance that should be used to log messages.
     */
    static final String APT_ENV_PROPERTY = VelocityAptLogSystem.class + "." + "environment";

    /**
     * The prefix to apply to Velocity error and warnings before passing to APT, to make it
     * easier to identify Velocity output
     */
    static final private String MESSAGE_PREFIX = "VELOCITY: ";

    /**
     * This can be set to true when debugging Velocity codegen templates to have all output
     * from Velocity sent to the APT logger
     */
    static final private boolean _debugging = false;

    public VelocityAptLogSystem(Messager messager)
    {
        _messager = messager;
    }

    public void init(RuntimeServices rs) throws java.lang.Exception
    {
    }

    public void logVelocityMessage(int level, java.lang.String message)
    {
        if (level == LogSystem.ERROR_ID)
            _messager.printError(MESSAGE_PREFIX + message);
        else if (level == LogSystem.WARN_ID)
            _messager.printWarning(MESSAGE_PREFIX + message);
        else if (_debugging)
            _messager.printNotice(MESSAGE_PREFIX + message); 
    }

    Messager _messager;
}
