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

import java.util.LinkedList;
import java.util.List;

/**
 * Method info for the Velocity engine.
 */
public final class MethodInfo {

    private LinkedList<ParameterInfo> _params;
    private String _methodName;
    private String _returnTypeName;
    private String _operationName;

    /**
     * Create a new Method info instance.
     */
    MethodInfo() {
        _params = new LinkedList<ParameterInfo>();
    }

    /**
     * Set the name of the wsc method.
     *
     * @param methodName Name of wsc method.
     */
    void setMethodName(String methodName) {

        // make sure the method name starts with a lowercase character
        char[] m = methodName.toCharArray();
        if (Character.isUpperCase(m[0])) {
            m[0] = Character.toLowerCase(m[0]);
            methodName = new String(m);
        }

        if (!GeneratorUtils.isValidJavaIdentifier(methodName)) {
            _methodName = GeneratorUtils.transformInvalidJavaIdentifier(methodName);
            System.out.println("Warning: method name " + methodName
                    + " is not a valid Java method name, changing to: " + _methodName);
        }
        else {
            _methodName = methodName;
        }

    }

    /**
     * Set the operation annotation's name, should be the same as the operation name in the WSDL.
     *
     * @param operationName Operation name.
     */
    void setOperationName(String operationName) {
        _operationName = operationName;
    }

    /**
     * Add a parameter to the method's parameter list (parameter mode is IN).
     *
     * @param name     The parameter name.
     * @param typeName The parameter class.
     */
    void addParameter(String name, String typeName) {
        _params.add(new ParameterInfo(name, _params.size(), typeName, ParameterInfo.ParamMode.IN));
    }

    /**
     * Add a parameter to the method's parameter list (parameter mode is OUT / INOUT).
     *
     * @param name     The parameter name.
     * @param typeName The parameter class.
     */
    void addOutParameter(String name, String typeName) {
        _params.add(new ParameterInfo(name, _params.size(), typeName, ParameterInfo.ParamMode.OUT));
    }

    /**
     * Get the name of this method.
     *
     * @return String
     */
    public String getMethodName() {
        return _methodName;
    }

    /**
     * Set the name of the return type.
     *
     * @param returnType
     */
    void setReturnTypeName(String returnType) {
        _returnTypeName = returnType;
        if (_returnTypeName.startsWith("java.lang.")) {
            _returnTypeName = _returnTypeName.substring(_returnTypeName.lastIndexOf('.') + 1);
        }
    }

    /**
     * Get the operation name this method corresponds to in the WSDL.
     *
     * @return String
     */
    public String getOperationName() {
        return _operationName;
    }

    /**
     * Get the return class name of this method.
     *
     * @return String
     */
    public String getReturnTypeName() {
        return _returnTypeName;
    }

    /**
     * Get the parameter list for this method.
     *
     * @return A List of ParameterInfo, if method has no params returns empty list.
     */
    public List<ParameterInfo> getParams() {
        return _params;
    }
}
