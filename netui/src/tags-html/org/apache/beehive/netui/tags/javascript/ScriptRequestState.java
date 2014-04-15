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

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

import org.apache.beehive.netui.tags.RequestUtils;
import org.apache.beehive.netui.tags.TagConfig;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.ScriptTag;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Provides tag specific support for the HTML tags so they can create JavaScript.  There is a bundle
 * which contains all of the Java Script.  This bundle will be cached inside this class.  THe properties
 * file does not get i18n support.
 */
public class ScriptRequestState implements Serializable
{
    /**
     * This is the name of a request scoped attribute that contains the status of what JavaScript processing
     * has taken place.
     */
    public static final String JAVASCRIPT_STATUS = "netui.javascript.status";

    private static final String BUNDLE_NAME = "org.apache.beehive.netui.tags.javascript.javaScript";

    private int _javaScriptFeatures;            // this is a integer bitmap indicating various feature have been written out or not
    private static ResourceBundle _bundle;      // This points to the bundle
    private ServletRequest _req;

    //***************************  PUBLIC STATIC METHODS *****************************************

    /**
     * This method will return the <code>javaScriptUtils</code> that is assocated
     * with this request.  If this doesn't exist, it will be created before it is
     * returned.
     * @param request the HttpServletRequest associated with this request
     * @return a <code>ScriptRequestState</code> assocated with the request
     */
    static public ScriptRequestState getScriptRequestState(HttpServletRequest request)
    {
        assert (request != null);
        ScriptRequestState srs = (ScriptRequestState) RequestUtils.getOuterAttribute(request, JAVASCRIPT_STATUS);
        if (srs == null) {
            srs = new ScriptRequestState();
            srs.setRequest(request);
            RequestUtils.setOuterAttribute(request, JAVASCRIPT_STATUS, srs);
        }

        assert (srs != null);
        return srs;
    }

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

    ////**************************  MEMBER FUNCTIONS **************************************

    /**
     * A ScriptRequestState is obtained through the factory method  ScriptRequestState.getScriptRequestState()
     * The constructor is private so that these cannot be created outside of the factory.
     */
    private ScriptRequestState()
    {
    }

    public boolean isFeatureWritten(CoreScriptFeature feature)
    {
        return ((_javaScriptFeatures & feature.value) != 0);
    }

    /**
     * @param scriptReporter
     * @param results
     * @param featureKey
     * @param args
     */
    public void writeFeature(IScriptReporter scriptReporter, AbstractRenderAppender results,
                             String featureKey, Object[] args)
    {
        String s = getString(featureKey, args);

        if (scriptReporter != null) {
            scriptReporter.addScriptFunction(null, s);
        }
        else {
            writeScriptBlock(_req, results, s);
        }
    }

    /**
     * @param scriptReporter
     * @param results
     * @param feature
     * @param singleInstance
     * @param inline
     * @param args
     */
    public void writeFeature(IScriptReporter scriptReporter, AbstractRenderAppender results,
                             CoreScriptFeature feature, boolean singleInstance, boolean inline,
                             Object[] args)
    {
        if (singleInstance) {
            if ((_javaScriptFeatures & feature.value) != 0)
                return;
            _javaScriptFeatures |= feature.value;
        }

        // get the JavaScript to write out
        String jsKey = getFeatureKey(feature);
        String s = getString(jsKey, args);

        if (inline || scriptReporter == null) {
            writeScriptBlock(_req, results, s);
            return;
        }

        scriptReporter.addScriptFunction(null, s);
    }

    /**
     * @param scriptReporter
     * @param tagId
     * @param realId
     * @param realName
     * @return String
     */
    public String mapTagId(IScriptReporter scriptReporter, String tagId, String realId, String realName)
    {
        if (scriptReporter != null) {
            scriptReporter.addTagIdMappings(tagId, realId, realName);
            return null;
        }

        // without a scripRepoter we need to create the actual JavaScript that will be written out
        InternalStringBuilder sb = new InternalStringBuilder(128);
        StringBuilderRenderAppender writer = new StringBuilderRenderAppender(sb);
        getTagIdMapping(tagId, realId, realName, writer);
        return sb.toString();
    }

    /**
     * This method will add a tagId and value to the ScriptRepoter TagId map.
     * The a ScriptContainer tag will create a JavaScript table that allows
     * the container, such as a portal, to rewrite the id so it's unique.
     * The real name may be looked up based  upon the tagId.
     *
     * If the no ScriptReporter is found, a script string will be returned
     * to the caller so they can output the script block.
     * @param tagId
     * @param value
     * @return String
     */
    public String mapLegacyTagId(IScriptReporter scriptReporter, String tagId, String value)
    {
        if (scriptReporter != null) {
            scriptReporter.addLegacyTagIdMappings(tagId, value);
            return null;
        }

        // without a scripRepoter we need to create the actual JavaScript that will be written out
        InternalStringBuilder sb = new InternalStringBuilder(64);
        StringBuilderRenderAppender writer = new StringBuilderRenderAppender(sb);
        getTagIdMapping(tagId, value, writer);
        return sb.toString();
    }

    /**
     * @param scriptReporter
     * @param writeLegacy
     * @param writeId
     * @param writeName
     * @return String
     */
    public String writeNetuiNameFunctions(IScriptReporter scriptReporter, boolean writeLegacy, boolean writeId, boolean writeName)
    {
        // allocate a String Buffer only if there is no script reporter
        InternalStringBuilder sb = null;
        if (scriptReporter == null)
            sb = new InternalStringBuilder(256);

        // if we are supporting legacy javascript then output the original javascript method
        if (TagConfig.isLegacyJavaScript() && writeLegacy) {
            writeLookupMethod(scriptReporter, sb, "getNetuiTagNameAdvanced", CoreScriptFeature.LEGACY_LOOKUP.value);
            writeLookupMethod(scriptReporter, sb, "getScopeId", CoreScriptFeature.LEGACY_SCOPE_LOOKUP.value);
        }

        // if we are supporting the default javascript then output the lookup methods for id and name
        if (TagConfig.isDefaultJavaScript()) {
            if (writeId)
                writeLookupMethod(scriptReporter, sb, "lookupIdByTagId", CoreScriptFeature.ID_LOOKUP.value);

            if (writeName)
                writeLookupMethod(scriptReporter, sb, "lookupNameByTagId", CoreScriptFeature.NAME_LOOKUP.value);

            if (writeId || writeName)
                writeLookupMethod(scriptReporter, sb, "lookupScopeId", CoreScriptFeature.SCOPE_LOOKUP.value);
        }

        return (sb != null) ? sb.toString() : null;
    }

    /**
     * This is a static method that will write a consistent look/feel to the
     * tags and comment markup that appears around the JavaScript.
     * @param results the InternalStringBuilder that will have the &lt;script>
     *                tag written into
     * @param script  the JavaScript block
     */
    public static void writeScriptBlock(ServletRequest req, AbstractRenderAppender results, String script)
    {
        assert(results != null);
        ScriptTag.State state = new ScriptTag.State();
        state.suppressComments = false;
        ScriptTag br = (ScriptTag) TagRenderingBase.Factory.getRendering(TagRenderingBase.SCRIPT_TAG, req);

        results.append("\n");
        br.doStartTag(results, state);
        results.append(script);
        br.doEndTag(results, false);
        results.append("\n");
    }

    /////************************* Private Methods *******************************************

    /**
     * The ScriptRequestState is stored in a request.  This is a back pointer to that request.
     * When this is created it the pointer will be stored.
     * @param req the Outer Request that this object is stored in
     */
    private void setRequest(ServletRequest req)
    {
        _req = req;
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
            case CoreScriptFeature.INT_AJAX_PREFIX:
                return "initTreePrefix";
            case CoreScriptFeature.INT_AJAX_PARAM:
                return "initTreeParam";
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
     * This method will write out a tagId map entry for when there
     * isn't a ScriptContainer defined.
     * @param tagId   the tagId value
     * @param value   the "real" value of the written out
     * @param results the JavaScript that will be output
     */
    private void getTagIdMapping(String tagId, String value, AbstractRenderAppender results)
    {
        if ((_javaScriptFeatures & CoreScriptFeature.ALLOCATE_LEGACY.value) == 0) {
            _javaScriptFeatures |= CoreScriptFeature.ALLOCATE_LEGACY.value;
            String s = getString("singleIdMappingTable", new Object[]{tagId, value});
            String meths = writeNetuiNameFunctions(null, true, false, false);
            if (meths != null)
                s += meths;
            writeScriptBlock(_req, results, s);
        }
        else {
            String s = getString("idMappingEntry", new Object[]{tagId, value});
            writeScriptBlock(_req, results, s);
        }
    }

    /**
     * @param tagId
     * @param realId
     * @param realName
     * @param results
     */
    private void getTagIdMapping(String tagId, String realId, String realName, AbstractRenderAppender results)
    {
        InternalStringBuilder sb = new InternalStringBuilder(128);
        if (realId != null) {
            if ((_javaScriptFeatures & CoreScriptFeature.ALLOCATE_ID.value) == 0) {
                _javaScriptFeatures |= CoreScriptFeature.ALLOCATE_ID.value;
                String meths = writeNetuiNameFunctions(null, false, true, false);
                if (meths != null)
                    sb.append(meths);
            }
        }

        if (realName != null) {
            if ((_javaScriptFeatures & CoreScriptFeature.ALLOCATE_NAME.value) == 0) {
                _javaScriptFeatures |= CoreScriptFeature.ALLOCATE_NAME.value;
                String s = getString("singleIdToNameMappingTable", new Object[]{tagId, realName});
                String meths = writeNetuiNameFunctions(null, false, false, true);
                if (meths != null)
                    s += meths;
                sb.append(s);
            }
            else {
                String s = getString("tagIdNameMappingEntry", new Object[]{tagId, realName});
                sb.append(s);
            }
        }
        writeScriptBlock(_req, results, sb.toString());
    }

    private void writeLookupMethod(IScriptReporter scriptReporter, InternalStringBuilder sb, String bundleString, int feature)
    {
        if ((_javaScriptFeatures & feature) != 0)
            return;
        _javaScriptFeatures |= feature;

        String s = getString(bundleString, null);
        if (scriptReporter != null)
            scriptReporter.addScriptFunction(null, s);
        else {
            sb.append(s);
            sb.append("\n");
        }
    }
}
