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
package com.github.danzx.zekke.constraint.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class DoubleRangeValidatorTest {

    private static final DoubleRangeValidator TEST_VALIDATOR = testValidator();

    @Test
    public void shouldValididationPassIfValueIsNull() {
        assertThat(TEST_VALIDATOR.isValid(null, null)).isTrue();
    }

    @Test
    @Parameters(method = "inRangeDoubles")
    public void shouldValididationPassIfValueIsInRange(double value) {
        assertThat(TEST_VALIDATOR.isValid(value, null)).isTrue();
    }

    @Test
    @Parameters(method = "outOfRangeDoubles")
    public void shouldValididationFailIfValueIsOutOfRange(double value) {
        assertThat(TEST_VALIDATOR.isValid(value, null)).isFalse();
    }

    protected Object[] inRangeDoubles() {
        return new Object[][] {
            {-5d},
            {-4d},
            {0d},
            {9d},
            {10d}
        };
    }

    protected Object[] outOfRangeDoubles() {
        return new Object[][] {
            {-6d},
            {11d}
        };
    }

    private static DoubleRangeValidator testValidator() {
        DoubleRangeValidator validator = new DoubleRangeValidator();
        validator.init(-5, 10);
        return validator;
    }
}
