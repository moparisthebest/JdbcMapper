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
package org.apache.beehive.samples.controls.pets;

import org.apache.beehive.samples.netui.beans.PetType;
import org.apache.beehive.controls.api.bean.ControlImplementation;

import java.util.Date;

@ControlImplementation(isTransient=true)
public class PetsImpl
    implements Pets
{
    public String hello() {
        return "hello!";
    }

    public PetType[] getPetList() {
        PetType[] petlist = {
            new PetType(1, "American Tabby", "Cat", 20.00),
            new PetType(2, "Short Haired", "Cat", 20.00),
            new PetType(3, "Long Haired", "Cat", 20.00),
            new PetType(4, "Blue Russian", "Cat", 80.00),
            new PetType(5, "Pixy Bob", "Cat", 20.00),
            new PetType(6, "Siamese", "Cat", 80.00),
            new PetType(7, "Minx", "Cat", 20.00),
            new PetType(8, "Bull Mastif", "Dog", 350.00),
            new PetType(9, "Dalmatian", "Dog", 20.00)
        };

        return petlist;
    }
}


