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
package com.github.danzx.zekke.domain;

import static com.github.danzx.zekke.domain.Coordinates.newLatLng;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.danzx.zekke.test.BaseValitionTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class CoordinatesTest extends BaseValitionTest {

    private static final double INVALID_UPPER_LAT = Coordinates.MAX_LATITUDE + 1;
    private static final double INVALID_LOWER_LAT = Coordinates.MIN_LATITUDE - 1;
    private static final double INVALID_UPPER_LNG = Coordinates.MAX_LONGITUDE + 1;
    private static final double INVALID_LOWER_LNG = Coordinates.MIN_LONGITUDE - 1;
    private static final Coordinates VALID_COORDINATES = newLatLng(19.054492, -98.283176);

    @Test
    @Parameters(method = "invalidLatLng")
    public void shouldFailValidationWhenLatLngAreOutOfBounds(double lat, double lng) {
        Set<ConstraintViolation<Coordinates>> violations = validator().validate(newLatLng(lat, lng));
        assertThat(violations).isNotEmpty();
    }

    protected Object[][] invalidLatLng() {
        return new Object[][]{
                {INVALID_UPPER_LAT, VALID_COORDINATES.getLongitude()},
                {INVALID_UPPER_LAT, INVALID_UPPER_LNG},
                {VALID_COORDINATES.getLatitude(), INVALID_UPPER_LNG},
                {INVALID_LOWER_LAT, VALID_COORDINATES.getLongitude()},
                {INVALID_LOWER_LAT, INVALID_UPPER_LNG},
                {VALID_COORDINATES.getLatitude(), INVALID_UPPER_LNG},
                {INVALID_UPPER_LAT, VALID_COORDINATES.getLongitude()},
                {INVALID_UPPER_LAT, INVALID_LOWER_LNG},
                {VALID_COORDINATES.getLatitude(), INVALID_LOWER_LNG},
                {INVALID_LOWER_LAT, VALID_COORDINATES.getLongitude()},
                {INVALID_LOWER_LAT, INVALID_LOWER_LNG},
                {VALID_COORDINATES.getLatitude(), INVALID_LOWER_LNG}
        };
    }
}
