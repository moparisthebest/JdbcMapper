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

package org.apache.beehive.netui.tools.testrecorder.shared.config;

import java.io.File;

/**
 * User: ozzy
 * Date: Apr 9, 2004
 * Time: 2:32:14 PM
 */
public class Category implements Comparable {

    private String name;
    private String description;
    private File reportDir;

    public Category( String name, String description, String baseDirPath ) throws ConfigException {
        this.name = name;
        this.description = description;
        this.reportDir = new File( baseDirPath + "/junit/" + name );
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getReportDirPath() {
        return reportDir.getAbsolutePath();
    }

    public int compareTo( Object o ) {
        if ( o instanceof Category ) {
            Category other = (Category) o;
            return getName().compareToIgnoreCase( other.getName() );
        }
        return 1;
    }

    public boolean equals( Object object ) {
        if ( this == object ) {
            return true;
        }
        else if ( object == null || getClass() != object.getClass() ) {
            return false;
        }
        Category other = (Category) object;
        return getName().equals( other.getName() );
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer( 64 );
        sb.append( "[ " );
        sb.append( "name( " + name + " )" );
        sb.append( ", reportDir( " + getReportDirPath() + " )" );
        sb.append( ", description( " + description + " )" );
        sb.append( " ]" );
        return sb.toString();
    }

}
