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

public class PoiTest {

    private static final Poi TEST_POI = newTestPoi();

    @Test
    public void shouldEqualsBeTrueWhenSameReference() {
        assertThat(TEST_POI.equals(TEST_POI)).isTrue();
    }

    @Test
    public void shouldEqualsBeTrueWhenObjectsAreNotTheSameReference() {
        assertThat(TEST_POI.equals(newTestPoi())).isTrue();
    }

    @Test
    public void shouldEqualsBeFalseWhenNull() {
        assertThat(TEST_POI.equals(null)).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenComparingWithDifferentObject() {
        assertThat(TEST_POI.equals(new PoiTest())).isFalse();
        Walkway testWalkway = new Walkway();
        copy(TEST_POI, testWalkway);
        assertThat(TEST_POI.equals(testWalkway)).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenAtLeastOnePropertyIsDifferent() {
        Poi testPoi2 = newTestPoi();
        testPoi2.setId(2);
        assertThat(TEST_POI.equals(testPoi2)).isFalse();

        copy(TEST_POI, testPoi2);
        testPoi2.setName("Other Name");
        assertThat(TEST_POI.equals(testPoi2)).isFalse();

        copy(TEST_POI, testPoi2);
        testPoi2.setCoordinates(Coordinates.ofLatLng(19.092, -98.831));
        assertThat(TEST_POI.equals(testPoi2)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        Poi testPoi2 = newTestPoi();
        assertThat(TEST_POI.hashCode()).isEqualTo(TEST_POI.hashCode()).isEqualTo(testPoi2.hashCode());
    }

    private static Poi newTestPoi() {
        Poi testPoi = new Poi();
        testPoi.setId(1);
        testPoi.setName("PoiTest");
        testPoi.setCoordinates(Coordinates.ofLatLng(19.054492, -98.283176));
        return testPoi;
    }

    private void copy(Waypoint src, Waypoint dest) {
        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
