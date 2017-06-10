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
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

/**
 * Waypoint to TypedWalkway Orika mapping config.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
public class WaypointToTypedWaypointMapping implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
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
