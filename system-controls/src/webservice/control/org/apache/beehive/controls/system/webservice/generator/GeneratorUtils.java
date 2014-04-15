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
 */
package org.apache.beehive.controls.system.webservice.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Utilities for service control generation.
 */
final class GeneratorUtils {
    private static final char JAVA_RESERVEDWORD_PREFIX = '_';
    private static final char[] PKG_SEPARATORS = {'.', ':'};

    /**
     * List of java reserved words.  Must be kept sorted in ascending order.
     */
    private static final String JAVA_RESERVED_WORDS[] =
    {
        "abstract",   "assert",     "boolean",      "break",        "byte",
        "case",       "catch",      "char",         "class",        "const",
        "continue",   "default",    "do",           "double",       "else",
        "enum",       "extends",    "false",        "final",        "finally",
        "float",      "for",        "goto",         "if",           "implements",
        "import",     "instanceof", "int",          "interface",    "long",
        "native",     "new",        "null",         "package",      "private",
        "protected",  "public",     "return",       "short",        "static",
        "strictfp",   "super",      "switch",       "synchronized", "this",
        "throw",      "throws",     "transient",    "true",         "try",
        "void",       "volatile",   "while"
    };


    /**
     * List of java.lang classes (1.5 JDK).
     */
    private final static Set<String> JAVA_LANG_NAMES = new HashSet<String>(Arrays.asList(
        new String[]
          {
            // Interfaces
            "Appendable", "CharSequence", "Cloneable", "Comparable",
            "Iterable",   "Readable",     "Runnable",

            // Classes
            "Boolean",         "Byte",                   "Character",          "Class",
            "ClassLoader",     "Compiler",               "Double",             "Enum",
            "Float",           "InheritableThreadLocal", "Integer",            "Long",
            "Math",            "Number",                 "Object",             "Package",
            "Process",         "ProcessBuilder",         "Runtime",            "RuntimePermission",
            "SecurityManager", "Short",                  "StackTraceElement",  "StrictMath",
            "String",          "StringBuffer",           "StringBuilder",      "System",
            "Thread",          "ThreadGroup",            "ThreadLocal",        "Throwable",
            "Void",

            // Exceptions
            "ArithmeticException",             "ArrayIndexOutOfBoundsException", "ArrayStoreException",
            "ClassCastException",              "ClassNotFoundException",         "CloneNotSupportedException",
            "EnumConstantNotPresentException", "Exception",                      "IllegalAccessException",
            "IllegalArgumentException",        "IllegalMonitorStateException",   "IllegalStateException",
            "IllegalThreadStateException",     "IndexOutOfBoundsException",      "InstantiationException",
            "InterruptedException",            "NegativeArraySizeException",     "NoSuchFieldException",
            "NoSuchMethodException",           "NullPointerException",           "NumberFormatException",
            "RuntimeException",                "SecurityException",              "StringIndexOutOfBoundsException",
            "TypeNotPresentException",         "UnsupportedOperationException",

            // Errors
            "AbstractMethodError",  "AssertionError",               "ClassCircularityError",
            "ClassFormatError",     "Error",                        "ExceptionInInitializerError",
            "IllegalAccessError",   "IncompatibleClassChangeError", "InstantiationError",
            "InternalError",        "LinkageError",                 "NoClassDefFoundError",
            "NoSuchFieldError",     "NoSuchMethodError",            "OutOfMemoryError",
            "StackOverflowError",   "ThreadDeath",                  "UnknownError",
            "UnsatisfiedLinkError", "UnsupportedClassVersionError", "VerifyError",
            "VirtualMachineError",

            // Annotation types
            "Deprecated", "Override", "SuppressWarnings"
          }
    ));

    /**
     * Is the value of checkString a java reserved word?
     *
     * @param checkString String value to check.
     * @return true if checkString exactly matches a java reserved word
     */
    private static boolean isJavaReservedWord(String checkString) {
        return Arrays.binarySearch(JAVA_RESERVED_WORDS, checkString) >= 0;
    }

    /**
     * Transform an invalid java identifier into a valid one.  Any invalid
     * characters are replaced with '_'s.
     *
     * @param id The invalid java identifier.
     * @return The transformed java identifier.
     */
    static String transformInvalidJavaIdentifier(String id) {
        if (id == null)
            throw new IllegalArgumentException("id cannot be null");

        final int len = id.length();
        if (len == 0)
            return "_";

        //
        // begin the transform
        //

        StringBuilder transformed = new StringBuilder(id);
        if (isJavaReservedWord(id) || JAVA_LANG_NAMES.contains(id)) {
            transformed.insert(0, JAVA_RESERVEDWORD_PREFIX);
        }

        if (!Character.isJavaIdentifierStart(transformed.charAt(0))) {
            transformed.insert(0, JAVA_RESERVEDWORD_PREFIX);
        }

        for (int i = 1; i < transformed.length(); i++) {
            if (!Character.isJavaIdentifierPart(transformed.charAt(i))) {
                transformed.replace(i, i + 1, "" + JAVA_RESERVEDWORD_PREFIX);
            }
        }

        return transformed.toString();
    }

    /**
     * Check the identifer to see if it is a valid Java identifier.
     *
     * @param id The Java identifier to check.
     * @return true if identifer is valid.
     */
    static boolean isValidJavaIdentifier(String id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }

        final int len = id.length();
        if (len == 0) {
            return false;
        }

        if (isJavaReservedWord(id) || JAVA_LANG_NAMES.contains(id)) {
            return false;
        }

        if (!Character.isJavaIdentifierStart(id.charAt(0))) {
            return false;
        }

        for (int i = 1; i < len; i++) {
            if (!Character.isJavaIdentifierPart(id.charAt(i)))
                return false;
        }

        return true;
    }

    /**
     * Convert a java package name to a file directory name.
     *
     * @param packageName Package name to convert.
     * @return The converted package name, empty string if packageName was null.
     */
    static String packageNameToDirectoryName(String packageName) {
        String dir;

        if (packageName != null) {
            for (char pkgSeparator : PKG_SEPARATORS) {
                packageName = packageName.replace(pkgSeparator, File.separatorChar);
            }
        }

        dir = packageName;
        return (dir == null) ? "" : dir + File.separatorChar;
    }

    /**
     * Generate a Java class name from a service name.
     *
     * @param serviceName Service name.
     * @return String.
     */
    static String serviceNameToClassName(String serviceName) {
        char[] chars = serviceName.toCharArray();
        StringBuilder className = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0) {
                className.append(Character.toUpperCase(chars[0]));
            }
            else {
                if (chars[i] == '.' || chars[i] == ' ') {
                    className.append(Character.toUpperCase(chars[++i]));
                }
                else {
                    className.append(chars[i]);
                }
            }
        }
        return className.toString();
    }

    /**
     * Generate a java package name from the specified namespace.
     *
     * @param namespace Namespace to generate package name from.
     * @return String
     */
    static String generatePackageName(String namespace) {

        String hostname;
        String path = "";

        try {
            URL u = new URL(namespace);
            hostname = u.getHost();
            path = u.getPath();
        }
        catch (MalformedURLException e) {
            if (namespace.indexOf(":") > -1) {
                hostname = namespace.substring(namespace.indexOf(":") + 1);

                if (hostname.indexOf("/") > -1) {
                    hostname = hostname.substring(0, hostname.indexOf("/"));
                }
            }
            else {
                hostname = namespace;
            }
        }

        if (hostname == null) {
            return null;
        }

        // check for and replace any illegal java identifier's
        hostname = hostname.replace('-', '_');
        path = path.replace('-', '_');

        // remove last forward slash in path, if necessary
        if ((path.length() > 0) && (path.charAt(path.length() - 1) == '/')) {
            path = path.substring(0, path.length() - 1);
        }

        // tokenize the hostname and reverse it
        StringTokenizer st = new StringTokenizer(hostname, ".:");
        String[] words = new String[st.countTokens()];
        for (int i = 0; i < words.length; ++i) {
            words[i] = st.nextToken();
        }

        StringBuilder sb = new StringBuilder(namespace.length());
        for (int i = words.length - 1; i >= 0; --i) {
            addToPackageName(sb, words[i], (i == words.length - 1));
        }

        // tokenize the path
        StringTokenizer st2 = new StringTokenizer(path, "/");
        while (st2.hasMoreTokens()) {
            addToPackageName(sb, st2.nextToken(), false);
        }
        return sb.toString();
    }

    /**
     * Copy src file to dest file.
     *
     * @param src  URL of the source file.
     * @param dest Destination file.
     * @throws IOException On error.
     */
    static void copyFile(URL src, File dest) throws IOException {
        InputStream is = src.openStream();
        OutputStream os = new FileOutputStream(dest);

        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) > 0) {
            os.write(buf, 0, len);
        }
        is.close();
        os.close();
    }

    /**
     * Generate a name for a WSDL file based on a URL which specifies the location
     * of the WSDL.
     *
     * @param wsdlURL URL to WSDL.
     * @return File name for the WSDL.
     */
    static String genWsdlFileNameFromURL(URL wsdlURL) {
        String urlPath = wsdlURL.getPath();
        int idx = urlPath.lastIndexOf('/') + 1;
        int dotIdx = urlPath.lastIndexOf('.');

        if (dotIdx > idx) {
            return urlPath.substring(idx, dotIdx) + ".wsdl";
        }
        else {
            return urlPath.substring(idx) + ".wsdl";
        }
    }

    /**
     * Add a new segement to a package name. Be sure to check that seqement is not
     * an java reserved word, if it is transform it into something safe.
     *
     * @param sb        the buffer to append to
     * @param id        the word to append
     * @param firstWord a flag indicating whether this is the first word
     */
    private static void addToPackageName(StringBuilder sb, String id, boolean firstWord) {

        if (isJavaReservedWord(id)) {
            id = transformInvalidJavaIdentifier(id);
        }

        // separate with dot after the first word
        if (!firstWord) {
            sb.append('.');
        }

        // prefix digits with underscores
        if (Character.isDigit(id.charAt(0))) {
            sb.append('_');
        }

        // replace periods with underscores
        if (id.indexOf('.') != -1) {
            id = id.replace('.', '_');
        }
        sb.append(id);
    }
}
