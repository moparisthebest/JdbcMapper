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
import org.apache.beehive.controls.test.controls.threading.DefaultThreadControlBean;
import org.apache.beehive.controls.test.controls.threading.SingleThreadControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

public class SingleThreadTest extends ControlTestCase {


    @Control
    private DefaultThreadControlBean sBean;

    @Control
    private SingleThreadControlBean stBean;

    public void testDefaultThread() throws Exception {

        DriveDefaultThread driver1 = new DriveDefaultThread("DefaultThread-driver1", sBean, true);
        DriveDefaultThread driver2 = new DriveDefaultThread("DefaultThread-driver2", sBean, false);

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
        assertEquals(0, driver2.getResult());
    }

    /**
     * Makes an instance of a single threaded control, passes it to two different
     * threads and run both threads.
     */
    public void testSingleThread() throws Exception {

        DriveSingleThread driver1 = new DriveSingleThread("SingleThread-driver1", stBean, true);
        DriveSingleThread driver2 = new DriveSingleThread("SingleThread-driver2", stBean, false);
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
        assertEquals(0, driver2.getResult());
    }

    private class DriveDefaultThread extends Thread {
        private boolean _role;
        private long _result = 0;
        private DefaultThreadControlBean _myControl;

        public DriveDefaultThread(String name, DefaultThreadControlBean bean, boolean role) {
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

    private class DriveSingleThread extends Thread {
        private SingleThreadControlBean _myControl;
        private boolean _role;
        private long _result = 0;

        public DriveSingleThread(String name, SingleThreadControlBean bean, boolean role) {
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
