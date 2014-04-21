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
package org.apache.beehive.controls.runtime.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.Date;

import org.apache.beehive.controls.api.bean.AnnotationMemberTypes;
import org.apache.beehive.controls.api.bean.AnnotationConstraints.MembershipRule;
import org.apache.beehive.controls.api.bean.AnnotationConstraints.MembershipRuleValues;
import org.apache.beehive.controls.api.properties.PropertyKey;

/**
 * This class offers methods for validating values assigned to a control property.
 * The validation process will ensure
 *    1. The value is appropriate for the property's property type
 *    2. The value satisfies the constraints defined on the property type
 *    3. The value satisfies the constraints defined on the property set that the property is defined in.
 * Refer to {@link org.apache.beehive.controls.api.bean.AnnotationMemberTypes AnnotationMemberTypes} and 
 * {@link org.apache.beehive.controls.api.bean.AnnotationConstraints AnnotationConstraints} for more 
 * information on property constraints.
 */
public class AnnotationConstraintValidator
{

    public AnnotationConstraintValidator()
    {
        super();
    }

    /**
     * This method ensures that any control property value assignment satisfies
     * all property constraints. This method should be called by control
     * property setters to ensure values assigned to properties at runtime are
     * validated.
     * 
     * @param key
     *            The property that the specified key is assigned to
     * @param value
     *            The value assigned to the specified property key
     * @throws IllegalArgumentException
     *             when the value assigned to the specified property key does
     *             not satisfy a property constraint.
     */
    public static void validate(PropertyKey key, Object value)
            throws IllegalArgumentException
    {
        validate(key.getAnnotations(), value);
    }

    /**
     * This method ensures the membership constraints defined on a property set
     * is satisfied.
     * 
     * @param propertySet the property set to validate
     */
    public static void validateMembership(Annotation propertySet)
    {
        Class c = propertySet.annotationType();
        MembershipRule rule = (MembershipRule) c.getAnnotation(MembershipRule.class);
        if (rule == null)
            return;
        MembershipRuleValues ruleValue = rule.value();
        String[] memberNames = rule.memberNames();
        Method[] members = getMembers(c, memberNames);
        int i = getNumOfMembersSet(propertySet, members);
        if (ruleValue == MembershipRuleValues.ALL_IF_ANY)
        {
            if (i != 0 && i != members.length) 
                throw new IllegalArgumentException("The membership rule on " + propertySet.toString() + 
                    " is not satisfied. Either all members must be set or none is set");
        }
        else if (ruleValue == MembershipRuleValues.EXACTLY_ONE)
        {
            if (i != 1)
                throw new IllegalArgumentException("The membership rule on " + propertySet.toString() + 
                	" is not satisfied. Exactly one member must be set");
        }
        else if (ruleValue == MembershipRuleValues.AT_LEAST_ONE)
        {
            if (i < 1)
                throw new IllegalArgumentException("The membership rule on " + propertySet.toString() + 
                	" is not satisfied. At least one member must be set");
        }
        else if (ruleValue == MembershipRuleValues.AT_MOST_ONE)
        {
            if (i > 1)
                throw new IllegalArgumentException("The membership rule on " + propertySet.toString() + 
                	" is not satisfied. At most one member may be set");
        }
    }

    private static Method[] getMembers(Class<? extends Annotation> c, String[] memberNames)
    {
        Method[] methods = null;
        if (memberNames == null || memberNames.length == 0)
        {
            methods = c.getDeclaredMethods();
        }
        else
        {
            methods = new Method[memberNames.length];
            for (int i = 0; i < memberNames.length; i++)
            {
                try
                {
                    methods[i] = c.getMethod(memberNames[i], (Class[]) null);
                }
                catch (Exception e)
                {
                    // method is not found, so the member is ignored.
                }
            }
        }
        return methods;
    }

    private static int getNumOfMembersSet(Annotation propertySet,
            Method[] members)
    {
        int num = 0;
        for (Method m : members)
        {
            Class returnType = m.getReturnType();
            Object o = null;
            try
            {
                o = m.invoke(propertySet, (Object[]) null);
            }
            catch (Exception e)
            {
                // This should never happen.
                throw new RuntimeException(e);
            }

            if ((returnType == String.class && !((String) o)
                    .equals(AnnotationMemberTypes.OPTIONAL_STRING))
                    || (returnType == int.class && ((Integer) o).intValue() != AnnotationMemberTypes.OPTIONAL_INT)
                    || (returnType == short.class && ((Short) o)
                            .shortValue() != AnnotationMemberTypes.OPTIONAL_SHORT)
                    || (returnType == long.class && ((Long) o).longValue() != AnnotationMemberTypes.OPTIONAL_LONG)
                    || (returnType == float.class && ((Float) o)
                            .floatValue() != AnnotationMemberTypes.OPTIONAL_FLOAT)
                    || (returnType == double.class && ((Double) o)
                            .doubleValue() != AnnotationMemberTypes.OPTIONAL_DOUBLE)
                    || (returnType == char.class && ((Character) o)
                            .charValue() != AnnotationMemberTypes.OPTIONAL_CHAR)
                    || (returnType == byte.class && ((Byte) o).byteValue() != AnnotationMemberTypes.OPTIONAL_BYTE)
                    || (returnType == boolean.class && !((Boolean) o)
                            .booleanValue()))
                	num++;
        }
        return num;
    }

    protected static synchronized void validate(Annotation[] annotations,
            Object value) throws IllegalArgumentException
    {

        // Determine if the member is optional. This is done in a separate loop
        // because a control property may have multiple constraints and the
        // optional
        // annotation may be declared after another constraint annotation.
        boolean optional = false;
        for (Annotation a : annotations)
        {
            if (a instanceof AnnotationMemberTypes.Optional)
            {
                optional = true;
                break;
            }
        }

        for (Annotation a : annotations)
        {
            if (a instanceof AnnotationMemberTypes.Text)
                validateText((AnnotationMemberTypes.Text) a, value, optional);
            else if (a instanceof AnnotationMemberTypes.Decimal)
                validateDecimal((AnnotationMemberTypes.Decimal) a, value,
                        optional);
            else if (a instanceof AnnotationMemberTypes.Int)
                validateInt((AnnotationMemberTypes.Int) a, value, optional);
            else if (a instanceof AnnotationMemberTypes.Date)
                validateDate((AnnotationMemberTypes.Date) a, value, optional);
            else if (a instanceof AnnotationMemberTypes.FilePath)
                validateFilePath((AnnotationMemberTypes.FilePath) a, value,
                        optional);
            else if (a instanceof AnnotationMemberTypes.JndiName)
                validateJndiName((AnnotationMemberTypes.JndiName) a, value,
                        optional);
            else if (a instanceof AnnotationMemberTypes.QName)
                validateQName((AnnotationMemberTypes.QName) a, value, optional);
            else if (a instanceof AnnotationMemberTypes.URI)
                validateURI((AnnotationMemberTypes.URI) a, value, optional);
            else if (a instanceof AnnotationMemberTypes.URL)
                validateURL((AnnotationMemberTypes.URL) a, value, optional);
            else if (a instanceof AnnotationMemberTypes.URN)
                validateURN((AnnotationMemberTypes.URN) a, value, optional);
            else if (a instanceof AnnotationMemberTypes.XML)
                validateXML((AnnotationMemberTypes.XML) a, value, optional);
        }
    }

    private static void validateXML(AnnotationMemberTypes.XML a, Object value,
            boolean optional)
    {
    }

    private static void validateURN(AnnotationMemberTypes.URN a, Object value,
            boolean optional)
    {
        if (optional
                && (value == null || value
                        .equals(AnnotationMemberTypes.OPTIONAL_STRING)))
            return;

        if (!(value instanceof String))
        {
            error("The value, "
                    + value
                    + ", assigned to an URN property must be of type java.lang.String.");
        }

        URI.create((String) value);
    }

    private static void validateURL(AnnotationMemberTypes.URL a, Object value,
            boolean optional)
    {
        if (optional
                && (value == null || value
                        .equals(AnnotationMemberTypes.OPTIONAL_STRING)))
            return;

        if (!(value instanceof String))
        {
            error("The value, "
                    + value
                    + ", assigned to an URL property must be of type java.lang.String.");
        }

        try
        {
            new URL((String) value);
        }
        catch (MalformedURLException mue)
        {
            error("The value, " + value
                    + ", assigned to the URL property is a malformed URL.", mue);
        }
    }

    private static void validateURI(AnnotationMemberTypes.URI a, Object value, boolean optional)
    {
        if (optional
                && (value == null || value
                        .equals(AnnotationMemberTypes.OPTIONAL_STRING)))
            return;

        if (!(value instanceof String))
        {
            error("The value, "
                    + value
                    + ", assigned to an URI property must be of type java.lang.String.");
        }

        URI.create((String) value);
    }

    private static void validateQName(AnnotationMemberTypes.QName a, Object value, boolean optional)
    {
    }

    private static void validateJndiName(AnnotationMemberTypes.JndiName a, Object value, boolean optional)
    {
    }

    private static void validateFilePath(AnnotationMemberTypes.FilePath a, Object value, boolean optional)
    {
        if (optional
                && (value == null || value
                        .equals(AnnotationMemberTypes.OPTIONAL_STRING)))
            return;

        if (!(value instanceof String))
        {
            error("The value, "
                    + value
                    + ", assigned to a FilePath property must be of type java.lang.String.");
        }

//Temporarily commenting out the following check on FilePath until
//an agreement is reached on what is a valid FilePath.
//
//        File file = new File((String) value);
//        if (!file.isFile() || !file.canRead())
//        {
//            error("The value, "
//                    + value
//                    + ", assigned to a FilePath property must be a readable file.");
//        }

    }

    private static void validateDate(AnnotationMemberTypes.Date a, Object value, boolean optional) {

        if (optional && (value == null || value.equals(AnnotationMemberTypes.OPTIONAL_STRING)))
            return;

        if (!(value instanceof String))
            error("The value, "
                + value
                + ", assigned to a date property must be of type java.lang.String.");

        String format = a.format();
        Date date = null;
        try {
            date = parseDate(format , (String)value);
        } catch (ParseException pe) {
            error("The value, "
                + value
                + ", assigned to a date property is not in the specified format of: "
                + format);
        }

        String minValue = a.minValue();
        if (minValue != null && minValue.length() > 0) {

            Date minDate = null;
            try {
                minDate = parseDate(format, a.minValue());
            } catch (ParseException pe) {
                error("The value, "
                    + minValue
                    + ", assigned to minValue date constraint property is not in the specified format of: "
                    + format);
            }

            if (minDate.compareTo(date) > 0) {
                    error("The value, "
                            + value
                            + ", assigned to a date property is earlier than the earliest date allowed: "
                            + minValue);
            }
        }

        String maxValue = a.maxValue();
        if (maxValue != null && maxValue.length() > 0) {

            Date maxDate = null;
            try {
                maxDate = parseDate(format, a.maxValue());
            } catch (ParseException pe) {
                error("The value, "
                    + maxValue
                    + ", assigned to maxValue date constraint property is not in the specified format of: "
                    + format);
            }

            if (maxDate.compareTo(date) < 0) {
                    error("The date, "
                            + value
                            + ", assigned to a date property is later than the latest date allowed: "
                            + maxValue);
            }
        }
    }

    /**
     * Parse a date value into the specified format.  Pay special attention to the case of the value
     * having trailing characters, ex. 12/02/2005xx which will not cause the parse of the date to fail
     * but should be still treated as an error for our purposes.
     *
     * @param format Format string for the date.
     * @param value A String containing the date value to parse.
     * @return A Date instance if the parse was successful.
     * @throws ParseException If the value is not a valid date.
     */
    public static Date parseDate(String format, String value)
        throws ParseException {

        SimpleDateFormat sdFormat = new SimpleDateFormat(format);
        sdFormat.setLenient(false);
        ParsePosition pp = new ParsePosition(0);
        Date d = sdFormat.parse(value, pp);

        /*
        a date value such as: 12/01/2005x will not cause a parse error,
        use the parse position to detect this case.
        */
        if (d == null || pp.getIndex() < value.length())
            throw new ParseException("Parsing date value, "
                + value
                + ", failed at index " + pp.getIndex(), pp.getIndex());
        else return d;
    }

    /**
     * @param value
     */
    private static void validateInt(AnnotationMemberTypes.Int a, Object value, boolean optional) {
        if (optional
                && (value == null ||
                        value.equals(AnnotationMemberTypes.OPTIONAL_STRING) ||
                        value.equals(AnnotationMemberTypes.OPTIONAL_INT)))
            return;

        int intValue = 0;

        if (value instanceof String)
        {
            try
            {
                intValue = Integer.parseInt((String) value);
            }
            catch (NumberFormatException nfe)
            {
                error("The value ,"
                        + value
                        + ", assigned to an int property does not represent an integer.");
            }
        }
        else if (value instanceof Integer)
        {
            intValue = ((Integer) value).intValue();
        }
        else
        {
            error("The value, "
                    + value
                    + ", assigned to an int property must be of type java.lang.String or int.");
        }

        if (intValue < a.minValue())
            error("The value, "
                    + intValue
                    + ", assigned to an int property is less than the minimum value allowed: "
                    + a.minValue() + ".");
        else if (intValue > a.maxValue())
            error("The value, "
                    + intValue
                    + ", assigned to an int property exeeds the maximum value allowed: "
                    + a.maxValue() + ".");
    }

    private static void validateDecimal(AnnotationMemberTypes.Decimal a,
            Object value, boolean optional)
    {
        if (optional
                && (value == null ||
                    value.equals(AnnotationMemberTypes.OPTIONAL_STRING) ||
                    value.equals(AnnotationMemberTypes.OPTIONAL_FLOAT) ||
                    value.equals(AnnotationMemberTypes.OPTIONAL_DOUBLE)))
            return;

        double doubleValue = 0;
        String doubleString = null;
        
        if (value instanceof String)
        {
            doubleValue = Double.parseDouble((String)value);
            doubleString = (String)value;
        }
        else if (value instanceof Float)
        {
            doubleValue = ((Float)value).doubleValue();
            doubleString = ((Float)value).toString();
        }
        else if (value instanceof Double)
        {
            doubleValue = ((Double)value).doubleValue();
            doubleString = ((Double)value).toString();
        }
        else
        {
            error("The value, "
                    + value
                    + ", assigned to a decimal property must be of type float, double, or java.lang.String.");
        }

        if (doubleValue < a.minValue())
            error("The value, "
                    + doubleValue
                    + ", assigned to a decimal property is less than the the minimum value allowed: "
                    + a.minValue() + ".");

        if (doubleValue > a.maxValue())
            error("The value, "
                    + doubleValue
                    + ", assigned to a decimal property exceeds the maximum value allowed: "
                    + a.maxValue() + ".");

        int decimalPos = doubleString.indexOf('.');

        if (decimalPos == -1 || a.places() == AnnotationMemberTypes.UNLIMITED_PLACES)
            return;

        if (doubleString.length() - decimalPos - 1 > a.places())
            error("The decimal places in the value, " + doubleString
                    + ", assigned to a decimal property exceeds " + a.places()
                    + ", the number of decimal places allowed.");

    }

    private static void validateText(AnnotationMemberTypes.Text a,
            Object value, boolean optional)
    {
        if (optional
                && (value == null || value
                        .equals(AnnotationMemberTypes.OPTIONAL_STRING)))
            return;

        if (!(value instanceof String))
            error("The value, "
                    + value
                    + ", assigned to a text property must be of type java.lang.String.");

        String str = (String) value;
        if (str.length() > a.maxLength())
            error("The value, "
                    + str
                    + ", assigned to a text property exceeds the maximum length allowed: "
                    + a.maxLength());

        if (a.isLong())
        {
            try
            {
                Long.parseLong(str);
            }
            catch (NumberFormatException nfe)
            {
                error("The value, "
                        + str
                        + ", assigned to a text property with a long number constraint does not represent a long number.");
            }
        }

    }

    private static void error(String message)
    {
        error(message, null);
    }

    private static void error(String message, Throwable t)
    {
        throw new IllegalArgumentException(message, t);
    }

}
