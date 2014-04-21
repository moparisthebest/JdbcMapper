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
package org.apache.beehive.netui.databinding.datagrid.runtime.util;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.beehive.netui.util.logging.Logger;

/**
 *
 */
public final class PagedDataSet
    implements Iterator {

    private static final Logger LOGGER = Logger.getInstance(PagedDataSet.class);

    private boolean _completedBootstrap = false;
    private int _dataSetSize = 0;
    private int _currentIndex = -1;
    private int _startWindow = -1;
    private int _endWindow = -1;

    private String _dataSource = null;
    private ArrayList _list = null;
    private Iterator _dataSetIterator = null;
    private Object _currentItem = null;

    public PagedDataSet(String dataSource, Iterator iterator) {
        _dataSource = dataSource;

        if(iterator == null)
            _dataSetIterator = Collections.EMPTY_LIST.iterator();
        else
            _dataSetIterator = iterator;

        LOGGER.debug("iterator type: " + _dataSetIterator.getClass().getName());

        /* todo: would be nice to have a limit here so that this isn't needed in order to find the end of the data set */
        _list = new ArrayList();
        while(_dataSetIterator.hasNext()) {
            _list.add(_dataSetIterator.next());
            _dataSetSize++;
        }
        _dataSetIterator = _list.iterator();
    }

    public void createWindow(int startWindow, int windowSize) {
        /* todo: error checking */
        _startWindow = startWindow;
        /* the size of the window is inclusive, so remove one from the total size */
        _endWindow = _startWindow + windowSize - 1;
    }

    public boolean hasNext() {
        if(!_completedBootstrap && _startWindow > 0) {

            while(_dataSetIterator.hasNext() && (_currentIndex+1) != _startWindow) {
                _dataSetIterator.next();
                _currentIndex++;
            }
            _completedBootstrap = true;
        }
        else if(_endWindow > -1 && (_currentIndex >= _endWindow)) {
            LOGGER.debug("current index: " + _currentIndex + " _endRenderWindiw: " + _endWindow + " end data set: " + (_currentIndex >= _endWindow));
            return false;
        }

        boolean hasNext = _dataSetIterator.hasNext();
        if(!hasNext) {
            _currentIndex = -1;
            _currentItem = null;
        }

        return hasNext;
    }

    public Object next() {
        _currentItem = _dataSetIterator.next();
        _currentIndex++;
        return _currentItem;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public String getDataSource() {
        return _dataSource;
    }

    public int getSize() {
        return _dataSetSize;
    }

    public int getCurrentIndex() {
        return _currentIndex;
    }

    public Object getCurrentItem() {
        return _currentItem;
    }
}
