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
package com.github.danzx.zekke.ws.rest.transformer.impl;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Supplier;

import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.ws.rest.common.BaseWaypoint;
import com.github.danzx.zekke.ws.rest.transformer.TransformationException;
import com.github.danzx.zekke.ws.rest.transformer.Transformer;

/**
 * Base transformer of BaseWaypoint to Waypoint.
 * 
 * @author Daniel Pedraza-Arcega
 *
 * @param <T> a subtype of BaseWaypoint.
 */
class BaseWaypoint2WaypointTransformer<T extends BaseWaypoint> implements Transformer<Waypoint, T> {

    private final Supplier<T> newBaseWaypointSupplier;

    /**
     * Constructor.
     *  
     * @param newBaseWaypointSupplier a way to create a BaseWaypoint.
     */
    BaseWaypoint2WaypointTransformer(Supplier<T> newBaseWaypointSupplier) {
        this.newBaseWaypointSupplier = requireNonNull(newBaseWaypointSupplier);
    }

    @Override
    public T convert(Waypoint source) {
        Optional<Waypoint> optionalSource = Optional.ofNullable(source);
        return optionalSource
                .map(waypoint -> {
                        T baseWaypoint = newBaseWaypointSupplier.get();
                        baseWaypoint.setId(source.getId());
                        baseWaypoint.setLocation(source.getLocation());
                        return baseWaypoint;
                    })
                .orElse(null);
    }

    @Override
    public Waypoint revert(T source) {
        Optional<T> optionalSource = Optional.ofNullable(source);
        return optionalSource
                .map(baseWaypoint -> {
                        Waypoint waypoint = new Waypoint();
                        waypoint.setId(source.getId());
                        waypoint.setLocation(source.getLocation());
                        return waypoint;
                    })
                .orElse(null);
    }

    /**
     * Checks whether the submited waypoint has the expected type.
     * 
     * @throws TransformationException if it's not the of the expected type.
     */
    void assertType(Type expected, Waypoint source) {
        Optional.ofNullable(source).ifPresent(waypoint -> {
            if (expected != source.getType()) {
                throw new TransformationException.Builder()
                    .messageKey("waypoint.expected_type.error")
                    .messageArgs(expected, source.getType())
                    .build();
            }
        });
    }
}
