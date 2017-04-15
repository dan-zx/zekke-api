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
package com.github.danzx.zekke.persistence.morphia.converter;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.test.spring.BaseSpringTest;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.junit.Before;
import org.junit.Test;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

public class OptionalConverterTest extends BaseSpringTest {

    @Inject private Morphia morphia;
    @Inject private Datastore datastore;

    @Before
    public void setUp() {
        assertThat(morphia).isNotNull();
        assertThat(datastore).isNotNull();
    }
    
    @Test
    public void shouldConvertEntityToDbObject() {
        String expectedName = "aName";
        Waypoint waypoint = new Waypoint();
        waypoint.setName(expectedName);
        
        DBObject dbObject = morphia.toDBObject(waypoint);
        assertThat((String) dbObject.get("name")).isNotNull().isNotBlank().isEqualTo(expectedName);
        
        dbObject = morphia.toDBObject(new Waypoint());
        assertThat(dbObject.get("name")).isNull();
    }
    
    @Test
    public void shouldConvertDbObjectToDbEntity() {
        String expectedName = "aName";
        DBObject dbObject = new BasicDBObject("name", expectedName);
        dbObject.put("type", "POI");
        Waypoint waypoint = morphia.fromDBObject(datastore, Waypoint.class, dbObject);
        assertThat(waypoint.getName().isPresent()).isTrue();
        assertThat(waypoint.getName().get()).isNotBlank().isEqualTo(expectedName);
        
        dbObject = new BasicDBObject("name", null);
        dbObject.put("type", "WALKWAY");
        waypoint = morphia.fromDBObject(datastore, Waypoint.class, dbObject);
        assertThat(waypoint.getName().isPresent()).isFalse();
    }
}
