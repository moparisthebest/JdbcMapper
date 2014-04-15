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

package org.apache.beehive.controls.test.controls.threading.nested;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Threading;
import org.apache.beehive.controls.api.bean.ThreadingPolicy;

import java.util.Date;

/**
 * An impl of NestedHelloControl.
 * By default, control impl is single thread.
 * <p/>
 * Test objective: doRead and doSet on same instance should
 * inter-lock each other when invoked by different thread.
 */
@ControlImplementation
@Threading(ThreadingPolicy.SINGLE_THREADED)
public class NestedSingleThreadControlImpl
        implements NestedSingleThreadControl, java.io.Serializable {
    public final static int LOOPS = 50;
    private long count = 0;

    public long doRead() {
        return count;
    }

    public void doSet() {
        for (int i = 0; i < LOOPS; i++) {
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {
            }
            Date now = new Date();
            count = now.getTime();
        }
    }
}
