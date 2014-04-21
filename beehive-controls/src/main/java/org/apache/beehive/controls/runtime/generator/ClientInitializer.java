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

/**
 * The ClientInitializer represents a generated class that contains the code
 * necessary to initialize a client that uses controls declaratively (via Control and
 * EventHandler annotations).
 */
public class ClientInitializer
{
    /**
     * Constructs a new ClientInitializer class
     * @param controlClient the control client this initializer will target
     */
    protected ClientInitializer(AptControlClient controlClient)
    {
        super();

        assert controlClient != null;

        _controlClient = controlClient;
        _packageName = _controlClient.getPackage();
        _shortName = _controlClient.getShortName() + "ClientInitializer";
        _className = isRootPackage() ? _shortName : _packageName + "." + _shortName;

        //
        // Compute the list of impl fields that will require reflected Fields.  This is
        // done unconditionally for all @Control fields (to support PropertyMap initialization)
        //

        _reflectFields = new ArrayList<AptField>();
        for (AptField genField : _controlClient.getControls())
            _reflectFields.add(genField);
    }

    /**
     * Returns the package name of the ClientInitializer
     */
    public String getPackage() { return _packageName; }

    /**
     * Is the ClientInitializer in the root package?
     */
    public boolean isRootPackage() { return getPackage() == null || getPackage().equals(""); }

    /**
     * Returns the unqualified classname of the ClientInitializer
     */
    public String getShortName() { return _shortName; }

    /**
     * Returns the fully qualfied classname of the ClientInitializer
     */
    public String getClassName() { return _className; }

    /**
     * Returns the ControlBean implementation instance
     */
    public AptControlClient getControlClient() { return _controlClient; }

    public ClientInitializer getSuperClass() { return null; }

    /**
     * Returns true if the initializer will use Reflection to initialize the field, false
     * otherwise.
     */
    static public boolean needsReflection(AptField genField)
    {
        //
        // Since initializers are generated into the same package as the initialized class,
        // only private access fields require reflection
        //
        String accessModifier = genField.getAccessModifier();
        if (accessModifier.equals("private"))
            return true;

        return false;
    }

    /**
     * Returns the list of impl class fields that must be initialized using Reflection
     */
    public ArrayList<AptField> getReflectFields()
    {
        return _reflectFields;
    }

    String _packageName;
    String _shortName;
    String _className;
    AptControlClient _controlClient;
    ArrayList<AptField> _reflectFields;
}
