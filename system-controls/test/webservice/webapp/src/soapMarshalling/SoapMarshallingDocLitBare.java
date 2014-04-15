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

import org.apache.beehive.soapMarshalling.ISoapMarshalling;
import org.apache.axis.types.Duration;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;

/**
 * Purpose: Used for validating datatype marshalling
 */
@WebService()
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class SoapMarshallingDocLitBare implements ISoapMarshalling
{
    @WebMethod()
    @WebResult(name="echobooleanResponse")
    public boolean echoboolean(@WebParam(name="echoboolean") boolean inputboolean) {
        return inputboolean;
    }

    @WebMethod()
    @WebResult(name="echoBooleanResponse")
    public Boolean echoBoolean(@WebParam(name="echoBoolean") Boolean inputBoolean) {
        return inputBoolean;
    }

    @WebMethod()
    @WebResult(name="echobyteResponse")
    public byte echobyte(@WebParam(name="echobyte") byte inputbyte) {
        return inputbyte;
    }

    @WebMethod()
    @WebResult(name="echoByteResponse")
    public Byte echoByte(@WebParam(name="echoByte") Byte inputByte) {
        return inputByte;
    }

    @WebMethod()
    @WebResult(name="echoshortResponse")
    public short echoshort(@WebParam(name="echoshort") short inputshort) {
        return inputshort;
    }

    @WebMethod()
    @WebResult(name="echoShortResponse")
    public Short echoShort(@WebParam(name="echoShort") Short inputShort) {
        return inputShort;
    }

    @WebMethod()
    @WebResult(name="echointResponse")
    public int echoint(@WebParam(name="echoint") int inputint) {
        return inputint;
    }

    @WebMethod()
    @WebResult(name="echoIntegerResponse")
    public Integer echoInteger(@WebParam(name="echoInteger") Integer inputInteger) {
        return inputInteger;
    }

    @WebMethod()
    @WebResult(name="echolongResponse")
    public long echolong(@WebParam(name="echolong") long inputlong) {
        return inputlong;
    }

    @WebMethod()
    @WebResult(name="echoLongResponse")
    public Long echoLong(@WebParam(name="echoLong") Long inputLong) {
        return inputLong;
    }

    @WebMethod()
    @WebResult(name="echoBigIntegerResponse")
    public BigInteger echoBigInteger(@WebParam(name="echoBigInteger") BigInteger inputBigInteger) {
        return inputBigInteger;
    }

    @WebMethod()
    @WebResult(name="echofloatResponse")
    public float echofloat(@WebParam(name="echofloat") float inputfloat) {
        return inputfloat;
    }

    @WebMethod()
    @WebResult(name="echoFloatResponse")
    public Float echoFloat(@WebParam(name="echoFloat") Float inputFloat) {
        return inputFloat;
    }

    @WebMethod()
    @WebResult(name="echodoubleResponse")
    public double echodouble(@WebParam(name="echodouble") double inputdouble) {
        return inputdouble;
    }

    @WebMethod()
    @WebResult(name="echoDoubleResponse")
    public Double echoDouble(@WebParam(name="echoDouble") Double inputDouble) {
        return inputDouble;
    }

    @WebMethod()
    @WebResult(name="echoBigDecimalResponse")
    public BigDecimal echoBigDecimal(@WebParam(name="echoBigDecimal") BigDecimal inputBigDecimal) {
        return inputBigDecimal;
    }

    @WebMethod()
    @WebResult(name="echoStringResponse")
    public String echoString(@WebParam(name="echoString") String inputString) {
        return inputString;
    }

    @WebMethod()
    @WebResult(name="echoDateResponse")
    public Date echoDate(@WebParam(name="echoDate") Date inputDate) {
        return inputDate;
    }

    //TODO: is this supported
    //@WebMethod()
    //public java.sql.Date echoSQLDate(java.sql.Date inputSQLDate) {return inputSQLDate;}

    @WebMethod()
    @WebResult(name="echoCalendarResponse")
    public Calendar echoCalendar(@WebParam(name="echoCalendar") Calendar inputCalendar) {
        return inputCalendar;
    }

    @WebMethod()
    @WebResult(name="echoQNameResponse")
    public QName echoQName(@WebParam(name="echoQName") QName inputQName)
    {
        return inputQName;
    }
}
