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

import static com.github.danzx.zekke.domain.Point.MAX_LATITUDE;
import static com.github.danzx.zekke.domain.Point.MAX_LONGITUDE;
import static com.github.danzx.zekke.domain.Point.MIN_LATITUDE;
import static com.github.danzx.zekke.domain.Point.MIN_LONGITUDE;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.danzx.zekke.test.BaseValitionTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class PointTest extends BaseValitionTest {

    private static final double INVALID_UPPER_LAT = MAX_LATITUDE + 1;
    private static final double INVALID_LOWER_LAT = MIN_LATITUDE - 1;
    private static final double INVALID_UPPER_LNG = MAX_LONGITUDE + 1;
    private static final double INVALID_LOWER_LNG = MIN_LONGITUDE - 1;
    private static final Point VALID_POINT = newPoint(19.054492, -98.283176);

    @Test
    @Parameters(method = "invalidLatitudes")
    public void shouldFailValidationWhenLatitudeIsOutOfBounds(double lat) throws Exception {
        Point point = new Point();
        Method setLatitudeMethod = Point.class.getMethod("setLatitude", Double.class);
        Object[] setLatitudeMethodValues = {lat};
        Set<ConstraintViolation<Point>> violations = validator().forExecutables().validateParameters(point, setLatitudeMethod, setLatitudeMethodValues);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @Parameters(method = "invalidLongitudes")
    public void shouldFailValidationWhenLongitudOutOfBounds(double lng) throws Exception {
        Point point = new Point();
        Method setLongitudeMethod = Point.class.getMethod("setLongitude", Double.class);
        Object[] setLongitudeMethodValues = {lng};
        Set<ConstraintViolation<Point>> violations = validator().forExecutables().validateParameters(point, setLongitudeMethod, setLongitudeMethodValues);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void shouldCoordinatesBeNotNullWhenLatitudeIsSet() {
        Point point = new Point();
        assertThat(point.getCoordinates()).isNull();
        assertThat(point.getLatitude()).isNull();
        double lat = 19.3423;
        point.setLatitude(lat);
        assertThat(point.getCoordinates()).isNotNull();
        assertThat(point.getLatitude()).isNotNull().isEqualTo(lat);
    }

    @Test
    public void shouldCoordinatesBeNotNullWhenLongitudeIsSet() {
        Point point = new Point();
        assertThat(point.getCoordinates()).isNull();
        assertThat(point.getLongitude()).isNull();
        double lng = 103.754;
        point.setLongitude(lng);
        assertThat(point.getCoordinates()).isNotNull();
        assertThat(point.getLongitude()).isNotNull().isEqualTo(lng);
    }

    @Test
    public void shouldEqualsBeTrueWhenSameReference() {
        assertThat(VALID_POINT.equals(VALID_POINT)).isTrue();
    }

    @Test
    public void shouldEqualsBeTrueWhenObjectsAreNotTheSameReference() {
        Point testPoint = newPoint(VALID_POINT.getLatitude(), VALID_POINT.getLongitude());
        assertThat(VALID_POINT.equals(testPoint)).isTrue();
    }

    @Test
    public void shouldEqualsBeFalseWhenNull() {
        assertThat(VALID_POINT.equals(null)).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenComparingWithDifferentObject() {
        assertThat(VALID_POINT.equals(new PointTest())).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenAtLeastOnePropertyIsDifferent() {
        Point testPoint = newPoint(13.492, VALID_POINT.getLongitude());
        assertThat(VALID_POINT.equals(testPoint)).isFalse();

        testPoint = newPoint(VALID_POINT.getLatitude(), -52.276);
        assertThat(VALID_POINT.equals(testPoint)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        Point testPoint = newPoint(VALID_POINT.getLatitude(), VALID_POINT.getLongitude());
        assertThat(VALID_POINT.hashCode()).isEqualTo(VALID_POINT.hashCode()).isEqualTo(testPoint.hashCode());
    }

    protected Object[][] invalidLatitudes() {
        return new Object[][] {
            {INVALID_UPPER_LAT},
            {INVALID_LOWER_LAT}
        };
    }

    protected Object[][] invalidLongitudes() {
        return new Object[][] {
            {INVALID_UPPER_LNG},
            {INVALID_LOWER_LNG}
        };
    }

    private static Point newPoint(double latitude, double longitude) {
        Point point = new Point();
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        return point;
    }
}
