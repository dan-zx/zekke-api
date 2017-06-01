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
package com.github.danzx.zekke.domain.constraint.validator;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintValidatorContext;

import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.test.mockito.BaseMockitoTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Answers;
import org.mockito.Mock;

@RunWith(JUnitParamsRunner.class)
public class WaypointValidatorTest extends BaseMockitoTest {

    private static final WaypointValidator TEST_VALIDATOR = new WaypointValidator();

    private @Mock(answer = Answers.RETURNS_MOCKS) ConstraintValidatorContext mockContext;

    @Test
    @Parameters(method = "testWaypoints")
    public void shouldValidateWaypoint(Waypoint waypoint, boolean expectedResult) {
        assertThat(TEST_VALIDATOR.isValid(waypoint, mockContext)).isEqualTo(expectedResult);
    }

    protected Object[][] testWaypoints() {
        Waypoint w1 = new Waypoint();
        w1.setType(Type.POI);
        w1.setName("Name");

        Waypoint w2 = new Waypoint();
        w2.setType(Type.WALKWAY);

        Waypoint w3 = new Waypoint();
        w3.setType(Type.WALKWAY);
        w3.setName("Name");

        Waypoint w4 = new Waypoint();
        w4.setType(Type.POI);

        return new Object[][] {
            {null, true},
            {w1, true},
            {w2, true},
            {w3, false},
            {w4, false}
        };
    }
}
