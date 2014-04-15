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
package org.apache.beehive.soapMarshalling;

import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public interface ISoapMarshallingArrayTypes {
    boolean[] echoboolean(boolean[] inputboolean);

    Boolean[] echoBoolean(Boolean[] inputBoolean);

    byte[] echobyte(byte[] inputbyte);

    Byte[] echoByte(Byte[] inputByte);

    short[] echoshort(short[] inputshort);

    Short[] echoShort(Short[] inputShort);

    int[] echoint(int[] inputint);

    Integer[] echoInteger(Integer[] inputInteger);

    long[] echolong(long[] inputlong);

    Long[] echoLong(Long[] inputLong);

    BigInteger[] echoBigInteger(BigInteger[] inputBigInteger);

    float[] echofloat(float[] inputfloat);

    Float[] echoFloat(Float[] inputFloat);

    double[] echodouble(double[] inputdouble);

    Double[] echoDouble(Double[] inputDouble);

    BigDecimal[] echoBigDecimal(BigDecimal[] inputBigDecimal);

    String[] echoString(String[] inputString);

    Date[] echoDate(Date[] inputDate);

    Calendar[] echoCalendar(Calendar[] inputCalendar);

    QName[] echoQName(QName[] inputQName);
}
