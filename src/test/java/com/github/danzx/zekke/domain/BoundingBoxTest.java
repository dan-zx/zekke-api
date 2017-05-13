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

import org.junit.Test;

public class BoundingBoxTest {

    private static final BoundingBox BBOX_TESTEE = newBoundingBox();

    @Test
    public void shouldEqualsBeTrueWhenSameReference() {
        assertThat(BBOX_TESTEE.equals(BBOX_TESTEE)).isTrue();
    }

    @Test
    public void shouldEqualsBeTrueWhenObjectsAreNotTheSameReference() {
        assertThat(BBOX_TESTEE.equals(newBoundingBox())).isTrue();
    }

    @Test
    public void shouldEqualsBeFalseWhenNull() {
        assertThat(BBOX_TESTEE.equals(null)).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenComparingWithDifferentObject() {
        assertThat(BBOX_TESTEE.equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenAtLeastOnePropertyIsDifferent() {
        BoundingBox testBbox = new BoundingBox(BBOX_TESTEE.getBottomLeftCoordinates(), Coordinates.ofLatLng(35.89, -77.984));
        assertThat(BBOX_TESTEE.equals(testBbox)).isFalse();

        testBbox = new BoundingBox(Coordinates.ofLatLng(35.89, -77.984), BBOX_TESTEE.getUpperRightCoordinates());
        assertThat(BBOX_TESTEE.equals(testBbox)).isFalse();
        
        testBbox = new BoundingBox(Coordinates.ofLatLng(35.89, -77.984), Coordinates.ofLatLng(35.89, -77.984));
        assertThat(BBOX_TESTEE.equals(testBbox)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        BoundingBox testBbox = newBoundingBox();
        assertThat(BBOX_TESTEE.hashCode()).isEqualTo(BBOX_TESTEE.hashCode()).isEqualTo(testBbox.hashCode());
    }

    private static BoundingBox newBoundingBox() {
        return new BoundingBox(
                Coordinates.ofLatLng(19.054492, -98.283176), 
                Coordinates.ofLatLng(43.9876, -103.7564));
    }
}
