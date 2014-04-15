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
 */
package org.apache.beehive.test.controls.system.ejb.entity;

import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.system.ejb.EJBControl;
import org.apache.beehive.controls.system.ejb.EntityEJBControl;

/**
 * This JCX defines a reference to the Entity bean
 */
@ControlExtension
@EJBControl.EJBHome(jndiName ="ejb.EntityBeanRemote", ejbLink="ejb.MockEntityEjbLink")
public interface RemoteEntityEjbControl
    extends EntityHome, EntityRemote, EntityEJBControl
{
    // There a no methods here, the CTRL creates a unified interface that
    // includes the home and remote interfaces to the EJB, plus the
    // EntityEJB control services defined by the EntityEJB control interface.
}
