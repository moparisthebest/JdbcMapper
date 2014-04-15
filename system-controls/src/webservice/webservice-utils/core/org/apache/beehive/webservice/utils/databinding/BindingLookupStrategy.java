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

import javax.xml.namespace.QName;

/*
 * There are varieties of XML to Java binding (XMLBeans, Axis, Castor, etc) implementations, each with its own nuances.
 * The TypeLookUpServices interface is intended to abstract the particular implementations strategy.
 * 
 * The qname to class mappings must  be predictable.  So that for a given class:
 * 		classA  = qname2Class(class2qname(ClassA)
 * And for a given QName:
 * 		qnameA = class2qname(qname2class(qnameA)
*/
public interface BindingLookupStrategy {

    QName class2qname(Class cls);

    QName class2qname(Class cls, String namespace);

    Class qname2class(QName qname);
}