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
package org.apache.beehive.samples.netui.beans;

import java.util.Date;

/**
 * Simple JavaBean representing a Pet.
 */
public class PetType {

    private int _petId;
    private String _name;
    private String _description;
    private double _price;
    private Date _purchaseDate;

    public PetType() {}

    public PetType(int petID, String name, String description, double price) {
        this(petID, name, description, price, new Date());
    }

    public PetType(int petID, String name, String description, double price, Date purchaseDate) {
        _petId= petID;
        _name = name;
        _description = description;
        _price = price;
        _purchaseDate = purchaseDate;
    }

    public int getPetId() {
        return _petId;
    }

    public void setPetId(int petId) {
        _petId = petId;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public double getPrice() {
        return _price;
    }

    public void setPrice(double price) {
        _price = price;
    }

    public Date getPurchaseDate() {
        return _purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        _purchaseDate = purchaseDate;
    }
}