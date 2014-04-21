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
package org.apache.beehive.controls.system.ejb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.beehive.controls.api.bean.AnnotationMemberTypes;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.AnnotationConstraints.MembershipRule;
import org.apache.beehive.controls.api.bean.AnnotationConstraints.MembershipRuleValues;
import org.apache.beehive.controls.api.properties.PropertySet;

/**
 * Enterprise Java Bean Control base interface
 */
@ControlInterface (defaultBinding="org.apache.beehive.controls.system.ejb.EJBControlImpl")
public interface EJBControl
{
    /**
     * EJBHome specifies the target EJB's home interface for the EJB control using the following attributes:
     * <ul>
     * <li><b>jndiName</b> specifies the JNDI name of the target EJB's home interface
     *   (e.g. EJBNameHome).  This value may also be an URL using the "JNDI:" 
     *   protocol (e.g. jndi://username:password@host:port/EJBNameHome).
     * </li>
     * <li><b>ejbLink</b> specifies the name of the target EJB using the application
     *   relative path to the EJB JAR.  This syntax causes the runtime to
     *   use an application scoped name when locating the referenced EJB.  
     *   The naming syntax is jarfile.jar#ejb-name (e.g. ejbModule.jar#HelloBean).
     * </li>
     * </ul>
     * An EJB Control in a web application would reference an EJB type using the
     * fully qualified name of the control interface with the suffix "jcx".  For example,
     * a control of type <code>controls.HelloEjbControl</code> would resolve the EJB using
     * the following entry in web.xml:
     * <pre>
     * &lt;ejb-ref>
     *     &lt;ejb-ref-name>controls.HelloEjbControl.jcx&lt;/ejb-ref-name>
     *     &lt;ejb-ref-type>Session&lt;/ejb-ref-type>
     *     &lt;home>ejbs.HelloBeanHome&lt;/home>
     *     &lt;remote>ejbs.HelloBeanRemote&lt;/remote>
     *     &lt;ejb-link>ejbModule.jar#HelloBean&lt;/ejb-link>
     * &lt;/ejb-ref>
     * </pre>
     */
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})  // allow override on declaration
    @MembershipRule(MembershipRuleValues.EXACTLY_ONE)
    public @interface EJBHome
    {
        String jndiName() default "";
        String ejbLink()  default "";
    }

    /**
     * JNDIContextEnv specifies the environment properties for the JNDI context that will
     * be used to lookup the target EJB.  This attribute is optional.  If you are using 
     * an URL with the "JNDI:" protocol or if you want to use a JNDI context with the 
     * default envirnoment properties, you do not need a specify any values for this attribute. 
     */
    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})  // allow override on declaration
    public @interface JNDIContextEnv
    {
        @AnnotationMemberTypes.Optional
        String contextFactory() default "";

        @AnnotationMemberTypes.Optional
        String providerURL() default "";

        @AnnotationMemberTypes.Optional
        String principal() default "";

        @AnnotationMemberTypes.Optional
        String credentials() default "";
    }

    /**
     * Returns an instance of the home interface associated with
     * the target bean component.
     */
    public Object getEJBHomeInstance();

    /**
     * Returns true if the EJB control currently has a target bean instance
     * upon which bean business interface methods may be invoked.  This will
     * be true after a successful create() or single select finder method
     * execution, or in cases where implicit creation or find has occurred
     * by the control on the control users behalf.  This provides a simple
     * way to procedurally check the status of explicit or implicit
     * bean instance creation or find operations.
     */
    public boolean  hasEJBBeanInstance();

    /**
     * Returns the current target instance of the bean business interface
     * used for business interface method invocations.  This API is
     * provided for advanced use cases were direct access to the local/
     * remote interfaces outside of the control is required.  It will
     * return <code>null</code> if no target instance is currently
     * selected.
     */
    public Object  getEJBBeanInstance();

    /**
     * Returns the last EJB exception serviced by the EJB control on the
     * developers behalf.  This can be used to discover or log additional
     * information, for example when a create or find method is unable to
     * locate a target bean instance.
     */
    public Throwable getEJBException();
}
