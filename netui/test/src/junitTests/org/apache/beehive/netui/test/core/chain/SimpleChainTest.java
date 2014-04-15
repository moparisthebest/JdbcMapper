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
package org.apache.beehive.netui.test.core.chain;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.beehive.netui.core.chain.Context;
import org.apache.beehive.netui.core.chain.Chain;
import org.apache.beehive.netui.core.chain.impl.ChainBase;
import org.apache.beehive.netui.core.chain.impl.ContextBase;
import org.apache.beehive.netui.test.core.chain.commands.EchoCommand;

/**
 *
 */
public class SimpleChainTest
    extends TestCase {

    public void testSimple()
        throws Exception {

        Context context = new ContextBase();
        Chain chain = new ChainBase();
        for(int i = 0; i < 3; i++) {
            EchoCommand echoCommand = new EchoCommand();
            echoCommand.setMessage("echo me " + i);
            chain.addCommand(echoCommand);
        }

        boolean result = false;
        try {
            chain.execute(context);
        }
        catch(Exception e) {
            throw e;
        }

        assertFalse(result);
    }

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public SimpleChainTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(SimpleChainTest.class);
    }
}

