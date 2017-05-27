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
package com.github.danzx.zekke.config;

import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.ws.rest.model.Poi;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.model.Walkway;
import com.github.danzx.zekke.ws.rest.transformer.Transformer;
import com.github.danzx.zekke.ws.rest.transformer.orika.WaypointOrikaTransformer;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Transformer configuration.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Configuration
public class TransformerConfig {

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

    @Bean
    public MapperFacade mapperFacade() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
                .mapNulls(false)
                .build();
        registerWaypoint2PoiMapper(mapperFactory);
        registerWaypoint2WalkwayMapper(mapperFactory);
        registerWaypoint2TypedWaypoint(mapperFactory);
        return mapperFactory.getMapperFacade();
    }

    private void registerWaypoint2PoiMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(Waypoint.class, Poi.class)
            .exclude("name")
            .customize(new CustomMapper<Waypoint, Poi>() {
                @Override
                public void mapAtoB(Waypoint a, Poi b, MappingContext context) {
                    a.getName().ifPresent(name -> b.setName(name));
                }

                @Override
                public void mapBtoA(Poi b, Waypoint a, MappingContext context) {
                    a.setName(b.getName());
                    a.setType(Type.POI);
                }
            })
            .byDefault()
            .register();
    }

    private void registerWaypoint2WalkwayMapper(MapperFactory mapperFactory) {
        mapperFactory.classMap(Waypoint.class, Walkway.class)
            .byDefault()
            .customize(new CustomMapper<Waypoint, Walkway>() {
                @Override
                public void mapBtoA(Walkway b, Waypoint a, MappingContext context) {
                    a.setType(Type.WALKWAY);
                }
            })
            .register();
    }

    private void registerWaypoint2TypedWaypoint(MapperFactory mapperFactory) {
        mapperFactory.classMap(Waypoint.class, TypedWaypoint.class)
            .exclude("name")
            .customize(new CustomMapper<Waypoint, TypedWaypoint>() {
                public void mapAtoB(Waypoint a, TypedWaypoint b, MappingContext context) {
                    a.getName().ifPresent(name -> b.setName(name));
                }

                @Override
                public void mapBtoA(TypedWaypoint b, Waypoint a, MappingContext context) {
                    a.setName(b.getName());
                }
            })
            .byDefault()
            .register();
    }
}
