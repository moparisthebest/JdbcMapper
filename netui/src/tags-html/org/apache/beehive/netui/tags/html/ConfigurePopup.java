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
package org.apache.beehive.netui.tags.html;

import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.util.Bundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.Tag;


/**
 * Configures popup window parameters for parent tags that can open popup windows.
 * @jsptagref.tagdescription Configures popup window parameters for parent tags that can open popup windows.
 * @example <pre>
 *     &lt;netui:anchor action="getCityZipFromNestedPageFlow" popup="true">
 *         Get a city and zip code
 *         &lt;netui:configurePopup resizable="false" width="400" height="200">
 *             &lt;netui:retrievePopupOutput tagIdRef="zipCodeField" dataSource="outputFormBean.zipCode" /&gt;
 *             &lt;netui:retrievePopupOutput tagIdRef="cityField" dataSource="outputFormBean.city" /&gt;
 *         &lt;/netui:configurePopup>
 *     &lt;/netui:anchor></pre>
 * @netui:tag name="configurePopup" description="Configures popup window parameters for parent tags that can open popup windows."
 */
public class ConfigurePopup
        extends AbstractClassicTag
{
    private String _name = "";
    private Boolean _toolbar = null;
    private Boolean _location = null;
    private Boolean _directories = null;
    private Boolean _status = null;
    private Boolean _menubar = null;
    private Boolean _resizable = null;
    private Boolean _scrollbars = null;
    private Integer _width = null;
    private Integer _height = null;
    private Integer _left = null;
    private Integer _top = null;
    private boolean _replace = false;
    private String _popupFunc;
    private String _onPopupDone;
    private boolean _updateFormFields = false;

    /**
     * Return the name of the Tag.
     */
    public String getTagName()
    {
        return "ConfigurePopup";
    }

    /**
     * Sets the JavaScript function to be called when the popup window is closing.  If this attribute is not present,
     * a default function will be generated.
     * @param onPopupDone the JavaScript function to be called when the popup window is closing.
     * @jsptagref.attributedescription The JavaScript function to be called when the popup window is closing.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_onPopupDone</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The JavaScript function to be called when the popup window is closing."
     */
    public void setOnPopupDone(String onPopupDone)
    {
        _onPopupDone = onPopupDone;
    }

    /**
     * Sets the JavaScript function to be called to open the popup window.  This function overrides the auto-generated
     * one that is based on the other attributes like <code>resizable</code>, <code>name</code>, etc.
     * @param popupFunc the JavaScript function to be called to open the popup window.
     * @jsptagref.attributedescription The JavaScript function to be called to open the popup window.
     * @jsptagref.databindable false
     * @jsptagref.attributesyntaxvalue <i>string_popupFunc</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The JavaScript function to be called to open the popup window."
     */
    public void setPopupFunc(String popupFunc)
    {
        _popupFunc = popupFunc;
    }

    /**
     * Sets whether the JavaScript function that opens the popup window should add data
     * from the form fields to the request.
     * @param updateFormFields whether the data from the form fields is included in the popup window request.
     * @jsptagref.attributedescription Whether the data from the form fields is included in the popup window request.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_updateFormFields</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether the data from the form fields is included in the popup window request."
     */
    public void setUpdateFormFields(boolean updateFormFields)
    {
        _updateFormFields = updateFormFields;
    }

    /**
     * Sets the name of the popup window.
     * @param name the name of the popup window.
     * @jsptagref.attributedescription The name of the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>string_name</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The name of the popup window."
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Sets whether the toolbar is visible in the popup window.
     * @param toolbar whether the toolbar is visible in the popup window.
     * @jsptagref.attributedescription Whether the toolbar is visible in the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_toolbar</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether the toolbar is visible in the popup window."
     */
    public void setToolbar(boolean toolbar)
    {
        _toolbar = Boolean.valueOf(toolbar);
    }

    /**
     * Sets whether the location (address) bar is visible in the popup window.
     * @param location whether the location (address) bar is visible in the popup window.
     * @jsptagref.attributedescription Whether the location (address) bar is visible in the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_location</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether the location (address) bar is visible in the popup window."
     */
    public void setLocation(boolean location)
    {
        _location = Boolean.valueOf(location);
    }

    /**
     * Sets whether directory buttons are displayed in the popup window.
     * @param directories whether directory buttons are displayed in the popup window.
     * @jsptagref.attributedescription Whether directory buttons are displayed in the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_directories</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether directory buttons are displayed in the popup window."
     */
    public void setDirectories(boolean directories)
    {
        _directories = Boolean.valueOf(directories);
    }

    /**
     * Sets whether the status bar is displayed in the popup window.
     * @param status whether the status bar is displayed in the popup window.
     * @jsptagref.attributedescription Whether the status bar is displayed in the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_status</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether the status bar is displayed in the popup window."
     */
    public void setStatus(boolean status)
    {
        _status = Boolean.valueOf(status);
    }

    /**
     * Sets whether the menu bar is displayed in the popup window.
     * @param menubar whether the menu bar is displayed in the popup window.
     * @jsptagref.attributedescription Whether the menu bar is displayed in the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_menubar</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether the menu bar is displayed in the popup window."
     */
    public void setMenubar(boolean menubar)
    {
        _menubar = Boolean.valueOf(menubar);
    }

    /**
     * Sets whether the popup window has scroll bars.
     * @param scrollbars whether the popup window has scroll bars.
     * @jsptagref.attributedescription Whether the popup window has scroll bars.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_scrollbars</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether the popup window has scroll bars."
     */
    public void setScrollbars(boolean scrollbars)
    {
        _scrollbars = Boolean.valueOf(scrollbars);
    }

    /**
     * Sets whether the popup window is resizable.
     * @param resizable whether the popup window is resizable.
     * @jsptagref.attributedescription Whether the popup window is resizable.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_resizable</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether the popup window is resizable."
     */
    public void setResizable(boolean resizable)
    {
        _resizable = Boolean.valueOf(resizable);
    }

    /**
     * Sets the width of the popup window.
     * @param width the width of the popup window.
     * @jsptagref.attributedescription The width of the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>integer_width</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The width of the popup window."
     */
    public void setWidth(int width)
    {
        _width = new Integer(width);
    }

    /**
     * Sets the height of the popup window.
     * @param height the height of the popup window.
     * @jsptagref.attributedescription The height of the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>integer_height</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The height of the popup window."
     */
    public void setHeight(int height)
    {
        _height = new Integer(height);
    }

    /**
     * Sets the X coordinate of the top left corner of the popup window.
     * @param left the X coordinate of the top left corner of the popup window.
     * @jsptagref.attributedescription The X coordinate of the top left corner of the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>integer_left</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The X coordinate of the top left corner of the popup window."
     */
    public void setLeft(int left)
    {
        _left = new Integer(left);
    }

    /**
     * Sets the Y coordinate of the top left corner of the popup window.
     * @param top the Y coordinate of the top left corner of the popup window.
     * @jsptagref.attributedescription The Y coordinate of the top left corner of the popup window.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>integer_left</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="The Y coordinate of the top left corner of the popup window."
     */
    public void setTop(int top)
    {
        _top = new Integer(top);
    }

    /**
     * Sets whether the popup window's location will replace the location in the current window's navigation history.
     * @param replace whether the popup window's location will replace the location in the current window's navigation history.
     * @jsptagref.attributedescription Whether the popup window's location will replace the location in the current window's navigation history.
     * @jsptagref.databindable true
     * @jsptagref.attributesyntaxvalue <i>boolean_replace</i>
     * @netui:attribute required="false" rtexprvalue="true"
     * description="Whether the popup window's location will replace the location in the current window's navigation history."
     */
    public void setReplace(boolean replace)
    {
        _replace = replace;
    }

    /**
     * @throws JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException
    {
        JspTag parentTag = SimpleTagSupport.findAncestorWithClass(this, IHasPopupSupport.class);
        if (parentTag == null) {
            String msg = Bundle.getString("Tags_InvalidConfigurePopupParent");
            registerTagError(msg, null);
            reportErrors();
        }
        else {
            PopupSupport popupSupport = ((IHasPopupSupport) parentTag).getPopupSupport();
            
            // if popupSupport is null, then the tag isn't set to open a popup window
            if (popupSupport != null) {
                popupSupport.setName(_name);
                if (_toolbar != null)
                    popupSupport.setToolbar(_toolbar.booleanValue());
                if (_location != null)
                    popupSupport.setLocation(_location.booleanValue());
                if (_directories != null)
                    popupSupport.setDirectories(_directories.booleanValue());
                if (_status != null)
                    popupSupport.setStatus(_status.booleanValue());
                if (_menubar != null)
                    popupSupport.setMenubar(_menubar.booleanValue());
                if (_resizable != null)
                    popupSupport.setResizable(_resizable.booleanValue());
                if (_scrollbars != null)
                    popupSupport.setScrollbars(_scrollbars.booleanValue());
                if (_width != null)
                    popupSupport.setWidth(_width.intValue());
                if (_height != null)
                    popupSupport.setHeight(_height.intValue());
                if (_left != null)
                    popupSupport.setLeft(_left.intValue());
                if (_top != null)
                    popupSupport.setTop(_top.intValue());
                popupSupport.setReplace(_replace);
                popupSupport.setPopupFunc(_popupFunc);
                popupSupport.setOnPopupDone(_onPopupDone);

                if (_updateFormFields) {
                    Form form = getNearestForm();
                    String realFormName = form.getRealFormId();
                    form.insureRealId();
                    popupSupport.setFormName(realFormName);
                }
            }
        }

        localRelease();
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Release any acquired resources.
     */
    protected void localRelease()
    {
        _name = "";
        _toolbar = null;
        _location = null;
        _directories = null;
        _status = null;
        _menubar = null;
        _resizable = null;
        _scrollbars = null;
        _width = null;
        _height = null;
        _left = null;
        _top = null;
        _replace = false;
        _popupFunc = null;
        _onPopupDone = null;
        _updateFormFields = false;

        super.localRelease();
    }

}
