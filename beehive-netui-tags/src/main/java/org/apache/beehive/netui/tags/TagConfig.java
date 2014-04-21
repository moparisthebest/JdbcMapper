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
package org.apache.beehive.netui.tags;

import org.apache.beehive.netui.tags.tree.InheritableState;
import org.apache.beehive.netui.util.config.ConfigUtil;
import org.apache.beehive.netui.util.config.bean.JspTagConfig;
import org.apache.beehive.netui.util.config.bean.IdJavascript;

/**
 * This class implements a series of static method that will return configuration information.  The
 * information is buffered, and config properties values defined here.  The document type configuration
 * is not found in this class.  It is found TagRenderBase {@link org.apache.beehive.netui.tags.rendering.TagRenderingBase}
 * and the HTML tag {@link org.apache.beehive.netui.tags.html.Html}.
 */
final public class TagConfig
{
    // This is the value of the javascript support for the webapp.  This should
    // be one of the enum values defined above.
    private static int javascriptMode = -1;

    private static String defaultTreeImageLocation = null;
    private static String defaultTreeRendererClassName = null;


    /**
     * Return true if the legacy JavaScript support should be written to the output stream.
     * @return boolean
     */
    public static boolean isLegacyJavaScript()
    {
        if (javascriptMode == -1) {
            setLegacyJavaScriptMode();
        }
        assert(javascriptMode != -1);
        return (javascriptMode == IdJavascript.INT_LEGACY
                || javascriptMode == IdJavascript.INT_LEGACY_ONLY);
    }

    /**
     * Return true if the default JavaScript support should be written to the output stream.
     * @return boolean
     */
    public static boolean isDefaultJavaScript()
    {
        if (javascriptMode == -1) {
            setLegacyJavaScriptMode();
        }
        assert(javascriptMode != -1);
        return (javascriptMode == IdJavascript.INT_DEFAULT
                || javascriptMode == IdJavascript.INT_LEGACY);
    }

    /**
     * This method returns the default location for the tree images.  This may be configured by setting
     * the &lt;tree-image-location> element in the netui.config file to the location of the images.
     * This location should not include the context path of the webapp or the leading '/', but should be an absolute
     * path within the webapp.
     * <p>&lt;tree-image-location>resources/images&lt;/tree-image-location>
     * </p>
     * @return the default location of the tree images.
     */
    public static String getTreeImageLocation()
    {
        if (defaultTreeImageLocation == null) {
            JspTagConfig tagConfig = ConfigUtil.getConfig().getJspTagConfig();
            if (tagConfig != null) {
                String s = tagConfig.getTreeImageLocation();
                defaultTreeImageLocation = (s != null) ? s : InheritableState.DEFAULT_IMAGES;
            }
            else
                defaultTreeImageLocation = InheritableState.DEFAULT_IMAGES;
        }
        assert(defaultTreeImageLocation != null);
        return defaultTreeImageLocation;
    }

    /**
     * This method returns the class name of the renderer to use for NetUI
     * trees in the Web application. The class name returned will usually
     * be the predefined default NetUI implementation,
     * {@link org.apache.beehive.netui.tags.tree.TreeRenderer}, unless
     * NetUI is configured to use a different implementation.
     * This may be configured by setting the &lt;tree-renderer-class>
     * element in the beehive-netui-config file to the name of a class that
     * extends {@link org.apache.beehive.netui.tags.tree.TreeRenderer}.
     * <p>&lt;tree-renderer-class>com.xyz.tree.CustomTreeRenderer&lt;/tree-renderer-class>
     * </p>
     * <p>Note that the NetUI {@link org.apache.beehive.netui.tags.tree.Tree}
     * tag has an attribute for setting the name of the TreeRenderer class to
     * use on a tree by tree bases.
     * </p>
     * @return the name of the class to use to renderer the trees in the application.
     */
    public static String getTreeRendererClassName()
    {
        if (defaultTreeRendererClassName == null) {
            JspTagConfig tagConfig = ConfigUtil.getConfig().getJspTagConfig();
            if (tagConfig != null) {
                // the schema config includes a default value so we shouldn't
                // get a null value from the config object.
                defaultTreeRendererClassName = tagConfig.getTreeRendererClass();
            }
        }
        return defaultTreeRendererClassName;
    }

    /**
     * This will set the JavaScript support level for the id and name attributes.
     */
    private static void setLegacyJavaScriptMode()
    {
        JspTagConfig tagConfig = ConfigUtil.getConfig().getJspTagConfig();
        if (tagConfig != null) {
            javascriptMode = tagConfig.getIdJavascript().getValue();
        }
        else {
            javascriptMode = IdJavascript.INT_DEFAULT;
        }
    }
}
