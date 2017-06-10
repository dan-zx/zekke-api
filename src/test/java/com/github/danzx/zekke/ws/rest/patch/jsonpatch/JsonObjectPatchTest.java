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
package com.github.danzx.zekke.ws.rest.patch.jsonpatch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.StringJoiner;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.test.spring.BaseSpringTest;
import com.github.danzx.zekke.util.Strings;
import com.github.danzx.zekke.ws.rest.ObjectMapperConfig;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;

import com.github.fge.jsonpatch.JsonPatch;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;

@RunWith(JUnitParamsRunner.class)
@ContextConfiguration(classes = ObjectMapperConfig.class)
public class JsonObjectPatchTest extends BaseSpringTest {

    private @Inject ObjectMapper mapper;

    @Before
    public void setUp() {
        assertThat(mapper).isNotNull();
    }

    @Test
    @Parameters(method = "pathcsAndResults")
    public void shouldPatchWaypoint(TypedWaypoint waypointToPatch, String patch, TypedWaypoint expectedWaypoint) throws Exception {
        JsonPatch jsonPatch = mapper.readValue(patch, JsonPatch.class);
        JsonObjectPatch patchingObj = new JsonObjectPatch(mapper, jsonPatch);
        TypedWaypoint patchedWaypoint = patchingObj.apply(waypointToPatch);

        assertThat(patchedWaypoint).isNotNull()
            .extracting(TypedWaypoint::getId, TypedWaypoint::getName, TypedWaypoint::getType, TypedWaypoint::getLocation)
            .containsOnly(expectedWaypoint.getId(), expectedWaypoint.getName(), expectedWaypoint.getType(), expectedWaypoint.getLocation());

    }

    protected Object[][] pathcsAndResults() {
        TypedWaypoint waypointToPatch = new TypedWaypoint();
        waypointToPatch.setId(1L);
        waypointToPatch.setName("A Name");
        waypointToPatch.setType(Type.POI);
        waypointToPatch.setLocation(Coordinates.ofLatLng(19.3, -80.12));

        TypedWaypoint expectedWaypoint1 = new TypedWaypoint();
        expectedWaypoint1.setId(1L);
        expectedWaypoint1.setName(null);
        expectedWaypoint1.setType(Type.WALKWAY);
        expectedWaypoint1.setLocation(Coordinates.ofLatLng(5d, -20d));

        String pathc1 = jsonPatchStr(
                setPropetyValue("/location/latitude", expectedWaypoint1.getLocation().getLatitude()), 
                setPropetyValue("/location/longitude", expectedWaypoint1.getLocation().getLongitude()),
                setPropetyValue("/name", expectedWaypoint1.getName()),
                setPropetyValue("/type", expectedWaypoint1.getType().toString()));
        
        String patch2 = jsonPatchStr();

        TypedWaypoint expectedWaypoint2 = new TypedWaypoint();
        copy(expectedWaypoint1, expectedWaypoint2);

        return new Object[][] {
            { waypointToPatch, pathc1, expectedWaypoint1 }, 
            { expectedWaypoint1, patch2, expectedWaypoint2 }
        };
    }

    private String jsonPatchStr(StringBuilder... operations) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        for (StringBuilder opt : operations) joiner.add(opt);
        return joiner.toString();
    }

    private StringBuilder setPropetyValue(String propety, Object value) {
        String strValue = value == null ? 
                "null" : 
                String.class.isAssignableFrom(value.getClass()) ? 
                Strings.quoted(value.toString()) : 
                value.toString();
        return new StringBuilder()
                .append("{ \"op\": \"replace\", \"path\": \"")
                .append(propety)
                .append("\", \"value\": ")
                .append(strValue)
                .append(" }");
    }

    private void copy(TypedWaypoint src, TypedWaypoint dest) {
        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
