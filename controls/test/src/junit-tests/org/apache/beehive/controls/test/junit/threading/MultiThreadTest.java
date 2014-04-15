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

package org.apache.beehive.controls.test.junit.threading;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.threading.MultiThreadControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

public class MultiThreadTest extends ControlTestCase {

    @Control
    private MultiThreadControlBean sBean;

    /**
     * Makes an instance of a single threaded control, passes it to two different
     * threads and run both threads.
     */
    public void testMultiThread() throws Exception {

        DriveMultiThread driver1 = new DriveMultiThread("MultiThread-driver1", sBean, true);
        DriveMultiThread driver2 = new DriveMultiThread("MultiThread-driver2", sBean, false);
        driver1.start();

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            fail(e.getMessage());
        }
        driver2.start();

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            fail(e.getMessage());
        }
        assertTrue(driver2.getResult() > 0);
    }

    private class DriveMultiThread extends Thread {

        public final static int LOOPS = 20;
        private MultiThreadControlBean _myControl;
        private boolean _role;
        private long _result = 0;

        public DriveMultiThread(String name, MultiThreadControlBean bean, boolean role) {
            super(name);
            _myControl = bean;
            _role = role;
        }

        public void run() {
            _result = _myControl.doSlowIncrement(_role);
        }

        public long getResult() {
            return _result;
        }
    }
}
