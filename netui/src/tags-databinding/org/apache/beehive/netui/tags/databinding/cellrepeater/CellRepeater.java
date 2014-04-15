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
package org.apache.beehive.netui.tags.databinding.cellrepeater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TryCatchFinally;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.ServletRequest;

import org.apache.beehive.netui.script.common.IDataAccessProvider;
import org.apache.beehive.netui.script.common.DataAccessProviderStack;
import org.apache.beehive.netui.tags.AbstractClassicTag;
import org.apache.beehive.netui.tags.ExpressionHandling;
import org.apache.beehive.netui.tags.rendering.TagRenderingBase;
import org.apache.beehive.netui.tags.rendering.TableTag;
import org.apache.beehive.netui.tags.rendering.TdTag;
import org.apache.beehive.netui.tags.rendering.TrTag;
import org.apache.beehive.netui.tags.rendering.AbstractRenderAppender;
import org.apache.beehive.netui.tags.rendering.StringBuilderRenderAppender;
import org.apache.beehive.netui.tags.rendering.ConstantRendering;
import org.apache.beehive.netui.util.Bundle;
import org.apache.beehive.netui.util.exception.LocalizedUnsupportedOperationException;
import org.apache.beehive.netui.util.internal.InternalStringBuilder;
import org.apache.beehive.netui.util.iterator.IteratorFactory;
import org.apache.beehive.netui.util.logging.Logger;

/**
 * <p/>
 * This tag is a repeating, databound tag that renders its body each cell of a table of the specified dimensions.
 * The tag is bound to a dataset that is specified in the <code>dataSource</code> attribute.  For each item in
 * the data set, the body of this tag is rendered, and NetUI tags in the body that are databound
 * can use the <code>container.item</code> syntax to access the "current" data item in the iteration.
 * Properties on this data item can be accessed using expressions.
 * </p>
 * <p>
 * The tag will automatically insert the open and close table, row, and cell tags which will comprise
 * the table that is rendered.  Style attributes may be set using attributes on this tag in order to
 * cusotmize the tag's final appearance.  The dimensions of the table are specified by using at least
 * one of the attributes columns and rows.  If only one is specified, the other will be inferred by
 * using the size of the given data set.  As a result, the entire dataset will be rendered.  For example,
 * if a table should be four columns wide and the data set has twenty items, the resulting table will
 * have five rows.  If the data set is fewer items than the number of cells that should be rendered,
 * the cells are padded with HTML table cells:
 * </p>
 * <pre>
 *     &lt;td&gt;&amp;nbsp;&lt;/td&gt;
 * </pre>
 * <p/>
 * This will prevent rendering a malformed HTML table.  If the number of cells to render is smaller than
 * the fully specified dimensions of the table, only this number of cells will be rendered.  For example,
 * if the data set is size fifty but the <code>rows</code> and the <code>columns</code> attributes are
 * both seven, only the first forty-nine items in the dataset will be rendered and the fiftieth
 * will not be shown.  The values of the <code>rows</code> and the <code>columns</code> can be databound with
 * an expression; in this case, each value will be converted into an integer.  An error will be reported
 * on the page if this conversion fails.
 * </p><p>
 * This tag implements the {@link IDataAccessProvider} interface which provides tags access to the "current"
 * data item.  Properties on the <code>IDataAccessProvider</code> interface are available through the
 * "container" binding context, which can be used inside of the body of the CellRepeater.  Properties of
 * the <code>IDataAccessProvider</code> interface that are available include:
 * <table border="1" cellspacing="0" cellpadding="5" width="75%">
 * <tr><td><b>Name</b></td><td><b>Description</b></td></tr>
 * <tr><td>index</td><td>the current index in the iteration; this index is absolute to the dataset</td></tr>
 * <tr><td>parent</td><td>any <code>IDataAccessProvider</code> parent of this tag</td></tr>
 * <tr><td>item</td><td>the current data item</td></tr>
 * </table>
 * </p>
 * <p/>
 * <b>Note:</b> the metadata property of the <code>container</code> binding context is not supported
 * on the CellRepeater.
 * </p>
 *
 * @jsptagref.tagdescription
 * <p/>
 * This tag is a repeating, databound tag that renders its body each cell of a table of the specified dimensions.
 * The tag is bound to a dataset that is specified in the <code>dataSource</code> attribute.  For each item in
 * the data set, the body of this tag is rendered, and NetUI tags in the body that are databound
 * can use the <code>container.item</code> syntax to access the "current" data item in the iteration.
 * Properties on this data item can be accessed using expressions.
 * </p>
 * <p>
 * The tag will automatically insert the open and close table, row, and cell tags which will comprise
 * the table that is rendered.  Style attributes may be set using attributes on this tag in order to
 * cusotmize the tag's final appearance.  The dimensions of the table are specified by using at least
 * one of the attributes columns and rows.  If only one is specified, the other will be inferred by
 * using the size of the given data set.  As a result, the entire dataset will be rendered.  For example,
 * if a table should be four columns wide and the data set has twenty items, the resulting table will
 * have five rows.  If the data set is fewer items than the number of cells that should be rendered,
 * the cells are padded with HTML table cells:
 * </p>
 * <pre>
 *     &lt;td&gt;&amp;nbsp;&lt;/td&gt;
 * </pre>
 * <p/>
 * This will prevent rendering a malformed HTML table.  If the number of cells to render is smaller than
 * the fully specified dimensions of the table, only this number of cells will be rendered.  For example,
 * if the data set is size fifty but the <code>rows</code> and the <code>columns</code> attributes are
 * both seven, only the first forty-nine items in the dataset will be rendered and the fiftieth
 * will not be shown.  The values of the <code>rows</code> and the <code>columns</code> can be databound with
 * an expression; in this case, each value will be converted into an integer.  An error will be reported
 * on the page if this conversion fails.
 * </p><p>
 * This tag implements the {@link IDataAccessProvider} interface which provides tags access to the "current"
 * data item.  Properties on the <code>IDataAccessProvider</code> interface are available through the
 * "container" binding context, which can be used inside of the body of the CellRepeater.  Properties of
 * the <code>IDataAccessProvider</code> interface that are available include:
 * <table border="1" cellspacing="0" cellpadding="5" width="75%">
 * <tr><td><b>Name</b></td><td><b>Description</b></td></tr>
 * <tr><td>index</td><td>the current index in the iteration; this index is absolute to the dataset</td></tr>
 * <tr><td>parent</td><td>any <code>IDataAccessProvider</code> parent of this tag</td></tr>
 * <tr><td>item</td><td>the current data item</td></tr>
 * </table>
 * </p>
 * <p/>
 * <b>Note:</b> the metadata property of the <code>container</code> binding context is not supported
 * on the CellRepeater.
 * </p>
 *
 * @example
 * In this example, the &lt;netui-data:cellRepeater> tag creates a table with the number of columns set
 * given as <code>${pageFlow.numColumns}</code> and as many rows as necessary to display all the items in the
 * <code>pageFlow.itemArray</code> data set.
 * <pre>
 *    &lt;netui-data:cellRepeater dataSource="pageFlow.itemArray" columns="pageFlow.numColumns">
 *        Item: &lt;netui:span value="${container.item}"/>
 *    &lt;/netui-data:cellRepeater>
 * </pre>
 * @netui:tag name="cellRepeater"
 *            description="A repeating, databound tag that renders its body into each cell of a table of the specified dimensions."
 */
public class CellRepeater
    extends AbstractClassicTag
    implements IDataAccessProvider, TryCatchFinally {

    private static final Logger LOGGER = Logger.getInstance(CellRepeater.class);
    private static final int DIMENSION_DEFAULT_VALUE = -1;
    private static final TableTag.State STATE_TABLE = new TableTag.State();
    private static final TrTag.State STATE_TR = new TrTag.State();
    private static final TdTag.State STATE_TD = new TdTag.State();

    private boolean _valid = true;
    private boolean _verticalRepeat = false;
    private boolean _containerInPageContext = false;
    private int _columns = DIMENSION_DEFAULT_VALUE;
    private int _rows = DIMENSION_DEFAULT_VALUE;
    private int _currentIndex = -1;
    private int _currentRow = -1;
    private int _currentColumn = -1;

    private ArrayList _dataList = null;
    private Object _currentItem = null;
    private String _dataSource = null;
    private String _altCellClass = null;
    private String _cellClass = null;
    private ConstantRendering _htmlConstantRendering = null;
    private TagRenderingBase _tableRenderer = null;
    private TagRenderingBase _trRenderer = null;
    private TagRenderingBase _tdRenderer = null;
    private TableTag.State _tableState = null;
    private TdTag.State _tdState = null;
    private TrTag.State _trState = null;
    private InternalStringBuilder _sb = null;
    private AbstractRenderAppender _appender = null;

    /**
     * Get the name of this tag.  This is used to identify the type of this tag
     * for reporting tag errors.
     *
     * @return a constant String representing the name of this tag.
     */
    public String getTagName() {
        return "CellRepeater";
    }

    /**
     * The HTML style class that is rendered on the HTML table.  For example, if the row class is "tableClass",
     * each opening table tag is:
     * <pre>
     *     &lt;table class="tableClass"&gt;
     * </pre>
     *
     * @param tableClass the name of a style class in a CSS
     * @jsptagref.attributedescription
     * The HTML style class that is rendered on the HTML table.  For example, if the row class is "tableClass",
     * each opening table tag is:
     * <pre>
     *     &lt;table class="tableClass"&gt;
     * </pre>
     * @jsptagref.attributesyntaxvalue <i>string_class</i>
     * @netui:attribute required="false"
     */
    public void setTableClass(String tableClass) {
        if("".equals(tableClass))
            return;
        _tableState = new TableTag.State();
        _tableState.styleClass = tableClass;
    }

    /**
     * Set the HTML style class that is rendered on each HTML table row that
     * is opened by this tag.  For example, if the row class is "rowClass",
     * each opening table row tag is:
     * <pre>
     *     &lt;tr class="rowClass"&gt;
     * </pre>
     *
     * @param rowClass the name of a style class in the CSS
     * @jsptagref.attributedescription
     * Set the HTML style class that is rendered on each HTML table row that
     * is opened by this tag.  For example, if the row class is "rowClass",
     * each opening table row tag is:
     * <pre>
     *     &lt;tr class="rowClass"&gt;
     * </pre>
     * @jsptagref.attributesyntaxvalue <i>string_class</i>
     * @netui:attribute required="false"
     */
    public void setRowClass(String rowClass) {
        if("".equals(rowClass))
            return;
        _trState = new TrTag.State();
        _trState.styleClass = rowClass;
    }

    /**
     * Set the HTML style class that is rendered on each HTML table cell that
     * is opened by this tag.  For example, if the cell class is "cellClass",
     * each opening table cell tag is:
     * <pre>
     *     &lt;td class="cellClass"&gt;
     * </pre>
     *
     * @param cellClass the name of a style class in a CSS
     * @jsptagref.attributedescription
     * Set the HTML style class that is rendered on each HTML table cell that
     * is opened by this tag.  For example, if the cell class is "cellClass",
     * each opening table cell tag is:
     * <pre>
     *     &lt;td class="cellClass"&gt;
     * </pre>
     * @jsptagref.attributesyntaxvalue <i>string_class</i>
     * @netui:attribute required="false"
     */
    public void setCellClass(String cellClass) {
        if("".equals(cellClass))
            return;
        _cellClass = cellClass;
    }

    /**
     * Set the HTML style class that is rendered on each HTML table cell that
     * is opened by this tag.  The starting cell is alternated for each row, which
     * results in a checkerboard colored table being displayed.  For example, if the
     * alteranting cell class is \"alternatingCellClass\", every other table cell
     * tag is:
     * <pre>
     *     &lt;td cell="alternatingCellClass"&gt;
     * </pre>
     *
     * @param alternatingCellClass the name of a style class in a CSS
     * @jsptagref.attributedescription
     * The HTML style class that is rendered on alternating table cells.
     * The starting cell is alternated for each row, which
     * results in a checkerboard colored table being displayed.  For example, if the
     * alteranting cell class is "alternatingCellClass", every other table cell
     * tag is:
     * <pre>
     *     &lt;td cell="alternatingCellClass"&gt;</pre>
     * @jsptagref.attributesyntaxvalue <i>string_class</i>
     * @netui:attribute required="false"
     */
    public void setAlternatingCellClass(String alternatingCellClass) {
        if("".equals(alternatingCellClass))
            return;
        _altCellClass = alternatingCellClass;
    }

    /**
     * This tag can render the items in its dataset horizontally or vertically.  If
     * the rows are rendered horizontally, the items in the dataset are rendered
     * across each row from top to bottom.  Otherwise, they are rendered down each
     * column from left to right.  The default is to render the items horizontally.
     *
     * @param verticalRepeat if set to <code>true</code>, the dataset is rendered down
     *                       each column; otherwise it is rendered across each row, the default.
     * @jsptagref.attributedescription
     * Boolean. If true the data set is rendered vertically, otherwise it is rendered horizontally.  If
     * the rows are rendered horizontally, the items in the data set are rendered
     * across each row from top to bottom.  Otherwise, they are rendered down each
     * column from left to right.  The default is to render the items horizontally.
     * @jsptagref.attributesyntaxvalue <i>boolean_verticalRepeat</i>
     * @netui:attribute required="false"
     */
    public void setVerticalRepeat(boolean verticalRepeat) {
        _verticalRepeat = verticalRepeat;
    }

    /**
     * Set the number of columns that should be rendered in the table
     * generated by the tag.  If the columns attribute is specified but
     * the rows attribute is not, the rows attribute will be inferred
     * using the size of the dataset.
     *
     * @param columns an integer or an expression
     * @jsptagref.attributedescription
     * Integer. The number of columns that should be rendered in the HTML table.
     * If the <code>columns</code> attribute is specified but
     * the <code>rows</code> attribute is not, the <code>rows</code> attribute will be inferred
     * using the size of the data set.
     * @jsptagref.attributesyntaxvalue <i>integer_columns</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setColumns(int columns) {
        _columns = columns;
    }

    /**
     * Set the number of rows that should be rendered in the table
     * generated by the tag.  If the rows attribute is specified but
     * the columns attribute is not, the columns attribute will be
     * inferred using the size of the dataset.
     *
     * @param rows an integer or an expression whose value can be
     *             converted into an integer.
     * @jsptagref.attributedescription
     * Integer. The number of rows that should be rendered in the HTML table.
     * If the <code>rows</code> attribute is specified but
     * the <code>columns</code> attribute is not, the <code>columns</code> attribute will be
     * inferred using the size of the data set.
     * @jsptagref.attributesyntaxvalue <i>integer_rows</i>
     * @netui:attribute required="false" rtexprvalue="true"
     */
    public void setRows(int rows) {
        _rows = rows;
    }

    /**
     * <p>The <code>dataSource</code> attribute determines both
     * (1) the source of populating data for the tag and
     * (2) the object to which the tag submits data.
     *
     * <p>For example, assume that the Controller file (= JPF file) contains
     * a Form Bean with the property foo.  Then the following &lt;netui:textBox> tag will
     * (1) draw populating data from the Form Bean's foo property and (2)
     * submit user defined data to the same property.
     *
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;netui:textBox dataSource="actionForm.foo" /></code>
     *
     * <p>When the tag is used to submit data, the data binding expression must
     * refer to a Form Bean property.
     * In cases where the tag is not used to submit data, but is used for
     * displaying data only, the data
     * binding expression need not refer to a Form Bean property.  For example,
     * assume that myIterativeData is a member variable on
     * the Controller file ( = JPF file).  The following &lt;netui-data:repeater>
     * tag draws its data from myIterativeData.

     * @param dataSource the data source
     * @jsptagref.attributedescription
     * <p>The <code>dataSource</code> attribute determines both
     * (1) the source of populating data for the tag and
     * (2) the object to which the tag submits data.
     *
     * <p>For example, assume that the Controller file (= JPF file) contains
     * a Form Bean with the property foo.  Then the following &lt;netui:textBox> tag will
     * (1) draw populating data from the Form Bean's foo property and (2)
     * submit user defined data to the same property.
     *
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;netui:textBox dataSource="actionForm.foo" /></code>
     *
     * <p>When the tag is used to submit data, the data binding expression must
     * refer to a Form Bean property.
     * In cases where the tag is not used to submit data, but is used for
     * displaying data only, the data
     * binding expression need not refer to a Form Bean property.  For example,
     * assume that myIterativeData is a member variable on
     * the Controller file ( = JPF file).  The following &lt;netui-data:repeater>
     * tag draws its data from myIterativeData.
     *
     * <p>&nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;netui-data:cellRepeater dataSource="pageFlow.myIterativeData"></code>
     * @jsptagref.attributesyntaxvalue <i>expression_datasource</i>
     * @netui:attribute required="true"
     */
    public void setDataSource(String dataSource) {
        _dataSource = dataSource;
    }

    /**
     * Prepare to render the dataset that was specified in the dataSource attribute.  The
     * dataSource expression is evaluated and the table's dimensions are computed.  If
     * there is no data in the dataset but the rows and columns attributes were specified,
     * an empty table of the given dimensions is rendered.
     *
     * @return EVAL_BODY_BUFFERED or SKIP_BODY if errors are reported, the data set
     *         is null, or there is no data in the data set
     * @throws JspException if errors occurred that could not be reported in the page
     */
    public int doStartTag()
            throws JspException {
        ServletRequest request = pageContext.getRequest();

        _tableRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TABLE_TAG, request);
        _trRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TR_TAG, request);
        _tdRenderer = TagRenderingBase.Factory.getRendering(TagRenderingBase.TD_TAG, request);
        _htmlConstantRendering = TagRenderingBase.Factory.getConstantRendering(request);

        _sb = new InternalStringBuilder(1024);
        _appender = new StringBuilderRenderAppender(_sb);

        Object source = evaluateDataSource();

        if(hasErrors())
            return SKIP_BODY;

        if(source != null) {
            Iterator iterator = IteratorFactory.createIterator(source);
            if(iterator == null) {
                LOGGER.info("CellRepeater: The data structure from which to create an iterator is null.");
                iterator = Collections.EMPTY_LIST.iterator();
            }

            if(iterator != null) {
                _dataList = new ArrayList();
                while(iterator.hasNext()) {
                    _dataList.add(iterator.next());
                }
            }
        }

        if(_rows == DIMENSION_DEFAULT_VALUE || _columns == DIMENSION_DEFAULT_VALUE) {
            /* try to guess the dimensions of the table */
            if(_dataList != null && _dataList.size() > 0) {
                guessDimensions(_dataList);

                if(hasErrors())
                    return SKIP_BODY;
            }
            /* the size of the data set isn't guessable */
            else {
                _valid = false;
                return SKIP_BODY;
            }
        }

        /* check to make sure the rows / columns are actually valid before starting to render */
        if(_rows <= 0) {
            String msg = Bundle.getString("Tags_CellRepeater_invalidRowValue", new Object[]{getTagName(), new Integer(_rows)});
            registerTagError(msg, null);
        }

        if(_columns <= 0) {
            String msg = Bundle.getString("Tags_CellRepeater_invalidColumnValue", new Object[]{getTagName(), new Integer(_columns)});
            registerTagError(msg, null);
        }

        if(hasErrors())
            return SKIP_BODY;

        openTableTag(_appender, _tableState);

        _currentRow = 0;
        _currentColumn = 0;

        DataAccessProviderStack.addDataAccessProvider(this, pageContext);
        _containerInPageContext = true;

        boolean haveItem = ensureItem(0, _dataList);
        if(haveItem) {
            openRowTag(_appender, _trState);
            openCellTag(_appender, _currentColumn);
            return EVAL_BODY_BUFFERED;
        }
        else {
            // special case -- with no items, render the entire table here
            for(int i = 0; i < _rows; i++) {
                openRowTag(_appender, _trState);
                for(int j = 0; j < _columns; j++) {
                    openCellTag(_appender, computeStyleIndex(i, j));
                    _htmlConstantRendering.NBSP(_appender);
                    closeCellTag(_appender);
                }
                closeRowTag(_appender);
                _appender.append("\n");
            }
            _currentRow = _rows;
            _currentColumn = _columns;
            return SKIP_BODY;
        }
    }

    /**
     * Continue rendering the body of this tag until the dimensions of the table have been reached or
     * the entire dataset has been rendered.  The buffered body content from the previous iteration
     * of the body is added to the content this tag will render, @see addContent(java.lang.String).
     * Pad the table if the dimensions have not been met but the dataset is empty.
     *
     * @return EVAL_BODY_BUFFERED if there is more data to render in the dataset or
     *         SKIP_BODY if the end of the dataset is reached or an error occurs
     */
    public int doAfterBody() {
        if(bodyContent != null) {
            _appender.append(bodyContent.getString());
            bodyContent.clearBody();
        }

        /*
           this loop exists so that the table is filled out correctly up to the specified
           or guessed table dimensions.  this is a little bit of a kludge; this logic should be done
           in doEndTag()
         */
        boolean haveNext = false;
        while(!haveNext) {
            _currentColumn++;

            /* close the previous cell whose content was rendered the last time the tag body was executed */
            closeCellTag(_appender);

            /* open a new table row */
            if(_currentColumn == _columns) {
                _currentRow++;
                _currentColumn = 0;
                closeRowTag(_appender);
                _appender.append("\n");
            }

            /* reached the end of the table as the current row is now equal to the total number of rows */
            if(_currentRow == _rows)
                return SKIP_BODY;

            if(_currentColumn == 0)
                openRowTag(_appender, _trState != null ? _trState : STATE_TR);

            int itemIndex = -1;
            if(_verticalRepeat)
                itemIndex = _currentColumn * _rows + _currentRow;
            else itemIndex = _currentRow * _columns + _currentColumn;

            haveNext = ensureItem(itemIndex, _dataList);

            openCellTag(_appender, computeStyleIndex(_currentRow,  _currentColumn)) ;

            /* render empty cell and continue filling the table */
            if(!haveNext)
                _htmlConstantRendering.NBSP(_appender);
            /* open a new table cell and render the body once again.  note, this exits the while loop above */
            else return EVAL_BODY_AGAIN;
        }

        /* default is to skip the tag body */
        return SKIP_BODY;
    }

    /**
     * Complete rendering the tag.  If no errors have occurred, the content that
     * the tag buffered is rendered.
     *
     * @return EVAL_PAGE to continue evaluating the page
     * @throws JspException if an error occurs that can not be reported on the page
     */
    public int doEndTag()
            throws JspException {
        if(hasErrors())
            reportErrors();
        else if(_valid) {
            closeTableTag(_appender);
            write(_sb.toString());
        }

        return EVAL_PAGE;
    }

    public void doFinally() {
        localRelease();
    }

    public void doCatch(Throwable t)
            throws Throwable {
        throw t;
    }

    /**
     * Gets the tag's data source (can be an expression).
     * @return the data source
     */
    public String getDataSource() {
        return "{" + _dataSource + "}";
    }
    
    /**
     * Get the index of the current iteration through the body of this tag.  This
     * data can be accessed using the expression <code>container.index</code>
     * on an attribute of a databindable NetUI tag that is contained within the
     * repeating body of this tag.  This expression is only valid when the dataset
     * is being rendered.
     *
     * @return the integer index of the current data item in the data set
     * @see org.apache.beehive.netui.script.common.IDataAccessProvider
     */
    public int getCurrentIndex() {
        return _currentIndex;
    }

    /**
     * Get the item that is currently being rendered by this repeating tag.
     * This can be accessed using the expression <code>expression.item</code>
     * on an attribute of a databindable netUI tag that is contained within
     * the repeating body of this tag.  The expression is only valid when the dataset
     * is being rendered.
     *
     * @return the current item in the data set
     * @see org.apache.beehive.netui.script.common.IDataAccessProvider
     */
    public Object getCurrentItem() {
        return _currentItem;
    }

    /**
     * Get the metadata for the current item.  This method is not supported by
     * this tag.
     *
     * @throws UnsupportedOperationException this tag does not support this method from the IDataAccessProvider interface
     * @see org.apache.beehive.netui.script.common.IDataAccessProvider
     */
    public Object getCurrentMetadata() {
        LocalizedUnsupportedOperationException uoe =
                new LocalizedUnsupportedOperationException("The " + getTagName() + "does not export metadata for its iterated items.");
        uoe.setLocalizedMessage(Bundle.getErrorString("Tags_DataAccessProvider_metadataUnsupported", new Object[]{getTagName()}));
        throw uoe;
    }

    /**
     * Get the parent IDataAccessProvider for this tag.  If this tag is contained within
     * a IDataAccessProvider, the containing IDataAccessProvider is available through the
     * expression <code>container.container</code>.  Any valid properties of the
     * parent IDataAccessProvider can be accessed through this expression.  This method
     * will return null if there is no parent IDataAccessProvider
     *
     * @return a containing IDataAccessProvider if one exists, null otherwise.
     * @see org.apache.beehive.netui.script.common.IDataAccessProvider
     */
    public IDataAccessProvider getProviderParent() {
        return (IDataAccessProvider)SimpleTagSupport.findAncestorWithClass(this, IDataAccessProvider.class);
    }

    /**
     * Return an <code>ArrayList</code> which represents a chain of <code>INameInterceptor</code>
     * objects.  This method by default returns <code>null</code> and should be overridden
     * by objects that support naming.
     * @return an <code>ArrayList</code> that will contain <code>INameInterceptor</code> objects.
     */
    protected List getNamingChain() {
        return AbstractClassicTag.DefaultNamingChain;
    }

    /**
     * Reset all of the fields of this tag.
     */
    protected void localRelease() {
        super.localRelease();

        if(bodyContent != null)
            bodyContent.clearBody();

        _rows = DIMENSION_DEFAULT_VALUE;
        _columns = DIMENSION_DEFAULT_VALUE;
        _currentRow = -1;
        _currentColumn = -1;
        _currentIndex = -1;
        _verticalRepeat = false;
        _dataList = null;
        _currentItem = null;
        _valid = true;
        _dataSource = null;

        if(_tdState != null)
            _tdState.clear();
        if(_trState != null)
            _trState.clear();
        if(_tableState != null)
            _tableState.clear();

        _tableRenderer = null;
        _tdRenderer = null;
        _trRenderer = null;

        _sb = null;
        _appender = null;

        if(_containerInPageContext) {
            DataAccessProviderStack.removeDataAccessProvider(pageContext);
            _containerInPageContext = false;
        }
    }

    private final void guessDimensions(ArrayList data)
        throws JspException {

        if(_rows == 0 || _columns == 0)
            registerTagError(Bundle.getString("Tags_CellRepeater_missingRowsOrColumns"), null);

        if(data == null)
            return;

        int dataSize = data.size();
        if(_rows == DIMENSION_DEFAULT_VALUE && _columns == DIMENSION_DEFAULT_VALUE) {
            registerTagError(Bundle.getString("Tags_CellRepeater_invalidRowOrColumn"), null);
        }
        else if(_rows == DIMENSION_DEFAULT_VALUE) {
            int remainder = dataSize % _columns;
            _rows = (dataSize / _columns) + (remainder > 0 ? 1 : 0);
            LOGGER.debug("guessed row size: " + _rows);
        }
        else if(_columns == DIMENSION_DEFAULT_VALUE) {
            int remainder = dataSize % _rows;
            _columns = (dataSize / _rows) + (remainder > 0 ? 1 : 0);
            LOGGER.debug("guessed column size: " + _columns);
        }
    }

    private void openTableTag(AbstractRenderAppender appender, TableTag.State tableState) {
        if(tableState == null)
            tableState = STATE_TABLE;
        _tableRenderer.doStartTag(appender, tableState);
    }

    private void closeTableTag(AbstractRenderAppender appender) {
        assert appender != null;
        assert _tableRenderer != null;
        _tableRenderer.doEndTag(appender);
    }

    private void openRowTag(AbstractRenderAppender appender, TrTag.State trState) {
        if(trState == null)
            trState = STATE_TR;
        _trRenderer.doStartTag(appender, trState);
    }

    private void closeRowTag(AbstractRenderAppender appender) {
        assert _trRenderer != null;
        assert appender != null;
        _trRenderer.doEndTag(appender);
    }

    private void openCellTag(AbstractRenderAppender appender, int index) {
        assert appender != null;
        assert index >= 0;
        assert _tdRenderer != null;

        TdTag.State tdState = STATE_TD;
        if(_cellClass != null) {
            if(_tdState != null)
                _tdState.clear();
            else _tdState = new TdTag.State();
            if(index % 2 == 0)
                _tdState.styleClass = _cellClass;
            else _tdState.styleClass = (_altCellClass != null ? _altCellClass : _cellClass);
            tdState = _tdState;
        }

        _tdRenderer.doStartTag(appender, tdState);
    }

    private void closeCellTag(AbstractRenderAppender appender) {
        assert _tdRenderer != null;
        assert appender != null;
        _tdRenderer.doEndTag(appender);
    }

    private int computeStyleIndex(int r, int c) {
        return c + (r % 2);
    }

    private boolean ensureItem(int index, ArrayList data) {
        LOGGER.debug("item: " + 0 + " data: " + (data == null ? "null data" :
                     (index < data.size() ? "" + index : "index out of bounds for size " + data.size())));

        if(data != null && index < data.size()) {
            _currentItem = data.get(index);
            _currentIndex = index;
            return true;
        }
        else return false;
    }

    /**
     * Return the Object that is represented by the specified data source.
     * @return Object
     * @throws JspException
     */
    private Object evaluateDataSource()
        throws JspException {
        ExpressionHandling expr = new ExpressionHandling(this);
        String dataSource = getDataSource();
        String ds = expr.ensureValidExpression(dataSource, "dataSource", "DataSourceError");
        if (ds == null)
            return null;

        Object o = expr.evaluateExpression(dataSource, "dataSource", pageContext);
        return o;
    }
}