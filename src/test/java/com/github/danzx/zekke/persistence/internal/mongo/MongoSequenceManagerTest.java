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
package com.github.danzx.zekke.persistence.internal.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;

import com.github.danzx.zekke.test.mongo.BaseSpringMongoTest;

public class MongoSequenceManagerTest extends BaseSpringMongoTest {

    private static final MongoSequence TEST_SEQUENCE = MongoSequence.WAYPOINT_ID;

    @Inject private MongoSequenceManager sequenceManager;

    @Override
    public void before() throws Exception {
        super.before();
        assertThat(sequenceManager).isNotNull();
    }

    @Test
    public void shouldRetunCurrentValue() {
        long currentValue = sequenceManager.getCurrentSequenceValue(TEST_SEQUENCE);
        assertThat(currentValue).isEqualTo(8L);
    }

    @Test
    public void shouldSetValue() {
        long newValue = 666L;
        sequenceManager.setSequenceValue(TEST_SEQUENCE, newValue);
        long currentValue = sequenceManager.getCurrentSequenceValue(TEST_SEQUENCE);
        assertThat(currentValue).isEqualTo(newValue);
    }

    @Test
    public void shouldRetunNextValue() {
        long currentValue = sequenceManager.getNextSequenceValue(TEST_SEQUENCE);
        assertThat(currentValue).isEqualTo(9L);
    }
}
