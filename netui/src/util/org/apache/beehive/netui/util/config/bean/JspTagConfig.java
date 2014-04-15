/**
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 $Header:$
 */
package org.apache.beehive.netui.util.config.bean;

/**
 *
 */
public class JspTagConfig {

    private static final DocType DEFAULT_DOC_TYPE = DocType.HTML4_LOOSE_QUIRKS;
    private static final IdJavascript DEFAULT_ID_JAVASCRIPT = IdJavascript.DEFAULT;
    private static final String DEFAULT_TREE_RENDERER_CLASS =
        "org.apache.beehive.netui.tags.tree.TreeRenderer";

    private DocType _docType;
    private IdJavascript _idJavascript;
    private String _treeImageLocation;
    private String _treeRendererClass;

    public JspTagConfig() {
        _docType = DEFAULT_DOC_TYPE;
        _idJavascript = DEFAULT_ID_JAVASCRIPT;
        _treeRendererClass = DEFAULT_TREE_RENDERER_CLASS;
    }

    public JspTagConfig(DocType docType, IdJavascript idJavascript, String treeImageLocation) {
        this();

        if(docType != null)
            _docType = docType;
        
        if(idJavascript != null)
            _idJavascript = idJavascript;

        _treeImageLocation = treeImageLocation;
    }

    public JspTagConfig(DocType docType, IdJavascript idJavascript, String treeImageLocation, String treeRendererClass) {
        this(docType, idJavascript, treeImageLocation);

        if(treeRendererClass != null)
            _treeRendererClass = treeRendererClass;
    }

    public DocType getDocType() {
        return _docType;
    }

    public IdJavascript getIdJavascript() {
        return _idJavascript;
    }

    public String getTreeImageLocation() {
        return _treeImageLocation;
    }

    public String getTreeRendererClass() {
        return _treeRendererClass;
    }
}
