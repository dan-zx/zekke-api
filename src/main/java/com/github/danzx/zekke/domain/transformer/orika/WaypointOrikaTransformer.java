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
package com.github.danzx.zekke.domain.transformer.orika;

import static java.util.Objects.requireNonNull;

import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.transformer.Transformer;

import ma.glasnost.orika.MapperFacade;

/**
 * Waypoint transformer Orika implementation.
 * 
 * @author Daniel Pedraza-Arcega
 *
 * @param <T> the type to transform.
 */
public class WaypointOrikaTransformer<T> implements Transformer<Waypoint, T> {

    private final MapperFacade mapperFacade;
    private final Class<T> targetClass;

    public WaypointOrikaTransformer(MapperFacade mapperFacade, Class<T> targetClass) {
        this.mapperFacade = requireNonNull(mapperFacade);
        this.targetClass = requireNonNull(targetClass);
    }

    @Override
    public T convertAtoB(Waypoint source) {
        return mapperFacade.map(source, targetClass);
    }

    @Override
    public Waypoint convertBtoA(T source) {
        return mapperFacade.map(source, Waypoint.class);
    }
}
