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
package com.github.danzx.zekke.ws.rest.config;

import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.transformer.orika.WaypointOrikaTransformer;
import com.github.danzx.zekke.transformer.Transformer;
import com.github.danzx.zekke.ws.rest.model.Poi;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.model.Walkway;

import ma.glasnost.orika.MapperFacade;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RESTful model transformer configuration.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Configuration
public class ModelTransformerConfig {

    @Bean
    public Transformer<Waypoint, Poi> waypointToPoiTransformer(MapperFacade mapperFacade) {
        return new WaypointOrikaTransformer<>(mapperFacade, Poi.class);
    }

    @Bean
    public Transformer<Waypoint, Walkway> waypointToWalkwayTransformer(MapperFacade mapperFacade) {
        return new WaypointOrikaTransformer<>(mapperFacade, Walkway.class);
    }

    @Bean
    public Transformer<Waypoint, TypedWaypoint> waypointToTypedWaypointTransformer(MapperFacade mapperFacade) {
        return new WaypointOrikaTransformer<>(mapperFacade, TypedWaypoint.class);
    }
}
