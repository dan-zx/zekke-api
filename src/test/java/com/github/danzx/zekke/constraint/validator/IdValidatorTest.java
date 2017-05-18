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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class IdValidatorTest {

    @Test
    @Parameters(method = "testObjsAndResult")
    public void shouldValidatedObjectWithId(boolean shouldBeNull, Object obj, boolean expectedResult) {
        IdValidator validator = new IdValidator();
        validator.init(shouldBeNull);
        assertThat(validator.isValid(obj, null)).isEqualTo(expectedResult);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenObjectHasNoIdGetter() {
        IdValidator validator = new IdValidator();
        validator.init(true);
        assertThatThrownBy(() -> validator.isValid(new Object(), null)).isInstanceOf(IllegalArgumentException.class);
    }

    protected Object[] testObjsAndResult() {
        return new Object[][] {
            {true, new TestObj(null), true},
            {true, new TestObj(1L), false},
            {false, new TestObj(null), false},
            {false, new TestObj(1L), true}
        };
    }

    public static class TestObj {
        
        private Long id;

        public TestObj(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }
}
