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
package org.apache.beehive.netui.test.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

class TestUtil {

    /* do not construct */
    private TestUtil() {
    }

    // read and process the warningorerror file contents into a set of Strings.
    public static Set<String> processWarningOrErrorContent(File file,
                                                           boolean localize,
                                                           boolean trim,
                                                           boolean removeXMLComment,
                                                           boolean removeJpfLineNumbers)
        throws PageFlowCompilerTestException {

        Set stringSet = null;
        StringBuffer fileInString = new StringBuffer();
        String outputDir = file.getParent();
        LineNumberReader lineReader = null;
        try {
            lineReader = new LineNumberReader(new FileReader(file));
            String line = "";
            stringSet = new TreeSet();

            while (line != null) {
                line = lineReader.readLine();
                if (line != null) {
                    line = processLine(line, localize, trim, removeXMLComment, removeJpfLineNumbers, outputDir);
                    stringSet.add(line);
                }
            }
        }
        catch (IOException ioe) {
            throw new PageFlowCompilerTestException("Failed to read file: " + file.getName(), ioe);
        }
        finally {
            try{if(lineReader != null) lineReader.close();} catch(IOException ignore) {}
        }

        return stringSet;
    }

    /**
     * Do a line by line comparison of 2 files.
     *
     * @param expected
     * @param actual
     * @param localize
     * @param trim
     * @param removeXMLComment
     * @throws PageFlowCompilerTestException
     */
    public static String fileCompare(File expected,
                                     File actual,
                                     boolean localize,
                                     boolean trim,
                                     boolean removeXMLComment,
                                     boolean removeJpfLineNumbers)
        throws PageFlowCompilerTestException {

        StringBuffer failMsg = new StringBuffer();

        if (expected == null)
            failMsg.append("Expected file must not be null.\n");

        if (actual == null)
            failMsg.append("Actual file must not be null.\n");

        String outputDir = actual.getParent();

        LineNumberReader expectedReader = null;
        LineNumberReader actualReader = null;
        try {
            expectedReader = new LineNumberReader(new FileReader(expected));
            actualReader = new LineNumberReader(new FileReader(actual));

            String expectedLine = "";
            String actualLine = "";

            while (expectedLine != null && actualLine != null) {
                expectedLine = expectedReader.readLine();
                actualLine = actualReader.readLine();
                String expectedNull = "In file [" + actual.getName() + "] line "
                    + Integer.toString(actualReader.getLineNumber()) + ": " + actualLine
                    + ", Expected null.";
                String actualNull = "In file [" + actual.getName() + "] line "
                    + Integer.toString(actualReader.getLineNumber() + 1) + ": null, \nExpected "
                    + expectedLine;

                if (actualLine == null && expectedLine == null)
                    break;
                else if (actualLine == null)
                    failMsg.append(actualNull + "\n");
                else if (expectedLine == null)
                    failMsg.append(expectedNull + "\n");

                if (actualLine != null && expectedLine != null) {
                    expectedLine =
                        processLine(expectedLine, localize, trim, removeXMLComment, removeJpfLineNumbers, outputDir);

                    actualLine =
                        processLine(actualLine, localize, trim, removeXMLComment, removeJpfLineNumbers, outputDir);

                    if (!expectedLine.equals(actualLine)) {
                        failMsg.append("In file: [" + actual.getName() + "] line "
                            + Integer.toString(actualReader.getLineNumber()) + ": " + actualLine
                            + "\nExpected " + expectedLine + "\n");
                    }
                }

            }
        }
        catch (IOException ioe) {
            throw new PageFlowCompilerTestException(TestPropertyMgr.EXCEPTION_FILE_COMPARISON, ioe);
        }
        finally {
            try{if(expectedReader != null) expectedReader.close();} catch(IOException ignore) {}
            try{if(actualReader != null) actualReader.close();} catch(IOException ignore) {}
        }

        if (failMsg.length() == 0)
            return null;
        else return failMsg.toString();
    }

    
    /**
     * Return a Set of Strings, tokenized by the delimiter.
     *
     * @param list
     * @param delim
     * @return a Set of Strings
     */
    protected static Set parseList(String list, String delim) {
        Set set = new TreeSet();
        StringTokenizer tokenizer = new StringTokenizer(list, delim);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            set.add(token.trim());
        }
        return set;
    }

    /**
     * Copy the test resources (.jpf, etc...) to the output directory for this
     * test, and create a WEB-INF and WEB-INF/classes directories for the
     * generated files, and reset jpf, jpfs, jcs, etc to .java file for apt to
     * process.
     *
     * @param testDir
     * @param outputDir
     */
    public static void createCompilerResources(File testDir, File outputDir)
        throws PageFlowCompilerTestException {

        try {
            copyDir(testDir, outputDir);
        }
        catch (IOException ioe) {
            throw new PageFlowCompilerTestException("Couldn't copy directory.", ioe);
        }

        // Create a needed WEB-INF for the compiler.
        File webinf = new File(outputDir, TestPropertyMgr.DIRNAME_WEB_INF);
        if (!webinf.exists()) {
            webinf.mkdirs();
        }

        // Create the class output dir "WEB-INF/classes" for javac output.
        File webinfClasses = new File(webinf, "classes");
        if (!webinfClasses.exists()) {
            webinfClasses.mkdirs();
        }
    }

    /**
     * A recursive directory copy.
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copyDir(File src, File dst) throws IOException {
        if (src.isDirectory() && !dst.exists())
            dst.mkdirs();

        File[] children = src.listFiles();
        for (int i = 0; i < children.length; i++) {
            if (children[i].isDirectory()) {
                copyDir(children[i], new File(dst, children[i].getName()));
            }
            else {
                fileCopy(children[i], new File(dst, children[i].getName()));
            }
        }
    }

    /**
     * Convenience method to delete a directory
     *
     * @param src directory to be deleted
     * @throws IOException
     */
    public static void deleteDir(File src)
        throws IOException {
        if (!src.exists())
            throw new IOException("src does not exists");

        File[] children = src.listFiles();
        for (int i = 0; i < children.length; i++) {
            if (children[i].delete()) {}
            else {
                deleteDir(children[i]);
                if (children[i].delete())
                    ;
            }
        }
        if (src.delete()) {}
        else throw new IOException("Could not delete: " + src);
    }

    // Find all src files recursively
    public static String[] findSrcFiles(File file, Set<String> extentions)
        throws PageFlowCompilerTestException {
        ArrayList<String> foundFiles = new ArrayList<String>();
        scanDir(file, extentions, foundFiles);
        if (foundFiles.size() == 0)
            throw new PageFlowCompilerTestException("No File found!");
        return foundFiles.toArray(new String[foundFiles.size()]);
    }

    public static void processTestSrcFiles(File srcDir, File tempDir, String[] files)
        throws IOException {

        String srcDirPath = srcDir.getAbsolutePath();
        for (int i = 0; i < files.length; i++) {
            File srcFile = new File(files[i]);
            String theFile = files[i];
            if (theFile.startsWith(srcDirPath)) {
                int trim = srcDirPath.length();
                theFile = theFile.substring(trim, theFile.length());
            }
            int dot = theFile.lastIndexOf('.');
            theFile = tempDir.getAbsolutePath() + theFile.substring(0, dot) + ".java";
            files[i] = theFile;
            File destFile = new File(files[i]);
            destFile.getParentFile().mkdirs();
            TestUtil.fileCopy(srcFile, destFile);
        }
    }

    /**
     * Process a line.
     * <p/>
     * This method is used to remove 'uncomparable' items from a line: like
     * hardcoded paths, whitespace at the end of a line, unix/windows paths, and
     * line numbers in exceptions.
     *
     * @param line
     * @param localize
     * @param trim
     * @param removeXMLComment
     * @param removeJpfLineNumbers
     * @param outputDir
     * @return String
     */
    private static String processLine(String line,
                                      boolean localize,
                                      boolean trim,
                                      boolean removeXMLComment,
                                      boolean removeJpfLineNumbers,
                                      String outputDir) {
        if (trim)
            line = line.trim();
        if (localize)
            line = localizePath(line, outputDir);
        if (removeXMLComment)
            line = removeXMLComment(line);
        if (removeJpfLineNumbers)
            line = removeJpfLineNumbers(line);
        return line;
    }

    /**
     * Replace any path referring to the .jpf's parent with [LOCAL_PATH], and
     * call normalizePath().
     *
     * @param path
     * @param outputDir
     * @return String
     */
    private static String localizePath(String path, String outputDir) {
        String root = normalizePath(outputDir.trim());
        path = normalizePath(path);
        return path.replaceAll(root, TestPropertyMgr.LOCAL_PATH_STRING);
    }

    /**
     * Replace backslashes with forwardslashes, and remove references to the
     * current directory ("/.").
     *
     * @param path
     * @return String
     */
    private static String normalizePath(String path) {
        return path.replace('\\', '/').replaceAll("/./", "/");
    }

    /**
     * Remove line numbers, which may cause problems for a file comparison.
     *
     * @param line
     * @return String
     */
    private static String removeJpfLineNumbers(String line) {
        /*
         * @todo: will probably need support for jpfs files. A list of supported
         * file extensions might be a good way to go.
         */
        String regex = "jpfs:[0-9]+:[0-9]+:";
        String processed = line.replaceAll(regex, "jpfs");
        regex = "app:[0-9]+:[0-9]+:";
        processed = processed.replaceAll(regex, "app");
        regex = "jpf:[0-9]+:[0-9]+:";
        processed = processed.replaceAll(regex, "jpf");
        return processed;
    }

    /**
     * Remove xml comments, which may cause problems for a file comparison.
     *
     * @param line
     * @return String
     */
    private static String removeXMLComment(String line) {
        String regex = "<!--.*-->";
        return line.replaceAll(regex, "");
    }

    /**
     * Convenience method to copy files from one location to another
     *
     * @param file1 source file path
     * @param file2 destination file path
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void fileCopy(File file1, File file2)
        throws FileNotFoundException, IOException {

        FileReader in = new FileReader(file1);
        FileWriter out = null;

        try {
            out = new FileWriter(file2);
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);
            }
        }
        finally {
            in.close();
            if (out != null)
                out.close();
        }
    }

    /**
     * Implements a tail recursive file scanner
     */
    private static void scanDir(File file, Set<String> extentions, ArrayList<String> foundList)
        throws PageFlowCompilerTestException {

        if (!file.exists())
            return;

        if (file.isFile()) {
            String name = file.getName().trim();
            int dotIndex = name.lastIndexOf(".");
            if (extentions.contains(name.substring(dotIndex + 1, name.length()))) {
                foundList.add(file.getAbsolutePath());
            }
            // ignoring file types that are not defined in the extention set
        }

        if (file.isDirectory() && file.getName().charAt(0) != '.') {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                scanDir(files[i], extentions, foundList);
            }
        }
    }
}
