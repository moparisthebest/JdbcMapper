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

import org.xmlsoap.schemas.wsdl.DefinitionsDocument;
import org.xmlsoap.schemas.wsdl.TPort;
import org.xmlsoap.schemas.wsdl.TService;
import org.xmlsoap.schemas.wsdl.TTypes;
import org.xmlsoap.schemas.wsdl.soap.TAddress;

/* todo: move this into the webservice control source root */
public class WSDLParser {

    private DefinitionsDocument _defDoc;
    private TTypes _tt;
    private Schema _schema;
    private TService[] _services;

    public WSDLParser(DefinitionsDocument defDoc)
        throws IllegalAccessException, NoSuchFieldException {

        _defDoc = defDoc;

        TTypes[] typesArray = defDoc.getDefinitions().getTypesArray();
        if (typesArray != null && typesArray.length > 0) {
            _tt = typesArray[0];
        }

        _schema = new Schema(_tt);
        _services = defDoc.getDefinitions().getServiceArray();
    }

    /**
     * @return Returns the schema.
     */
    public Schema getSchema() {
        return _schema;
    }

    public String getSoapAddressLocation()
        throws IllegalAccessException {
        String location = null;
        if(_services.length > 0) {
            TPort port = _services[0].getPortArray(0);
            if(null != port) {
                try {
                    TAddress[] soapAddress = Utilities.selectChildren(port, TAddress.class);
                    if(soapAddress.length > 0) {
                        location = soapAddress[0].getLocation();
                    }
                }
                catch(NoSuchFieldException e) {
                    // just return null
                }
            }
        }
        return location;
    }
}
