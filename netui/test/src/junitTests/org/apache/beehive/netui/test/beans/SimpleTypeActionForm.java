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
package org.apache.beehive.netui.test.beans;

import org.apache.struts.action.ActionForm;

/**
 * This is a Struts ActionForm that provides properties of primitive types and
 * arrays of those primitive types.  The bean is used to test databinding scenarios
 * that need to work across many different types.
 */
public class SimpleTypeActionForm
    extends ActionForm {

    private String stringProperty = "default string";
    private String _StringProperty = "underbar CAP default string";
    private String _stringProperty = "underbar default string";

    private String[] _stringArrayProperty = null;

    private String _nullProperty = null;

    private boolean _boolProperty = false;
    private byte _byteProperty = 0;;
    private char _charProperty = 'a';
    private double _doubleProperty = 0.0;
    private float _floatProperty = 0.0f;
    private int _intProperty = 0;
    private long _longProperty = 0;
    private short _shortProperty = 0;

    private Boolean _boolWrapperProperty = null;
    private Byte _byteWrapperProperty = null;
    private Character _charWrapperProperty = null;
    private Double _doubleWrapperProperty = null;
    private Float _floatWrapperProperty = null;
    private Integer _intWrapperProperty = null;
    private Long _longWrapperProperty = null;
    private Short _shortWrapperProperty = null;

    private boolean[] _boolArrayProperty = null;
    private byte[] _byteArrayProperty = null;
    private char[] _charArrayProperty = null;
    private double[] _doubleArrayProperty = null;
    private float[] _floatArrayProperty = null;
    private int[] _intArrayProperty = null;
    private long[] _longArrayProperty = null;
    private short[] _shortArrayProperty = null;

    private Boolean[] _boolWrapperArrayProperty = null;
    private Byte[] _byteWrapperArrayProperty = null;
    private Character[] _charWrapperArrayProperty = null;
    private Double[] _doubleWrapperArrayProperty = null;
    private Float[] _floatWrapperArrayProperty = null;
    private Integer[] _intWrapperArrayProperty = null;
    private Long[] _longWrapperArrayProperty = null;
    private Short[] _shortWrapperArrayProperty = null;

    public void set_StringProperty(String stringProperty) {
        _StringProperty = stringProperty;
    }

    public String get_StringProperty() {
        return _StringProperty;
    }

    public void set_stringProperty(String stringProperty) {
        _stringProperty = stringProperty;
    }

    public String get_stringProperty() {
        return _stringProperty;
    }

    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public String getStringProperty() {
        return this.stringProperty;
    }

    public void setStringArrayProperty(String[] stringArrayProperty) {
        _stringArrayProperty = stringArrayProperty;
    }

    public String[] getStringArrayProperty() {
        return _stringArrayProperty;
    }

    public String getNullProperty() {
        return _nullProperty;
    }

    public void setNullProperty(String nullProperty) {
        _nullProperty = nullProperty;
    }

    /*
     *
     * Primitive Properties
     *
     */

    public void setBoolProperty(boolean boolProperty) {
        _boolProperty = boolProperty;
    }

    public boolean getBoolProperty() {
        return _boolProperty;
    }

    public void setIntProperty(int intProperty) {
        _intProperty = intProperty;
    }

    public int getIntProperty() {
        return _intProperty;
    }

    public void setCharProperty(char charProperty) {
        _charProperty = charProperty;
    }

    public char getCharProperty() {
        return _charProperty;
    }

    public void setByteProperty(byte byteProperty) {
        _byteProperty = byteProperty;
    }

    public byte getByteProperty() {
        return _byteProperty;
    }

    public void setShortProperty(short shortProperty) {
        _shortProperty = shortProperty;
    }

    public short getShortProperty() {
        return _shortProperty;
    }

    public void setLongProperty(long longProperty) {
        _longProperty = longProperty;
    }

    public long getLongProperty() {
        return _longProperty;
    }

    public void setFloatProperty(float floatProperty) {
        _floatProperty = floatProperty;
    }

    public float getFloatProperty() {
        return _floatProperty;
    }

    public void setDoubleProperty(double doubleProperty) {
        _doubleProperty = doubleProperty;
    }

    public double getDoubleProperty() {
        return _doubleProperty;
    }
    
    /*
     *
     * Primitive Arrays
     *
     */

    public void setBoolArrayProperty(boolean[] boolArrayProperty) {
        _boolArrayProperty = boolArrayProperty;
    }

    public boolean[] getBoolArrayProperty() {
        return _boolArrayProperty;
    }

    public void setIntArrayProperty(int[] intArrayProperty) {
        _intArrayProperty = intArrayProperty;
    }

    public int[] getIntArrayProperty() {
        return _intArrayProperty;
    }

    public void setCharArrayProperty(char[] charArrayProperty) {
        _charArrayProperty = charArrayProperty;
    }

    public char[] getCharArrayProperty() {
        return _charArrayProperty;
    }

    public void setByteArrayProperty(byte[] byteArrayProperty) {
        _byteArrayProperty = byteArrayProperty;
    }

    public byte[] getByteArrayProperty() {
        return _byteArrayProperty;
    }

    public void setShortArrayProperty(short[] shortArrayProperty) {
        _shortArrayProperty = shortArrayProperty;
    }

    public short[] getShortArrayProperty() {
        return _shortArrayProperty;
    }

    public void setLongArrayProperty(long[] longArrayProperty) {
        _longArrayProperty = longArrayProperty;
    }

    public long[] getLongArrayProperty() {
        return _longArrayProperty;
    }

    public void setFloatArrayProperty(float[] floatArrayProperty) {
        _floatArrayProperty = floatArrayProperty;
    }

    public float[] getFloatArrayProperty() {
        return _floatArrayProperty;
    }

    public void setDoubleArrayProperty(double[] doubleArrayProperty) {
        _doubleArrayProperty = doubleArrayProperty;
    }

    public double[] getDoubleArrayProperty() {
        return _doubleArrayProperty;
    }

    /*
     *
     * Primitive Wrappers
     *
     */

    public void setBooleanWrapperProperty(Boolean boolWrapperProperty) {
        _boolWrapperProperty = boolWrapperProperty;
    }

    public Boolean getBooleanWrapperProperty() {
        return _boolWrapperProperty;
    }

    public void setByteWrapperProperty(Byte byteWrapperProperty) {
        _byteWrapperProperty = byteWrapperProperty;
    }

    public Byte getByteWrapperProperty() {
        return _byteWrapperProperty;
    }

    public void setCharWrapperProperty(Character charWrapperProperty) {
        _charWrapperProperty = charWrapperProperty;
    }

    public Character getCharWrapperProperty() {
        return _charWrapperProperty;
    }

    public void setDoubleWrapperProperty(Double doubleWrapperProperty) {
        _doubleWrapperProperty = doubleWrapperProperty;
    }

    public Double getDoubleWrapperProperty() {
        return _doubleWrapperProperty;
    }

    public void setFloatWrapperProperty(Float floatWrapperProperty) {
        _floatWrapperProperty = floatWrapperProperty;
    }

    public Float getFloatWrapperProperty() {
        return _floatWrapperProperty;
    }

    public void setIntWrapperProperty(Integer intWrapperProperty) {
        _intWrapperProperty = intWrapperProperty;
    }

    public Integer getIntWrapperProperty() {
        return _intWrapperProperty;
    }

    public void setLongWrapperProperty(Long longWrapperProperty) {
        _longWrapperProperty = longWrapperProperty;
    }

    public Long getLongWrapperProperty() {
        return _longWrapperProperty;
    }

    public void setShortWrapperProperty(Short shortWrapperProperty) {
        _shortWrapperProperty = shortWrapperProperty;
    }

    public Short getShortWrapperProperty() {
        return _shortWrapperProperty;
    }

    /*
     *
     * Primitive Wrapper Arrays
     *
     */

    public void setBoolWrapperArrayProperty(Boolean[] boolWrapperArrayProperty) {
        _boolWrapperArrayProperty = boolWrapperArrayProperty;
    }

    public Boolean[] getBoolWrapperArrayProperty() {
        return _boolWrapperArrayProperty;
    }

    public void setIntWrapperArrayProperty(Integer[] intWrapperArrayProperty) {
        _intWrapperArrayProperty = intWrapperArrayProperty;
    }

    public Integer[] getIntWrapperArrayProperty() {
        return _intWrapperArrayProperty;
    }

    public void setCharWrapperArrayProperty(Character[] charWrapperArrayProperty) {
        _charWrapperArrayProperty = charWrapperArrayProperty;
    }

    public Character[] getCharWrapperArrayProperty() {
        return _charWrapperArrayProperty;
    }

    public void setByteWrapperArrayProperty(Byte[] byteWrapperArrayProperty) {
        _byteWrapperArrayProperty = byteWrapperArrayProperty;
    }

    public Byte[] getByteWrapperArrayProperty() {
        return _byteWrapperArrayProperty;
    }

    public void setShortWrapperArrayProperty(Short[] shortWrapperArrayProperty) {
        _shortWrapperArrayProperty = shortWrapperArrayProperty;
    }

    public Short[] getShortWrapperArrayProperty() {
        return _shortWrapperArrayProperty;
    }

    public void setLongWrapperArrayProperty(Long[] longWrapperArrayProperty) {
        _longWrapperArrayProperty = longWrapperArrayProperty;
    }

    public Long[] getLongWrapperArrayProperty() {
        return _longWrapperArrayProperty;
    }

    public void setFloatWrapperArrayProperty(Float[] floatWrapperArrayProperty) {
        _floatWrapperArrayProperty = floatWrapperArrayProperty;
    }

    public Float[] getFloatWrapperArrayProperty() {
        return _floatWrapperArrayProperty;
    }

    public void setDoubleWrapperArrayProperty(Double[] doubleWrapperArrayProperty) {
        _doubleWrapperArrayProperty = doubleWrapperArrayProperty;
    }

    public Double[] getDoubleWrapperArrayProperty() {
        return _doubleWrapperArrayProperty;
    }

}
