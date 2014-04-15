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
package org.apache.beehive.controls.system.ejb.sample.client;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.apache.beehive.controls.system.ejb.sample.bean.HelloHome;
import org.apache.beehive.controls.system.ejb.sample.bean.HelloRemote;

public class HelloBeanClient
{

    /**
     * 
     */
    public HelloBeanClient()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static void main(String[] args)
    {
        try
        {
            Context ctx = null;
            Hashtable ht = new Hashtable();
            ht.put(Context.INITIAL_CONTEXT_FACTORY,
                    "weblogic.jndi.WLInitialContextFactory");
            ht.put(Context.PROVIDER_URL, "t3://localhost:7001");
            ht.put(Context.SECURITY_PRINCIPAL, "weblogic");
            ht.put(Context.SECURITY_CREDENTIALS, "weblogic");
            ctx = new InitialContext(ht);
            
            //Object objref = ctx.lookup("java:comp/env/ejb/messageBuffer.TestHome");
            Object objref = ctx.lookup("org.apache.beehive.controls.system.ejb.sample.HelloHome");
            HelloHome home = (HelloHome) PortableRemoteObject.narrow(objref,
                    HelloHome.class);

            HelloRemote hello = (HelloRemote) PortableRemoteObject.narrow(home.create(), HelloRemote.class);
            String txt = hello.hello();
            System.out.println("Message returned from Hello Bean: " + txt);
        }
        catch (Exception ex)
        {
            System.err.println("Caught an unexpected exception!");
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
