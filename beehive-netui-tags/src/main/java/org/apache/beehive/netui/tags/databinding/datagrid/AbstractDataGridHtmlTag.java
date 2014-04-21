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
package org.apache.beehive.netui.tags.databinding.datagrid;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.beehive.netui.tags.AbstractSimpleTag;
import org.apache.beehive.netui.tags.TagConfig;
import org.apache.beehive.netui.tags.javascript.ScriptRequestState;
import org.apache.beehive.netui.tags.html.Form;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractHtmlControlState;
import org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel;

/**
 * <p>
 * This class is a base class for all data grid tags that render HTML output for the grid.  This base
 * class provides services to its subclasses including lookup of the {@link DataGridTagModel} for the
 * current data grid.  In addition, this class exposes tagId generation functionality.  These methods
 * are used to create strings that will be written onto HTML tags for the "id" or "name" attributes.
 * Such identifiers can be created in one of two ways -- indexed or un-indexed.  An un-indexed tag
 * identifier is one that the page author is responsible for making unique within a given
 * scope in the rendered output.  An indexed tag identifier is one that the data grid will suffix
 * with the index for the current row in the grid.  This is used to help create unique identifiers
 * from JSP tags that are rendered repeatedly.  For example, a {@link SpanCell} renders an HTML
 * &lt;span&gt; tag; with its <code>tagId</code> attribute set, it will render a tag identifier for
 * every row in the page.  The indexed is added as a suffix in order to help page authors create
 * more unique identifiers.  Ultimately, the page author is responsible for ensuring that their
 * tag identifiers are unique within a scope.
 * </p>
 */
public abstract class AbstractDataGridHtmlTag
    extends AbstractSimpleTag {

    /**
     * Get the {@link DataGridTagModel} for the data grid.
     * @return the data grid tag model
     */
    protected final DataGridTagModel lookupDataGridTagModel() {
        return DataGridUtil.getDataGridTagModel(getJspContext());
    }

    /**
     * Create an indexed tag identifier given a state object and a base tag identifier.  The <code>tagId</code>
     * will have the index of the current item in the data grid attached as a suffix to the
     * the given base identifier.
     * @param state the {@link AbstractHtmlState} upon which the tag identifier will be set once created
     * @param tagId the base tag identifier name
     * @throws JspException
     */
    protected final void applyIndexedTagId(AbstractHtmlState state, String tagId)
        throws JspException {
        state.id = indexTagId(generateTagId(tagId));
    }

    /**
     * Create an un-indexed tag identifier for the given state object.
     * @param state the {@link AbstractHtmlState} upon which the tag identifier will be set once created
     * @param tagId the base tag identifier
     * @throws JspException
     */
    protected final void applyTagId(AbstractHtmlState state, String tagId)
        throws JspException {
        state.id = generateTagId(tagId);
    }

    /**
     * Generate a tag ID.
     * @param tagId the tag id
     * @return the generated tag id
     * @throws JspException if an error occurs creating the value of the tag id attribute 
     */
    private final String generateTagId(String tagId)
        throws JspException {
        return setRequiredValueAttribute(tagId, "tagId");
    }

    /**
     * Generate an indexed tag ID.  This method will use the
     * {@link org.apache.beehive.netui.databinding.datagrid.api.rendering.DataGridTagModel#getCurrentIndex()} method
     * to scope a given name to a particular row in the data grid.
     * @param tagId the base tag id
     * @return an index-scoped tag id
     */
    private final String indexTagId(String tagId) {
        DataGridTagModel dataGridTagModel = lookupDataGridTagModel();
        assert dataGridTagModel != null;

        int index = dataGridTagModel.getCurrentIndex();
        assert index >= 0;

        return tagId + index;
    }

    /**
     * <p>
     * Generate a name and id given a {@link AbstractHtmlState} object.  Data grid callers may invoke this
     * method with subclasses rendering markup containing tags that must set HTML tag IDs for use
     * via JavaScript on the client.
     * <br/>
     * Assumptions:
     * <ul>
     * <li>The state.name must be fully formed or the "real name" of the form.</li>
     * <li>The state.id is the tagId value set on the tag and <b>has not</b> be rewritten yet to form the "real id"</li>
     * </ul>
     * </p>
     * @param state the HTML state whose tag id to set
     * @param parentForm a {@link Form} tag if one contains this tag
     * @return String a block of JavaScript if script must e rendered to the page in order to support
     *         lookups of HTML elements using a tag id.  If returned, the String <b>must</b> be rendered
     *         to the output stream.  <code>null</code> if no script must be rendered.
     */
    protected final String renderNameAndId(HttpServletRequest request, AbstractHtmlState state, Form parentForm) {
        // if id is not set then we need to exit
        if (state.id == null)
            return null;

        // check to see if this is an instance of a HTML Control
        boolean ctrlState = (state instanceof AbstractHtmlControlState);

        // form keeps track of this so that it can add this control to it's focus map
        if (parentForm != null && ctrlState) {
            AbstractHtmlControlState hcs = (AbstractHtmlControlState) state;
            if (hcs.name == null && parentForm.isFocusSet())
                hcs.name = state.id;
            parentForm.addTagID(state.id, ((AbstractHtmlControlState) state).name);
        }

        // rewrite the id, save the original value so it can be used in maps
        String id = state.id;
        state.id = getIdForTagId(id);

        // Legacy Java Script support -- This writes out a single table with both the id and names
        // mixed.  This is legacy support to match the pre beehive behavior.
        String idScript = null;
        if (TagConfig.isLegacyJavaScript()) {
            ScriptRequestState srs = ScriptRequestState.getScriptRequestState(request);
            if (!ctrlState) {
                idScript = srs.mapLegacyTagId(getScriptReporter(), id, state.id);
            }
            else {
                AbstractHtmlControlState cState = (AbstractHtmlControlState) state;
                if (cState.name != null)
                    idScript = srs.mapLegacyTagId(getScriptReporter(), id, cState.name);
                else
                    idScript = srs.mapLegacyTagId(getScriptReporter(), id, state.id);
            }
        }

        // map the tagId to the real id
        String name = null;
        if (ctrlState) {
            AbstractHtmlControlState cState = (AbstractHtmlControlState) state;
            name = cState.name;
        }

        String script = renderDefaultNameAndId((HttpServletRequest)request, state, id, name);
        if (script != null) {
            if (idScript != null)
                idScript = idScript + script;
            else idScript = script;
        }
        return idScript;
    }


    protected String renderDefaultNameAndId(HttpServletRequest request, AbstractHtmlState state, String id, String name) {
        // map the tagId to the real id
        String script = null;
        if (TagConfig.isDefaultJavaScript()) {
            ScriptRequestState srs = ScriptRequestState.getScriptRequestState(request);
            script = srs.mapTagId(getScriptReporter(), id, state.id, name);
        }
        return script;
    }
}
