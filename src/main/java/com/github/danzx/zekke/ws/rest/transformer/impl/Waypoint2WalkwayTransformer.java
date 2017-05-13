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
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.ws.rest.common.Walkway;
import com.github.danzx.zekke.ws.rest.transformer.Transformer;

import org.springframework.stereotype.Component;

/**
 * Transforms waypoints to walkways to waypoints.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class Waypoint2WalkwayTransformer implements Transformer<Waypoint, Walkway> {

    private final BaseWaypoint2WaypointTransformer<Walkway> baseTransformer;

    public Waypoint2WalkwayTransformer() {
        baseTransformer = new BaseWaypoint2WaypointTransformer<>(Walkway::new);
    }

    @Override
    public Walkway convert(Waypoint source) {
        Optional<Waypoint> optionalSource = Optional.ofNullable(source);
        baseTransformer.assertType(Type.WALKWAY, source);
        return optionalSource
                .map(waypoint -> baseTransformer.convert(waypoint))
                .orElse(null);
    }

    @Override
    public Waypoint revert(Walkway source) {
        Optional<Waypoint> optionalWaypoint = Optional.ofNullable(baseTransformer.revert(source));
        optionalWaypoint.ifPresent(waypoint -> waypoint.setType(Type.WALKWAY));
        return optionalWaypoint.orElse(null);
    }
}
