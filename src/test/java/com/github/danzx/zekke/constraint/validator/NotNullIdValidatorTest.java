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

import javax.validation.ConstraintValidatorContext;

import com.github.danzx.zekke.test.mockito.BaseMockitoTest;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;

@RunWith(JUnitParamsRunner.class)
public class NotNullIdValidatorTest extends BaseMockitoTest {

    private static final NotNullIdValidator TEST_VALIDATOR = new NotNullIdValidator();

    private @Mock(answer = Answers.RETURNS_MOCKS) ConstraintValidatorContext mockContext;

    @Test
    @Parameters(method = "testObjsAndResult")
    public void shouldValidatedObjectWithId(Object obj, boolean expectedResult) {
        assertThat(TEST_VALIDATOR.isValid(obj, mockContext)).isEqualTo(expectedResult);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenObjectHasNoIdGetter() {
        assertThatThrownBy(() -> TEST_VALIDATOR.isValid(new Object(), mockContext)).isInstanceOf(IllegalArgumentException.class);
    }

    protected Object[] testObjsAndResult() {
        return new Object[][] {
            {new TestObj(null), false},
            {new TestObj(1L), true}
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
