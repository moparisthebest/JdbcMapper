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
package org.apache.beehive.controls.runtime.bean;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The ControlBeanInfo class is an abstract base class for the JavaBean BeanInfo classes generated 
 * to support Beehive controls.   It is used to bundle helper code common across all generated
 * BeanInfo classes.
 */
abstract public class ControlBeanInfo extends java.beans.SimpleBeanInfo
{
    /**
     * Protected constructor that is called from generated BeanInfo subclasses.
     * @param beanClass the JavaBean class for which BeanInfo is being provided.
     */
    protected ControlBeanInfo(Class beanClass)
    {
        super();
        _beanClass = beanClass;
    }

    /*
     * Gets a possibly-localized string for the given input string. 
     * @param input This the the string that may be localizable.  If it is of the form
     * "%foo.bar.Baz%", then the resource Baz will be looked up in the foo.bar bundle using 
     * standard ResourceBundle rules for the default Locale using the control's classloader.  
     * If the input does not start and end with '%', or if the bundle is not located, the string 
     * will be returned verbatim.
     * @return the string to be displayed, or the input string if no resource is found.
     */
    final protected String localizeString(String input)
    {
        if (input == null || !input.startsWith("%") || !input.endsWith("%"))
            return input;
        String bundleName = input.substring(1, input.length()-1);
        String resourceName = null;
        int lastDot = bundleName.lastIndexOf('.');
        while (lastDot != -1 && lastDot != 0 && (lastDot+1 < bundleName.length()))
        {
            // move last element from bundle to resource.  foo.bar.Baz could be the 
            // Baz property in foo.bar, or the bar.Baz property in foo.
            if (resourceName == null)
                resourceName = bundleName.substring(lastDot+1);
            else
                resourceName = bundleName.substring(lastDot+1) + '.' + resourceName;
            bundleName = bundleName.substring(0, lastDot);

            try
            {            
                ResourceBundle bundle = ResourceBundle.getBundle(bundleName, Locale.getDefault(), 
                                                                 _beanClass.getClassLoader());
                if (bundle != null)
                {
                    String lookup = bundle.getString(resourceName);
                    if (lookup != null)
                        return lookup;
                }
            }
            catch (MissingResourceException mre)
            { }
                
            lastDot = bundleName.lastIndexOf('.');
        }

        return input;
    }

    Class   _beanClass;
}
