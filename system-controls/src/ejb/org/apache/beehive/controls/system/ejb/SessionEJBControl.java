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

import org.apache.beehive.controls.api.bean.ControlInterface;

/**
 * As part of the EJB control, this interface simplifies access to
 * session Enterprise JavaBeans (EJBs). You do not need to use
 * this interface directly.
 * <br/><br/>
 * The EJB control is actually made up of two main interfaces,
 * one for access to entity EJBs
 * and another for access to session EJBs. The presence of these
 * two interfaces is invisible when you use the EJB control; their
 * methods are called behind the scenes.
 * <br/><br/>
 * Typically, you use the EJB control by adding the control to a
 * component design (such as a web service or pageflow design),
 * then calling the methods it provides. Those methods are not
 * exposed by these control interfaces, but rather are extensions
 * of the EJB itself that are generated when you add
 * the EJB control.
 * <br/><br/>
 * For more information about using the EJB control, see
 * <a href="../../../../guide/controls/ejb/navEJBControl.html">EJB Control</a>.
 */
@ControlInterface (defaultBinding="org.apache.beehive.controls.system.ejb.SessionEJBControlImpl")
public interface SessionEJBControl
    extends EJBControl
{
}
