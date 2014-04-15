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
package controls; 

import java.sql.SQLException; 
import java.sql.ResultSet;
import org.apache.beehive.controls.system.jdbc.JdbcControl;

/** 
 * This Jdbc Control is used to populate the sample database.
 */ 
@org.apache.beehive.controls.api.bean.ControlExtension
@JdbcControl.ConnectionDriver(databaseDriverClass="org.apache.derby.jdbc.EmbeddedDriver",
		databaseURL="jdbc:derby:c:/Apache/Derby/jdbcSample/jdbcSampleDB;create=true")
public interface PopulateDBCtrl extends JdbcControl
{ 
    /**
     * Create the products table in the database.
     */
    @JdbcControl.SQL(statement="CREATE TABLE products (name VARCHAR(64), description VARCHAR(128), quantity INT)")
    public void createProductsTable() throws SQLException;

    /**
     * Add a row to the products table.
     * @param name String
     * @param description String 
     * @param quantity int
     */
    @JdbcControl.SQL(statement="INSERT INTO products VALUES ({name}, {description}, {quantity})")
    public void addProductRow(String name, String description, int quantity) throws SQLException;

    /**
     * Returns the number of rows in the products table.  
     * @return 
     */
    @JdbcControl.SQL(statement="SELECT name FROM products WHERE name='apple'")
    public String isProductsTableEmpty() throws SQLException;
    
    static final long serialVersionUID = 1L;
}
