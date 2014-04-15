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
package org.apache.beehive.netui.simpletags.core.services;

import org.apache.beehive.netui.simpletags.appender.Appender;
import org.apache.beehive.netui.simpletags.javascript.CoreScriptFeature;
import org.apache.beehive.netui.simpletags.javascript.ScriptPlacement;
import org.apache.beehive.netui.simpletags.rendering.ScriptTag;
import org.apache.beehive.netui.simpletags.rendering.TagRenderingBase;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

public class ScriptReporter
{
    private static final String BUNDLE_NAME = "org.apache.beehive.netui.tags.javascript.javaScript";
    private static ResourceBundle _bundle;      // This points to the bundle

    private ArrayList/*<String>*/ _funcBlocks;
    private ArrayList/*<String>*/ _codeBefore;
    private ArrayList/*<String>*/ _codeAfter;
    private HashMap/*<String, String>*/ _idToNameMap;

    private boolean _writeScript = false;
    private boolean _writeId = false;

    private int _javaScriptFeatures;            // this is a integer bitmap indicating various feature have been written out or not

    /**
     * Returns the string specified by aKey from the errors.properties bundle.
     * @param aKey The key for the message pattern in the bundle.
     * @param args The args to use in the message format.
     */
    public static String getString(String aKey, Object[] args)
    {
        assert (aKey != null);

        String pattern = getBundle().getString(aKey);
        if (args == null)
            return pattern;

        MessageFormat format = new MessageFormat(pattern);
        return format.format(args).toString();
    }

    /**
     * This method will add Script as a function.
     * @param placement
     * @param script    the text of the function. This value must not be null.
     */
    public void addScriptFunction(ScriptPlacement placement, String script)
    {
        assert (script != null) : "The paramter 'script' must not be null";

        // get the list of function blocks and add this script to it.
        if (placement == null || placement == ScriptPlacement.PLACE_INFRAMEWORK) {
            if (_funcBlocks == null) {
                _funcBlocks = new ArrayList/*<String>*/();
            }
            assert (_funcBlocks != null) : "_funcBlocks should not be null";
            _funcBlocks.add(script);
        }
        else if (placement == ScriptPlacement.PLACE_BEFORE) {
            if (_codeBefore == null)
                _codeBefore = new ArrayList/*<String>*/();
            _codeBefore.add(script);
        }
        else if (placement == ScriptPlacement.PLACE_AFTER) {
            if (_codeAfter == null)
                _codeAfter = new ArrayList/*<String>*/();
            _codeAfter.add(script);
        }
        else {
            assert(false) : "unsupported placement:" + placement;
        }
    }

    /**
     * This will add the mapping between the tagId and the real name to the NameMap hashmap.
     * @param tagId
     * @param realId
     * @param realName
     */
    public void addTagIdMappings(String tagId, String realId, String realName)
    {
        assert (tagId != null) : "The parameter 'tagId' must not be null";
        assert (realId != null) : "The parameter 'realId' must not be null";

        // mark the fact that we are writting out the Id methods
        _writeId = true;

        // If we have a name, then add it to the map mapping tagId to realName
        if (realName != null) {
            if (_idToNameMap == null)
                _idToNameMap = new HashMap/*<String, String>*/();
            _idToNameMap.put(tagId, realName);
        }
    }

    /**
     * @param featureKey
     * @param args
     */
    public void writeFeature(String featureKey, Object[] args)
    {
        String s = getString(featureKey, args);
        addScriptFunction(null, s);
    }

    /**
     * @param feature
     * @param singleInstance
     * @param inline
     * @param args
     */
    public void writeFeature(CoreScriptFeature feature, boolean singleInstance, boolean inline, Object[] args)
    {
        if (singleInstance) {
            if ((_javaScriptFeatures & feature.value) != 0)
                return;
            _javaScriptFeatures |= feature.value;
        }

        // get the JavaScript to write out
        String jsKey = getFeatureKey(feature);
        String s = getString(jsKey, args);
        addScriptFunction(null, s);
    }

    public boolean isFeatureWritten(CoreScriptFeature feature)
    {
        return ((_javaScriptFeatures & feature.value) != 0);
    }

    /**
     * This method will output all of the Script associated with the script reporter.
     * @param appender The script is written into the provided InternalStringBuilder. This value must not be null.
     */
    public void writeScript(Appender appender)
    {
        assert(appender != null) : "The paramter 'appender' must not be null;";
        if (_writeScript)
            return;

        _writeScript = true;

        writeBeforeBlocks(appender);
        writeFrameworkScript(appender);
        writeAfterBlocks(appender);
    }

    public void writeBeforeBlocks(Appender appender)
    {
        if (_codeBefore == null || _codeBefore.size() == 0)
            return;

        InternalStringBuilder s = new InternalStringBuilder(256);
        for (Iterator i = _codeBefore.iterator(); i.hasNext();)
        {
            String code = ( String ) i.next();
            s.append(code);
            s.append("\n");
        }
        writeScriptBlock(appender, s.toString());

    }

    public void writeAfterBlocks(Appender appender)
    {
        if (_codeAfter == null || _codeAfter.size() == 0)
            return;

        InternalStringBuilder s = new InternalStringBuilder(256);
        for (Iterator i = _codeAfter.iterator(); i.hasNext();)
        {
            String code = ( String ) i.next();
            s.append(code);
            s.append("\n");
        }
        writeScriptBlock(appender, s.toString());
    }

    /**
     * This will write the script block.
     */
    public void writeFrameworkScript(Appender appender)
    {
        boolean script = false;
        //ScriptRequestState jsu = ScriptRequestState.getScriptRequestState();

        boolean writeName = false;

        String idMap = null;
        String val = processIdMap(_idToNameMap, "tagIdNameMappingEntry");
        if (val != null) {
            idMap = getString("tagIdNameMappingTable", new Object[]{val});
            writeName = true;
        }

        // write out the name features...
        if (_writeId || writeName)
            writeNetuiNameFunctions(this, _writeId, writeName);

        ScriptTag.State state = null;
        ScriptTag br = null;
        if (_funcBlocks != null && _funcBlocks.size() > 0) {
            if (!script) {
                state = new ScriptTag.State();
                state.suppressComments = false;
                br = (ScriptTag) TagRenderingBase.Factory.getRendering(TagRenderingBase.SCRIPT_TAG);
                br.doStartTag(appender, state);
                script = true;
            }
            String s = getString("functionComment", null);
            appender.append(s);
            if (idMap != null)
                appender.append(idMap);
            int cnt = _funcBlocks.size();
            for (int i = 0; i < cnt; i++) {
                appender.append((String)_funcBlocks.get(i));
                if (i != cnt - 1) {
                    appender.append("\n");
                }
            }
        }

        if (script) {
            assert(br != null);
            br.doEndTag(appender, false);
        }
    }

    /**
     * This is a static method that will write a consistent look/feel to the
     * tags and comment markup that appears around the JavaScript.
     * @param results the InternalStringBuilder that will have the &lt;script>
     *                tag written into
     * @param script  the JavaScript block
     */
    public void writeScriptBlock(Appender results, String script)
    {
        assert(results != null) : "The parameter 'results' must not be null";

        ScriptTag.State state = new ScriptTag.State();
        state.suppressComments = false;
        ScriptTag br = (ScriptTag) TagRenderingBase.Factory.getRendering(TagRenderingBase.SCRIPT_TAG);

        results.append("\n");
        br.doStartTag(results, state);
        results.append(script);
        br.doEndTag(results, false);
        results.append("\n");
    }

    private String processIdMap(HashMap/*<String, String>*/ map, String mapEntry)
    {
        // if no map or empty then return
        if (map == null || map.size() == 0)
            return null;

        InternalStringBuilder results = new InternalStringBuilder(128);
        Iterator/*<String>*/ ids = map.keySet().iterator();
        while (ids.hasNext()) {
            String id = (String) ids.next();
            String value = (String) map.get(id);
            String entry = getString(mapEntry, new Object[]{id, value});
            results.append(entry);
        }
        return results.toString();
    }

   /**
     * Returns the resource bundle named Bundle[].properties in the
     * package of the specified class.
     * This is ok to cache because we don't localize the JavaScript resources.
     */
    private static ResourceBundle getBundle()
    {
        if (_bundle == null)
            _bundle = ResourceBundle.getBundle(BUNDLE_NAME);
        return _bundle;
    }

    /**
     * @param scriptReporter
     * @param writeId
     * @param writeName
     */
    private void writeNetuiNameFunctions(ScriptReporter scriptReporter, boolean writeId, boolean writeName)
    {
        assert(scriptReporter != null) : "The parameter 'scriptReporter' must not be null";

        // if we are supporting the default javascript then output the lookup methods for id and name
        if (writeId)
            writeLookupMethod("lookupIdByTagId", CoreScriptFeature.ID_LOOKUP.value);

        if (writeName)
            writeLookupMethod("lookupNameByTagId", CoreScriptFeature.NAME_LOOKUP.value);

        if (writeId || writeName)
            writeLookupMethod("lookupScopeId", CoreScriptFeature.SCOPE_LOOKUP.value);
   }

    /**
     * This method will add the method described by the <code>bundleString</code> to the
     * scriptReporter's script functions.
     * @param bundleString
     * @param feature
     */
    private void writeLookupMethod(String bundleString, int feature)
    {
        assert(bundleString != null ) : "Parameter 'bundleString' must not be null";

        // see if we've written this feature already
        if ((_javaScriptFeatures & feature) != 0)
            return;
        _javaScriptFeatures |= feature;

        // add the function to the script reporter
        String s = getString(bundleString, null);
        addScriptFunction(null, s);
    }

    /**
     * This will map the Features into their keys
     * @param feature
     * @return String
     */
    private String getFeatureKey(CoreScriptFeature feature)
    {
        switch (feature.getIntValue()) {
            case CoreScriptFeature.INT_ANCHOR_SUBMIT:
                return "anchorFormSubmit";
            case CoreScriptFeature.INT_SET_FOCUS:
                return "setFocus";
            case CoreScriptFeature.INT_POPUP_OPEN:
                return "popupSupportPopupWindow";
            case CoreScriptFeature.INT_POPUP_DONE:
                return "popupDone";
            case CoreScriptFeature.INT_POPUP_UPDATE_FORM:
                return "popupSupportUpdateForm";
            case CoreScriptFeature.INT_ROLLOVER:
                return "rollover";
            case CoreScriptFeature.INT_TREE_INIT:
                return "initTree";
            case CoreScriptFeature.INT_DIVPANEL_INIT:
                return "initDivPanel";
            case CoreScriptFeature.INT_DYNAMIC_INIT:
                return "writeWebAppName";
            case CoreScriptFeature.INT_BUTTON_DISABLE_AND_SUBMIT:
                return "buttonDisableAndSubmitForm";
            case CoreScriptFeature.INT_BUTTON_DISABLE:
                return "buttonDisable";
        }
        assert(false) : "getFeature fell through on feature:" + feature;
        return null;
    }
}
