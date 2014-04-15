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
package org.apache.beehive.netui.tags.databinding.repeater.pad;

import org.apache.beehive.netui.util.internal.InternalStringBuilder;

/**
 * A JavaBean that encapsulates the data needed to pad a Repeater with
 * text.  The PadContext is needed if the given consttraints, the
 * minRepeat and maxRepeat attributes, require that the data rendered
 * in the repeater be padded.
 */
public class PadContext {

    private static final String EMPTY_STRING = "";

    /**
     * @exclude
     */
    static final int DEFAULT_VALUE = -1;

    private int _maxRepeat = DEFAULT_VALUE;
    private int _minRepeat = DEFAULT_VALUE;
    private String _padText = EMPTY_STRING;

    /**
     * @param padText   the text that will be used to pad the Repeater content, if necessary
     * @param minRepeat the minimum number of items that must be rendered in the repeater's body
     * @param maxRepeat the maximum number of items to render in the repeater's body
     */
    public PadContext(String padText, int minRepeat, int maxRepeat) {
        _padText = (padText != null ? padText : EMPTY_STRING);
        _minRepeat = minRepeat;
        _maxRepeat = maxRepeat;
    }

    /**
     * Get the text to use when padding the {@link org.apache.beehive.netui.tags.databinding.repeater.Repeater}
     *
     * @return the pad text
     */
    public String getPadText() {
        return _padText;
    }

    /**
     * Get the minimum number of times to render an item in the repeater's body.
     *
     * @return the minimum number of items that must be rendered in the repeater's body
     */
    public int getMinRepeat() {
        return _minRepeat;
    }

    /**
     * Get the maximum number of times to render items in the repeater's body.
     *
     * @return the maximum number of items to render items in the repeater's body
     */
    public int getMaxRepeat() {
        return _maxRepeat;
    }

    /**
     * @param currCount the count of the number of items rendered.
     * @return <code>true</code> if the minimum number of items have been rendered; <code>false</code> otherwise
     */
    public boolean checkMinRepeat(int currCount) {
        if(_minRepeat == DEFAULT_VALUE)
            return true;
        else if(currCount >= _minRepeat)
            return true;
        else return false;
    }

    /**
     * @param currCount the count of the number of items rendered so far
     * @return <code>true</code> if the maximum number of items have been rendered; <code>false</code> otherwise
     */
    public boolean checkMaxRepeat(int currCount) {
        if(_maxRepeat == DEFAULT_VALUE)
            return false;
        else if(currCount < _maxRepeat)
            return false;
        else return true;
    }

    /**
     * Get a debugging String that represents a PadContext.
     *
     * @return a String representation of the PadContext
     */
    public String toString() {
        InternalStringBuilder buf = new InternalStringBuilder(32);
        buf.append("\nPadContext: ");
        buf.append("padText: " + _padText + "\n");
        buf.append("minRepeat: " + _minRepeat + "\n");
        buf.append("maxRepeat: " + _maxRepeat + "\n");
        return buf.toString();
    }
}
