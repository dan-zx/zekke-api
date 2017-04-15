/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.danzx.zekke.persistence.internal.mongo;

import static com.github.danzx.zekke.util.Strings.quoted;

import com.github.danzx.zekke.persistence.internal.Sequence;

/**
 * MongoDB sequences stored in the sequence collection and implemented as JS functions.
 * 
 * @author Daniel Pedraza-Arcega
 */
public enum MongoSequence implements Sequence {

    WAYPOINT_ID;

    public static final String COLLECTION_NAME = "sequences";
    private static final String NEXT_VALUE_FUNCTION_PATTERN = "getNextSequenceValue(%s)";

    private final String id;
    private final String nextValueFunctionCall;
    
    MongoSequence() {
        id = name().toLowerCase();
        nextValueFunctionCall = String.format(NEXT_VALUE_FUNCTION_PATTERN, quoted(id));
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String nextValueFunctionCall() {
        return nextValueFunctionCall;
    }
}
