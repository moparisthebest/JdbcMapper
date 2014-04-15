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
package bugs.b41391;

import org.apache.beehive.netui.pageflow.Forward;
import org.apache.beehive.netui.pageflow.PageFlowController;
import org.apache.beehive.netui.pageflow.annotations.Jpf;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller for testing the interaction of the ignoreNulls attribute and the repeater's padding
 * feature.
 *
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/Controller.jpf"/>
 * <pageflow-object id="page:index.jsp">
 *   <property name="x" value="220"/>
 *   <property name="y" value="80"/>
 * </pageflow-object>
 * <pageflow-object id="page:error.jsp">
 *   <property name="x" value="220"/>
 *   <property name="y" value="160"/>
 * </pageflow-object>
 * <pageflow-object id="action:begin.do">
 *   <property name="x" value="60"/>
 *   <property name="y" value="80"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#index.jsp#@action:begin.do@">
 *   <property name="elbowsY" value="72,72,72,72"/>
 *   <property name="elbowsX" value="96,140,140,184"/>
 *   <property name="toPort" value="West_1"/>
 *   <property name="fromPort" value="East_1"/>
 *   <property name="label" value="index"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */
@Jpf.Controller(
    )
@Jpf.ViewProperties(
    value = {
        "<!-- This data is auto-generated. Hand-editing this section is not recommended. -->",
        "<view-properties>",
        "<pageflow-object id='pageflow:/Controller.jpf'/>",
        "<pageflow-object id='page:index.jsp'>",
        "  <property name='x' value='220'/>",
        "  <property name='y' value='80'/>",
        "</pageflow-object>",
        "<pageflow-object id='page:error.jsp'>",
        "  <property name='x' value='220'/>",
        "  <property name='y' value='160'/>",
        "</pageflow-object>",
        "<pageflow-object id='action:begin.do'>",
        "  <property name='x' value='60'/>",
        "  <property name='y' value='80'/>",
        "</pageflow-object>",
        "<pageflow-object id='forward:path#index#index.jsp#@action:begin.do@'>",
        "  <property name='elbowsY' value='72,72,72,72'/>",
        "  <property name='elbowsX' value='96,140,140,184'/>",
        "  <property name='toPort' value='West_1'/>",
        "  <property name='fromPort' value='East_1'/>",
        "  <property name='label' value='index'/>",
        "</pageflow-object>",
        "</view-properties>"
    })
public class Controller extends PageFlowController
{
    public String[] _nullArray = null;
    public String[] _emptyArray = null;
    public String[] _sparseArray_lastNull = null;
    public String[] _sparseArray_firstNull = null;
    public String[] _denseArray = null;
    public String[] _sparseArray_endsNull = null;
    public String[] _sparseArray_oddNull = null;
    public String[] _sparseArray_evenNull = null;
    public String[] _sparseArray_middleNonNull = null;
    
    public Map _testMap = null;

    public String[] getNullArray() {return _nullArray;}
    public String[] getEmptyArray() {return _emptyArray;}
    public String[] getSparseArray_lastNull() {return _sparseArray_lastNull;}
    public String[] getSparseArray_firstNull() {return _sparseArray_firstNull;}
    public String[] getDenseArray() {return _denseArray;}
    public String[] getSparseArray_endsNull() {return _sparseArray_endsNull;}
    public String[] getSparseArray_oddNull() {return _sparseArray_oddNull;}
    public String[] getSparseArray_evenNull() {return _sparseArray_evenNull;}
    public String[] getSparseArray_middleNonNull() {return _sparseArray_middleNonNull;}
    public Map getTestMap() {return _testMap;}

    protected void onCreate()
    {
        // all null
        _emptyArray = new String[5];

        // all non-null
        _denseArray = new String[5];
        
        // 0, 2, 4 non-null; 1, 3 null
        _sparseArray_firstNull = new String[5];

        // 1, 3 non-null; 0, 2, 4 null
        _sparseArray_lastNull = new String[5];

        // 0, 4 null; 1, 2, 3 non-null
        _sparseArray_endsNull = new String[5];

        // 0, 2, 4 non-null; 1, 3 null
        _sparseArray_oddNull = new String[5];

        // 1, 3 non-null; 0, 2, 4 null
        _sparseArray_evenNull = new String[5];

        // 2 non-null; 0, 1, 3, 4 null
        _sparseArray_middleNonNull = new String[5];
        
        for(int i = 0; i < 5; i++)
        {
            String val = "String: " + i;
            if(i == 0)
            {
                _sparseArray_lastNull[i] = val;
                _sparseArray_firstNull[i] = null;
            }
            else if(i == 4)
            {
                _sparseArray_firstNull[i] = val;
                _sparseArray_lastNull[i] = null;
            }
            else
            {
                _sparseArray_lastNull[i] = val;
                _sparseArray_firstNull[i] = val;
            }
            
            if(i == 2)
            {
                _sparseArray_middleNonNull[i] = val;
            }

            if(i % 2 == 0)
            {
                _sparseArray_oddNull[i] = val;
            }
            else
            {
                _sparseArray_evenNull[i] = val;
            }
            
            if(i > 0 && i < 4)
                _sparseArray_endsNull[i] = val;
            
            _denseArray[i] = val;
        }

        _testMap = new LinkedHashMap();
        _testMap.put("Null Array", _nullArray);
        _testMap.put("Empty Array", _emptyArray);
        _testMap.put("Dense Array", _denseArray);
        _testMap.put("SparseArray_firstNull", _sparseArray_firstNull);
        _testMap.put("SparseArray_lastNull", _sparseArray_lastNull);
        _testMap.put("SparseArray_endsNull", _sparseArray_endsNull);
        _testMap.put("SparseArray_oddNull", _sparseArray_oddNull);
        _testMap.put("SparseArray_evenNull", _sparseArray_evenNull);
        _testMap.put("SpraseArray_middleNonNull", _sparseArray_middleNonNull);
    }
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="index.jsp"
     */
    @Jpf.Action(
        forwards = {
            @Jpf.Forward(
                name = "index",
                path = "index.jsp") 
        })
    protected Forward begin()
    {
        return new Forward("index");
    }
    
}
