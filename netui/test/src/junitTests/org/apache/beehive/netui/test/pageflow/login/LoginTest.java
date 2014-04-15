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
package org.apache.beehive.netui.test.pageflow.login;

import junit.framework.TestSuite;
import junit.framework.Test;
import org.apache.beehive.netui.test.pageflow.MockPageFlowTestCase;
import org.apache.beehive.netui.test.util.config.TestXmlInputStreamResolver;
import org.apache.beehive.netui.util.xml.XmlInputStreamResolver;

/**
 * Tests of swapping in a LoginHandler and accessing it through base class methods on FlowController.
 */
public class LoginTest
        extends MockPageFlowTestCase
{
    /**
     * Test successful login.
     */
    public void testLoginSuccess()
    {
        addUpdateExpression("actionForm.username", "goodusername");
        addUpdateExpression("actionForm.password", "goodpassword");
        runAction("login");
        verifyForward("success");
        verifyActionOutput("userPrincipal", new TestLoginHandler.UserPrincipal());
        verifyActionOutput("isUserInGoodrole", Boolean.TRUE);
        verifyActionOutput("isUserInBadrole", Boolean.FALSE);
    }

    /**
     * Test unsuccessful login.
     */
    public void testLoginFailure()
    {
        addUpdateExpression("actionForm.username", "badusername");
        addUpdateExpression("actionForm.password", "badpassword");
        runAction("login");
        verifyForwardPath("failure.doesnotexist");
    }

    /**
     * Test logout.
     */
    public void testLogout()
    {
        runAction("logout");
        verifyForward("success");
        verifyActionOutput("userPrincipal", null);
        verifyActionOutput("isUserInGoodrole", Boolean.FALSE);
    }

    protected XmlInputStreamResolver getOverrideConfigResolver() {
        return new TestXmlInputStreamResolver("org/apache/beehive/netui/test/pageflow/login/test-beehive-netui-config.xml");
    }

    public LoginTest(String name)
    {
        super(name);
    }

    protected String getPageFlowClassName()
    {
        return LoginController.class.getName();
    }

    public static Test suite()
    {
        return new TestSuite(LoginTest.class);
    }
}
