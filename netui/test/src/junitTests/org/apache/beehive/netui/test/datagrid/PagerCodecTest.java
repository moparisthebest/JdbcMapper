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
package org.apache.beehive.netui.test.datagrid;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.test.servlet.ServletFactory;
import org.apache.beehive.netui.databinding.datagrid.api.pager.PagerModel;

/**
 *
 */
public class PagerCodecTest
    extends TestCase {

    private HttpServletRequest _request = null;

    public void testFiller() {}
    
/*
    public void testPagerCodec() {
        String namespace = "bugs";

        PagerCodec pagerCodec = new PagerCodec();
        Map<String, PagerModel> pagerModels = pagerCodec.decode(_request.getParameterMap());
        PagerModel pagerModel = pagerModels.get(namespace);
        assertNull(pagerModel);
    }
*/

    protected void setUp() {
        _request = ServletFactory.getServletRequest();
    }

    protected void tearDown() {
        _request = null;
    }

    public PagerCodecTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(PagerCodecTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
