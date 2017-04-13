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
package com.github.danzx.zekke.persistence.morphia;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.beanutils.BeanUtils;

import org.junit.Test;

public class MorphiaPathTest {

    private static final MorphiaPath TEST_PATH = newTestPath();

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
        assertThat(TEST_PATH.equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenAtLeastOnePropertyIsDifferent() {
        MorphiaPath testPath2 = newTestPath();
        testPath2.setDistance(354.234);
        assertThat(TEST_PATH.equals(testPath2)).isFalse();

        copy(TEST_PATH, testPath2);
        testPath2.setToWaypoint(3L);
        assertThat(TEST_PATH.equals(testPath2)).isFalse();
        
        copy(TEST_PATH, testPath2);
        testPath2.setFromWaypoint(3L);
        assertThat(TEST_PATH.equals(testPath2)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        MorphiaPath testPath2 = newTestPath();
        assertThat(TEST_PATH.hashCode()).isEqualTo(TEST_PATH.hashCode()).isEqualTo(testPath2.hashCode());
    }

    private static MorphiaPath newTestPath() {
        MorphiaPath testPath = new MorphiaPath();
        testPath.setFromWaypoint(1L);
        testPath.setToWaypoint(9L);
        testPath.setDistance(100.4235);
        return testPath;
    }

    private void copy(MorphiaPath src, MorphiaPath dest) {
        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
