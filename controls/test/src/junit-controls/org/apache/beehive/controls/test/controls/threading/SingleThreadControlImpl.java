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

package org.apache.beehive.controls.test.controls.threading;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Threading;
import org.apache.beehive.controls.api.bean.ThreadingPolicy;

/**
 * An impl of HelloControl.
 */
@ControlImplementation
@Threading(ThreadingPolicy.SINGLE_THREADED)
public class SingleThreadControlImpl implements SingleThreadControl, java.io.Serializable {
    private long counter = 0;
    private boolean marker = false;


    public long doSlowIncrement(boolean isBlocker) {

        if (isBlocker) {
            marker = true;
            while (marker) {
                counter++;
                try {
                    Thread.currentThread().sleep(200);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return counter;
        }
        else {
            marker = false;
            return counter;
        }
    }
}
