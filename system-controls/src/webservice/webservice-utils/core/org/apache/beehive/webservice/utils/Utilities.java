/*
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
 */
package org.apache.beehive.webservice.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.xb.xsdschema.SchemaDocument;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;

public class Utilities {

    // Keep a cache of schemas that have been read.
    private static Map<String, Schema> SCHEMA_CACHE = new HashMap<String, Schema>();

    public static DefinitionsDocument parseWSDL(String wsdlLocation)
            throws IOException, MalformedURLException, XmlException {
        if (wsdlLocation.indexOf("://") > 2) {
            return parseWSDL(new URL(wsdlLocation));
        } else {
            return parseWSDL(new File(wsdlLocation));
        }
    }

    public static DefinitionsDocument parseWSDL(File wsdlFile)
        throws IOException, XmlException {
        return DefinitionsDocument.Factory.parse(wsdlFile);
    }

    public static DefinitionsDocument parseWSDL(URL wsdlURL)
        throws IOException, XmlException {
        return DefinitionsDocument.Factory.parse(wsdlURL);
    }

    public static DefinitionsDocument parseWSDL(InputStream wsdlStream)
        throws IOException, XmlException {
        return DefinitionsDocument.Factory.parse(wsdlStream);
    }

    public static SchemaDocument parseSchema(InputStream stream)
        throws XmlException, IOException {
        return SchemaDocument.Factory.parse(stream);
    }

    public static <T extends XmlObject> T[] selectChildren(XmlObject parent, Class<T> childClass)
        throws IllegalAccessException, NoSuchFieldException {

        if (parent == null) {
            return (T[]) Array.newInstance(childClass, 0);
        }
        
        // retrieve the SchemaType from the static type field
        SchemaType st = (SchemaType) childClass.getField("type").get(null);
        XmlObject[] kids = parent.selectChildren(st.getDocumentElementName());
        T[] castKids = (T[]) Array.newInstance(childClass, kids.length);
        for (int j = 0; j < castKids.length; j++) {
            castKids[j] = childClass.cast(kids[j]);
        }
        return castKids;
    }

    public static Schema findtSchemaDocument(SchemaType docType)
        throws XmlException, IOException, IllegalAccessException, NoSuchFieldException {

        Schema mySchema = null;
        if(null != (mySchema = SCHEMA_CACHE.get(docType.getName().getNamespaceURI()))) {
            return mySchema;
        }

        String schemaSrc = docType.getSourceName();
        InputStream stream = null;
        try {
            stream = docType.getTypeSystem().getSourceAsStream(schemaSrc);

            if (null == stream)
                throw new RuntimeException("WSDL file not found: " + schemaSrc);

            if (schemaSrc.toLowerCase().endsWith(".wsdl"))
                mySchema = new WSDLParser(parseWSDL(stream)).getSchema();
            else mySchema = new Schema(parseSchema(stream));
        }
        finally {
            if (null != stream)
                stream.close();
        }

        SCHEMA_CACHE.put(docType.getName().getNamespaceURI(), mySchema);
        return mySchema;
    }
}
