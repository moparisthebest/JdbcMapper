/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 */
package org.apache.beehive.controls.system.webservice.utils;

import javax.xml.rpc.holders.Holder;

/**
 * GenericHolder is a JAX-RPC Holder implementation for any class which does not
 * have a holder type defined in the javax.xml.rpc.holders package.
 */
public class GenericHolder<T>
    implements Holder {

    public T value;

    // The blank constructore is made private so that the value is always set in the holder.  The type of the value
    // is used by the service control to determine the type of class conversion it would need to stuff value to the
    // holder from the soap message result.    The only way I have been able to get the type of the Value is by
    // looking at its class, which requires that  for the value class to have been instantiated.
    private GenericHolder() {
    }

    public GenericHolder(T value) {
        this.value = value;
    }
}
