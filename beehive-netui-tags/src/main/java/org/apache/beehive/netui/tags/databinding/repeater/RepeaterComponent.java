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
package org.apache.beehive.netui.tags.databinding.repeater;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.BodyContent;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p>
 * The base class for tags that are part of the {@link Repeater} tag set and participate in the structured nature
 * of {@link Repeater} rendering.  This class provides typed access to the {@link Repeater} tag and
 * enforces the basic JSP tag parenting requirements of tags that can only be nested within the
 * {@link Repeater} tag.
 * </p>
 */
public abstract class RepeaterComponent
    extends AbstractClassicTag {

    private static final Logger LOGGER = Logger.getInstance(RepeaterComponent.class);

    private Repeater _repeater = null;

    /**
     * <p>
     * Starts a tag's lifecycle.  This method performs several operations before
     * invoking the @see renderStartTag(int) method.  In order, these stages are:
     * <ol>
     * <li>@see verifyAttributes()</li>
     * <li>@see renderStartTag(int)</li>
     * </ol>
     * <br/>
     * Any errors that occur before calling @see renderStartTag(int) are reported
     * in the page.
     * </p>
     * @return the value returned from calling @see renderStartTag(int), which can be
     *         any value that can be returned from the @see javax.servlet.jsp.tagext.TagSupport
     *         class.  If an error occurs, the tag returns SKIP_BODY.
     */
    public int doStartTag()
        throws JspException {
        
        int ret = SKIP_BODY;
        try {
            Tag parent = getParent();
            Class validContainer = Repeater.class;

            if((validContainer != null && parent == null) ||
                    validContainer != null && !validContainer.isAssignableFrom(parent.getClass())) {
                if(LOGGER.isErrorEnabled()) {
                    LOGGER.error("A tag of type \"" + getClass().getName() + "\" must be nested within a tag of type \"" + Repeater.class.getName() + "\"");
                }

                String msg = Bundle.getString("Tags_RepeaterComponent_invalidParent", new Object[]{getClass().getName(), Repeater.class.getName()});
                registerTagError(msg, null);
            }

            if(LOGGER.isDebugEnabled())
                LOGGER.debug("verifyStructure: hasErrors=" + hasErrors());

            if(hasErrors()) {
                reportErrors();
                return SKIP_BODY;
            }

            /* the repeater property needs to be populated *before* verifyAttributes() is called */
            _repeater = (Repeater)getParent();

            verifyAttributes();

            _repeater.registerChildTag(this);

            if(LOGGER.isDebugEnabled())
                LOGGER.debug("verifyAttributes: hasErrors=" + hasErrors());

            if(hasErrors()) {
                reportErrors();
                return SKIP_BODY;
            }

            if(shouldRender())
                ret = EVAL_BODY_BUFFERED;
            else
                ret = SKIP_BODY;
        }
        catch(Exception e) {
            String msg = Bundle.getString("Tags_RepeaterComponent_startTagError", new Object[]{getTagName(), e});

            if(LOGGER.isErrorEnabled())
                LOGGER.error("An error occurred rendering the startTag of the tag \"" + getTagName() + "\".  Cause: " + e, e);

            registerTagError(msg, e);
            reportErrors();
            ret = SKIP_BODY;
        }

        return ret;
    }

    /**
     * Default implementation of this JSP lifecycle method.
     *
     * @return SKIP_BODY
     */
    public int doAfterBody()
            throws JspException {
        assert _repeater != null;
        _repeater.addContent(bodyContent.getString());
        return SKIP_BODY;
    }

    /**
     * Ends a tag's lifecycle.  This call is a wrapper around the @see renderEndTag(int) call
     * that allows a tag directly contained in a repeating tag to act based on the state
     * of the parent.
     *
     * @return EVAL_PAGE
     * @throws JspException if an error that occurred that could not be reported to the page
     */
    public int doEndTag()
            throws JspException {
        /*
           note, this does not report errors because the <repeater> tag itself does
           the error reporting
         */
        if(hasErrors()) {
            /* bug: this doesn't report errors */
            localRelease();
            return EVAL_PAGE;
        }

        int ret = EVAL_PAGE;
        try {
            int state = _repeater.getRenderState();
            ret = renderEndTag(state);
        }
        catch(Exception e) {
            String msg = Bundle.getString("Tags_RepeaterComponent_endTagError", new Object[]{getTagName(), e.toString()});
            registerTagError(msg, e);
            reportErrors();
            ret = EVAL_PAGE;
        }

        localRelease();
        return ret;
    }

    /**
     * Get the {@link Repeater} parent of this tag.
     *
     * @return the {@link Repeater} parent of this tag
     */
    protected final Repeater getRepeater() {
        return _repeater;
    }

    /**
     * Reset all of the fields of this tag.
     */
    protected void localRelease() {
        super.localRelease();
        _repeater = null;
    }

    protected abstract boolean shouldRender();

    protected void verifyAttributes()
            throws JspException {
    }

    protected int renderEndTag(int state)
            throws JspException {
        return EVAL_PAGE;
    }
}
