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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        BoundingBox testBbox = BoundingBox.ofBottomTop(BBOX_TESTEE.getBottomCoordinates(), Coordinates.ofLatLng(35.89, -77.984));
        assertThat(BBOX_TESTEE.equals(testBbox)).isFalse();

        testBbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(35.89, -77.984), BBOX_TESTEE.getTopCoordinates());
        assertThat(BBOX_TESTEE.equals(testBbox)).isFalse();
        
        testBbox = BoundingBox.ofBottomTop(Coordinates.ofLatLng(35.89, -77.984), Coordinates.ofLatLng(35.89, -77.984));
        assertThat(BBOX_TESTEE.equals(testBbox)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        BoundingBox testBbox = newBoundingBox();
        assertThat(BBOX_TESTEE.hashCode()).isEqualTo(BBOX_TESTEE.hashCode()).isEqualTo(testBbox.hashCode());
    }

    @Test
    public void shouldConvertToAndFromString() {
        assertThat(BoundingBox.fromString(BBOX_TESTEE.toString())).isNotNull().isEqualTo(BBOX_TESTEE);
    }

    @Test
    public void shouldFromStringReturnNullWhenNull() {
        assertThat(BoundingBox.fromString(null)).isNull();
    }

    @Test
    public void shouldFromStringThrowIllegalArgumentExceptionWhenValidIsNotValid() {
        assertThatThrownBy(() -> BoundingBox.fromString("sdfsdf")).isInstanceOf(IllegalArgumentException.class);
    }

    private static BoundingBox newBoundingBox() {
        return BoundingBox.ofBottomTop(
                Coordinates.ofLatLng(19.054492, -98.283176), 
                Coordinates.ofLatLng(43.9876, -103.7564));
    }
}
