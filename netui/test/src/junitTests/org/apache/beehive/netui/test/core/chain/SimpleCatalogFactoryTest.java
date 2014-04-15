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

import junit.framework.TestCase;
import org.apache.beehive.netui.core.chain.CatalogFactory;
import org.apache.beehive.netui.core.chain.Catalog;
import org.apache.beehive.netui.core.chain.Chain;
import org.apache.beehive.netui.core.chain.Command;
import org.apache.beehive.netui.core.chain.Context;
import org.apache.beehive.netui.core.chain.impl.CatalogBase;
import org.apache.beehive.netui.core.chain.impl.ChainBase;
import org.apache.beehive.netui.core.chain.impl.ContextBase;
import org.apache.beehive.netui.test.core.chain.commands.EchoCommand;

/**
 * 
 */
public class SimpleCatalogFactoryTest
    extends TestCase {

    public void testSimple()
        throws Exception {
        Chain chain = new ChainBase();
        for(int i = 0; i < 3; i++) {
            EchoCommand echoCommand = new EchoCommand();
            echoCommand.setMessage("echo me " + i);
            chain.addCommand(echoCommand);
        }

        Catalog catalog = new CatalogBase();
        catalog.addCommand("echo", chain);

        CatalogFactory catalogFactory = CatalogFactory.getInstance();
        catalogFactory.addCatalog("simple", catalog);

        Context context = new ContextBase();
        Catalog theCatalog = catalogFactory.getCatalog("simple");
        Command theCommand = theCatalog.getCommand("echo");
        try {
            theCommand.execute(context);
        }
        catch(Exception e) {
            throw e;
        }
    }
}
