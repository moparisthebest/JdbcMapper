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
package org.apache.beehive.controls.test.junit.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class ControlTestUtils {

    public static ByteArrayOutputStream serialize(Object object)
        throws IOException {
        // Test the relationships and identifiers are preserved across serialization and deserialization
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(object);
            oos.close();
        }
        finally {
            if(oos != null)
                oos.close();
            if(baos != null)
                baos.close();
        }
        return baos;
    }

    public static Object deserialize(ByteArrayOutputStream baos)
        throws IOException, ClassNotFoundException {

        Object object = null;

        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(baos.toByteArray());
            ois = new ObjectInputStream(bais);
            object = ois.readObject();
        }
        finally {
            if(ois != null)
                ois.close();
            if(bais != null)
                bais.close();
        }

        return object;
    }

    /**
       Compare two bean info files.  Two InputStream instances are created given the parameters
       and compared byte-by-byte.  Any difference between these comparisons will result in a 
       return value of <code>false</code>.
       
       @param expectedBeanInfo the String path to an expected beaninfo file that must be available in classloader
       @param tempFile the location of a temporary file into which the actual beaninfo will be generated
       @param controlBeanClass the type of control bean to introspect
       @return <code>false</code> if the files differ by any byte; <code>true</code> otherwise
    */
    public static boolean compareBeanInfo(String expectedBeanInfo, File tempFile, Class controlBeanClass) 
	throws Exception {

	assert controlBeanClass != null;
	assert tempFile != null;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempFile);
            ControlIntrospector ci = new ControlIntrospector(controlBeanClass, fos);
            ci.introspect();
        }
        finally {
            if (fos != null)
                fos.close();
        }

	boolean fail = false;
        InputStream valid = null;
        InputStream current = null;
        try {
            valid = controlBeanClass.getClassLoader().getResourceAsStream(expectedBeanInfo);
            current = new FileInputStream(tempFile);

            assert valid != null;
	    assert current != null;

            int validVal;
            int currentVal;
            do {
                validVal = valid.read();
                currentVal = current.read();

		if(validVal != currentVal) 
		    return false;
            } while (validVal != -1);
        }
        finally {
            if (current != null)
                current.close();
            if (valid != null)
                valid.close();
        }
	
	return true;
    }
}
