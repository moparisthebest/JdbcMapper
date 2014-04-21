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

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.TagConfig;
import org.apache.beehive.netui.tags.RequestUtils;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.ScriptTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.WriteRenderAppender;
import org.apache.beehive.netui.pageflow.scoping.ScopedRequest;
import org.apache.beehive.netui.pageflow.scoping.ScopedServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Acts as a container that will bundle up JavaScript created by other NetUI tags,
 * and output it within a single &lt;script> tag. This is especially needed for
 * Portal web applications, because they often cannot rely on having
 * &lt;html> ... &lt;/html> tags to provide a default container. In a portlet,
 * some JSP pages might be included into other JSP pages. Having redundant
 * &lt;html> ... &lt;/html> tags in the rendered portlet JSP can result in display
 * problems for some browsers. However, omitting the &lt;html> tag (and the
 * container it provides) can result in cluttered code, especially where Javascript
 * appears in the file. To solve this issue, Beehive provides the
 * &lt;netui:scriptContainer> tag.
 * 
 * @jsptagref.tagdescription Acts as a container that will bundle up JavaScript created by other &lt;netui...> tags,
 * and outputs it within a single &lt;script> tag. This is especially useful for
 * Portal web applications, because they often cannot rely on having
 * &lt;html> ... &lt;/html> tags to provide a default container. In a Portlet,
 * some JSP pages might be included in other JSP pages. Having redundant
 * &lt;html> ... &lt;/html> tags in the rendered Portlet JSP can result in display
 * problems for some browsers. On the other hand, omitting the &lt;html> tag (and the
 * container it provides) can result in cluttered code, especially where JavaScript
 * appears in the file. To solve this issue, Beehive provides the
 * &lt;netui:scriptContainer> tag.
 *
 * <p>The &lt;netui:scriptContainer> ... &lt;/netui:scriptContainer> tag set should
 * enclose those &lt;netui:...> tags that you want included in the script container.
 * The first &lt;netui:scriptContainer> tag should appear after the JSP's &lt;body> tag.
 * The closing &lt;/netui:scriptContainer> tag should appear before the JSP's &lt;/body> tag.
 * @example The &lt;netui:scriptContainer> ... &lt;/netui:scriptContainer> tag set simply
 * encloses other NetUI tags that you want to belong to that script container.
 * The first &lt;netui:scriptContainer> tag should appear after the JSP's &lt;body> tag.
 * The closing &lt;/netui:scriptContainer> tag should appear before the JSP's &lt;/body> tag.
 * @netui:tag name="scriptContainer" description="ScriptContainers defines a container that will gather all of the JavaScript of their children and output it in a single &lt;script> tag.  In addition, they providing scoping of tagIds."
 */
public class ScriptContainer extends AbstractClassicTag
        implements IScriptReporter
{
    public final static String SCOPE_ID = "netui:scopeId";

    private String _idScope = null;
    private ArrayList/*<String>*/ _funcBlocks;
    private ArrayList/*<String>*/ _codeBefore;
    private ArrayList/*<String>*/ _codeAfter;
    private HashMap/*<String, String>*/ _idMap;
    private HashMap/*<String, String>*/ _idToNameMap;
    private boolean _genScope = false;
    private boolean _writeScript = false;
    private boolean _writeId = false;

    /**
     * Returns the name of the Tag.
     */
    public String getTagName()
    {
        return "ScriptContainer";
    }

    /////////////////////////// ScriptReporter Interface ////////////////////////////

    /**
     * This method will add Script as a function.
     * @param placement
     * @param script    the text of the function. This value must not be null.
     */
    public void addScriptFunction(ScriptPlacement placement, String script)
    {
        assert (script != null) : "The paramter 'script' must not be null";
        IScriptReporter sr = getParentScriptReporter();
        if (sr != null) {
            sr.addScriptFunction(placement, script);
            return;
        }

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
     * Adds a tagID and tagName to the Html's getId javascript function.
     * @param tagId   the id of a child tag.
     * @param tagName the name of a child tag.
     */
    public void addLegacyTagIdMappings(String tagId, String tagName)
    {
        assert (tagId != null) : "The parameter 'tagId' must not be null";
        assert (tagName != null) : "The parameter 'tagName' must not be null";

        if (_idMap == null) {
            _idMap = new HashMap/*<String, String>*/();
        }

        assert (_idMap != null) : "_idMap should not be null";
        _idMap.put(tagId, tagName);
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

        _writeId = true;

        if (realName != null) {
            if (_idToNameMap == null)
                _idToNameMap = new HashMap/*<String, String>*/();
            _idToNameMap.put(tagId, realName);
        }
    }

    /**
     * This method will output all of the Script associated with the script reporter.
     * @param sb The script is written into the provided InternalStringBuilder. This value must not be null.
     */
    public void writeScript(AbstractRenderAppender sb)
    {
        assert(sb != null) : "The paramter 'sb' must not be null;";
        if (_writeScript)
            return;

        _writeScript = true;
        IScriptReporter sr = getParentScriptReporter();
        if (sr != null) {
            sr.writeScript(sb);
            return;
        }

        writeBeforeBlocks(sb);
        writeFrameworkScript(sb);
        writeAfterBlocks(sb);
    }

    /////////////////////////// Attributes ////////////////////////////

    /**
     * Set the idScope associated with the code methods
     * @jsptagref.attributedescription The id that is associated with the script methods.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_scopeId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The id that is associated with the script methods."
     */
    public void setIdScope(String idScope)
    {
        _idScope = idScope;
    }

    /**
     * return the scopeId associated with the ScriptContainer
     */
    public String getIdScope()
    {
        return _idScope;

    }

    /**
     * If true generate a scope id for this script container.  If this is set to true
     * and a scopeId is also set, the scopeId set will be written.
     * @jsptagref.attributedescription Automatically generate a scopeId for this script container.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_generateScopeId</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Automatically generate a ScopeId."
     */
    public void setGenerateIdScope(boolean genScopeValue)
    {
        _genScope = genScopeValue;
    }
    ///////////////////////////////// Tag Methods ////////////////////////////////////////

    public int doStartTag()
            throws JspException
    {
        String scope = getRealIdScope();
        pushIdScope();

        WriteRenderAppender writer = new WriteRenderAppender(pageContext);
        writeBeforeBlocks(writer);

        // if there is a scopeId, then we need to create a div to contains everything
        if (_idScope != null) {
            write("<div");
            write(" netui:idScope=\"");
            write(scope);
            write("\" ");
            write(">");
        }

        return EVAL_BODY_INCLUDE;
    }

    /**
     * Write out the body content and report any errors that occured.
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag()
            throws JspException
    {

        popIdScope();

        // writeout the script.
        WriteRenderAppender writer = new WriteRenderAppender(pageContext);

        // if we wrote out the scopeId then we end it.
        if (_idScope != null) {
            writer.append("</div>");
        }

        writeFrameworkScript(writer);
        writeAfterBlocks(writer);
        localRelease();
        return EVAL_PAGE;
    }

    /////////////////////////////////// Protected Support ////////////////////////////////////

    protected void pushIdScope()
    {
        if (_idScope != null) {
            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            ArrayList/*<String>*/ list = (ArrayList/*<String>*/) RequestUtils.getOuterAttribute(req,SCOPE_ID);
            if (list == null) {
                list = new ArrayList/*<String>*/();
                RequestUtils.setOuterAttribute(req,SCOPE_ID,list);
            }
            list.add(_idScope);

        }
    }

    protected void popIdScope()
    {
        if (_idScope != null) {
            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            ArrayList/*<String>*/ list = (ArrayList/*<String>*/) RequestUtils.getOuterAttribute(req,SCOPE_ID);
            assert(list != null);
            list.remove(list.size() -1);
        }
    }

    /**
     * This method will return the real scope id for the script container. 
     * @return String
     */
    protected String getRealIdScope()
    {
        ServletRequest request = pageContext.getRequest();
        
        // default to the set idScope.
        String idScope = _idScope;

        // if there isn't a set idScope and generate scope is on, generate the scope id.
        if (_idScope == null && _genScope) {
            int id = getNextId(request);
            idScope = "n" + Integer.toString(id);
            _idScope = idScope;
        }
        
        // if there's still no idScope and we're in a ScopedRequest, use the scope-key from the request.
        if (_idScope == null) {
            ScopedRequest scopedRequest = ScopedServletUtils.unwrapRequest(request);
            if (scopedRequest != null) {
                _idScope = scopedRequest.getScopeKey().toString();
                idScope = _idScope;
            }
        }
        
        return idScope;
    }

    protected void writeBeforeBlocks(AbstractRenderAppender sb)
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
        ScriptRequestState.writeScriptBlock(pageContext.getRequest(), sb, s.toString());

    }

    protected void writeAfterBlocks(AbstractRenderAppender sb)
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
        ScriptRequestState.writeScriptBlock(pageContext.getRequest(), sb, s.toString());
    }

    /**
     * This will write the script block.
     */
    protected void writeFrameworkScript(AbstractRenderAppender sb)
    {
        boolean script = false;
        ScriptRequestState jsu = ScriptRequestState.getScriptRequestState((HttpServletRequest) pageContext.getRequest());

        boolean writeLegacy = false;
        boolean writeName = false;
        String val;

        // if we are writing out legacy JavaScript support output the idMap
        if (TagConfig.isLegacyJavaScript()) {
            val = processIdMap(_idMap, "idMappingEntry", _idScope);
            if (val != null) {
                writeIdMap(this, "idMappingTable", val);
                writeLegacy = true;
            }
        }

        // if we are writing out default JavaScript support we create the name map
        if (TagConfig.isDefaultJavaScript()) {
            String idScope = getJavaScriptId();
            if (idScope.equals(""))
                idScope = null;
            val = processIdMap(_idToNameMap, "tagIdNameMappingEntry", idScope);
            if (val != null) {
                writeIdMap(this, "tagIdNameMappingTable", val);
                writeName = true;
            }
        }

        if (writeLegacy || _writeId || writeName)
            jsu.writeNetuiNameFunctions(this, writeLegacy, _writeId, writeName);

        ScriptTag.State state = null;
        ScriptTag br = null;
        if (_funcBlocks != null && _funcBlocks.size() > 0) {
            if (!script) {
                state = new ScriptTag.State();
                state.suppressComments = false;
                br = (ScriptTag) TagRenderingBase.Factory.getRendering(TagRenderingBase.SCRIPT_TAG, pageContext.getRequest());
                br.doStartTag(sb, state);
                script = true;
            }
            String s = ScriptRequestState.getString("functionComment", null);
            sb.append(s);
            int cnt = _funcBlocks.size();
            for (int i = 0; i < cnt; i++) {
                sb.append((String)_funcBlocks.get(i));
                if (i != cnt - 1) {
                    sb.append("\n");
                }
            }
        }

        if (script) {
            assert(br != null);
            br.doEndTag(sb, false);
        }
    }

    /////////////////////////////////// Private Support ////////////////////////////////////

    /**
     * @param scriptRepoter
     * @param mapObj
     * @param entries
     * @return returns a string containing JavaScript if there isn't a ScriptReporter
     */
    private String writeIdMap(IScriptReporter scriptRepoter, String mapObj, String entries)
    {
        String s = ScriptRequestState.getString(mapObj, new Object[]{entries});
        if (scriptRepoter != null) {
            scriptRepoter.addScriptFunction(null, s);
            return null;
        }
        return s;
    }


    private String getJavaScriptId()
    {
        String idScope = "";
        Tag tag = this;
        while (tag != null) {
            if (tag instanceof ScriptContainer) {
                String sid = ((ScriptContainer) tag).getIdScope();
                if (sid != null) {
                    idScope = sid + "_" + idScope;
                }
            }
            tag = tag.getParent();
        }
        return idScope;
    }

    private String processIdMap(HashMap/*<String, String>*/ map, String mapEntry, String idScope)
    {
        // if no map or empty then return
        if (map == null || map.size() == 0)
            return null;

        InternalStringBuilder results = new InternalStringBuilder(128);
        Iterator/*<String>*/ ids = map.keySet().iterator();
        while (ids.hasNext()) {
            String id = (String) ids.next();
            String value = (String) map.get(id);
            if (idScope != null)
                id = idScope + "__" + id;
            String entry = ScriptRequestState.getString(mapEntry, new Object[]{id, value});
            results.append(entry);
        }
        return results.toString();
    }

    /////////////////////////////////// Local Release ////////////////////////////////////
    private IScriptReporter getParentScriptReporter()
    {
        Tag parent = getParent();
        if (parent == null)
            return null;
        return (IScriptReporter) SimpleTagSupport.findAncestorWithClass(parent, IScriptReporter.class);
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        super.localRelease();

        _idScope = null;
        _writeScript = false;
        _genScope = false;
        _writeId = false;

        if (_funcBlocks != null)
            _funcBlocks.clear();
        if (_codeBefore != null)
            _codeBefore.clear();
        if (_codeAfter != null)
            _codeAfter.clear();

        if (_idMap != null)
            _idMap.clear();
        if (_idToNameMap != null)
            _idToNameMap.clear();
    }

}
