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

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class FloatRangeValidatorTest {

    private static final FloatRangeValidator TEST_VALIDATOR = testValidator();

    @Test
    public void shouldValididationPassIfValueIsNull() {
        assertThat(TEST_VALIDATOR.isValid(null, null)).isTrue();
    }

    @Test
    @Parameters(method = "inRangeFloats")
    public void shouldValididationPassIfValueIsInRange(float value) {
        assertThat(TEST_VALIDATOR.isValid(value, null)).isTrue();
    }

    @Test
    @Parameters(method = "outOfRangeFloats")
    public void shouldValididationFailIfValueIsOutOfRange(float value) {
        assertThat(TEST_VALIDATOR.isValid(value, null)).isFalse();
    }

    protected Object[] inRangeFloats() {
        return new Object[][] {
            {-5f},
            {-4f},
            {0f},
            {9f},
            {10f}
        };
    }

    protected Object[] outOfRangeFloats() {
        return new Object[][] {
            {-6f},
            {11f}
        };
    }

    private static FloatRangeValidator testValidator() {
        FloatRangeValidator validator = new FloatRangeValidator();
        validator.init(-5, 10);
        return validator;
    }
}
