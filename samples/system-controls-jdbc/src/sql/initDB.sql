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
drop table products;

-- Create tables in proper order
CREATE TABLE products (name VARCHAR(64), description VARCHAR(128), quantity INT);

-- Derby Specific Statements
-- Place statements here

-- PostgreSQL Specific Statements
-- Place statements here

-- Add data to tables
INSERT INTO products VALUES ('apple', 'red apples', 200);
INSERT INTO products VALUES ('orange', 'orange oranges', 400);
INSERT INTO products VALUES ('kiwi', 'green with black seeds', 800);
INSERT INTO products VALUES ('banana', 'yellow bananas', 123);
INSERT INTO products VALUES ('coconut', 'coconutty', 21);
INSERT INTO products VALUES ('plum', 'purple plums', 999);