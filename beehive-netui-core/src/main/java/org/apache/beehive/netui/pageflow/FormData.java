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
package org.apache.beehive.netui.pageflow;

import org.apache.beehive.netui.pageflow.internal.BaseActionForm;

/**
 * Base class for form beans associated with action methods in {@link PageFlowController}s.  Note that Page Flow actions
 * may take form beans of any type.
 * 
 * @deprecated Page Flow actions can accept form beans of any type; it is not necessary to extend this class.  This
 *     will be removed in the next major version.  In general, it's possible to remove <code>extends FormData</code>
 *     and replace it with <code>implements {@link java.io.Serializable}</code>.
 */
public class FormData extends BaseActionForm
{
}

