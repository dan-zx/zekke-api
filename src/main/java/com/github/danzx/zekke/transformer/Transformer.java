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
package com.github.danzx.zekke.transformer;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * Transforms objects back and forth
 * 
 * @author Daniel Pedraza-Arcega
 *
 * @param <A> the first object type.
 * @param <B> the second object type.
 */
public interface Transformer<A, B> {

    /** 
     * Converts the first type object into the second.
     * 
     * @param source the object to be converted.
     * @return the object converted. 
     */
    B convertAtoB(A source);

    /**
     * Converts the second type object into the first.
     * 
     * @param source the object to be converted.
     * @return the object converted.
     */
    A convertBtoA(B source);

    /**
     * Converts a list of the first type object into a list of the second type.
     * 
     * @param sourceList the list of objects to be converted.
     * @return a list with the objects converted.
     */
    default List<B> convertListAtoListB(List<A> sourceList) {
        requireNonNull(sourceList);
        return sourceList.stream().map(this::convertAtoB).collect(toList());
    }

    /** Converts a list of the second type object into a list of the first type. 
     * 
     * @param sourceList the list of objects to be converted.
     * @return a list with the objects converted.
     */
    default List<A> convertListBtoListA(List<B> sourceList) {
        requireNonNull(sourceList);
        return sourceList.stream().map(this::convertBtoA).collect(toList());
    }
}
