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

package org.apache.beehive.controls.system.jdbc.units.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Stored procedures for testing with Derby
 */
public final class StoredProcedures {


    /**
     * Stored procedure which selects all products whose price is over $1000.00
     * @param outProducts
     * @throws Exception
     */
    public static void getExpensiveProductsSP(String[] outProducts) throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection conn = DriverManager.getConnection("jdbc:derby:MyDB");

        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT PRODUCT_NAME from PRODUCTS WHERE PRICE > 1000.00");

        rs.next();
        outProducts[0] = rs.getString(1);

        rs.close();
        s.close();
        conn.close();
    }

    /**
     * Simple stored procedure which doesn't make a query into the database
     * @param outProduct
     * @throws Exception
     */
    public static void getExpensiveProductSP(String[] outProduct) throws Exception {
        outProduct[0] = "foo";
    }

    /**
     * This stored procedure has an in and an out parameter, queries the db for all product names whose color
     * matches the inColor parameter.
     * @param inColor
     * @param outProducts
     * @throws Exception
     */
    public static void getProductsByColorSP(String inColor, String[] outProducts) throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection conn = DriverManager.getConnection("jdbc:derby:MyDB");

        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT PRODUCT_NAME from PRODUCTS WHERE COLOR='" + inColor +"'");

        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            sb.append(rs.getString(1));
            sb.append(',');
        }

        outProducts[0] = sb.toString();

        rs.close();
        s.close();
        conn.close();
    }

    /**
     *
     * @param inColor
     * @param inSku
     */
    public static void getProductSP(String inColor, int inSku) {
        // noop
        assert "red".equals(inColor);
        assert inSku == 1234;
    }
}

