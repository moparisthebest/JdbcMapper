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
package org.apache.beehive.controls.test.controls.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Utility code for testing serialization behavior
 */
public final class SerializeUtils {
    /**
     * Serializes an object into a byte array, then deserializes it from the array and returns
     * the deserialized object.
     */
    public static <T extends Object> T testSerialize(T obj) {
        byte [] serializedObject;
        T returnObject;

        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            serializedObject = baos.toByteArray();
        }
        catch (Exception e) {
            throw new RuntimeException("Error serializing object", e);
        }
        finally {
            if (oos != null)
                try {
                    oos.close();
                }
                catch (IOException ignore) {
                }
            if (baos != null)
                try {
                    baos.close();
                }
                catch (IOException ignore) {
                }
        }

        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(serializedObject);
            ois = new ObjectInputStream(bais);
            returnObject = (T) ois.readObject();
            ois.close();
            bais.close();
        }
        catch (Exception e) {
            throw new RuntimeException("Error deserializing object", e);
        }
        finally {
            if (bais != null)
                try {
                    bais.close();
                }
                catch (IOException ignore) {
                }
            if (ois != null)
                try {
                    ois.close();
                }
                catch (IOException ignore) {
                }
        }

        if (!obj.equals(returnObject))
            throw new RuntimeException("Deserialized object is not equivalent to original!");

        return returnObject;
    }
}
