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
package org.apache.beehive.netui.core.chain.impl;

import org.apache.beehive.netui.core.chain.Chain;
import org.apache.beehive.netui.core.chain.Command;
import org.apache.beehive.netui.core.chain.Context;

/**
 *
 */
public class ChainBase
    implements Chain {

    private Command[] _commands = new Command[0];
    private boolean _frozen;

    public ChainBase() {
    }

    /**
     * Add a command to the chian.
     * @param command the new command
     */
    public void addCommand(Command command) {
        if(command == null)
            throw new IllegalArgumentException();

        if(_frozen)
            throw new IllegalStateException();

        Command[] results = new Command[_commands.length + 1];
        System.arraycopy(_commands, 0, results, 0, _commands.length);
        results[_commands.length] = command;
        _commands = results;
    }

    public Command[] getCommands() {
        return _commands;
    }

    /**
     * Execute the chain using the provided {@link Context}.
     * 
     * @param context
     * @return
     * @throws Exception
     */
    public boolean execute(Context context)
     throws Exception {
        if(context == null)
            throw new IllegalArgumentException();

        if(!_frozen)
            _frozen = true;

        boolean result = false;
        Exception saveException = null;
        for(int i = 0; i < _commands.length; i++) {
            try {
                result = _commands[i].execute(context);
                if(result)
                    break;
            }
            catch(Exception e) {
                saveException = e;
            }
        }

        if(saveException != null)
            throw saveException;
        else return result;
    }
}
