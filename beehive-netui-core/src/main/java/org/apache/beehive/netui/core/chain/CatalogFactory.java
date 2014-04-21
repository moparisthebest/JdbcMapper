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
package org.apache.beehive.netui.core.chain;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.InvocationTargetException;

import org.apache.beehive.netui.core.chain.impl.CatalogFactoryBase;
import org.apache.beehive.netui.core.chain.impl.CatalogBase;
import org.apache.beehive.netui.core.chain.impl.ChainBase;
import org.apache.beehive.netui.util.config.bean.CatalogConfig;
import org.apache.beehive.netui.util.config.bean.ChainConfig;
import org.apache.beehive.netui.util.config.bean.CommandConfig;
import org.apache.beehive.netui.util.config.bean.CustomPropertyConfig;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Abstract factory class for configuring a {@link Catalog} of {@link Command}s in a chain.
 */
public abstract class CatalogFactory {

    private static Map FACTORIES = new HashMap();

    public abstract void setCatalog(Catalog catalog);

    public abstract Catalog getCatalog();

    public abstract Catalog getCatalog(String name);

    public abstract void addCatalog(String name, Catalog catalog);

    public static CatalogFactory getInstance() {
        CatalogFactory catalogFactory = null;
        ClassLoader classLoader = getClassLoader();
        synchronized(FACTORIES) {
            catalogFactory = (CatalogFactory)FACTORIES.get(classLoader);
            if(catalogFactory == null) {
                catalogFactory = new CatalogFactoryBase();
                FACTORIES.put(classLoader, catalogFactory);
            }
        }
        return catalogFactory;
    }

    public static CatalogFactory getInstance(CatalogConfig catalogConfig) {

        if(catalogConfig == null)
            return null;

        CatalogFactory catalogFactory = getInstance();
        assert catalogFactory != null;
        Catalog catalog = new CatalogBase();
        List chainConfigs = catalogConfig.getCommands();
        for(int i = 0; i < chainConfigs.size(); i++) {
            CommandConfig commandConfig = (CommandConfig)chainConfigs.get(i);
            assert commandConfig != null;

            if(commandConfig instanceof ChainConfig) {
                ChainConfig chainConfig = (ChainConfig)commandConfig;
                Chain chain = new ChainBase();

                List commandConfigs = chainConfig.getCommands();
                for(int j = 0; j < commandConfigs.size(); j++) {
                    CommandConfig chainCommandConfig = (CommandConfig)commandConfigs.get(j);
                    Command command = createCommand(chainCommandConfig);
                    chain.addCommand(command);
                }
                catalog.addCommand(chainConfig.getName(), chain);
            }
            else {
                Command command = createCommand(commandConfig);
                catalog.addCommand(commandConfig.getId(), command);
            }
        }
        catalogFactory.setCatalog(catalog);
        return catalogFactory;
    }

    private static Command createCommand(CommandConfig commandConfig) {
        Command command = null;
        try {
            Class commandClass = Class.forName(commandConfig.getClassname(), true, getClassLoader());
            if (!Command.class.isAssignableFrom(commandClass))
                throw new RuntimeException("Created command that is not a command class");

            command = (Command) commandClass.newInstance();
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("Problem occurred creating Chain.  Cause: " + e, e);
        }
        catch (InstantiationException e) {
            throw new RuntimeException("Problem occurred creating Chain.  Cause: " + e, e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Problem occurred creating Chain.  Cause: " + e, e);
        }

        List propertyConfigs = commandConfig.getParameters();
        for(int k = 0; k < propertyConfigs.size(); k++) {
            CustomPropertyConfig customPropertyConfig = (CustomPropertyConfig)propertyConfigs.get(k);
            assert customPropertyConfig.getName() != null;
            assert customPropertyConfig.getValue() != null;

            try {
                BeanUtils.setProperty(command,  customPropertyConfig.getName(), customPropertyConfig.getValue());
            }
            catch (IllegalAccessException e) {
                throw new RuntimeException("Problem occurred setting property on Command.  Cause: " + e, e);
            }
            catch (InvocationTargetException e) {
                throw new RuntimeException("Problem occurred setting property on Command.  Cause: " + e, e);
            }

        }
        return command;
    }

    private static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader == null)
             classLoader = CatalogFactory.class.getClassLoader();
        return classLoader;
    }
}
