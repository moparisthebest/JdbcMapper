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

package org.apache.beehive.controls.test.controls.composition;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.events.Client;

@ControlImplementation(assembler = NestedAssembler.class)
public class NestedImpl implements Nested, java.io.Serializable {

    @Client
    Return returnClient;

    @Client
    Args argsClient;

    @Client
    Except exceptClient;

    public void fireEvent(String set, String name) {
        if (set.equals("Return")) {
            if (name.equals("returnVoid"))
                returnClient.returnVoid();

            if (name.equals("returnInt"))
                returnClient.returnInt();

            if (name.equals("returnString"))
                returnClient.returnString();

        } else if (set.equals("Args")) {
            if (name.equals("argsInt"))
                argsClient.argsInt(1);

            if (name.equals("argsString"))
                argsClient.argsString("foo");

            if (name.equals("argsMultiple"))
                argsClient.argsMultiple(2, "bar");

        } else if (set.equals("Except")) {
            try {
                if (name.equals("exceptIO"))
                    exceptClient.exceptIO();

                if (name.equals("exceptRuntime"))
                    exceptClient.exceptRuntime();

                if (name.equals("exceptLocal"))
                    exceptClient.exceptLocal();

                if (name.equals("exceptMultiple"))
                    exceptClient.exceptMultiple();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
