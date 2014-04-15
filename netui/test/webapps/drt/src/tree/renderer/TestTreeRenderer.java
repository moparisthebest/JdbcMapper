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
package tree.renderer;

import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.ImageTag;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.tree.AttributeRenderer;
import org.apache.beehive.netui.tags.tree.InheritableState;
import org.apache.beehive.netui.tags.tree.TreeElement;
import org.apache.beehive.netui.tags.tree.TreeRenderer;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;

public class TestTreeRenderer extends TreeRenderer
{
    protected void renderIndentation(AbstractRenderAppender writer, TreeElement node, int level, InheritableState state)
    {
        InternalStringBuilder img = new InternalStringBuilder(32);

        // Create the appropriate number of indents
        // These are either the spacer.gif if the parent is the last in the line or the
        // vertical line gif if the parent is not the last child.
        ImageTag.State imgState = new ImageTag.State();
        TagRenderingBase imageRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG, _req);
        imgState.clear();
        imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, WIDTH, "16px");
        imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BORDER, "0");
        for (int i = 0; i < level; i++) {
            int levels = level - i;
            TreeElement parent = node;
            for ( int j = 1; j <= levels; j++ )
            {
                parent = parent.getParent();
            }

            img.setLength(0);
            img.append(state.getImageRoot());
            img.append('/');
            if (parent.isLast()) {
                img.append(state.getImageSpacer());
                imgState.style = null;
                imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, "spacer", false);
            }
            else {
                img.append(state.getVerticalLineImage());
                imgState.style = "vertical-align:baseline;";
                imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, "vertical line", false);
            }
            renderSpacerPrefix(writer, node);
            imgState.src = img.toString();
            imageRenderer.doStartTag(writer, imgState);
            imageRenderer.doEndTag(writer);
            renderSpacerSuffix(writer, node);
        }
    }

    protected void renderItemIcon(AbstractRenderAppender writer,
                                  TreeElement node,
                                  AttributeRenderer attrs,
                                  InheritableState state)
    {
        renderItemIconPrefix(writer, node);

        String icon = node.getIcon();
        if (icon == null) {
            icon = state.getIconRoot() + "/" + state.getItemIcon();
        }
        else {
            icon = state.getIconRoot() + "/" + icon;
        }

        // write out the icon
        if (icon != null) {
            ImageTag.State imgState = new ImageTag.State();
            TagRenderingBase imageRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.IMAGE_TAG, _req);
            imgState.src = icon;
            imgState.style = "vertical-align:text-top";
            String alt = "custom item icon alt text";
            imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, WIDTH, "16px");
            imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, ALT, alt, false);
            imgState.registerAttribute(AbstractHtmlState.ATTR_GENERAL, BORDER, "0");

            // set the inheritted attributes
            attrs.renderIconImage(imgState, node);
            imageRenderer.doStartTag(writer, imgState);
            imageRenderer.doEndTag(writer);
            renderItemIconSuffix(writer, node);
        }
    }

    // Overriding these methods to remove some of the formatting white space
    // and add a span around the item icon image.

    protected void renderSpacerPrefix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderSpacerSuffix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderConnectionImagePrefix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderConnectionImageSuffix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderSelectionLinkPrefix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderSelectionLinkSuffix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderItemIconPrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append("<span id=\"itemIcon\">");
    }

    protected void renderItemIconSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append("</span>");
    }

    protected void renderLabelPrefix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderLabelSuffix(AbstractRenderAppender writer, TreeElement node)
    {
    }

    protected void renderContentPrefix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE);
        writer.append(FORMAT_NEWLINE);
    }

    protected void renderContentSuffix(AbstractRenderAppender writer, TreeElement node)
    {
        writer.append(FORMAT_NEWLINE);
    }
}
