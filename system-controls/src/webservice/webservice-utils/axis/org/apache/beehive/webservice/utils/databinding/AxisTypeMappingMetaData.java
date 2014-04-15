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
package org.apache.beehive.webservice.utils.databinding;

import java.util.HashMap;
import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.utils.JavaUtils;
import org.apache.axis.types.HexBinary;

/**
 * 
 */
public class AxisTypeMappingMetaData {

    private static HashMap<Class, QName> builtInTypes = new HashMap<Class, QName>();

    static {
        initBuildInTypes();
    }

    public static QName getBuiltInTypeQname(Class cls) {
        return builtInTypes.get(cls);
    }

    private static void initBuildInTypes() {
        // HexBinary binary data needs to use the hex binary serializer/deserializer
        addBuiltInType(Constants.XSD_HEXBIN, HexBinary.class);
        addBuiltInType(Constants.XSD_HEXBIN, byte[].class);

        addBuiltInType(Constants.XSD_BYTE, byte[].class);

        addBuiltInType(Constants.XSD_BASE64, byte[].class);

        // anySimpleType is mapped to java.lang.String according to JAX-RPC 1.1
        // spec.
        addBuiltInType(Constants.XSD_ANYSIMPLETYPE, java.lang.String.class);

        // If SOAP 1.1 over the wire, map wrapper classes to XSD primitives.
        addBuiltInType(Constants.XSD_STRING, java.lang.String.class);
        addBuiltInType(Constants.XSD_BOOLEAN, java.lang.Boolean.class);
        addBuiltInType(Constants.XSD_DOUBLE, java.lang.Double.class);
        addBuiltInType(Constants.XSD_FLOAT, java.lang.Float.class);
        addBuiltInType(Constants.XSD_INT, java.lang.Integer.class);
        addBuiltInType(Constants.XSD_INTEGER, java.math.BigInteger.class);
        addBuiltInType(Constants.XSD_DECIMAL, java.math.BigDecimal.class);
        addBuiltInType(Constants.XSD_LONG, java.lang.Long.class);
        addBuiltInType(Constants.XSD_SHORT, java.lang.Short.class);
        addBuiltInType(Constants.XSD_BYTE, java.lang.Byte.class);

        // The XSD Primitives are mapped to java primitives.
        addBuiltInType(Constants.XSD_BOOLEAN, boolean.class);
        addBuiltInType(Constants.XSD_DOUBLE, double.class);
        addBuiltInType(Constants.XSD_FLOAT, float.class);
        addBuiltInType(Constants.XSD_INT, int.class);
        addBuiltInType(Constants.XSD_LONG, long.class);
        addBuiltInType(Constants.XSD_SHORT, short.class);
        addBuiltInType(Constants.XSD_BYTE, byte.class);

        // Map QNAME to the jax rpc QName class
        addBuiltInType(Constants.XSD_QNAME, javax.xml.namespace.QName.class);

        // The closest match for anytype is Object
        addBuiltInType(Constants.XSD_ANYTYPE, java.lang.Object.class);

        // See the SchemaVersion classes for where the registration of
        // dateTime (for 2001) and timeInstant (for 1999 & 2000) happen.
        addBuiltInType(Constants.XSD_DATE, java.sql.Date.class);

        // See the SchemaVersion classes for where the registration of
        // dateTime (for 2001) and timeInstant (for 1999 & 2000) happen.
        addBuiltInType(Constants.XSD_DATETIME, java.util.Date.class);
        addBuiltInType(Constants.XSD_DATETIME, java.util.Calendar.class);

        // Mapping for xsd:time. Map to Axis type Time
        addBuiltInType(Constants.XSD_TIME, org.apache.axis.types.Time.class);
        // These are the g* types (gYearMonth, etc) which map to Axis types
        addBuiltInType(Constants.XSD_YEARMONTH, org.apache.axis.types.YearMonth.class);
        addBuiltInType(Constants.XSD_YEAR, org.apache.axis.types.Year.class);
        addBuiltInType(Constants.XSD_MONTH, org.apache.axis.types.Month.class);
        addBuiltInType(Constants.XSD_DAY, org.apache.axis.types.Day.class);
        addBuiltInType(Constants.XSD_MONTHDAY, org.apache.axis.types.MonthDay.class);

        // Serialize all extensions of Map to SOAP_MAP
        // Order counts here, HashMap should be last.
        addBuiltInType(Constants.SOAP_MAP, java.util.Hashtable.class);
        addBuiltInType(Constants.SOAP_MAP, java.util.Map.class);
        // The SOAP_MAP will be deserialized into a HashMap by default.
        addBuiltInType(Constants.SOAP_MAP, java.util.HashMap.class);

        // Use the Element Serializeration for elements
        addBuiltInType(Constants.SOAP_ELEMENT, org.w3c.dom.Element.class);

        // Use the Document Serializeration for Document's
        addBuiltInType(Constants.SOAP_DOCUMENT, org.w3c.dom.Document.class);

        addBuiltInType(Constants.SOAP_VECTOR, java.util.Vector.class);

        if (JavaUtils.isAttachmentSupported()) {
         addBuiltInType(Constants.MIME_IMAGE, java.awt.Image.class);
        // addBuiltInType(Constants.MIME_MULTIPART, javax.mail.internet.MimeMultipart.class);
        addBuiltInType(Constants.MIME_SOURCE, javax.xml.transform.Source.class);
        // addBuiltInType(Constants.MIME_OCTETSTREAM, OctetStream.class);
        // addBuiltInType(Constants.MIME_DATA_HANDLER, javax.activation.DataHandler.class);
        }

        addBuiltInType(Constants.XSD_TOKEN, org.apache.axis.types.Token.class);
        addBuiltInType(Constants.XSD_NORMALIZEDSTRING, org.apache.axis.types.NormalizedString.class);
        addBuiltInType(Constants.XSD_UNSIGNEDLONG, org.apache.axis.types.UnsignedLong.class);
        addBuiltInType(Constants.XSD_UNSIGNEDINT, org.apache.axis.types.UnsignedInt.class);
        addBuiltInType(Constants.XSD_UNSIGNEDSHORT, org.apache.axis.types.UnsignedShort.class);
        addBuiltInType(Constants.XSD_UNSIGNEDBYTE, org.apache.axis.types.UnsignedByte.class);
        addBuiltInType(Constants.XSD_NONNEGATIVEINTEGER, org.apache.axis.types.NonNegativeInteger.class);
        addBuiltInType(Constants.XSD_NEGATIVEINTEGER, org.apache.axis.types.NegativeInteger.class);
        addBuiltInType(Constants.XSD_POSITIVEINTEGER, org.apache.axis.types.PositiveInteger.class);
        addBuiltInType(Constants.XSD_NONPOSITIVEINTEGER, org.apache.axis.types.NonPositiveInteger.class);
        addBuiltInType(Constants.XSD_NAME, org.apache.axis.types.Name.class);
        addBuiltInType(Constants.XSD_NCNAME, org.apache.axis.types.NCName.class);
        addBuiltInType(Constants.XSD_ID, org.apache.axis.types.Id.class);
        addBuiltInType(Constants.XML_LANG, org.apache.axis.types.Language.class);
        addBuiltInType(Constants.XSD_LANGUAGE, org.apache.axis.types.Language.class);
        addBuiltInType(Constants.XSD_NMTOKEN, org.apache.axis.types.NMToken.class);
        addBuiltInType(Constants.XSD_NMTOKENS, org.apache.axis.types.NMTokens.class);
        addBuiltInType(Constants.XSD_NOTATION, org.apache.axis.types.Notation.class);
        addBuiltInType(Constants.XSD_ENTITY, org.apache.axis.types.Entity.class);
        addBuiltInType(Constants.XSD_ENTITIES, org.apache.axis.types.Entities.class);
        addBuiltInType(Constants.XSD_IDREF, org.apache.axis.types.IDRef.class);
        addBuiltInType(Constants.XSD_IDREFS, org.apache.axis.types.IDRefs.class);
        addBuiltInType(Constants.XSD_DURATION, org.apache.axis.types.Duration.class);
        addBuiltInType(Constants.XSD_ANYURI, org.apache.axis.types.URI.class);
        addBuiltInType(Constants.XSD_SCHEMA, org.apache.axis.types.Schema.class);
        addBuiltInType(Constants.SOAP_ARRAY, java.util.ArrayList.class);
    }

    private static void addBuiltInType(QName q, Class c) {
        builtInTypes.put(c, q);
    }
}
