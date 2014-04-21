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
public class CoreScriptFeature
{
    public static final int INT_LEGACY_LOOKUP = 0x00001;
    public static final int INT_ID_LOOKUP = 0x00002;
    public static final int INT_NAME_LOOKUP = 0x00004;
    public static final int INT_SCOPE_LOOKUP = 0x00008;
    public static final int INT_ROLLOVER = 0x00010;
    public static final int INT_ANCHOR_SUBMIT = 0x00020;
    public static final int INT_POPUP_OPEN = 0x00040;
    public static final int INT_POPUP_DONE = 0x00080;
    public static final int INT_POPUP_UPDATE_FORM = 0x00100;
    public static final int INT_ALLOCATE_LEGACY = 0x00200;
    public static final int INT_ALLOCATE_ID = 0x00400;
    public static final int INT_ALLOCATE_NAME = 0x00800;
    public static final int INT_LEGACY_SCOPE_LOOKUP = 0x01000;
    public static final int INT_TREE_INIT = 0x02000;
    public static final int INT_DIVPANEL_INIT = 0x04000;
    public static final int INT_DYNAMIC_INIT = 0x08000;
    public static final int INT_BUTTON_DISABLE_AND_SUBMIT = 0x10000;
    public static final int INT_BUTTON_DISABLE = 0x20000;
    public static final int INT_AJAX_PREFIX = 0x40000;
    public static final int INT_AJAX_PARAM = 0x80000;

    // These features are not written out once.  They are identified by setting the top bit
    protected static final int INT_SET_FOCUS = 0x10000001;

    public int value;

    CoreScriptFeature(int val)
    {
        value = val;
    }
    
    public static final CoreScriptFeature LEGACY_LOOKUP = new CoreScriptFeature(INT_LEGACY_LOOKUP);
    public static final CoreScriptFeature ID_LOOKUP = new CoreScriptFeature(INT_ID_LOOKUP);
    public static final CoreScriptFeature NAME_LOOKUP = new CoreScriptFeature(INT_NAME_LOOKUP);
    public static final CoreScriptFeature SCOPE_LOOKUP = new CoreScriptFeature(INT_SCOPE_LOOKUP);
    public static final CoreScriptFeature ROLLOVER = new CoreScriptFeature(INT_ROLLOVER);
    public static final CoreScriptFeature ANCHOR_SUBMIT = new CoreScriptFeature(INT_ANCHOR_SUBMIT);
    public static final CoreScriptFeature POPUP_OPEN = new CoreScriptFeature(INT_POPUP_OPEN);
    public static final CoreScriptFeature POPUP_DONE = new CoreScriptFeature(INT_POPUP_DONE);
    public static final CoreScriptFeature POPUP_UPDATE_FORM = new CoreScriptFeature(INT_POPUP_UPDATE_FORM);
    public static final CoreScriptFeature ALLOCATE_LEGACY = new CoreScriptFeature(INT_ALLOCATE_LEGACY);
    public static final CoreScriptFeature ALLOCATE_ID = new CoreScriptFeature(INT_ALLOCATE_ID);
    public static final CoreScriptFeature ALLOCATE_NAME = new CoreScriptFeature(INT_ALLOCATE_NAME);
    public static final CoreScriptFeature LEGACY_SCOPE_LOOKUP = new CoreScriptFeature(INT_LEGACY_SCOPE_LOOKUP);
    public static final CoreScriptFeature TREE_INIT = new CoreScriptFeature(INT_TREE_INIT);
    public static final CoreScriptFeature AJAX_PREFIX = new CoreScriptFeature(INT_AJAX_PREFIX);
    public static final CoreScriptFeature AJAX_PARAM = new CoreScriptFeature(INT_AJAX_PARAM);
    public static final CoreScriptFeature DIVPANEL_INIT = new CoreScriptFeature(INT_DIVPANEL_INIT);
    public static final CoreScriptFeature DYNAMIC_INIT = new CoreScriptFeature(INT_DYNAMIC_INIT);
    public static final CoreScriptFeature BUTTON_DISABLE_AND_SUBMIT = new CoreScriptFeature(INT_BUTTON_DISABLE_AND_SUBMIT);
    public static final CoreScriptFeature BUTTON_DISABLE = new CoreScriptFeature(INT_BUTTON_DISABLE);
    public static final CoreScriptFeature SET_FOCUS = new CoreScriptFeature(INT_SET_FOCUS);

    public String toString()
    {
        switch ( value )
        {
            case INT_LEGACY_LOOKUP: return "LEGACY_LOOKUP";
            case INT_ID_LOOKUP: return "ID_LOOKUP";
            case INT_NAME_LOOKUP: return "NAME_LOOKUP";
            case INT_SCOPE_LOOKUP: return "SCOPE_LOOKUP";
            case INT_ROLLOVER: return "ROLLOVER";
            case INT_ANCHOR_SUBMIT: return "ANCHOR_SUBMIT";
            case INT_POPUP_OPEN: return "POPUP_OPEN";
            case INT_POPUP_DONE: return "POPUP_DONE";
            case INT_POPUP_UPDATE_FORM: return "POPUP_UPDATE_FORM";
            case INT_ALLOCATE_LEGACY: return "ALLOCATE_LEGACY";
            case INT_ALLOCATE_ID: return "ALLOCATE_ID";
            case INT_ALLOCATE_NAME: return "ALLOCATE_NAME";
            case INT_LEGACY_SCOPE_LOOKUP: return "LEGACY_SCOPE_LOOKUP";
            case INT_TREE_INIT: return "TREE_INIT";
            case INT_DIVPANEL_INIT: return "DIVPANEL_INIT";
            case INT_DYNAMIC_INIT: return "DYNAMIC_INIT";
            case INT_BUTTON_DISABLE_AND_SUBMIT: return "BUTTON_DISABLE_AND_SUBMIT";
            case INT_BUTTON_DISABLE: return "BUTTON_DISABLE";
            case INT_SET_FOCUS: return "SET_FOCUS";
        }
        
        assert false : value;
        return "<unknown CoreScriptFeature>";
    }
    
    public boolean equals( Object o )
    {
        if (o == null) return false;
        if (o == this ) return true;
        if (!(o instanceof CoreScriptFeature)) return false;
        return ((CoreScriptFeature)o).value == value;
    }
    
    public int hashCode()
    {
        return value;
    }
    
    public int getIntValue()
    {
        return value;
    }
}
