-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--      http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

-- Drop existing tables in proper order
drop table OrderItems;
drop table Orders;
drop table Addresses;
drop table Accounts;
drop table Items;
drop table Products;
drop table Categories;

-- Create tables in proper order
create table Categories(catId varchar(10) not null primary key, name varchar(20) not null, image varchar(20) not null, description varchar(100) not null);
create table Products(productId varchar(10) not null primary key, catID varchar(10) references Categories(catId) not null, name varchar(20) not null, image varchar(30) not null, description varchar(100) not null);
create table Items(itemId varchar(10) not null primary key, productId varchar(10) references Products(productId) not null, listPrice numeric(8,2) not null, unitCost numeric(8,2) not null, supplier int not null, status varchar(10) not null, attr1 varchar(20), inventoryQuantity int not null);
create table Accounts(userId varchar(20) not null primary key, password varchar(20) not null, email varchar(50) not null, firstName varchar(20) not null, lastName varchar(30) not null, status varchar(20) not null, favCategory varchar(10) not null references Categories(catId), langPref varchar(20), bannerData varchar(20), myListOpt smallint not null, bannerOpt smallint not null);

-- Derby Specific
create table Addresses(addressId int not null GENERATED ALWAYS AS IDENTITY primary key, userId varchar(20) references Accounts(userId) not null, name varchar(100) not null, phone char(12) not null, addr1 varchar(100) not null, addr2 varchar(100), city varchar(100) not null, state varchar(20) not null, zip varchar(12) not null, country varchar(20) not null);
create table Orders(orderId int not null GENERATED ALWAYS AS IDENTITY primary key, userId varchar(20) references Accounts(userId), orderDate timestamp not null DEFAULT current_timestamp, totalPrice double not null, creditCard varchar(20) not null, exprDate varchar(20) not null, cardType varchar(20) not null, status varchar(10) not null, shippingAddress int references Addresses(addressId) not null, billingAddress int references Addresses(addressId) not null);

-- PostgreSQL Specific
-- create table Addresses(addressId serial primary key, userId varchar(20) references Accounts(userId) not null, name varchar(100) not null, phone char(12) not null, addr1 varchar(100) not null, addr2 varchar(100), city varchar(100) not null, state varchar(20) not null, zip varchar(12) not null, country varchar(20) not null);
-- create table Orders(orderId serial primary key, userId varchar(20) references Accounts(userId), orderDate timestamp not null DEFAULT current_timestamp, totalPrice numeric(8,2) not null, creditCard varchar(20) not null, exprDate varchar(20) not null, cardType varchar(20) not null, status varchar(10) not null, shippingAddress int references Addresses(addressId) not null, billingAddress int references Addresses(addressId) not null);

create table OrderItems(orderId int not null references Orders(orderId), itemId varchar(10) not null references Items(ItemId), quantity int not null, primary key (orderId, itemId));

-- Add data to tables
insert into Categories values('FISH', 'Fish', 'fish_icon.gif', 'Saltwater, Freshwater');
insert into Categories values('DOGS', 'Dogs', 'dogs_icon.gif', 'Various Breeds');
insert into Categories values('REPTILES', 'Reptiles', 'reptiles_icon.gif', 'Lizards, Turtles, Snakes');
insert into Categories values('CATS', 'Cats', 'cats_icon.gif', 'Various Breeds, Exotic Varieties');
insert into Categories values('BIRDS', 'Birds', 'birds_icon.gif', 'Exotic Varieties');

insert into Accounts (userId, password, email, firstName, lastName, status, favCategory, langPref, bannerData, myListOpt, bannerOpt) values ('beehive', 'beehive', 'yourname@yourdomain.com', 'Joe', 'User', 'OK', 'DOGS', 'ENGLISH', '', 1, 1);

insert into Addresses  (name, userId, addr1, addr2, city, state, zip, country, phone) values ('Home', 'beehive', '901 San Antonio Road', 'MS UCUP02-206', 'Palo Alto', 'CA', '94303', 'USA', '555-555-5555');

insert into Products values ('AV-CB-01', 'BIRDS', 'Amazon Parrot', 'bird2.jpg', 'Great companion for up to 75 years');
insert into Products values ('AV-SB-02', 'BIRDS', 'Finch', 'bird1.jpg', 'Great stress reliever');
insert into Products values ('FI-FW-01', 'FISH', 'Koi', 'fish3.jpg', 'Fresh Water fish from Japan');
insert into Products values ('FI-FW-02', 'FISH', 'Goldfish', 'fish2.jpg', 'Fresh Water fish from China');
insert into Products values ('FI-SW-01', 'FISH', 'Angelfish', 'fish1.jpg', 'Salt Water fish from Australia');
insert into Products values ('FI-SW-02', 'FISH', 'Tiger Shark', 'fish4.jpg', 'Salt Water fish from Australia');
insert into Products values ('FL-DLH-02', 'CATS', 'Persian', 'cat1.jpg', 'Friendly house cat, doubles as a princess');
insert into Products values ('FL-DSH-01', 'CATS', 'Manx', 'cat2.jpg', 'Great for reducing mouse populations');
insert into Products values ('K9-BD-01', 'DOGS', 'Bulldog', 'dog1.jpg', 'Friendly dog from England');
insert into Products values ('K9-CW-01', 'DOGS', 'Chihuahua', 'dog2.jpg', 'Great companion dog');
insert into Products values ('K9-DL-01', 'DOGS', 'Dalmation', 'dog3.jpg', 'Great dog for a Fire Station');
insert into Products values ('K9-PO-02', 'DOGS', 'Poodle', 'dog6.jpg', 'Cute dog from France');
insert into Products values ('K9-RT-01', 'DOGS', 'Golden Retriever', 'dog4.jpg', 'Great family dog');
insert into Products values ('K9-RT-02', 'DOGS', 'Labrador Retriever', 'dog5.jpg', 'Great hunting dog');
insert into Products values ('RP-LI-02', 'REPTILES', 'Iguana', 'lizard1.jpg', 'Friendly green friend');
insert into Products values ('RP-SN-01', 'REPTILES', 'Rattlesnake', 'snake1.jpg', 'Doubles as a watch dog');

insert into Items values ('EST-1', 'FI-SW-01', 16.50, 10.00, 1, 'P', 'Large', 10000);
insert into Items values ('EST-2', 'FI-SW-01', 16.50, 10.00, 1, 'P', 'Small', 10000);
insert into Items values ('EST-3', 'FI-SW-02', 18.50, 12.00, 1, 'P', 'Toothless', 10000);
insert into Items values ('EST-4', 'FI-FW-01', 18.50, 12.00, 1, 'P', 'Spotted', 10000);
insert into Items values ('EST-5', 'FI-FW-01', 18.50, 12.00, 1, 'P', 'Spotless', 10000);
insert into Items values ('EST-6', 'K9-BD-01', 18.50, 12.00, 1, 'P', 'Male Adult', 10000);
insert into Items values ('EST-7', 'K9-BD-01', 18.50, 12.00, 1, 'P', 'Female Puppy', 10000);
insert into Items values ('EST-8', 'K9-PO-02', 18.50, 12.00, 1, 'P', 'Male Puppy', 10000);
insert into Items values ('EST-9', 'K9-DL-01', 18.50, 12.00, 1, 'P', 'Spotless Male Puppy', 10000);
insert into Items values ('EST-10', 'K9-DL-01', 18.50, 12.00, 1, 'P', 'Spotted Adult Female', 10000);
insert into Items values ('EST-11', 'RP-SN-01', 18.50, 12.00, 1, 'P', 'Venomless', 10000);
insert into Items values ('EST-12', 'RP-SN-01', 18.50, 12.00, 1, 'P', 'Rattleless', 10000);
insert into Items values ('EST-13', 'RP-LI-02', 18.50, 12.00, 1, 'P', 'Green Adult', 10000);
insert into Items values ('EST-14', 'FL-DSH-01', 58.50, 12.00, 1, 'P', 'Tailless', 10000);
insert into Items values ('EST-15', 'FL-DSH-01', 23.50, 12.00, 1, 'P', 'With tail', 10000);
insert into Items values ('EST-16', 'FL-DLH-02', 93.50, 12.00, 1, 'P', 'Adult Female', 10000);
insert into Items values ('EST-17', 'FL-DLH-02', 93.50, 12.00, 1, 'P', 'Adult Male', 10000);
insert into Items values ('EST-18', 'AV-CB-01', 193.50, 92.00, 1, 'P', 'Adult Male', 10000);
insert into Items values ('EST-19', 'AV-SB-02', 15.50, 2.00, 1, 'P', 'Adult Male', 10000);
insert into Items values ('EST-20', 'FI-FW-02', 5.50, 2.00, 1, 'P', 'Adult Male', 10000);
insert into Items values ('EST-21', 'FI-FW-02', 5.29, 1.00, 1, 'P', 'Adult Female', 10000);
insert into Items values ('EST-22', 'K9-RT-02', 135.50, 100.00, 1, 'P', 'Adult Male', 10000);
insert into Items values ('EST-23', 'K9-RT-02', 145.49, 100.00, 1, 'P', 'Adult Female', 10000);
insert into Items values ('EST-24', 'K9-RT-02', 255.50, 92.00, 1, 'P', 'Adult Male', 10000);
insert into Items values ('EST-25', 'K9-RT-02', 325.29, 90.00, 1, 'P', 'Adult Female', 10000);
insert into Items values ('EST-26', 'K9-CW-01', 125.50, 92.00, 1, 'P', 'Adult Male', 10000);
insert into Items values ('EST-27', 'K9-CW-01', 155.29, 90.00, 1, 'P', 'Adult Female', 10000);
insert into Items values ('EST-28', 'K9-RT-01', 155.29, 90.00, 1, 'P', 'Adult Female', 10000);
