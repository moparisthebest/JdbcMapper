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

package org.apache.beehive.netui.tools.testrecorder.qa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * User: ozzy
 * Date: Jul 7, 2003
 * Time: 7:26:48 PM
 */
public class PropertiesHelper {

    public static Properties load( File propFile, Properties props ) throws IOException {
        if ( props == null ) {
            props = new Properties();
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream( propFile );
            props.load( fis );
        }
        finally {
            if ( fis != null ) {
                fis.close();
            }
        }
        return props;
    }

    public static void store( File propFile, Properties props, String comment,
                              boolean replace )
            throws IOException {
        if ( !replace && propFile.exists() ) {
            throw new RuntimeException( "properties file( " +
                    propFile.getAbsolutePath() + " exists" );
        }
        if ( comment == null ) {
            comment = "";
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream( propFile, false );
            props.store( fos, comment );
        }
        finally {
            if ( fos != null ) {
                fos.close();
            }
        }
        fos.close();
    }
}
