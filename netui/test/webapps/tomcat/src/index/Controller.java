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
package index;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.tools.testrecorder.shared.config.Category;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinition;
import org.apache.beehive.netui.tools.testrecorder.shared.config.TestDefinitions;
import org.apache.beehive.netui.tools.testrecorder.server.TestRecorderFilter;

import java.io.File;
import java.util.Iterator;
import java.util.List;


@Jpf.Controller (
    simpleActions={
        @Jpf.SimpleAction(name="begin", path="index.jsp")
    },
    longLived = true
)
public class Controller extends PageFlowController
{
    private final String ROW_ON_STYLE = "row";
    private final String ROW_OFF_STYLE = "altRow";
    private final String TESTS = " Tests";
    transient private TestDefinitions _td;
    transient private Category[] _cats;
    private String _cat = "bvt";

    private boolean _rowOn = false;

    public Iterator getCategories()
    {
        CatIterator it = new CatIterator(_cats,_cat);
        return it;
    }

    public Iterator getTests()
    {
        TestIterator it = new TestIterator(_td.getCategories().getTests(_cat));
        return it;
    }

    public String getTitle() {
        Category c = _td.getCategories().getCategory(_cat);
        String d = c.getDescription();
        int pos = d.indexOf("Tests");
        return c.getDescription() + ((pos != -1) ? "" : TESTS);
    }

    public String getRowStyle() {
        _rowOn = !_rowOn;
        return (_rowOn) ? ROW_ON_STYLE : ROW_OFF_STYLE;
    }


    protected void onCreate()
    {
        TestDefinitions td = TestRecorderFilter.instance().getTestDefinitions();
        if (td == null) {
            System.err.println("Didn't find the test Definitions");
        }
        _td = td;
        _cats = _td.getCategories().getCategories();
    }

    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "success",
                path = "index.jsp")
        })
    public Forward selectList()
    {
        _rowOn = false;
        String cat = getRequest().getParameter("target");
        if (cat != null) {
            _cat = cat;
        }
        return new Forward( "success" );
    }

    public class CatIterator implements Iterator
    {
        private Category[] _cats;
        private int pos;
        private Category _cat;
        private String _selCat;

        public CatIterator(Category[] cats, String selCat) {
            _cats = cats;
            _selCat = selCat;
            pos = 0;
        }

        public String getName() {
            return _cat.getName();
        }

        public String getDescription() {
            return _cat.getDescription();
        }

        public String getSelection() {
            return (_cat.getName().equals(_selCat)) ? "uberlink" : null;
        }

        public boolean hasNext()
        {
            return (pos < _cats.length);
        }

        public Object next()
        {
            _cat = _cats[pos++];
            return this;
        }

        public void remove()
        {
            throw new RuntimeException("Iterator does not support remove.");
        }
    }

    public class TestIterator implements Iterator
    {
        private List _list;
        private int _cur;
        private TestDefinition _test;

        public TestIterator(List l)
        {
            _list = l;
            _cur = 0;
        }

        public String getName()
        {
            return _test.getName();
        }

        public boolean isDiff()
        {
            File file = new File(_test.getResultDiffFilePath());
            return file.exists();
        }

        public String getDescription()
        {
            return _test.getDescription();
        }

        //*********************************** ITERATOR ******************************************
        public boolean hasNext()
        {
            return (_cur != _list.size());
        }

        public Object next()
        {
            _test = (TestDefinition) _list.get(_cur++);
            return this;
        }

        public void remove()
        {
            throw new RuntimeException("Iterator does not support remove.");
        }
    }
}
