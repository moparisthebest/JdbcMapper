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
package org.apache.beehive.netui.tags.tree;

import org.apache.beehive.netui.tags.rendering.AbstractHtmlState;
import org.apache.beehive.netui.tags.rendering.AnchorTag;
import org.apache.beehive.netui.tags.rendering.DivTag;
import org.apache.beehive.netui.tags.rendering.ImageTag;

import java.util.ArrayList;

public class AttributeRenderer
{
    private static final ArrayList empty = new ArrayList();
    private static final RemoveInfo emptyRemoves = new RemoveInfo();
    private ArrayList[] _lists;
    private RemoveInfo _removes;

    /**
     * Create the attribute renderer.  This will create Empty lists for each of the types of
     * attributes supported.  These empty lists will be replaced once an attribute is added to them.
     */
    public AttributeRenderer()
    {
        _lists = new ArrayList[TreeHtmlAttributeInfo.HTML_LOCATION_CNT];
        for (int i = 0; i < _lists.length; i++) {
            _lists[i] = empty;
        }
    }

    /**
     * Add all of the attributes associated with an element to the internal lists.
     * @param elem
     */
    public RemoveInfo addElement(TreeElement elem)
    {
        // if the attributes are empty then there is nothing to add to lists
        ArrayList attrs = elem.getAttributeList();
        if (attrs == null || attrs.size() == 0)
            return emptyRemoves;

        // We will track all of the elements that we are removing from the current inheritence
        // list set and return those back to the caller to stash away on the stack.
        RemoveInfo removes = _removes;
        if (removes == null)
            removes = new RemoveInfo();

        // walk all of the attributes
        int cnt = attrs.size();
        assert (cnt > 0);
        for (int i = 0; i < cnt; i++) {
            TreeHtmlAttributeInfo attr = (TreeHtmlAttributeInfo) attrs.get(i);
            if (attr.isOnDiv()) {
                addAttribute(TreeHtmlAttributeInfo.HTML_LOCATION_DIV, attr, removes);
            }
            if (attr.isOnIcon()) {
                addAttribute(TreeHtmlAttributeInfo.HTML_LOCATION_ICON, attr, removes);
            }
            if (attr.isOnSelectionLink()) {
                addAttribute(TreeHtmlAttributeInfo.HTML_LOCATION_SELECTION_LINK, attr, removes);
            }
        }

        // if we didn't remove anything then we should simply stash the array list away for next time
        // and return null.
        if (removes.removes.size() == 0) {
            _removes = removes;
            removes = null;
        }
        else {
            _removes = null;
        }

        return removes;
    }

    /**
     * This method will remove all of the elements scoped to the attribute.
     * @param elem
     */
    public void removeElementScoped(TreeElement elem, RemoveInfo removes)
    {
        assert(elem != null);

        // check to see if we can exist without processing this element.  If the element
        // didn't define any attribute then we can.  This is inforced in the addElement method.
        ArrayList attrs = elem.getAttributeList();
        if (attrs == null || attrs.size() == 0)
            return;

        // walk all of the lists and each list looking for attributes that
        // are scoped only to the element
        for (int i = 0; i < _lists.length; i++) {
            ArrayList al = _lists[i];
            assert(al != null);

            int cnt = al.size();
            for (int j = 0; j < cnt; j++) {
                TreeHtmlAttributeInfo attr = (TreeHtmlAttributeInfo) al.get(j);
                assert(attr != null);
                // Remove all of the attributes not scoped to the element itself.  We need to
                // update the indexes because this removes the current item..
                if (!attr.isApplyToDescendents()) {
                    al.remove(j);
                    cnt--;
                    j--;
                    if (removes != null && removes.scopeOverrides) {
                        TreeHtmlAttributeInfo addBack = checkScopeRemoval(i, attr, removes);
                        if (addBack != null) {
                            if (!al.contains(addBack))
                                al.add(addBack);
                        }
                    }
                }
            }
        }
    }

    public void removeElement(TreeElement elem, RemoveInfo removes)
    {
        // walk all of the attributes associated with the element, for any that are passed
        // on to their decendents we will remove.  This will be done for each type of element
        // being removed.
        ArrayList attrs = elem.getAttributeList();
        if (attrs != null) {
            int cnt = attrs.size();
            for (int i = 0; i < cnt; i++) {
                TreeHtmlAttributeInfo attr = (TreeHtmlAttributeInfo) attrs.get(i);

                // if the attribute is being passed on to the decendents then we need to output
                // that attribute.
                if (attr.isApplyToDescendents()) {
                    if (attr.isOnDiv()) {
                        removeAttribute(TreeHtmlAttributeInfo.HTML_LOCATION_DIV, attr, removes);
                    }
                    if (attr.isOnIcon()) {
                        removeAttribute(TreeHtmlAttributeInfo.HTML_LOCATION_ICON, attr, removes);
                    }
                    if (attr.isOnSelectionLink()) {
                        removeAttribute(TreeHtmlAttributeInfo.HTML_LOCATION_SELECTION_LINK, attr, removes);
                    }
                }
            }
        }
    }

    /**
     * This method will render the values assocated with the Icon Image.
     * @param state
     * @param elem
     */
    public void renderIconImage(ImageTag.State state, TreeElement elem)
    {
        ArrayList al = _lists[TreeHtmlAttributeInfo.HTML_LOCATION_ICON];

        assert(al != null);
        if (al.size() == 0)
            return;

        int cnt = al.size();
        for (int i = 0; i < cnt; i++) {
            TreeHtmlAttributeInfo attr = (TreeHtmlAttributeInfo) al.get(i);
            state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, attr.getAttribute(), attr.getValue());
        }
    }

    /**
     * This method will render the values assocated with the selection link.
     * @param state
     * @param elem
     */
    public void renderSelectionLink(AnchorTag.State state, TreeElement elem)
    {
        ArrayList al = _lists[TreeHtmlAttributeInfo.HTML_LOCATION_SELECTION_LINK];

        assert(al != null);
        if (al.size() == 0)
            return;

        int cnt = al.size();
        for (int i = 0; i < cnt; i++) {
            TreeHtmlAttributeInfo attr = (TreeHtmlAttributeInfo) al.get(i);
            state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, attr.getAttribute(), attr.getValue());
        }
    }

    /**
     * This method will render the values assocated with the div around a treeItem.
     * @param state
     * @param elem
     */
    public void renderDiv(DivTag.State state, TreeElement elem)
    {
        ArrayList al = _lists[TreeHtmlAttributeInfo.HTML_LOCATION_DIV];

        assert(al != null);
        if (al.size() == 0)
            return;

        int cnt = al.size();
        for (int i = 0; i < cnt; i++) {
            TreeHtmlAttributeInfo attr = (TreeHtmlAttributeInfo) al.get(i);
            state.registerAttribute(AbstractHtmlState.ATTR_GENERAL, attr.getAttribute(), attr.getValue());
        }
    }

    public String toString()
    {
        return "AttributeRender: D:" + _lists[TreeHtmlAttributeInfo.HTML_LOCATION_DIV].size() +
                " I:" + _lists[TreeHtmlAttributeInfo.HTML_LOCATION_ICON].size() +
                " IL:" + _lists[TreeHtmlAttributeInfo.HTML_LOCATION_SELECTION_LINK].size();
    }

    private TreeHtmlAttributeInfo checkScopeRemoval(int list, TreeHtmlAttributeInfo attr, RemoveInfo removes)
    {
        int cnt = removes.removes.size();
        assert(cnt > 0);

        for (int i = 0; i < cnt; i++) {
            TreeHtmlAttributeInfo a = (TreeHtmlAttributeInfo) removes.removes.get(i);
            if (a.getAttribute().equals(attr.getAttribute())) {
                switch (list) {
                    case TreeHtmlAttributeInfo.HTML_LOCATION_DIV:
                        if (a.isOnDiv())
                            return a;
                        break;
                    case TreeHtmlAttributeInfo.HTML_LOCATION_ICON:
                        if (a.isOnIcon())
                            return a;
                        break;
                    case TreeHtmlAttributeInfo.HTML_LOCATION_SELECTION_LINK:
                        if (a.isOnSelectionLink())
                            return a;
                        break;
                }
            }
        }
        return null;
    }

    /**
     * This method will add an attribute to the list of inheritted attributes.
     * @param list
     * @param attr
     * @param removes
     */
    private void addAttribute(int list, TreeHtmlAttributeInfo attr, RemoveInfo removes)
    {
        ArrayList al = _lists[list];
        // if the array list is the empty list then we need to allocate a new array list
        if (al == empty) {
            al = new ArrayList();
            _lists[list] = al;
        }

        // check to see if this attribute is already inside the tree.
        int cnt = al.size();
        for (int i = 0; i < cnt; i++) {
            TreeHtmlAttributeInfo a = (TreeHtmlAttributeInfo) al.get(i);
            assert(a != null);
            if (a.getAttribute().equals(attr.getAttribute())) {
                removes.removes.add(a);
                if (!attr.isApplyToDescendents()) {
                    removes.scopeOverrides = true;
                }
                al.remove(a);
                break;
            }
        }

        // add this to the list
        al.add(attr);
    }

    /**
     * This method will add an attribute to the list of inheritted attributes.
     * @param list
     * @param attr
     * @param removes
     */
    private void removeAttribute(int list, TreeHtmlAttributeInfo attr, RemoveInfo removes)
    {
        assert(attr != null);

        // get the correct array list
        ArrayList al = _lists[list];
        assert(al != null);

        // remove the attributes from the array list -- this should never fail
        boolean removed = al.remove(attr);
        assert(removed) : "Unable to remove attribute:" + attr.getAttribute() + "=" + attr.getValue();

        // if there was a list of removed attributes we need to possibly add one back
        // We do this by walking the removes list and when we find an attribute with the
        // same name and supporting the same list type, we add it to the list.
        if (removes != null) {

            // walk the remove list looking for an attribute with the same name
            int cnt = removes.removes.size();
            for (int i = 0; i < cnt; i++) {
                TreeHtmlAttributeInfo a = (TreeHtmlAttributeInfo) removes.removes.get(i);
                if (attr.getAttribute().equals(a.getAttribute())) {

                    // based upon the type we are removing, if the matching attribute supports the type
                    // then we add it back.
                    switch (list) {
                        case TreeHtmlAttributeInfo.HTML_LOCATION_DIV:
                            if (a.isOnDiv() && !al.contains(a))
                                al.add(a);
                            break;
                        case TreeHtmlAttributeInfo.HTML_LOCATION_ICON:
                            if (a.isOnIcon() && !al.contains(a))
                                al.add(a);
                            break;
                        case TreeHtmlAttributeInfo.HTML_LOCATION_SELECTION_LINK:
                            if (a.isOnSelectionLink() && !al.contains(a))
                                al.add(a);
                            break;
                    }
                }
            }
        }
    }

    public static class RemoveInfo
    {
        ArrayList removes;
        boolean scopeOverrides;

        public RemoveInfo()
        {
            removes = new ArrayList();
            scopeOverrides = false;
        }
    }
}
