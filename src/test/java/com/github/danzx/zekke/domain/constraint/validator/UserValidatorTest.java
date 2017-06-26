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
package com.github.danzx.zekke.domain.constraint.validator;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintValidatorContext;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.test.mockito.BaseMockitoTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Answers;
import org.mockito.Mock;

@RunWith(JUnitParamsRunner.class)
public class UserValidatorTest extends BaseMockitoTest {

    private static final UserValidator TEST_VALIDATOR = new UserValidator();

    private @Mock(answer = Answers.RETURNS_MOCKS) ConstraintValidatorContext mockContext;

    @Test
    @Parameters(method = "testUsers")
    public void shouldValidateUser(User user, boolean expectedResult) {
        assertThat(TEST_VALIDATOR.isValid(user, mockContext)).isEqualTo(expectedResult);
    }

    protected Object[][] testUsers() {
        User u1 = new User();
        u1.setRole(User.Role.ADMIN);
        u1.setPassword("password");

        User u2 = new User();
        u2.setRole(User.Role.ANONYMOUS);
        
        User u3 = new User();
        u3.setRole(User.Role.ADMIN);
        
        User u4 = new User();
        u4.setRole(User.Role.ANONYMOUS);
        u4.setPassword("password");

        User u5 = new User();

        return new Object[][] {
            {null, true},
            {u1, true},
            {u2, true},
            {u5, true},
            {u3, false},
            {u4, false}
        };
    }
}
