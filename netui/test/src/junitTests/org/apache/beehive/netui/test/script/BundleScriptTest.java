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
package org.apache.beehive.netui.test.script;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import org.apache.beehive.netui.script.common.BundleMap;

/**
 *
 */
public abstract class BundleScriptTest
    extends AbstractExpressionTest {

    private BundleMap createBundleMap() {
        return new BundleMap((HttpServletRequest)getPageContext().getRequest(),
                             getPageContext().getServletContext());
    }

    public void testSimple()
        throws Exception {
        BundleMap bundleMap = createBundleMap();
        getPageContext().setAttribute("bundle", bundleMap);

        bundleMap.registerResourceBundle("bundle1", "org/apache/beehive/netui/test/databinding/testdata/bundle1", null);
        bundleMap.registerResourceBundle("bundle2", "org/apache/beehive/netui/test/databinding/testdata/bundle2", null);

        String val = (String)evaluateExpression("{bundle.bundle1.simpleKey}", getPageContext());
        assertEquals("This is a simple message.", val);

        val = (String)evaluateExpression("{bundle.bundle2.bundle2Message}", getPageContext());
        assertEquals("bundle2MessageValue", val);
    }

    public void testLocaleSupport()
        throws Exception {
        Locale germany = Locale.GERMAN;
        BundleMap bundleMap = createBundleMap();
        bundleMap.registerResourceBundle("bundle1", "org/apache/beehive/netui/test/databinding/testdata/bundle1", germany);
        bundleMap.registerResourceBundle("bundle2", "org/apache/beehive/netui/test/databinding/testdata/bundle2", null);

        getPageContext().setAttribute("bundle", bundleMap);

        String val = (String)evaluateExpression("{bundle.bundle1.localHello}", getPageContext());
        assertEquals("Hallo von Deutschland", val);

        val = (String)evaluateExpression("{bundle.bundle2.whereverYouAre}", getPageContext());
        assertEquals("Hello from Colorado", val);

        val = (String)evaluateExpression("{bundle.bundle1.noi18nMessage}", getPageContext());
        assertEquals("This message is never localized", val);
    }

    /**
     * Simple test to make sure that BundleMap.toString() doesn't throw exceptions and generally works.
     *
     * @throws Exception
     */
    public void testToString()
        throws Exception {
        BundleMap bundleMap = createBundleMap();

        // toString an empty map
        System.out.println("budleMap.toString(): " + bundleMap.toString());

        getPageContext().setAttribute("bundle", bundleMap);

        bundleMap.registerResourceBundle("bundle1", "org/apache/beehive/netui/test/databinding/testdata/bundle1", null);
        bundleMap.registerResourceBundle("bundle2", "org/apache/beehive/netui/test/databinding/testdata/bundle2", null);

        String val = (String)evaluateExpression("{bundle.bundle1.simpleKey}", getPageContext());
        assertEquals("This is a simple message.", val);

        // toString a BundleMap that has bundles
        System.out.println("bundleMap.toString(): " + bundleMap.toString());
    }

    public BundleScriptTest(String name) {
        super(name);
    }
}
