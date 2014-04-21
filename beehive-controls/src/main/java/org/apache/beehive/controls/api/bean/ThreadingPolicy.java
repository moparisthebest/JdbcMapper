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
package org.apache.beehive.controls.api.bean;

/**
 * Specifies threading policy for control implementations.  The constants
 * of this enumerated type describe the threading policies that apply
 * during execution of controls.  They are used in conjunction with the
 * {@link Threading} annotation type to specify the responsibilities of
 * the runtime infrastructure and control implementation with respect to
 * threading.
 */
public enum ThreadingPolicy
{
    /**
     * When a control implementation is declared as SINGLE_THREADED, the 
     * controls infrastructure ensures that only a single thread will be
     * executing in a particular instance of that control at any time.
     * This is the default policy if no {@link Threading} annotation is
     * specified.
     */
    SINGLE_THREADED,
    /**
     * When a control implementation is declared as MULTI_THREADED, the
     * controls infrastructure permits multiple threads to concurrently
     * execute in instances of that control.  It is then the responsibility
     * of the implementation to ensure internal thread-safety using
     * standard Java concurrency mechanisms.  This policy may yield higher
     * performance, at the cost of additional work on the implementor's part.
     */
    MULTI_THREADED
}
