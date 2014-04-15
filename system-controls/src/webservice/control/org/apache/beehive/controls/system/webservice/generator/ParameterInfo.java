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

package org.apache.beehive.controls.system.webservice.generator;

import org.apache.beehive.controls.system.webservice.utils.HolderUtils;

/**
 * Method parameter info for the Veclocity engine.
 */
public final class ParameterInfo {

    private static final String PARAM_BASE_NAME = "param";

    enum ParamMode {
        IN, OUT, INOUT
    }

    private final String _name;
    private final String _className;

    /**
     * package protected constructor.
     *
     * @param name     The parameter name, if null a name will be generated based on the position parameter.
     * @param position The position in the argument list of this parameter.
     * @param typeName The class name of the parameter
     * @param mode     Mode may be any of the values defined by the ParamMode enumeration.
     */
    protected ParameterInfo(String name, int position, String typeName, ParamMode mode) {
        _className = resolveParameterType(typeName, mode);

        if (name == null) {
            _name = PARAM_BASE_NAME + position;
        }
        else if (!GeneratorUtils.isValidJavaIdentifier(name)) {
            _name = GeneratorUtils.transformInvalidJavaIdentifier(name);
            System.out.println("Warning: Parameter name " + name
                    + " is not a valid Java method parameter name, changing to: " + _name);
        }
        else {
            _name = name;
        }
    }

    /**
     * Get the name of the parameter,
     *
     * @return The parameter name, guarenteed NOT to return null.
     */
    public String getName() {
        return _name;
    }

    /**
     * The class name of the parameter type.  The class name will be a fully qualified name
     * unless the class is in the java.lang package (in which case the package name is truncated).
     *
     * @return String
     */
    public String getClassName() {
        return _className;
    }

    /**
     * Given the class and mode of the parameter, generate the classname.
     *
     * @param typeName  Parameter class name.
     * @param paramMode Mode.
     * @return A string containing the class name of the parameter.
     */
    private String resolveParameterType(String typeName, ParamMode paramMode) {
        if (paramMode == ParamMode.INOUT || paramMode == ParamMode.OUT)
            return HolderUtils.getHolderForClass(typeName);
        else {
            if (typeName.startsWith("java.lang."))
                typeName = typeName.substring(typeName.lastIndexOf('.') + 1);
            return typeName;
        }
    }
}
