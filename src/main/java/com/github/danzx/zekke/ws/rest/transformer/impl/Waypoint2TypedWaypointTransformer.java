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

import java.util.Optional;

import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.ws.rest.common.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.transformer.Transformer;

import org.springframework.stereotype.Component;

/**
 * Transforms waypoints to TypedWaypoints to waypoints.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class Waypoint2TypedWaypointTransformer implements Transformer<Waypoint, TypedWaypoint> {

    private final BaseWaypoint2WaypointTransformer<TypedWaypoint> baseTransformer;

    public Waypoint2TypedWaypointTransformer() {
        baseTransformer = new BaseWaypoint2WaypointTransformer<>(TypedWaypoint::new);
    }

    @Override
    public TypedWaypoint convert(Waypoint source) {
        Optional<TypedWaypoint> optionalTypedWaypoint = Optional.ofNullable(baseTransformer.convert(source));
        optionalTypedWaypoint.ifPresent(typedWaypoint -> {
            typedWaypoint.setType(source.getType());
            typedWaypoint.setName(source.getName().orElse(null));
        });
        return optionalTypedWaypoint.orElse(null);
    }

    @Override
    public Waypoint revert(TypedWaypoint source) {
        Optional<Waypoint> optionalWaypoint = Optional.ofNullable(baseTransformer.revert(source));
        optionalWaypoint.ifPresent(waypoint -> {
            waypoint.setType(source.getType());
            waypoint.setName(source.getName());
        });
        return optionalWaypoint.orElse(null);
    }
}
