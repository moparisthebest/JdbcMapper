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

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;

/**
 * The AdaptorPersistenceDelegate class supports the XML persistance of Control Client Event 
 * Adaptor instances by implementing the <code>java.beans.PersistenceDelegate</b> API, and 
 * overriding the default persistance algorithm based upon the runtime structure for Controls.
 */
public class AdaptorPersistenceDelegate extends DefaultPersistenceDelegate
{
    /**
     * PersistenceDelegate.instantiate()
     */
    protected Expression instantiate(Object oldInstance, Encoder out)
    {
        if (! (oldInstance instanceof EventAdaptor))
            return super.instantiate(oldInstance, out);

        //
        // An implementation instance is actually constructed at decode time by calling
        // ControlBean.ensureControl on the parent bean.  This will create a new impl
        // instance and run the impl initializer on it.
        //
        return new Expression(oldInstance, oldInstance.getClass(), "new", 
                              new Object[] { ((EventAdaptor)oldInstance).getClient() });
    }

    /**
     * PersistenceDelegate.initialize()
     */
    protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder out)
    {
        super.initialize(type, oldInstance, newInstance, out);
    }

    /**
     * PersistenceDelegate.writeObject()
     */
    public void writeObject(Object oldInstance, Encoder out)
    {
        super.writeObject(oldInstance, out);
    }
}
