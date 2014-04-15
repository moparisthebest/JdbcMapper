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

import java.io.InputStream;

import junit.framework.TestCase;
import org.apache.beehive.netui.core.chain.Catalog;
import org.apache.beehive.netui.core.chain.CatalogFactory;
import org.apache.beehive.netui.core.chain.Command;
import org.apache.beehive.netui.core.chain.Context;
import org.apache.beehive.netui.util.config.bean.CatalogConfig;
import org.apache.beehive.netui.util.config.internal.catalog.CatalogParser;
import org.apache.beehive.netui.core.chain.impl.ContextBase;
import org.apache.beehive.netui.test.core.chain.commands.ConfigurableCommand;

/**
 *
 */
public class CatalogParserTest
    extends TestCase {

    private String _resourcePath = "org/apache/beehive/netui/test/core/chain/xmls/simple-chain.xml";

    public void testSimple()
        throws Exception {

        load(_resourcePath);
        CatalogFactory catalogFactory = CatalogFactory.getInstance();

        /* execute a command */
        Catalog catalog = catalogFactory.getCatalog();

        Context context = new ContextBase();
        Command command = catalog.getCommand("echo-1234");
        command.execute(context);
    }

    public void testConfigurable()
        throws Exception {
        load(_resourcePath);
        CatalogFactory catalogFactory = CatalogFactory.getInstance();

        Catalog catalog = catalogFactory.getCatalog();
        Command command = catalog.getCommand("configurable");
        ConfigurableCommand configurableCommand = (ConfigurableCommand)command;
        assertNotNull(configurableCommand);
        assertEquals(54321, configurableCommand.getBar());
        assertEquals("Homer", configurableCommand.getFoo());

        command.execute(new ContextBase());
    }

    private void load(String resourcePath)
        throws Exception {

        InputStream inputStream = null;
        try {
            /* parse an XML file that defines a catalog */
            inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            CatalogParser catalogParser = CatalogParser.getInstance();
            CatalogConfig catalogConfig = catalogParser.parse(resourcePath, inputStream);

            /* create a real catalog instance */
            CatalogFactory catalogFactory = CatalogFactory.getInstance(catalogConfig);
            assertNotNull(catalogFactory);
        }
        catch(Exception e) {
            throw e;
        }
        finally {
            if(inputStream != null) inputStream.close();
        }
    }
}



