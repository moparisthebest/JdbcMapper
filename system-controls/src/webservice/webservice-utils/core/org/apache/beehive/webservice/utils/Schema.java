/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 */
package org.apache.beehive.webservice.utils;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelComplexType;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelElement;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelSimpleType;
import org.xmlsoap.schemas.wsdl.TTypes;

public class Schema {

    private SchemaDocument.Schema[] _schemas;

    /**
     */
    public Schema(TTypes tt)
        throws IllegalAccessException, NoSuchFieldException {
        _schemas = Utilities.selectChildren(tt, SchemaDocument.Schema.class);
    }

    public Schema(SchemaDocument schemaDoc) {
        _schemas = new SchemaDocument.Schema[] {schemaDoc.getSchema()};
    }

    public XmlObject getTypeNode(QName q) {
        // first find the schema with matching namespace
        SchemaDocument.Schema schema = null;
        for (SchemaDocument.Schema nxtSchema : _schemas) {
            if (nxtSchema.getTargetNamespace() != null
                && nxtSchema.getTargetNamespace().equals(
                q.getNamespaceURI())) {
                schema = nxtSchema;
                break;
            }
        }
        if (null == schema)
            return null; // namespace is not found in this schema.

        // look in complex types
        TopLevelComplexType[] tlComplexTypes = schema.getComplexTypeArray();
        for (TopLevelComplexType nxtComplexType : tlComplexTypes) {
            if (nxtComplexType.getName().equals(q.getLocalPart())) {
                return nxtComplexType;
            }
        }

        // look in simple types
        TopLevelSimpleType[] tlSimpleTypes = schema.getSimpleTypeArray();
        for (TopLevelSimpleType nxtSimpleType : tlSimpleTypes) {
            if (nxtSimpleType.getName().equals(q.getLocalPart())) {
                return nxtSimpleType;
            }
        }

        // look in element types
        TopLevelElement[] tlElementTypes = schema.getElementArray();
        for (TopLevelElement nxtElement : tlElementTypes) {
            if (nxtElement.getName().equals(q.getLocalPart())) {
                return nxtElement;
            }
        }

        return null;  // it is not in comlex or simple types!
    }
}