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
package org.apache.beehive.controls.api.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>AnnotationMemberTypes defines a set of annotations meant to used on
 * annotation members to specify additional syntatic and semantic behaviour
 * or constraints.</p>
 *
 * <p>J2SE 5 annotation members provide a very weak level of syntactic and
 * semantic enforcement.  Annotation members may only be a certain type
 * (mostly primitives, arrays, plus java.lang.String and a few other classes);
 * it is often useful to be more specific than those types permit.</p>
 *
 * <p>Consider the following example:</p>
 * 
 * <pre>
 * public &#064;interface LastChanged
 * {
 *     &#064;AnnotationMemberTypes.Date()
 *     public String date();
 * }
 * </pre>
 *
 * <p>The use of <code>&#064;AnnotationMemberTypes.Date</code> means that the
 * value of the <code>date</code> string must be a date in some standard
 * form.</p>
 *
 * <p>AnnotationMemberTypes defines a set of annotations and their semantics,
 * but actual enforcement of those semantics is implementation dependent.
 * An <code>apt</code>-based reference implementation is provided by
 * {@link org.apache.beehive.controls.runtime.bean.AnnotationConstraintValidator}.</p>
 *
 * @see org.apache.beehive.controls.runtime.bean.AnnotationConstraintValidator
 */
public interface AnnotationMemberTypes
{
    public final static String OPTIONAL_STRING = "";
    public final static double OPTIONAL_DOUBLE = Double.MIN_VALUE;
    public final static float  OPTIONAL_FLOAT  = Float.MIN_VALUE;
    public final static int    OPTIONAL_INT    = Integer.MIN_VALUE;
    public final static long   OPTIONAL_LONG   = Long.MIN_VALUE;
    public final static short  OPTIONAL_SHORT  = Short.MIN_VALUE;
    public final static char   OPTIONAL_CHAR   = Character.MIN_VALUE;
    public final static byte   OPTIONAL_BYTE   = Byte.MIN_VALUE;
    public final static int    UNLIMITED_PLACES = -1;

    /**
     * Marks a member as optional.  Member must have
     * a default value.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Optional
    {
    }

    /**
     * Member must be a String value.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Text
    {
        boolean isLong() default false;
        int maxLength()  default Integer.MAX_VALUE;
    }

    /**
     * Member is a Decimal Value.
     * Can be applied to a member that returns float, double or String.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Decimal
    {
        int places()      default UNLIMITED_PLACES;
        double minValue() default Double.MIN_VALUE;
        double maxValue() default Double.MAX_VALUE;
    }

    /**
     * Member is an Integer value.
     * Can be applied to a member that returns String or int.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Int
    {
        int minValue() default  Integer.MIN_VALUE;
        int maxValue() default Integer.MAX_VALUE;
    }

    /**
     * Member is a Date in the format specified (default is YYYY/MM/DD)
     * Only valid on a member that returns String
     * @see java.text.SimpleDateFormat when selecting another date format.
     * Note: JSR175 does not allow java.util.Date as
     * a member type.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Date
    {
    	String format() default "yyyy/MM/dd";
        String minValue() default "";
        String maxValue() default "";
    }

    /**
     * Member is a URI
     * Only valid on a member that returns String
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface URI
    {
    }

    /**
     * Member is a URN
     * Only valid on a member that returns String
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface URN
    {
    }

    /**
     * Member is a URL
     * Only valid on a member that returns String
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface URL
    {
    }

    /**
     * Member is a QName
     * Only valid on a member that returns String
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface QName
    {
    }

    /**
     * Member contains well formed XML
     * Only valid on a member that returns String
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface XML
    {
    }

    /**
     * Member is a File Path
     * Compiler MUST validate that value points
     * to a <code>readable</code> file.
     * Only valid on a member that returns String.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface FilePath
    {
    }

    /**
     * Member is a JNDI name.
     */
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface JndiName
    {
        /**
         * Defines the type of JNDI resource reference by a member.
         */
        public enum ResourceType
        {
            DATASOURCE,
            EJB,
            JMS_TOPIC,
            JMS_QUEUE ,
            OTHER
        }

        ResourceType resourceType();
    }
}
