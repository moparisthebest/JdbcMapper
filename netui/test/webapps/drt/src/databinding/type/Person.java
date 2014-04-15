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
package databinding.type;

// java imports

// internal imports
import org.apache.beehive.netui.util.type.TypeConverter;

// external imports

/**
 *
 */
public class Person
    implements java.io.Serializable
{
    private String name = null;

    public Person()
    {
    }

    public Person(String name)
    {
        setName(name);
    }

    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("name: ");
        buf.append(name);
        return buf.toString();
    }

    public boolean equals(Object object)
    {
        if(!(object instanceof Person) || object == null)
            return false;
        
        if(name.equals(((Person)object).getName()))
            return true;
        else return false;
    }

    public static class PersonTypeConverter
        implements TypeConverter
    {
        public Object convertToObject(String string)
        {
            if(string == null || string.equals(""))
                return new Person();
            else return new Person(string);
        }
    }
}
