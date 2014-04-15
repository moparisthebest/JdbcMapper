/*
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 $Header:$
 */
package org.apache.beehive.netui.test.util.config;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.ConfigInitializationException;
import org.apache.beehive.netui.util.config.parser.NetUIConfigParser;
import org.apache.beehive.netui.util.config.bean.NetUIConfig;
import org.apache.beehive.netui.util.config.bean.DocType;
import org.apache.beehive.netui.util.config.bean.IdJavascript;
import org.apache.beehive.netui.util.xml.XmlInputStreamResolver;

/**
 *
 */
public class ConfigBeanTest
    extends TestCase {

    /**
     * Test to ensure that the ConfigUtil only allows configuration of the webapp to be performed once.
     *
     * @throws Exception
     */
    public void testConfigUtil()
        throws Exception {
        XmlInputStreamResolver xmlResolver = new TestXmlInputStreamResolver("WEB-INF/beehive-netui-config.xml");

        /*
        Just ensure that some configuration is loaded.
        */
        if(!ConfigUtil.isInit())
            ConfigUtil.init(xmlResolver);

        NetUIConfig config = ConfigUtil.getConfig();
        assertNotNull(config);
        assertFalse(config.getPageFlowConfig().isEnableSelfNesting());
        assertTrue(config.getJspTagConfig().getDocType() == DocType.HTML4_LOOSE_QUIRKS);

        boolean caughtException = false;
        try {
            ConfigUtil.init(xmlResolver);
        }
        catch(Exception e) {
            if(e instanceof ConfigInitializationException)
                caughtException = true;
        }

        assertTrue(caughtException);
    }

    public void testValidationFailure()
        throws Exception {
        boolean caughtException = false;
        try {
            parseNetUIConfig("org/apache/beehive/netui/test/util/config/xmls/beehive-netui-config-fail-validation.xml");
        }
        catch(Throwable e) {
            /* assumes that a ConfigInitializationException resulted from a failure in XML validation */
            if(e instanceof ConfigInitializationException)
                caughtException = true;
        }
        assertTrue(caughtException);
    }

    public void testParsing()
        throws Exception {
        String resourcePath = "org/apache/beehive/netui/test/util/config/xmls/beehive-netui-config-default-1.0.xml";

        NetUIConfig config = parseNetUIConfig(resourcePath);

        assertNotNull(config);
        assertNotNull(config.getJspTagConfig());

        assertTrue(config.getJspTagConfig().getDocType() == DocType.HTML4_LOOSE_QUIRKS);
        assertTrue(config.getJspTagConfig().getIdJavascript() == IdJavascript.DEFAULT);
        assertNull(config.getJspTagConfig().getTreeImageLocation());

        assertNotNull(config.getTypeConverters());
        assertTrue(config.getTypeConverters().length > 0);
        assertEquals("org.foo.FooBean", config.getTypeConverters()[0].getType());
        assertEquals("org.foo.FooBeanConverter", config.getTypeConverters()[0].getConverterClass());
    }

    public void testParsingWithChain()
        throws Exception {
        String resourcePath = "org/apache/beehive/netui/test/util/config/xmls/beehive-netui-config-with-chain.xml";

        NetUIConfig config = parseNetUIConfig(resourcePath);

        assertNotNull(config);
        assertNotNull(config.getJspTagConfig());

        assertTrue(config.getJspTagConfig().getDocType() == DocType.HTML4_LOOSE_QUIRKS);
        assertTrue(config.getJspTagConfig().getIdJavascript() == IdJavascript.DEFAULT);
        assertNull(config.getJspTagConfig().getTreeImageLocation());

        assertNotNull(config.getTypeConverters());
        assertTrue(config.getTypeConverters().length > 0);
        assertEquals("org.foo.FooBean", config.getTypeConverters()[0].getType());
        assertEquals("org.foo.FooBeanConverter", config.getTypeConverters()[0].getConverterClass());
    }

    public void testParsingPageFlowConfig()
        throws Exception {
        String resourcePath = "org/apache/beehive/netui/test/util/config/xmls/beehive-netui-config-pageflowConfig.xml";

        NetUIConfig netuiConfig = parseNetUIConfig(resourcePath);

        assertEquals(314, netuiConfig.getPageFlowConfig().getMaxForwardsPerRequest());
        assertEquals(10, netuiConfig.getPageFlowConfig().getMaxNestingStackDepth());
        assertFalse(netuiConfig.getPageFlowConfig().isEnableSelfNesting());

    }

    private NetUIConfig parseNetUIConfig(String resourcePath)
        throws Exception {

        XmlInputStreamResolver xmlResolver = new TestXmlInputStreamResolver(resourcePath);
        NetUIConfigParser configParser = new NetUIConfigParser();
        return configParser.parse(xmlResolver);
    }

    public ConfigBeanTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ConfigBeanTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
