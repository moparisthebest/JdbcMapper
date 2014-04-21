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
package org.apache.beehive.netui.tags.javascript;

/**
 *
 */
public class ScriptPlacement
{
    protected static final int INT_PLACE_BEFORE = 0;
    protected static final int INT_PLACE_AFTER = 1;
    protected static final int INT_PLACE_INLINE = 2;
    protected static final int INT_PLACE_INFRAMEWORK = 3;
    
    public static final ScriptPlacement PLACE_BEFORE = new ScriptPlacement(INT_PLACE_BEFORE);
    public static final ScriptPlacement PLACE_AFTER = new ScriptPlacement(INT_PLACE_AFTER);
    public static final ScriptPlacement PLACE_INLINE = new ScriptPlacement(INT_PLACE_INLINE);
    public static final ScriptPlacement PLACE_INFRAMEWORK = new ScriptPlacement(INT_PLACE_INFRAMEWORK);
    
    private int _val;
    
    private ScriptPlacement(int val)
    {
        _val = val;
    }
    
    public String toString()
    {
        switch (_val)
        {
            case INT_PLACE_BEFORE : return "PLACE_BEFORE";
            case INT_PLACE_AFTER: return "PLACE_AFTER";
            case INT_PLACE_INLINE: return "PLACE_INLINE";
            case INT_PLACE_INFRAMEWORK: return "PLACE_INFRAMEWORK";
        }
        
        assert false : _val;
        return "<unknown ScriptPlacement>";
    }
    
    public boolean equals( Object o )
    {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof ScriptPlacement)) return false;
        return ((ScriptPlacement )o)._val == _val;
    }
    
    public int hashCode()
    {
        return _val;
    }
}
