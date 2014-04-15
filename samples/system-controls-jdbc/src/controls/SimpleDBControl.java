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
 * JdbcControl implementation for the JdbcControl sample app.
 * 
 * 
 * The following @JdbcControl.ConnectionDriver works
 * @JdbcControl.ConnectionDriver(databaseDriverClass="org.apache.derby.jdbc.EmbeddedDriver",
 *	databaseURL="jdbc:derby:c:/Apache/Derby/jdbcSample/jdbcSampleDB;create=true")  
 *
 *
@JdbcControl.ConnectionDataSource(jndiName="java:comp/env/jdbc/JdbcControlSampleDB")
 *
 * 
 */ 
@org.apache.beehive.controls.api.bean.ControlExtension
@JdbcControl.ConnectionDriver(databaseDriverClass="org.apache.derby.jdbc.EmbeddedDriver",
  databaseURL="jdbc:derby:C:/apache/derby/jdbcSample/JdbcControlSampleDB;create=true")  
public interface SimpleDBControl extends JdbcControl
{ 

    static public class Product 
    { 
        private String _name; 
        private String _description;
        private int _quantity; 

        public int getQuantity() { return _quantity; }
        public void setQuantity(int i) { _quantity = i; }

        public String getName() { return _name; }
        public void setName(String n) { _name = n; }

        public String getDescription() { return _description; }
        public void setDescription(String n) { _description = n; }
    }

    /**
     * Get the name column from the products table.
     * @return An array of strings.
     */
    @JdbcControl.SQL(statement="SELECT name FROM products")
    public String[] getProductNames() throws SQLException;

    /**
     * Get the rest of the columns associated with a product name.
     * @param productName Name of product to lookup.
     * @return An instance of Product.
     */
    @JdbcControl.SQL(statement="SELECT * FROM products WHERE name={productName}")
    public Product getProductDetails(String productName) throws SQLException;
   
    static final long serialVersionUID = 1L;
}
