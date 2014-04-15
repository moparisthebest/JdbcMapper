/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package soapMarshalling;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;

import org.apache.beehive.soapMarshalling.ISoapMarshallingArrayTypes;

/**
 * Purpose: Tests datatype marshalling using arrays
 */
@WebService()
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class SoapMarshallingDocLitWrappedArrayTypes implements ISoapMarshallingArrayTypes
{

    @WebMethod()
    public boolean[] echoboolean(boolean[] inputboolean) {
        return inputboolean;
    }

    @WebMethod()
    public Boolean[] echoBoolean(Boolean[] inputBoolean) {
        return inputBoolean;
    }

    @WebMethod()
    public byte[] echobyte(byte[] inputbyte) {
        return inputbyte;
    }

    @WebMethod()
    public Byte[] echoByte(Byte[] inputByte) {
        return inputByte;
    }

    @WebMethod()
    public short[] echoshort(short[] inputshort) {
        return inputshort;
    }

    @WebMethod()
    public Short[] echoShort(Short[] inputShort) {
        return inputShort;
    }

    @WebMethod()
    public int[] echoint(int[] inputint) {
        return inputint;
    }

    @WebMethod()
    public Integer[] echoInteger(Integer[] inputInteger) {
        return inputInteger;
    }

    @WebMethod()
    public long[] echolong(long[] inputlong) {
        return inputlong;
    }

    @WebMethod()
    public Long[] echoLong(Long[] inputLong) {
        return inputLong;
    }

    @WebMethod()
    public BigInteger[] echoBigInteger(BigInteger[] inputBigInteger) {
        return inputBigInteger;
    }

    @WebMethod()
    public float[] echofloat(float[] inputfloat) {
        return inputfloat;
    }

    @WebMethod()
    public Float[] echoFloat(Float[] inputFloat) {
        return inputFloat;
    }

    @WebMethod()
    public double[] echodouble(double[] inputdouble) {
        return inputdouble;
    }

    @WebMethod()
    public Double[] echoDouble(Double[] inputDouble) {
        return inputDouble;
    }

    @WebMethod()
    public BigDecimal[] echoBigDecimal(BigDecimal[] inputBigDecimal) {
        return inputBigDecimal;
    }

    @WebMethod()
    public String[] echoString(String[] inputString) {
        return inputString;
    }

    @WebMethod()
    public Date[] echoDate(Date[] inputDate) {
        return inputDate;
    }

    //TODO: is this supported
    //@WebMethod()
    //public java.sql.Date[] echoSQLDate(java.sql.Date[] inputSQLDate) {return inputSQLDate;}

    @WebMethod()
    public Calendar[] echoCalendar(Calendar[] inputCalendar) {
        return inputCalendar;
    }

    @WebMethod()
    public QName[] echoQName(QName[] inputQName) {
        return inputQName;
    }
}
