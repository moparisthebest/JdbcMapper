/**
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 $Header:$
 */
package org.apache.beehive.webservice.utils.databinding;

import javax.xml.namespace.QName;

/**
 *
 */
public interface TypeMappingConstants {

    public static final String NS_URI_XML = "http://www.w3.org/XML/1998/namespace";

    public static final String URI_1999_SCHEMA_XSD = "http://www.w3.org/1999/XMLSchema";
    public static final String URI_2000_SCHEMA_XSD = "http://www.w3.org/2000/10/XMLSchema";
    public static final String URI_2001_SCHEMA_XSD = "http://www.w3.org/2001/XMLSchema";
    public static final String URI_DEFAULT_SCHEMA_XSD = URI_2001_SCHEMA_XSD;

    public static final String URI_SOAP11_ENC = "http://schemas.xmlsoap.org/soap/encoding/";
    public static final String URI_SOAP12_ENC = "http://www.w3.org/2003/05/soap-encoding";
    public static final String URI_DEFAULT_SOAP_ENC = URI_SOAP11_ENC;

    public static final String NS_URI_XMLSOAP = "http://xml.apache.org/xml-soap";

    public static final QName XSD_STRING = new QName(URI_DEFAULT_SCHEMA_XSD, "string");
    public static final QName XSD_BOOLEAN = new QName(URI_DEFAULT_SCHEMA_XSD, "boolean");
    public static final QName XSD_DOUBLE = new QName(URI_DEFAULT_SCHEMA_XSD, "double");
    public static final QName XSD_FLOAT = new QName(URI_DEFAULT_SCHEMA_XSD, "float");
    public static final QName XSD_INT = new QName(URI_DEFAULT_SCHEMA_XSD, "int");
    public static final QName XSD_INTEGER = new QName(URI_DEFAULT_SCHEMA_XSD, "integer");
    public static final QName XSD_LONG = new QName(URI_DEFAULT_SCHEMA_XSD, "long");
    public static final QName XSD_SHORT = new QName(URI_DEFAULT_SCHEMA_XSD, "short");
    public static final QName XSD_BYTE = new QName(URI_DEFAULT_SCHEMA_XSD, "byte");
    public static final QName XSD_DECIMAL = new QName(URI_DEFAULT_SCHEMA_XSD, "decimal");
    public static final QName XSD_BASE64 = new QName(URI_DEFAULT_SCHEMA_XSD, "base64Binary");
    public static final QName XSD_HEXBIN = new QName(URI_DEFAULT_SCHEMA_XSD, "hexBinary");
    public static final QName XSD_ANYSIMPLETYPE = new QName(URI_DEFAULT_SCHEMA_XSD, "anySimpleType");
    public static final QName XSD_ANYTYPE = new QName(URI_DEFAULT_SCHEMA_XSD, "anyType");
    public static final QName XSD_ANY = new QName(URI_DEFAULT_SCHEMA_XSD, "any");
    public static final QName XSD_QNAME = new QName(URI_DEFAULT_SCHEMA_XSD, "QName");
    public static final QName XSD_DATETIME = new QName(URI_DEFAULT_SCHEMA_XSD, "dateTime");
    public static final QName XSD_DATE = new QName(URI_DEFAULT_SCHEMA_XSD, "date");
    public static final QName XSD_TIME = new QName(URI_DEFAULT_SCHEMA_XSD, "time");
    public static final QName XSD_TIMEINSTANT1999 = new QName(URI_1999_SCHEMA_XSD, "timeInstant");
    public static final QName XSD_TIMEINSTANT2000 = new QName(URI_2000_SCHEMA_XSD, "timeInstant");

    public static final QName XSD_NORMALIZEDSTRING = new QName(URI_2001_SCHEMA_XSD, "normalizedString");
    public static final QName XSD_TOKEN = new QName(URI_2001_SCHEMA_XSD, "token");

    public static final QName XSD_UNSIGNEDLONG = new QName(URI_2001_SCHEMA_XSD, "unsignedLong");
    public static final QName XSD_UNSIGNEDINT = new QName(URI_2001_SCHEMA_XSD, "unsignedInt");
    public static final QName XSD_UNSIGNEDSHORT = new QName(URI_2001_SCHEMA_XSD, "unsignedShort");
    public static final QName XSD_UNSIGNEDBYTE = new QName(URI_2001_SCHEMA_XSD, "unsignedByte");
    public static final QName XSD_POSITIVEINTEGER = new QName(URI_2001_SCHEMA_XSD, "positiveInteger");
    public static final QName XSD_NEGATIVEINTEGER = new QName(URI_2001_SCHEMA_XSD, "negativeInteger");
    public static final QName XSD_NONNEGATIVEINTEGER = new QName(URI_2001_SCHEMA_XSD, "nonNegativeInteger");
    public static final QName XSD_NONPOSITIVEINTEGER = new QName(URI_2001_SCHEMA_XSD, "nonPositiveInteger");

    public static final QName XSD_YEARMONTH = new QName(URI_2001_SCHEMA_XSD, "gYearMonth");
    public static final QName XSD_MONTHDAY = new QName(URI_2001_SCHEMA_XSD, "gMonthDay");
    public static final QName XSD_YEAR = new QName(URI_2001_SCHEMA_XSD, "gYear");
    public static final QName XSD_MONTH = new QName(URI_2001_SCHEMA_XSD, "gMonth");
    public static final QName XSD_DAY = new QName(URI_2001_SCHEMA_XSD, "gDay");
    public static final QName XSD_DURATION = new QName(URI_2001_SCHEMA_XSD, "duration");

    public static final QName XSD_NAME = new QName(URI_2001_SCHEMA_XSD, "Name");
    public static final QName XSD_NCNAME = new QName(URI_2001_SCHEMA_XSD, "NCName");
    public static final QName XSD_NMTOKEN = new QName(URI_2001_SCHEMA_XSD, "NMTOKEN");
    public static final QName XSD_NMTOKENS = new QName(URI_2001_SCHEMA_XSD, "NMTOKENS");
    public static final QName XSD_NOTATION = new QName(URI_2001_SCHEMA_XSD, "NOTATION");
    public static final QName XSD_ENTITY = new QName(URI_2001_SCHEMA_XSD, "ENTITY");
    public static final QName XSD_ENTITIES = new QName(URI_2001_SCHEMA_XSD, "ENTITIES");
    public static final QName XSD_IDREF = new QName(URI_2001_SCHEMA_XSD, "IDREF");
    public static final QName XSD_IDREFS = new QName(URI_2001_SCHEMA_XSD, "IDREFS");
    public static final QName XSD_ANYURI = new QName(URI_2001_SCHEMA_XSD, "anyURI");
    public static final QName XSD_LANGUAGE = new QName(URI_2001_SCHEMA_XSD, "language");
    public static final QName XSD_ID = new QName(URI_2001_SCHEMA_XSD, "ID");
    public static final QName XSD_SCHEMA = new QName(URI_2001_SCHEMA_XSD, "schema");

    public static final QName XML_LANG = new QName(NS_URI_XML, "lang");

    public static final QName SOAP_BASE64 = new QName(URI_DEFAULT_SOAP_ENC, "base64");
    public static final QName SOAP_BASE64BINARY = new QName(URI_DEFAULT_SOAP_ENC, "base64Binary");
    public static final QName SOAP_STRING = new QName(URI_DEFAULT_SOAP_ENC, "string");
    public static final QName SOAP_BOOLEAN = new QName(URI_DEFAULT_SOAP_ENC, "boolean");
    public static final QName SOAP_DOUBLE = new QName(URI_DEFAULT_SOAP_ENC, "double");
    public static final QName SOAP_FLOAT = new QName(URI_DEFAULT_SOAP_ENC, "float");
    public static final QName SOAP_INT = new QName(URI_DEFAULT_SOAP_ENC, "int");
    public static final QName SOAP_LONG = new QName(URI_DEFAULT_SOAP_ENC, "long");
    public static final QName SOAP_SHORT = new QName(URI_DEFAULT_SOAP_ENC, "short");
    public static final QName SOAP_BYTE = new QName(URI_DEFAULT_SOAP_ENC, "byte");
    public static final QName SOAP_INTEGER = new QName(URI_DEFAULT_SOAP_ENC, "integer");
    public static final QName SOAP_DECIMAL = new QName(URI_DEFAULT_SOAP_ENC, "decimal");
    public static final QName SOAP_ARRAY = new QName(URI_DEFAULT_SOAP_ENC, "Array");
    public static final QName SOAP_COMMON_ATTRS11 = new QName(URI_SOAP11_ENC, "commonAttributes");
    public static final QName SOAP_COMMON_ATTRS12 = new QName(URI_SOAP12_ENC, "commonAttributes");
    public static final QName SOAP_ARRAY_ATTRS11 = new QName(URI_SOAP11_ENC, "arrayAttributes");
    public static final QName SOAP_ARRAY_ATTRS12 = new QName(URI_SOAP12_ENC, "arrayAttributes");
    public static final QName SOAP_ARRAY12 = new QName(URI_SOAP12_ENC, "Array");

    // unsupported since they're bound to Axis
    /*
    private static final QName SOAP_MAP = new QName(NS_URI_XMLSOAP, "Map");
    private static final QName SOAP_ELEMENT = new QName(NS_URI_XMLSOAP, "Element");
    private static final QName SOAP_DOCUMENT = new QName(NS_URI_XMLSOAP, "Document");
    private static final QName SOAP_VECTOR = new QName(NS_URI_XMLSOAP, "Vector");
    */
}
