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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.samples.petstore.controls.CatalogControl;
import org.apache.beehive.samples.petstore.util.DBProperties;
import org.apache.beehive.samples.petstore.controls.exceptions.DataStoreException;

import webappRoot.SharedFlow;

@Jpf.Controller(
    forwards={@Jpf.Forward(name="index", path="index.jsp")},
    sharedFlowRefs={@Jpf.SharedFlowRef(name="rootSharedFlow", type=webappRoot.SharedFlow.class)}
)
public class Controller
    extends PageFlowController {

    private static final String SQL_FILE = "sql/initDB.sql";

    @Jpf.SharedFlowField(name="rootSharedFlow")
    private SharedFlow _sharedFlow;

    @Control()
    private CatalogControl _catalogControl;
    
    @Jpf.Action(
        forwards = { 
            @Jpf.Forward(name = "shop", path = "/shop/Controller.jpf")
        }
    )
    public Forward begin() {
        if (_sharedFlow.isUserLoggedIn())
            return new Forward("shop");
        else return new Forward("index");
    }

    @Jpf.Action()
    public Forward stopDB() 
        throws Exception {

        /* move this code... */
        try {
            java.sql.DriverManager.getConnection("jdbc:derby:" + DBProperties.DATABASE_LOCATION + ";shutdown=true");
        } 
        catch(Exception e) {}

        getRequest().setAttribute("message", "DB Stopped.");
        return new Forward("index");
    }
       
    @Jpf.Action()
    public Forward initDB() 
        throws Exception {
        LinkedList<String> dbInit = getSqlStatements(SQL_FILE);

        for(String sql : dbInit) {
            try {
                    _catalogControl.initDB(sql);
             } 
             catch(DataStoreException e) {
                 // Ignore exceptions on DROP statements since the tables may simply not exist
                if (!sql.startsWith("drop"))
                    throw e;
            }
        }
        getRequest().setAttribute("message", "DB Initialized.");

        return new Forward("index");
    }

    private LinkedList<String> getSqlStatements(String sqlFile) 
        throws Exception {

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sqlFile);
        BufferedReader fs = new BufferedReader(new InputStreamReader(is));
        LinkedList<String> statements = new LinkedList<String>();
        try {
            String line = null;
            while((line = fs.readLine()) != null) 
            {
                // Ignore comments and empty lines
                if (line.endsWith(";") && !(line.startsWith("--"))) 
                    statements.add(line.replace(';', ' '));
            }
        } 
        catch(IOException io) {
            throw new IllegalStateException("Unable to read input stream: " + sqlFile);
        } 
        finally {
            try {
                if(fs != null) fs.close();
            } catch(Exception ignore) {}
        }

        for(String s : statements) {
            System.out.println(s + "\n");
        }

        return statements;
    }
}