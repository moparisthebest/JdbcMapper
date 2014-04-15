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

package org.apache.beehive.controls.test.junit.inherit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileInputStream;

import junit.framework.TestCase;
import org.apache.beehive.controls.test.junit.utils.ControlTestUtils;

/**
 * A TestCase that tests control interface, extension, and implementation inheritance
 */
public class InheritTest extends TestCase {

    /**
     * Verifies the generated BeanInfo for Intf1
     */
    public void testIntf1BeanInfo() throws Exception {
        validateBeanInfo("Intf1Bean");
    }

    /**
     * Verifies the generated BeanInfo for Intf2
     */
    public void testIntf2BeanInfo() throws Exception {
        validateBeanInfo("Intf2Bean");
    }

    /**
     * Verifies the generated BeanInfo for Ext1
     */
    public void testExt1BeanInfo() throws Exception {
        validateBeanInfo("Ext1Bean");
    }

    /**
     * Verifies the generated BeanInfo for Ext2
     */
    public void testExt2BeanInfo() throws Exception {
        validateBeanInfo("Ext2Bean");
    }

    private void validateBeanInfo(String simpleName) 
	throws Exception {

        String beanClassName = "org.apache.beehive.controls.test.controls.inherit." + simpleName;
        String beanInfo = "org/apache/beehive/controls/test/junit/inherit/" + simpleName + ".beaninfo";
	
	File tempFile = File.createTempFile(simpleName, ".beaninfo");
	boolean same = ControlTestUtils.compareBeanInfo(beanInfo, tempFile, Class.forName(beanClassName));
	
	if(!same) 
	    fail("Generated beaninfo file " + tempFile + " differs from expected beaninfo " + beanInfo + " in classloader");
	else tempFile.delete();
    }	
    /*

        File tempFile = null;
        FileOutputStream fos = null;
        try {
            tempFile = File.createTempFile(shortName, ".beaninfo");
            fos = new FileOutputStream(tempFile);

            Class beanClass = Class.forName(beanClassName);
            ControlIntrospector ci = new ControlIntrospector(beanClass, fos);
            ci.introspect();
        }
        finally {
            if(fos != null)
                fos.close();
        }

        InputStream expected = null;
        InputStream actual = null;
        try {
            expected = getClass().getClassLoader().getResourceAsStream(beanInfo);

            if (expected == null)
                fail("Could not locate expected beaninfo file \"" + beanInfo + "\"");

            actual = new FileInputStream(tempFile);
	    
	    boolean same = ControlTestUtils.compareFiles(expected, actual);
	    
	    if(!same) 
		fail("File " + tempFile.getName() " differs from expected file " + beanInfo + " in classloader");
        }
        finally {
            if(expected != null)
                expected.close();
            if(actual != null)
                actual.close();
        }

        // Pass, so remove the temporary output file
        tempFile.delete();
    }
    */
}
