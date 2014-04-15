/*
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
package ui.datagrid.sortandfilter;

import java.util.List;
import java.util.LinkedList;

import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.annotations.Jpf;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfig;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridStateFactory;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridState;
import org.apache.beehive.netui.databinding.datagrid.api.DataGridConfigFactory;
import org.apache.beehive.netui.databinding.datagrid.api.sort.Sort;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterOperationHint;
import org.apache.beehive.netui.databinding.datagrid.api.filter.Filter;
import org.apache.beehive.netui.databinding.datagrid.api.filter.FilterTypeHint;
import org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.CustomerBean;
import org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.util.SortByProperty;
import org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.util.FilterByProperty;
import org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.FilterPredicateCustomerID;
import org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.FilterPredicateCompanyName;
import org.apache.beehive.samples.netui.ui.datagrid.sortandfilter.SampleDataUtil;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * <p>
 * This page flow controller demonstrates using the NetUI data grid's sort and filter functionality.
 * The sort and filter UI is contained in the <i>index.jsp</i> and submits data into the page flow
 * scoped form object {@link #_customerFilterForm}.  This information processed from the
 * {@link javax.servlet.http.HttpServletRequest} object into a set of sort and filter objects that
 * represent each of the sort and filter criteria to apply to a data set.
 * </p>
 * <p>
 * The data set {@link #_customers} contains the complete set of items in the data set.  When
 * sorting and filtering the data set, a subset of those items will be presented to the UI by
 * creating a {@link List} that contains only the items that passed any filter criteria.  This
 * data set will also be ordered based on the sort criteria.  This rendered list simply contains
 * pointers into the original {@link #_customers} list.
 * </p>
 * <p>
 * In order to maintain a data grid's sort, filter, and page state when navigating to another page,
 * a {@link #_dataGridState} object holds this information while navigating away to other pages.  This
 * state can be reattached so that the data grid re-renders with its original appearance when
 * the data grid is re-displayed.
 * </p>
 */
@Jpf.Controller(
    forwards={
    @Jpf.Forward(name="grid-action", path="grid.do"),
    @Jpf.Forward(name="grid-page", path="index.jsp")
        }
    )
public class Controller
    extends PageFlowController {

    private static final Log LOG = LogFactory.getLog(Controller.class);
    private static final String GRID_NAME_CUSTOMERS = "customers";
    private static final String FILTER_EXPRESSION_CUSTOMERID = "customerid";
    private static final String FILTER_EXPRESSION_COMPANYNAME = "companyname";

    private transient DataGridConfig _dataGridConfig = null;
    private List<CustomerBean> _customers = null;
    private CustomerFilterForm _customerFilterForm = null;
    private DataGridState _dataGridState = null;

    @Jpf.Action
    public Forward begin() {
        saveDataGridState();
        return new Forward("grid-action");
    }

    @Jpf.Action
    public Forward grid() {
        return decorateGridForward(new Forward("grid-page"), lookupCustomers());
    }

    @Jpf.Action
    public Forward sort() {
        saveDataGridState();
        return decorateGridForward(new Forward("grid-page"), lookupCustomers());
    }

    @Jpf.Action(useFormBean="_customerFilterForm")
    public Forward filter(CustomerFilterForm customerFilterForm) {
        saveDataGridState();
        return decorateGridForward(new Forward("grid-page"), lookupCustomers());
    }

    private Forward decorateGridForward(Forward forward, List<CustomerBean> customers) {
        assert forward != null;
        forward.addActionOutput("customers", customers);
        return forward;
    }

    private List<CustomerBean> lookupCustomers() {
        DataGridState dataGridState = DataGridStateFactory.getInstance(getRequest()).getDataGridState(GRID_NAME_CUSTOMERS);

        List<CustomerBean> dataSet = _customers;

        /* implement sorting */
        final List<Sort> sorts = (List<Sort>)dataGridState.getSortModel().getSorts();
        if(sorts != null && sorts.size() < 2) {

            Sort sort = (Sort)sorts.get(0);
            SortByProperty sorter = new SortByProperty();
            dataSet = sorter.sort(sort, _customers);
        }

        /* implement filtering */
        if(_customerFilterForm != null) {
            final List<Filter> filters = _customerFilterForm.getFilters();
            if(filters != null && filters.size() > 0) {
                FilterByProperty filterer = new FilterByProperty();
                for(Filter filter : filters) {
                    if(filter.getFilterExpression().equals(FILTER_EXPRESSION_CUSTOMERID))
                        filterer.addPredicate(new FilterPredicateCustomerID(filter.getOperationHint(),
                                                                            Integer.parseInt(filter.getValue().toString())));
                    if(filter.getFilterExpression().equals(FILTER_EXPRESSION_COMPANYNAME))
                        filterer.addPredicate(new FilterPredicateCompanyName(filter.getOperationHint(),
                                                                             filter.getValue()));
                }
                dataSet = filterer.filter(dataSet);
            }
        }

        return dataSet;
    }

    private void saveDataGridState() {
        _dataGridState = DataGridStateFactory.getInstance(getRequest()).getDataGridState(GRID_NAME_CUSTOMERS);
    }

    @Override
    protected void onCreate() {
        LOG.debug("page flow created");

        /* lookup sample data set */
        _customers = SampleDataUtil.createSampleData();

        /* create the DataGridConfig object used to configure the data grid rendered from this page flow */
        _dataGridConfig = DataGridConfigFactory.getInstance();

        /* initialize the CustomerFilterForm page flow scoped form bean */
        _customerFilterForm = new CustomerFilterForm();
        _customerFilterForm.setDataGridConfig(_dataGridConfig);
    }

    /**
     * Simple form bean that takes a current "customerid" and "companyname" filters.  This form
     * implements a basic equals filter on the customer id and a "contains" filter on the companyname.
     * The {@link #getFilters()} method is used to get the {@link Filter} objects that are represented
     * by this form.
     */
    public static class CustomerFilterForm
        implements java.io.Serializable {

        private String _customerId = null;
        private String _companyName = null;
        private DataGridConfig _dataGridConfig = null;

        public void setDataGridConfig(DataGridConfig dataGridConfig) {
            _dataGridConfig = dataGridConfig;
        }

        public String getCustomerId() {
            return _customerId;
        }

        public void setCustomerId(String customerId) {
            _customerId = customerId;
        }

        public String getCompanyName() {
            return _companyName;
        }

        public void setCompanyName(String companyName) {
            _companyName = companyName;
        }

        public List<Filter> getFilters() {
            LinkedList<Filter> filters = new LinkedList<Filter>();

            Filter filter = null;
            if(_customerId != null && !_customerId.equals("")) {
                filter = buildFilter(FILTER_EXPRESSION_CUSTOMERID,
                                     _customerId,
                                     FilterTypeHint.NUMERIC,
                                     FilterOperationHint.EQUAL);
                filters.add(filter);
            }

            if(_companyName != null && !_companyName.equals("")) {
                filter = buildFilter(FILTER_EXPRESSION_COMPANYNAME,
                                     _companyName,
                                     FilterTypeHint.STRING,
                                     FilterOperationHint.CONTAINS);
                filters.add(filter);
            }

            return filters;
        }

        private Filter buildFilter(String filterExpression,
                                   Object value,
                                   FilterTypeHint typeHint,
                                   FilterOperationHint operationHint) {

            Filter filter = _dataGridConfig.createFilter();
            filter.setFilterExpression(filterExpression);
            filter.setTypeHint(typeHint);
            filter.setValue(value);
            filter.setOperationHint(operationHint);
            return filter;
        }
    }
}
