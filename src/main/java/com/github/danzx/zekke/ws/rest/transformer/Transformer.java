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
package com.github.danzx.zekke.ws.rest.transformer;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * Transforms objects back and forth
 * 
 * @author Daniel Pedraza-Arcega
 *
 * @param <T1> the first object type.
 * @param <T2> the second object type.
 */
public interface Transformer<T1, T2> {

    /** Converts the first type object into the second. */
    T2 convert(T1 source);

    /** Converts the second type object into the first. */
    T1 revert(T2 source);

    /** Converts a list of the first type object into a list of the second type. */
    default List<T2> convertList(List<T1> list) {
        requireNonNull(list);
        return list.stream().map(this::convert).collect(toList());
    }

    /** Converts a list of the second type object into a list of the first type. */
    default List<T1> revertList(List<T2> list) {
        requireNonNull(list);
        return list.stream().map(this::revert).collect(toList());
    }
}
