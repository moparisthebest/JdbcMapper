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
package org.apache.beehive.netui.databinding.datagrid.api.pager;

import org.apache.beehive.netui.util.Bundle;

/**
 * <p>
 * The PagerModel is a JavaBean that represents the page state of a data grid.  In the default implementation,
 * the page state consists of three pieces of data:
 * <ul>
 * <li>the current row</li>
 * <li>current page</li>
 * <li>the href / action used to page through data</li>
 * </ul>
 * This pager model implementation is row based which means that the notion of the current page
 * is based on which row is at the top of a page.  Row numbering starts at zero and continues to page size.
 * For example, in a data grid on its first page and with a page size of 10, the rows 0-9 will be displayed.  The
 * next page would contain rows 10-11 and so on.
 * </p>
 * <p>
 * The pager model provides JavaBean-style access to the properties of a pager.  In addition, it provides read-only
 * access to information about the row to use in order to navigate to a specific page.  To navigate to the previous
 * page, the {@link #getRowForPreviousPage()} will return the row number that will appear at the top of the previous
 * page.  In order to build effective paging UI, it is also often useful to know the absolute page number.
 * As with row numbers, page numbers are zero based.  For example, if a data set displayed in a data grid has 30
 * records and the grid is on page a page displaying rows 10-19, the current page is 1.  When displaying this value
 * in UI, it is often useful to display it as:
 * <pre>
 *     Page 2 of 3
 * </pre>
 * Random page access can also be accomplished using the {@link #encodeRowForPage(int)} method which will return
 * the row number to display when jumping to a specific page in a grid.
 * </p>
 */
public class PagerModel
    implements java.io.Serializable {

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_ROW = 0;

    private String _pageHref = null;
    private String _pageAction = null;
    private Integer _currentRow = null;
    private Integer _dataSetSize = null;
    private Integer _explicitPageSize = null;
    private Integer _defaultPageSize = null;

    /**
     * Default constructor.  This initializes the current row to the default row value {@link #DEFAULT_ROW}.
     */
    public PagerModel() {
        _currentRow = new Integer(DEFAULT_ROW);
    }

    /**
     * Get the action used when building URLs for navigating to another page.
     * @return the action name or <code>null</code> if no action name is set
     */
    public String getPageAction() {
        return _pageAction;
    }

    /**
     * Set the action used to navigate to another page.
     * @param pageAction the action name
     */
    public void setPageAction(String pageAction) {
        _pageAction = pageAction;
    }

    /**
     * Get the href used when building URLs for navigating to another page.
     * @return the href or <code>null</code> if no href is set
     */
    public String getPageHref() {
        return _pageHref;
    }

    /**
     * Set the href used to navigate to another page.
     * @param pageHref the href
     */
    public void setPageHref(String pageHref) {
        _pageHref = pageHref;
    }

    /**
     * Get the default page size.  If no page size has been set via {@link #setDefaultPageSize(int)} this
     * value is {@link #DEFAULT_PAGE_SIZE}
     * @return the default page size
     */
    public int getDefaultPageSize() {
        if(_defaultPageSize != null)
            return _defaultPageSize.intValue();
        else return DEFAULT_PAGE_SIZE;
    }

    /**
     * Set the default page size.  The default page size is used when no other page size has been set and is useful
     * when clients wish to occasionally override the page size but wish to have the default page size set
     * differently than the PagerModel's default.
     *
     * @param pageSize the new page size
     * @throws IllegalArgumentException if the page size is less than 1
     */
    public void setDefaultPageSize(int pageSize) {
        if(pageSize < 1)
            throw new IllegalArgumentException(Bundle.getErrorString("PagerModel_IllegalDefaultPageSize"));

        _defaultPageSize = new Integer(pageSize);
    }

    /**
     * Set the data set size.  In order to calculate paging state for the last page such as the
     * state returned for {@link #getRowForLastPage()} the default PagerModel implementation must
     * know the total size of the data set.
     *
     * @return the size
     */
    public int getDataSetSize() {
        if(_dataSetSize == null)
            return 0;
        else return _dataSetSize.intValue();
    }

    /**
     * Set the data set size.
     * @param dataSetSize the size
     */
    public void setDataSetSize(int dataSetSize) {
        _dataSetSize = new Integer(dataSetSize);
    }

    /**
     * Get the current page size.
     * @return the page size
     */
    public int getPageSize() {
        return _explicitPageSize != null ? _explicitPageSize.intValue() : getDefaultPageSize();
    }

    /**
     * Sets the page size and overrides the default page size if one has been set.
     * @param pageSize the specific page size
     */
    public void setPageSize(int pageSize) {
        if(pageSize < 1)
            throw new IllegalArgumentException(Bundle.getErrorString("PagerModel_IllegalPageSize"));

        _explicitPageSize = new Integer(pageSize);
    }

    /**
     * Get the page number given the current page size and current row.  The page number is zero based and should be
     * adjusted by one when being displayed to users.
     * @return the page number
     */
    public int getPage() {
        int row = getRow();
        assert row % getPageSize() == 0 : "Invalid current row \"" + row + "\" for page size \"" + getPageSize() + "\"";
        assert getPageSize() > 0;
        return row / getPageSize();
    }

    /**
     * Set a specific page.  This will change the current row to match the given page value.
     *
     * @param page the new page
     * @throws IllegalArgumentException if the given page is less than zero
     */
    public void setPage(int page) {
        if(page < 0)
            throw new IllegalArgumentException(Bundle.getErrorString("PagerModel_IllegalPage"));

        /* todo: need to check that the new 'current' page is in range given the first/last boundaries */
        _currentRow = new Integer(page * getPageSize());
    }

    /**
     * Get the current row.  If no row has been specified, the default row is returned.
     * @return the current row
     */
    public int getRow() {
        if(_currentRow != null) {
            int row = _currentRow.intValue();

            /* if the row is out of range, simply adjust to the last row */
            if(_dataSetSize != null && (row > _dataSetSize.intValue()))
                row = _dataSetSize.intValue();

            if(row % getPageSize() != 0) {
                int adjustedPage = row - (row % getPageSize());
                return adjustedPage;
            }
            else return row;
        }
        else return DEFAULT_ROW;
    }

    /**
     * Set the current row.
     * @param row the new row
     * @throws IllegalArgumentException if the given row is less than zero
     */
    public void setRow(int row) {
        if(row < 0)
            throw new IllegalArgumentException(Bundle.getErrorString("PagerModel_IllegalRow"));

        _currentRow = new Integer(row);
    }

    /**
     * <p>
     * Get the last row for the current page of data.  This value is useful when displaying paging UI like:
     * <pre>
     *   Row 11 through 20 of 60
     * </pre>
     * The last row on the page is returned as a zero-based number from the beginning of the data set.  In the
     * case above, the value returned is <code>19</code> and is converted to <code>20</code> for readability
     * by adding one.  If the current page is only partially filled, this method will return the value for a partial page.
     * For example, with a data set of size 4 on a page of size 10, the value <code>3</code> would be returned.
     * </p>
     * @return the last row for the current page
     */
    public int getLastRowForPage() {
        int row = getRow();
        if(_dataSetSize != null) {
            if(_dataSetSize.intValue() - row < getPageSize())
                return row + (_dataSetSize.intValue() - row) - 1;
            else return row + getPageSize() - 1;
        }
        else return row + getPageSize() - 1;
    }

    /**
     * Get the row used to display the first page.
     * @return the row for the first page
     */
    public int getRowForFirstPage() {
        return DEFAULT_ROW;
    }

    /**
     * Get the row used to display the previous page.  Note, a return value of less than zero means that the previous
     * page does not exist as it would scroll off the beginning of the data set and is invalid.
     * @return the row for the previous page
     */
    public int getRowForPreviousPage() {
        int value = getRow() - getPageSize();
        return value > -1 ? value : -1;
    }

    /**
     * Get the row used to display the next page.  Note, if this value is greater than the size of the data set
     * it would scroll off the end of the data set and is invalid.
     * @return the row for the previous page
     */
    public int getRowForNextPage() {
        return getRow() + getPageSize();
    }

    /**
     * Get the row used to display the last page.  This requires tha the data set size has been set via
     * @return the row for the last page
     * @throws IllegalStateException when the size of the data set has not been set
     */
    public int getRowForLastPage() {
        Integer lastRow = internalGetLastRow();
        if(lastRow != null)
            return lastRow.intValue();
        else throw new IllegalStateException(Bundle.getErrorString("PagerModel_CantCalculateLastPage"));
    }

    /**
     * Get the row needed to jump to the given <code>page</code>
     * @param page the new page
     * @return the row used to jump to the new page
     * @throws IllegalArgumentException if the given page value is less than zero
     */
    public int encodeRowForPage(int page) {
        if(page < 0)
            throw new IllegalArgumentException(Bundle.getErrorString("PagerModel_IllegalPage"));

        return page * getPageSize();
    }

    /**
     * Get the total number of pages.  This value is useful when displaying the total number of pages in UI like:
     * <pre>
     *   Page 4 of 10
     * </pre>
     * This method returns an absolute count of the number of pages which could be displayed given the
     * size of the data set and the current page size.  This method requires the PagerModel know the
     * total size of the data set.
     * @return the number of pages
     * @throws  IllegalStateException when the size of the data set has not been set
     */
    public int getPageCount() {
        if(_dataSetSize != null)
            return (int)Math.ceil(_dataSetSize.doubleValue()/(double)getPageSize());
        else throw new IllegalStateException(Bundle.getErrorString("PagerModel_CantCalculateLastPage"));
    }

    /**
     * Get the page number of the first page.
     * @return the first page
     */
    public int getFirstPage() {
        return 0;
    }

    /**
     * Get the page number of the previous page.
     * @return the previous page
     */
    public int getPreviousPage() {
        int previousPageRow = getRowForPreviousPage();
        return previousPageRow == -1 ? previousPageRow : (int)(previousPageRow / getPageSize());
    }

    /**
     * Get the page number for the next page.
     * @return the next page
     */
    public int getNextPage() {
        return (int)(getRowForNextPage() / getPageSize());
    }

    /**
     * Get the page number for the last page.
     * @return the last page
     */
    public int getLastPage() {
        return (int)(Math.floor(getRowForLastPage() / getPageSize()));
    }

    /**
     * Internal method used to calculate the last row given a data set size.
     * @return the last row or <code>null</code> if the last row can not be calculated
     */
    private Integer internalGetLastRow() {
        if(_dataSetSize != null) {
            /*
              29 / 10: 0-9, 10-19, 20-29          _lastRow = 20
              30 / 10: 0-9, 10-19, 20-29, 30-39   _lastRow = 30
              31 / 10: 0-9, 10-19, 20-29, 30-39   _lastRow = 30
              32 / 10: 0-9, 10-19, 20-29, 30-39   _lastRow = 30

              29 - (29%10) = 20
              30 - (30%10) = 30
              30 - (30%10) = 30
              36 - (36%10) = 30

              12 / 2: 0-1, 2-3, 4-5, 6-7, 8-9, 10-11, 12-13 _lastRow=10
              12 / 5: 0-4, 5-9, 10-14 _lastRow=5
             */
            int lastRow = getPageSize() * (int)Math.floor((double)(_dataSetSize.intValue()-1) / (double)getPageSize());
            return new Integer(lastRow);
        }
        else return null;
    }
}