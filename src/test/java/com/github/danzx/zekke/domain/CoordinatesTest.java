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

import static com.github.danzx.zekke.domain.Coordinates.MAX_LATITUDE;
import static com.github.danzx.zekke.domain.Coordinates.MAX_LONGITUDE;
import static com.github.danzx.zekke.domain.Coordinates.MIN_LATITUDE;
import static com.github.danzx.zekke.domain.Coordinates.MIN_LONGITUDE;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.github.danzx.zekke.test.BaseValidationTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mongodb.morphia.geo.GeoJson;
import org.mongodb.morphia.geo.Point;

@RunWith(JUnitParamsRunner.class)
public class CoordinatesTest extends BaseValidationTest {

    private static final double INVALID_UPPER_LAT = MAX_LATITUDE + 1;
    private static final double INVALID_LOWER_LAT = MIN_LATITUDE - 1;
    private static final double INVALID_UPPER_LNG = MAX_LONGITUDE + 1;
    private static final double INVALID_LOWER_LNG = MIN_LONGITUDE - 1;
    private static final Coordinates VALID_COORDINATES = Coordinates.ofLatLng(19.054492, -98.283176);

    @Test
    @Parameters(method = "invalidLatLng")
    public void shouldFailValidationWhenLatLngAreOutOfBounds(double lat, double lng) {
        Set<ConstraintViolation<Coordinates>> violations = validator().validate(Coordinates.ofLatLng(lat, lng));
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void shouldEqualsBeTrueWhenSameReference() {
        assertThat(VALID_COORDINATES.equals(VALID_COORDINATES)).isTrue();
    }

    @Test
    public void shouldEqualsBeTrueWhenObjectsAreNotTheSameReference() {
        Coordinates testPoint = Coordinates.ofLatLng(VALID_COORDINATES.getLatitude(), VALID_COORDINATES.getLongitude());
        assertThat(VALID_COORDINATES.equals(testPoint)).isTrue();
    }

    @Test
    public void shouldConvertToGeoJsonPoint() {
        assertThat(VALID_COORDINATES.toGeoJsonPoint()).isNotNull().extracting(Point::getLatitude, Point::getLongitude).containsExactly(VALID_COORDINATES.getLatitude(), VALID_COORDINATES.getLongitude());
    }

    @Test
    public void shouldConvertGeoJsonPointToCoordinates() {
        Point point = GeoJson.point(VALID_COORDINATES.getLatitude(), VALID_COORDINATES.getLongitude());
        assertThat(Coordinates.valueOf(point)).isNotNull().isEqualTo(VALID_COORDINATES);
    }

    @Test
    public void shouldReturnNullWhenPointToConvertIsNull() {
        assertThat(Coordinates.valueOf(null)).isNull();
    }

    @Test
    public void shouldEqualsBeFalseWhenNull() {
        assertThat(VALID_COORDINATES.equals(null)).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenComparingWithDifferentObject() {
        assertThat(VALID_COORDINATES.equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenAtLeastOnePropertyIsDifferent() {
        Coordinates testPoint = Coordinates.ofLatLng(13.492, VALID_COORDINATES.getLongitude());
        assertThat(VALID_COORDINATES.equals(testPoint)).isFalse();

        testPoint = Coordinates.ofLatLng(VALID_COORDINATES.getLatitude(), -52.276);
        assertThat(VALID_COORDINATES.equals(testPoint)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        Coordinates testPoint = Coordinates.ofLatLng(VALID_COORDINATES.getLatitude(), VALID_COORDINATES.getLongitude());
        assertThat(VALID_COORDINATES.hashCode()).isEqualTo(VALID_COORDINATES.hashCode()).isEqualTo(testPoint.hashCode());
    }

    @Test
    public void shouldConvertToAndFromString() {
        assertThat(Coordinates.fromString(VALID_COORDINATES.toString())).isNotNull().isEqualTo(VALID_COORDINATES);
    }

    @Test
    public void shouldFromStringReturnNullWhenNull() {
        assertThat(Coordinates.fromString(null)).isNull();
    }

    @Test
    public void shouldFromStringThrowIllegalArgumentExceptionWhenValidIsNotValid() {
        assertThatThrownBy(() -> Coordinates.fromString("sdfsdf")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Coordinates.fromString("sdf,sdf")).isInstanceOf(IllegalArgumentException.class);
    }

    protected Object[][] invalidLatLng() {
        return new Object[][] {
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
