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
package org.apache.beehive.netui.tags.tree;

import org.apache.beehive.netui.util.internal.DiscoveryUtils;
import org.apache.beehive.netui.util.logging.Logger;

import java.io.Serializable;


/**
 * Instantiates a TreeRenderer object that can be used to render the
 * Tree, writing out the formatting and markup for the Tree elements.
 *
 * @see org.apache.beehive.netui.tags.tree.TreeRenderer
 */
public class TreeRendererFactory implements Serializable
{
    private static final Logger _log = Logger.getInstance(TreeRendererFactory.class);

    /* do not construct */
    private TreeRendererFactory()
    {
    }

    /**
     * Create an instance of a {@link TreeRenderer} object. This method
     * creates an instance that provides the NetUI predefined default
     * implementation for tree rendering.
     *
     * @return the default tree renderer object.
     */
    public static final TreeRenderer getInstance()
    {
        return new TreeRenderer();
    }

    /**
     * Create an instance of a {@link TreeRenderer} object given a class
     * name. The given class must extend the {@link TreeRenderer} base class.
     *
     * @param className the name of the TreeRenderer class to instantiate
     * @return the new {@link TreeRenderer} instance
     */
    public static final TreeRenderer getInstance(String className)
    {
        TreeRenderer renderer = null;
        if (className != null) {
            className = className.trim();

            // create an instance of the def template formatter class
            ClassLoader cl = DiscoveryUtils.getClassLoader();
            try {
                Class rendererClass = cl.loadClass(className);
                if (!TreeRenderer.class.isAssignableFrom(rendererClass)) {
                    _log.error("The tree renderer class, " + className
                            + ", does not extend TreeRenderer.");
                }
                else {
                    renderer = (TreeRenderer) rendererClass.newInstance();
                }
            }
            catch ( ClassNotFoundException e ) {
                _log.error( "Could not find TreeRenderer class " + className, e );
            }
            catch (InstantiationException e) {
                _log.error( "Could not instantiate TreeRenderer class " + className, e );
            }
            catch (IllegalAccessException e) {
                _log.error( "Could not instantiate TreeRenderer class " + className, e );
            }
        }
        return renderer;
    }
}
