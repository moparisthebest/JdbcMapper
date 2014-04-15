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
package org.apache.beehive.webservice.utils.databinding;

public class AxisBindingLookupFactory
    extends BindingLookupFactory {

    private static Class XML_OBJECT = null;
    static {
        try {
            XML_OBJECT = Class.forName("org.apache.xmlbeans.XmlObject");
        }
        catch (ClassNotFoundException e) {
            // not an error, xmlbeans is not present
        }
    }

    public BindingLookupStrategy getInstance() {
        BindingLookupChain chain = new BindingLookupChain();
        if (XML_OBJECT != null) {
            chain.addBindingLookupService(new XmlBeanLookupStrategy());
        }
        chain.addBindingLookupService(new AxisLookupStrategy());
        return chain;
    }
}