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
package org.apache.beehive.netui.test.databinding.expression;

import javax.servlet.jsp.tagext.Tag;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.script.ExpressionEvaluator;
import org.apache.beehive.netui.script.ExpressionEvaluatorFactory;
import org.apache.beehive.netui.script.common.IDataAccessProvider;
import org.apache.beehive.netui.tags.naming.IndexedNameInterceptor;
import org.apache.beehive.netui.util.logging.Logger;
import org.apache.beehive.netui.test.util.config.TestConfigUtil;

/**
 * This TestCase tests the ability of the IndexedNameInterceptor to correctly
 * rewrite and qualify "container.item" bound expressions into the correct
 * binding contexts.  The test cases below simulate JSP tag hierarchies that
 * could appear in JSP pages when data binding HTML <input> elements
 * to nested repeaters or grids.
 */
public class IndexedNameTest
    extends TestCase {

    private static final Logger _logger = Logger.getInstance(IndexedNameTest.class);

    private IndexedNameTestCase[] _cases = new IndexedNameTestCase[]
    {
        new IndexedNameTestCase("{actionForm.beans[42]}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans}", 42);
                return new Leaf(qualifier, root, "{container.item}");
            }
        }),
        new IndexedNameTestCase("{actionForm.beans[42].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans}", 42);
                return new Leaf(qualifier, root, "{container.item.foo}");
            }
        }),
        new IndexedNameTestCase("{actionForm.beans[42].beans2[43].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans}", 42);
                SimpleDataAccessProvider branch = new SimpleDataAccessProvider(qualifier, root, "{container.item.beans2}", 43);
                return new Leaf(qualifier, branch, "{container.item.foo}");
            }
        }),
        new IndexedNameTestCase("{pageFlow.beans2[44].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{container.item}", 43);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{pageFlow.beans2}", 44);
                return new Leaf(qualifier, branch2, "{container.item.foo}");
            }
        }),
        new IndexedNameTestCase("{pageFlow.beans3[45].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{container.item}", 43);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item}", 44);
                SimpleDataAccessProvider branch3 = new SimpleDataAccessProvider(qualifier, branch2, "{pageFlow.beans3}", 45);
                return new Leaf(qualifier, branch3, "{container.item.foo}");
            }
        }),
        new IndexedNameTestCase("{pageFlow.beans[43].values2[44].values3[45].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{pageFlow.beans}", 43);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item.values2}", 44);
                SimpleDataAccessProvider branch3 = new SimpleDataAccessProvider(qualifier, branch2, "{container.item.values3}", 45);
                return new Leaf(qualifier, branch3, "{container.item.foo}");
            }
        }),
        new IndexedNameTestCase("{actionForm.beans[42].values1[43].values2[44].values3[45].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{container.item.values1}", 43);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item.values2}", 44);
                SimpleDataAccessProvider branch3 = new SimpleDataAccessProvider(qualifier, branch2, "{container.item.values3}", 45);
                return new Leaf(qualifier, branch3, "{container.item.foo}");
            }
        }),
        new IndexedNameTestCase("{actionForm.beans[42].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans}", 42);
                SimpleDataAccessProvider branch = new SimpleDataAccessProvider(qualifier, root, "{container.item.beans2}", 43);
                return new Leaf(qualifier, branch, "{container.container.item.foo}");
            }
        }),

        new IndexedNameTestCase("{actionForm.beans1[42].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans1}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{container.item.beans2}", 43);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item.beans3}", 43);
                return new Leaf(qualifier, branch2, "{container.container.container.item.foo}");
            }
        }),
        new IndexedNameTestCase("{pageFlow.beans1[42].foo}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans1}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{pageFlow.beans1}", 42);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item.beans2}", 43);
                SimpleDataAccessProvider branch3 = new SimpleDataAccessProvider(qualifier, branch2, "{container.item.beans3}", 43);
                return new Leaf(qualifier, branch3, "{container.container.container.item.foo}");
            }
        }),
        new IndexedNameTestCase("{pageFlow.beans1[42].foo.container.bar}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans1}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{pageFlow.beans1}", 42);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item.beans2}", 43);
                SimpleDataAccessProvider branch3 = new SimpleDataAccessProvider(qualifier, branch2, "{container.item.beans3}", 43);
                return new Leaf(qualifier, branch3, "{container.container.container.item.foo.container.bar}");
            }
        }),
        new IndexedNameTestCase("{pageFlow.beans1[42].foo.container.item}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans1}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{pageFlow.beans1}", 42);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item.beans2}", 43);
                SimpleDataAccessProvider branch3 = new SimpleDataAccessProvider(qualifier, branch2, "{container.item.beans3}", 43);
                return new Leaf(qualifier, branch3, "{container.container.container.item.foo.container.item}");
            }
        }),
        new IndexedNameTestCase("{pageFlow.item.foo.container.item}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans1}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{pageFlow.beans1}", 42);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item.beans2}", 43);
                SimpleDataAccessProvider branch3 = new SimpleDataAccessProvider(qualifier, branch2, "{container.item.beans3}", 43);
                return new Leaf(qualifier, branch3, "{pageFlow.item.foo.container.item}");
            }
        }),
        new IndexedNameTestCase("{pageFlow.container.item.foo.container.item}", new ILeafFactory() {
            public Leaf createLeaf(IExpressionQualifier qualifier) {
                SimpleDataAccessProvider root = new SimpleDataAccessProvider(qualifier, null, "{actionForm.beans1}", 42);
                SimpleDataAccessProvider branch1 = new SimpleDataAccessProvider(qualifier, root, "{pageFlow.beans1}", 42);
                SimpleDataAccessProvider branch2 = new SimpleDataAccessProvider(qualifier, branch1, "{container.item.beans2}", 43);
                SimpleDataAccessProvider branch3 = new SimpleDataAccessProvider(qualifier, branch2, "{container.item.beans3}", 43);
                return new Leaf(qualifier, branch3, "{pageFlow.container.item.foo.container.item}");
            }
        })
    };

    public void testRewrite()
        throws Throwable {
        runSuite(new NetUIELExpressionLanguage());
    }

    protected final void runSuite(ExpressionEngine qualifier)
        throws Throwable {
        for(int i = 0; i < _cases.length; i++) {
            try {
                IndexedNameTestCase test = _cases[i];
                test.setExpressionQualifier(qualifier);
                String exp = test.getExpected();
                ILeafFactory factory = test.getLeafFactory();
                Leaf leaf = factory.createLeaf(qualifier);

                TestNameInterceptor interceptor = new TestNameInterceptor(leaf, qualifier.getEngineName());

                String act = interceptor.rewriteName(leaf.getName(), null);

                System.out.println("**** act: " + act);
                System.out.println("**** exp: " + exp);

                if(!exp.equals(act))
                    throw new IllegalStateException("The expected (" + exp + ") and actual (" + act + "} values do not match for test case [" + i + "]");
            }
                // this generally poor practice, but because multiple test cases are run for this method,
                // we need to catch AssertionError in order to know which test case failed.
            catch(Throwable t) {
                t.printStackTrace();
                throw t;
            }
        }
    }

    /**
     * Simple interface used to qualify an given expression into a
     * a particular language.  The expectation is that 'expr' is of the
     * form {...}
     */
    public interface IExpressionQualifier {

        public String qualify(String expr);
    }

    /**
     * Type that represents an expression language and expression
     * syntax.  The engine name is used to get an expression evaluator
     * for a particular engine type.  This type also implements
     * the ExpressionQualifier interface, which qualifies an
     * expression string into the syntax expected by the
     * engine.
     * <p/>
     * Note, this makes the assumption that all expressions are
     * of the same syntax between the ${...} or {...} tokens.
     */
    public static abstract class ExpressionEngine
        implements IExpressionQualifier {

        private String _engine = null;

        public ExpressionEngine(String engine) {
            _engine = engine;
        }

        public String getEngineName() {
            return _engine;
        }
    }

    /**
     * 
     */
    public static class NetUIELExpressionLanguage
        extends ExpressionEngine {

        public NetUIELExpressionLanguage() {
            super("netuiel");
        }

        public String qualify(String expr) {
            return expr;
        }
    }

    /**
     * Type that represents a test case for indexed name rewriting.
     * This class contains an expected outcome String and a LeafFactory
     * which is used to create a hierarchy of IDataAccessProviders.
     * The IDataAccessProvider hierarchy mimics that which the JSP
     * tags create when there are nested repeaters / grids in a JSP page.
     */
    public static class IndexedNameTestCase
        extends ExpressionQualifiable {

        private String _expected = null;
        private ILeafFactory _factory = null;

        public IndexedNameTestCase(String expected, ILeafFactory factory) {
            _expected = expected;
            _factory = factory;
        }

        public String getExpected() {
            return qualify(_expected);
        }

        public ILeafFactory getLeafFactory() {
            return _factory;
        }
    }

    /**
     * An extension to the {@link IndexedNameInterceptor} that
     * overrides the getCurrentProvider and getExpressionEvaluator
     * methods.  This decopules the IndexedNameInterceptor from the
     * JSP tags and from the NetUI config file, which dictates the
     * expression language in use for a page.
     */
    public static class TestNameInterceptor
        extends IndexedNameInterceptor {

        private Leaf _leaf = null;
        private String _engine = null;

        public TestNameInterceptor(Leaf leaf, String engine) {
            _leaf = leaf;
            _engine = engine;
        }

        protected IDataAccessProvider getCurrentProvider(Tag currentTag) {
            return _leaf.getProvider();
        }

        protected ExpressionEvaluator getExpressionEvaluator() {
            if(_logger.isDebugEnabled()) _logger.debug("get expression evaluator named \"" + _engine + "\"");

            return ExpressionEvaluatorFactory.getInstance(_engine);
        }
    }

    /**
     * Base type for a class that needs to be able
     * to qualify an expression using an IExpressionQualifier.
     */
    public abstract static class ExpressionQualifiable {

        private IExpressionQualifier _qualifier = null;

        public ExpressionQualifiable() {
        }

        public ExpressionQualifiable(IExpressionQualifier qualifier) {
            _qualifier = qualifier;
        }

        public void setExpressionQualifier(IExpressionQualifier qualifier) {
            _qualifier = qualifier;
        }

        protected String qualify(String expression) {
            return _qualifier.qualify(expression);
        }
    }

    /**
     * Type that represents a leaf in a IDataAccessProvider hierarchy.
     * In JSP tag parlance, this class is equivalent to the <netui:label> in this example:
     * <p/>
     * <netui-data:repeater dataSource="{actionForm.customers}">
     * <netui:label value="{container.item.name}"/>
     * </netui-data:repeater>
     * <p/>
     * It is a root that may be parented by a IDataAccessProvider hierarchy.
     */
    public static class Leaf
        extends ExpressionQualifiable {

        private IDataAccessProvider _provider = null;
        private String _dataSource = null;

        public Leaf(IExpressionQualifier qualifier, IDataAccessProvider provider, String dataSource) {
            super(qualifier);

            _provider = provider;
            _dataSource = dataSource;
        }

        public void setExpressionQualifier(IExpressionQualifier qualifier) {
            super.setExpressionQualifier(qualifier);
            ((SimpleDataAccessProvider)_provider).setExpressionQualifier(qualifier);
        }

        public String getName() {
            return qualify(_dataSource);
        }

        public IDataAccessProvider getProvider() {
            return _provider;
        }
    }

    /**
     * Non-tag implementation of a data access provider that mimics a
     * hierarchy of IDataAccessProviders as would exist in a hierarchy
     * of repeaters.
     */
    public static class SimpleDataAccessProvider
        extends ExpressionQualifiable
        implements IDataAccessProvider {

        private IDataAccessProvider _parent = null;
        private String _dataSource = null;
        private int _currentIndex = 42;

        public SimpleDataAccessProvider(IExpressionQualifier qualifier, IDataAccessProvider parent, String dataSource, int currentIndex) {
            super(qualifier);

            _parent = parent;
            _dataSource = dataSource;
            _currentIndex = currentIndex;
        }

        public int getCurrentIndex() {
            return _currentIndex;
        }

        public Object getCurrentItem() {
            return new String("some current item");
        }

        public String getDataSource() {
            return qualify(_dataSource);
        }

        public Object getCurrentMetadata() {
            return null;
        }

        public IDataAccessProvider getProviderParent() {
            return _parent;
        }

        /**
         * This method indicates that the expression of the repeater is bound into a
         * client based context.
         *
         * @return <code>true</code> if the expression of this is bound on the client.
         */
        public boolean isBindingOnClient() {
            return false;
        }

        public void setDataSource(String dataSource) {
        }
    }

    /**
     * Simple interface that is implemented with anonymous inner
     * classes in a test case.
     */
    public static interface ILeafFactory {
        public Leaf createLeaf(IExpressionQualifier qualifier);
    }

    public IndexedNameTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(IndexedNameTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        TestConfigUtil.testInit();
    }

    protected void tearDown() {
    }
}
