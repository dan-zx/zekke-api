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

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.beanutils.BeanUtils;

import org.junit.Test;

public class PathTest {

    private static final Path TEST_PATH = newTestPath();

    @Test
    public void shouldEqualsBeTrueWhenSameReference() {
        assertThat(TEST_PATH.equals(TEST_PATH)).isTrue();
    }

    @Test
    public void shouldEqualsBeTrueWhenObjectsAreNotTheSameReference() {
        assertThat(TEST_PATH.equals(newTestPath())).isTrue();
    }

    @Test
    public void shouldEqualsBeFalseWhenNull() {
        assertThat(TEST_PATH.equals(null)).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenComparingWithDifferentObject() {
        assertThat(TEST_PATH.equals(new PathTest())).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenAtLeastOnePropertyIsDifferent() {
        Path testPath2 = newTestPath();
        testPath2.setDistance(354.234);
        assertThat(TEST_PATH.equals(testPath2)).isFalse();

        copy(TEST_PATH, testPath2);
        testPath2.setFrom(newTestWaypoint1());
        assertThat(TEST_PATH.equals(testPath2)).isFalse();

        copy(TEST_PATH, testPath2);
        testPath2.setTo(newTestWaypoint2());
        assertThat(TEST_PATH.equals(testPath2)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        Path testPath2 = newTestPath();
        assertThat(TEST_PATH.hashCode()).isEqualTo(TEST_PATH.hashCode()).isEqualTo(testPath2.hashCode());
    }

    private static Path newTestPath() {
        Path testPath = new Path();
        testPath.setTo(newTestWaypoint1());
        testPath.setFrom(newTestWaypoint2());
        testPath.setDistance(100.4235);
        return testPath;
    }

    private static Waypoint newTestWaypoint1() {
        Waypoint testPoi = new Walkway();
        testPoi.setId(1);
        testPoi.setCoordinates(Coordinates.ofLatLng(19.054492, -98.283176));
        return testPoi;
    }

    private static Waypoint newTestWaypoint2() {
        Waypoint testPoi = new Walkway();
        testPoi.setId(2);
        testPoi.setCoordinates(Coordinates.ofLatLng(21.4235601, -101.546821));
        return testPoi;
    }

    private void copy(Path src, Path dest) {
        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
