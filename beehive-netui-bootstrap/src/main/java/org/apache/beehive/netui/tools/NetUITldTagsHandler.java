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
package org.apache.beehive.netui.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import xdoclet.DocletSupport;
import xdoclet.XDocletException;
import xdoclet.XDocletMessages;
import xdoclet.tagshandler.AbstractProgramElementTagsHandler;
import xdoclet.tagshandler.TypeTagsHandler;
import xdoclet.util.Translator;
import xdoclet.util.TypeConversionUtil;

import xjavadoc.XTag;
import xjavadoc.XProgramElement;
import xjavadoc.XMethod;
import xjavadoc.XClass;
import xjavadoc.XMember;

/**
 *
 * @xdoclet.taghandler namespace="NetUITldGen"
 */
public class NetUITldTagsHandler
    extends AbstractProgramElementTagsHandler
{
    private static final Log logger = LogFactory.getLog(NetUITldTagsHandler.class);
    private static final boolean DEBUG = true; //logger.isDebugEnabled();

    private static final String NETUI_ATTRIBUTE = "netui:attribute";
    private static final String NETUI_TLDX_ATTRIBUTE = "netui.tldx:attribute";

    private static final String EMPTY_STRING = "";

    private String currentTagName;

    /**
     * @doc:tag type="block"
     * @doc:param name="paramName" description="The parameter name"
     */
    public void ifHasTagValue(String template, Properties attributes) 
        throws XDocletException
    {
        String value = paramValue(attributes);
        if(value != null)
            generate(template);
    }

    /**
     * @doc:tag type="content"
     * @doc:param name="paramName" description="The parameter name"
     * @doc.param name="values" 
     *            description="The valid values for the parameter, comma separated. An error message is printed if the parameter value is not one of the values."
     */
    public String paramValue(Properties attributes)
        throws XDocletException
    {
        XTag cTag = getCurrentClassTag();
        XTag mTag = getCurrentMethodTag();

        String validValues = attributes.getProperty("values");
        String paramName = attributes.getProperty("paramName");
        String tagName = getCurrentTagName();

        if(DEBUG) logger.debug("current class: " + getCurrentClass().getName());
        if(DEBUG) logger.debug("cTag value for attribute \"" + 
                                            attributes.getProperty("paramName") + "\" is " + 
                                            (cTag != null ? cTag.getAttributeValue(attributes.getProperty("paramName")) : "null"));

        XProgramElement member = null;
        String value = null;
        if(cTag != null)
        {
            value = cTag.getAttributeValue(paramName);
            member = getCurrentClass();
        }
        else if(mTag != null) 
        {
            value = mTag.getAttributeValue(paramName);
            member = getCurrentMethod();
        }

        // a value was found. perform sanity checks on valid values
        if (validValues != null) 
        {
            if(DEBUG) logger.debug("validValues: " + validValues);
            // check if the value is among the valid values
            StringTokenizer st = new StringTokenizer(validValues, ",");
            boolean valid = false;
            while (st.hasMoreTokens()) 
            {
                if (st.nextToken().equals(value)) 
                    valid = true;
            }

            if(!valid)
            {
                if(DEBUG) logger.debug("FOUND AN INVALID VALUE: " + value);

                if(member instanceof XMethod)
                {
                    throw new XDocletException
                        (Translator.getString(XDocletMessages.class, 
                                              XDocletMessages.INVALID_TAG_PARAM_VALUE_METHOD,
                                              new String[]{value, 
                                                           paramName, 
                                                           tagName, 
                                                           ((XMethod)member).getName(), 
                                                           ((XMethod)member).getContainingClass().getQualifiedName(), 
                                                           validValues}));
                }
                else if(member instanceof XClass)
                {
                    throw new XDocletException(Translator.getString(XDocletMessages.class, 
                                                                    XDocletMessages.INVALID_TAG_PARAM_VALUE_CLASS,
                                                                    new String[]{value, 
                                                                                 paramName, 
                                                                                 tagName, ((XClass)member).getQualifiedName(), 
                                                                                 validValues}));
                }
            }

            // it's ridiculous that this is private in the base class
            //invalidParamValueFound(doc, paramName, "", value, validValues);
        }
        
        return value;
    }

    /**
     * @doc.tag                     type="block"
     * @doc.param                   name="abstract" optional="true" values="true,false" description="If true then accept
     *      abstract classes also; otherwise don't."
     * @doc.param                   name="type" optional="true" description="For all classes by the type."
     * @doc.param                   name="extent" optional="true" values="concrete-type,superclass,hierarchy"
     *      description="Specifies the extent of the type search. If concrete-type then only check the concrete type, if
     *      superclass then check also superclass, if hierarchy then search the whole hierarchy and find if the class is
     *      of the specified type. Default is hierarchy."
     */
    public void forAllClasses(String template, Properties attributes)
        throws XDocletException
    {
        String abstractStr = attributes.getProperty("abstract");
        boolean acceptAbstractClasses = TypeConversionUtil.stringToBoolean(abstractStr, true);
        String typeName = attributes.getProperty("type");
        String extentStr = attributes.getProperty("extent");
        int extent = TypeTagsHandler.extractExtentType(extentStr);

        Object obj = getDocletContext().getConfigParam(getDocletContext().getActiveSubTask().getSubTaskName() + ".packageName");
        String packageName = (obj != null && !obj.equals(EMPTY_STRING) ? obj.toString() : null);

        if (DEBUG) 
        {
            logger.debug("filter on package name: " + packageName);
            logger.debug("acceptAbstractClasses=" + acceptAbstractClasses);
            logger.debug("typeName=" + typeName);
            logger.debug("extentStr=" + extentStr);
            logger.debug("extent=" + extent);
        }

        //System.out.println("packageName: " + packageName);

        Collection classes = getAllClasses();

        // sort alphabetically
        Iterator i = sort(classes.iterator());
        while(i.hasNext())
        {
            XClass currentClass = (XClass)i.next();
            
            if(packageName != null && !currentClass.getQualifiedName().startsWith(packageName))
                continue;

             //System.out.println("currentClass=" + currentClass.getQualifiedName());
             //System.out.println(" packageName: " + packageName);
             //System.out.println(" startsWith: " + currentClass.getQualifiedName().startsWith(packageName));

            setCurrentClass(currentClass);

            if (DocletSupport.isDocletGenerated(getCurrentClass()) || (getCurrentClass().isAbstract() && acceptAbstractClasses == false)) 
            {
                logger.debug("isDocletGenerated or isAbstract");
                continue;
            }

            if (typeName != null) 
            {
                if (TypeTagsHandler.isOfType(currentClass, typeName, extent)) 
                {
                    if(DEBUG) {
                        logger.debug("isOfType true, generate().");
                        logger.debug("handling type: "  + currentClass.getQualifiedName());
                    }

                    generate(template);
                }
                else if(DEBUG) logger.debug("isOfType false, generate().");
            }
            else 
            {
                if(DEBUG) logger.debug("typeName=null, generate().");
                generate(template);
            }
        }
    }

    /**
     * @doc.tag                     type="block"
     * @doc.param                   name="abstract" optional="true" values="true,false" description="If true then accept abstract classes also; otherwise don't."
     * @doc.param                   name="type" optional="true" description="For all classes by the type."
     * @doc.param                   name="extent" optional="true" values="concrete-type,superclass,hierarchy"
     *      description="Specifies the extent of the type search. If concrete-type then only check the concrete type, if
     *      superclass then check also superclass, if hierarchy then search the whole hierarchy and find if the class is
     *      of the specified type. Default is hierarchy."
     */
    public void forAllFunctions(String template, Properties attributes)
        throws XDocletException
    {
        String abstractStr = attributes.getProperty("abstract");
        boolean acceptAbstractClasses = TypeConversionUtil.stringToBoolean(abstractStr, true);
        String typeName = attributes.getProperty("type");
        String extentStr = attributes.getProperty("extent");
        int extent = TypeTagsHandler.extractExtentType(extentStr);

        Object obj = getDocletContext().getConfigParam(getDocletContext().getActiveSubTask().getSubTaskName() + ".functionPackage");
        String packageName = (obj != null && !obj.equals(EMPTY_STRING) ? obj.toString() : null);

        if (DEBUG)
        {
            logger.debug("filter on package name: " + packageName);
            logger.debug("acceptAbstractClasses=" + acceptAbstractClasses);
            logger.debug("typeName=" + typeName);
            logger.debug("extentStr=" + extentStr);
            logger.debug("extent=" + extent);
        }

        //System.out.println("packageName: " + packageName);

        Collection classes = getAllClasses();

        // sort alphabetically
        Iterator i = sort(classes.iterator());
        while(i.hasNext())
        {
            XClass currentClass = (XClass)i.next();

            if(packageName != null && !currentClass.getQualifiedName().startsWith(packageName))
                continue;

             //System.out.println("currentClass=" + currentClass.getQualifiedName());
             //System.out.println(" packageName: " + packageName);
             //System.out.println(" startsWith: " + currentClass.getQualifiedName().startsWith(packageName));

            setCurrentClass(currentClass);

            if (DocletSupport.isDocletGenerated(getCurrentClass()) || (getCurrentClass().isAbstract() && acceptAbstractClasses == false))
            {
                logger.debug("isDocletGenerated or isAbstract");
                continue;
            }

            if (typeName != null)
            {
                if (TypeTagsHandler.isOfType(currentClass, typeName, extent))
                {
                    if(DEBUG) {
                        logger.debug("isOfType true, generate().");
                        logger.debug("handling type: "  + currentClass.getQualifiedName());
                    }

                    generate(template);
                }
                else if(DEBUG) logger.debug("isOfType false, generate().");
            }
            else
            {
                if(DEBUG) logger.debug("typeName=null, generate().");
                generate(template);
            }
        }
    }

    /**
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc:tag                     type="block"
     * @doc:tag name="tagName" optional="false" values="netui:jspfunction"
     */
    public void forAllMethods(String template, Properties attributes)
        throws XDocletException {
        String tagName = attributes.getProperty("tagName");

        if(DEBUG)
            logger.debug("Handling tagName: " + tagName);

        setCurrentTagName(tagName);

        try {
            XClass currentClass = getCurrentClass();

            if (currentClass == null)
                throw new XDocletException("currentClass == null!!!");

            Collection members = currentClass.getMethods(true);

            List sortedMembers = new ArrayList(members);
            Collections.sort(sortedMembers, memberComparator);
            members = sortedMembers;

            Iterator methods = members.iterator();
            while(methods.hasNext())
            {
                XMethod xm = (XMethod)methods.next();

                if(DEBUG)
                    logger.debug("handle method: " + xm.getName());

                XTag attribute = getFirstTag(getCurrentClass(), xm.getName(), tagName);

                setCurrentMethod(xm);
                setCurrentMethodTag(attribute);

                if(getCurrentMethod() != null && attribute != null)
                    generate(template);

                setCurrentMethodTag(null);
                setCurrentClassTag(null);
                setCurrentMethod(null);
            }
            setCurrentClass(currentClass);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new XDocletException(e, "An error occurred in the forAllAttributes tag: " + e);
        }
    }

    /**
     * @param template              Describe what the parameter does
     * @param attributes            Describe what the parameter does
     * @exception XDocletException  Describe the exception
     * @doc:tag                     type="block"
     * @doc:tag name="tagName" optional="false" values="netui.tldx:attribute,netui-tld:attribute"
     * @doc:param name="superclasses" optional="true" values="true,false" 
     *            description="If true then traverse superclasses also, otherwise look up the tag in current concrete class only."
     */
    public void forAllAttributes(String template, Properties attributes) throws XDocletException
    {
        String tagName = attributes.getProperty("tagName");
        
        if(DEBUG) logger.debug("Handling tagName: " + tagName);
        //System.out.println("type: " + getCurrentClass().getQualifiedName());

        setCurrentTagName(tagName);
        
        boolean genTldx = getCurrentTagName().equals(NETUI_TLDX_ATTRIBUTE);

        try
        {
            XClass currentClass = getCurrentClass();
            
            if(DEBUG) 
            {
                logger.debug("handle class: " + currentClass.getName());
                XClass parent = currentClass.getSuperclass();
                while(parent != null)
                {
                    logger.debug("superclass: " + parent.getName());
                    if(parent.getName().equals("DataSourceTag"))
                    {
                        Iterator tmp = parent.getMethods().iterator();
                        if(tmp.hasNext())
                        {
                            logger.debug("found methods on DataSourceTag");
                            while(tmp.hasNext())
                            {
                                logger.debug("method: " + tmp.next());
                            }
                        }
                        else logger.debug("found NO methods on DataSourceTag");
                    }
                    
                    parent = parent.getSuperclass();
                }
            }

            if (currentClass == null) 
                throw new XDocletException("currentClass == null!!!");

            // need to check all methods
            Collection members = currentClass.getMethods(true);
            
            // sort fields, but we should make a copy first, because members is not a new copy, it's shared by all
            List sortedMembers = new ArrayList(members);
            Collections.sort(sortedMembers, memberComparator);
            members = sortedMembers;
            
            // for all XMethods
            Iterator methods = members.iterator();
            while(methods.hasNext())
            {
                XMethod xm = (XMethod)methods.next();
                
                if(DEBUG) logger.debug("handle method: " + xm.getName());
                
                // if(DEBUG) logger.debug("found @jsp:attribute tag on method: " + tldAttribute);
                
                // to create a TLD entry
                // 1) @netui:attribute
                // 2) @netui:attribute name="propName" -- use this for a class-level attribute when there can't be a method level one

                // to create a TLDX entry
                // 0) @netui:attribute -- there are not TLDX properties, but the attribute still needs to be created
                // 1) @netui.tldx:attribute -- specify TLDX properties
                // 2) @netui.tldx:attribute name="propName" -- use this for a class-level attribute when there can't be a method level one

                // get all @netui:attribute annotated methods
                XTag tldAttribute = getFirstTag(getCurrentClass(), xm.getName(), NETUI_ATTRIBUTE);

                //if(tldAttribute == null) continue;
                //setCurrentMethod(xm);

                // @netui.tldx:attribute
                if(tldAttribute != null)
                {
                    if(genTldx)
                    {
                        // if the attribute has been omitted in the TLD, omit in the TLDX
                        // 
                        // @todo: this needs to happen from the class-level
                        // 
                        XTag classTldOverride = getOverrideClassTag(xm.getPropertyName(), NETUI_ATTRIBUTE, getCurrentClass());
                        if(classTldOverride != null && TypeConversionUtil.stringToBoolean(classTldOverride.getAttributeValue("hide"), false))
                            continue;

                        // get the first @netui.tldx:attribute tag if it exists
                        XTag tldxAttribute = getFirstTag(getCurrentClass(), xm.getName(), NETUI_TLDX_ATTRIBUTE);
                        
                        // if(DEBUG) logger.debug("found @netui.tldx:attribute tag on method: " + tldxAttribute);
                        
                        if(tldxAttribute != null)
                            setCurrentMethodTag(tldxAttribute);
                    }
                    // @netui-tld:attribute
                    else if(tldAttribute != null)
                        setCurrentMethodTag(tldAttribute);
                     
                    XTag tag = getOverrideClassTag(xm.getPropertyName(), getCurrentTagName(), getCurrentClass());
                    
                    if(tag != null)
                        setCurrentClassTag(tag);

                    setCurrentMethod(xm);
                }
                else
                {
//                     System.out.println("***** search for class override: " + xm.getPropertyName());
//                     XTag tag = getOverrideClassTag(xm.getPropertyName(), getCurrentTagName(), getCurrentClass());
//                     System.out.println("method: " + xm.getName() + " found class override: " + tag);

//                     if(tag != null)
//                     {
//                         setCurrentMethod(xm);
//                         setCurrentClassTag(tag);
//                     }
                }
                    
                if(getCurrentMethod() != null)
                {
                    if(getCurrentClassTag() == null ||
                       !TypeConversionUtil.stringToBoolean(getCurrentClassTag().getAttributeValue("hide"), false))
                    {
                        generate(template);
                    }
                }

                setCurrentMethodTag(null);
                setCurrentClassTag(null);
                setCurrentMethod(null);
            }
            setCurrentClass(currentClass);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new XDocletException(e, "An error occurred in the forAllAttributes tag: " + e);
        }
    }

    private XTag getOverrideClassTag(String name, String tagName, XClass currentClass)
    {
        // look for a class override for either netui:attribute or netui.tldx:attribute, depending on the current tagName
        Iterator iterator = currentClass.getDoc().getTags(tagName, false).iterator();
        while(iterator.hasNext())
        {
            XTag tag = (XTag)iterator.next();
//             System.out.println("check class-level tag named " + tag.getName() + " with name attribute " + tag.getAttributeValue("name") + " searching for: " + name);
            
            if(tag.getAttributeValue("name").equals(name))
            {
                // if(DEBUG) logger.debug("setting class tag");
                return tag;
            }
        }
        
        return null;
    }

    private XTag getFirstTag(XClass clazz, String methodName, String tagName)
        throws XDocletException
    {
        List methods = clazz.getMethods(false);
        
        //if(DEBUG) logger.debug("getFirstTag from class: " + clazz.getName() + " with method " + methodName + " and tag " + tagName);

        for(int i = 0; i < methods.size(); i++)
        {
            XMethod method = (XMethod)methods.get(i);
            
            //if(DEBUG) logger.debug("check method: " + method.getName());

            if(method.getName().equals(methodName))
            {
                Collection coll = method.getDoc().getTags(tagName, false);
                if(coll.size() == 1)
                {
                    //if(DEBUG) logger.debug("found matching tag; return");
                    // return the matching XTag
                    return (XTag)coll.iterator().next();
                }
                else if(coll.size() == 0 && clazz.getSuperclass() != null)
                {
                    //if(DEBUG) logger.debug("no tag found; check superclass");

                    return getFirstTag(clazz.getSuperclass(), methodName, tagName);
                }
                else 
                {
                    throw new XDocletException("Found " + coll.size() + " tags for the class/method/tag " + clazz.getName() + "/" + methodName + "/" + tagName);
                }
            }
        }

        if(clazz.getSuperclass() != null)
        {
            return getFirstTag(clazz.getSuperclass(), methodName, tagName);
        }

        return null;
    }
    
    private void setCurrentTagName(String tagName)
    {
        currentTagName = tagName;
    }
    
    private String getCurrentTagName()
    {
        return currentTagName;
    }

//     private static final void debug(String msg)
//     {
//         if(DEBUG) 
//             System.out.println(msg);
//     }

    private final static Comparator memberComparator =
        new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                XMember m1 = (XMember) o1;
                XMember m2 = (XMember) o2;

                return m1.getName().compareTo(m2.getName());
            }

            public boolean equals(Object obj)
            {
                // dumb
                return obj == this;
            }
        };

    private Iterator sort(Iterator iterator)
    {
        List sorted = new ArrayList();
        
        while(iterator.hasNext())
        {
            XClass clazz = (XClass)iterator.next();
            
            sorted.add(clazz);
        }

        java.util.Collections.sort(sorted, new java.util.Comparator() 
            {
                public int compare(Object o1, Object o2)
                {
                    return ((XClass)o1).getName().compareTo(((XClass)o2).getName());
                }
                
                public boolean equals(Object obj)
                {
                    if(this == obj)
                        return true;
                    else return false;
                }
            }
                                   );
        
        return sorted.iterator();
    }
}
