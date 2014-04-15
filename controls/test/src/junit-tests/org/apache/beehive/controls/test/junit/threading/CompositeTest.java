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

import org.apache.beehive.controls.test.controls.threading.nested.CompositeMThreadControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import org.apache.beehive.controls.api.bean.Control;

public class CompositeTest extends ControlTestCase {

    @Control
    private CompositeMThreadControlBean sBean;

    public void testNestedSingleThread() throws Exception {

        DriveNestedSingleThread singleThreadStarter = new DriveNestedSingleThread("single thread starter", sBean, false);
        DriveNestedSingleThread singleThreadStopper = new DriveNestedSingleThread("single thread stopper", sBean, true);

        singleThreadStarter.start();

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            fail("InterruptedException!");
        }

        singleThreadStopper.start();

        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
            fail("InterruptedException!");
        }

        assertEquals(0, singleThreadStopper.getResult());
    }

    public void testNestedMultiThread() throws Exception {

        DriveNestedMultiThread multiThreadStarter = new DriveNestedMultiThread("multi thread starter", sBean, false);
        DriveNestedMultiThread multiThreadStopper = new DriveNestedMultiThread("multi thread stopper", sBean, true);

        multiThreadStarter.start();

        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
        }

        multiThreadStopper.start();

        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e) {
        }

        assertTrue(multiThreadStopper.getResult() > 0);
    }

    private class DriveNestedSingleThread extends Thread {

        private CompositeMThreadControlBean _myControl;
        private boolean _isStopperRole = false;
        private int _result = 0;

        public DriveNestedSingleThread(String name, CompositeMThreadControlBean bean, boolean role) {
            super(name);
            _myControl = bean;
            _isStopperRole = role;
        }

        public void run() {
            if (_isStopperRole) {
                _result = _myControl.stopSingleThreadNestedControl();
            }
            else {
                _result = _myControl.startSingleThreadNestedControl();
            }
        }

        public int getResult() {
            return _result;
        }
    }

    private class DriveNestedMultiThread extends Thread {

        private CompositeMThreadControlBean _myControl;
        private boolean _isStopperRole = false;
        private int _result = 0;

        public DriveNestedMultiThread(String name, CompositeMThreadControlBean bean, boolean role) {
            super(name);
            _myControl = bean;
            _isStopperRole = role;
        }

        public void run() {
            if (_isStopperRole) {
                _result = _myControl.stopMultiThreadNestedControl();
            }
            else {
                _result = _myControl.startMultiThreadNestedControl();
            }
        }

        public int getResult() {
            return _result;
        }
    }
}
