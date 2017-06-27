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
package com.github.danzx.zekke.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.domain.User.Role;
import com.github.danzx.zekke.persistence.dao.UserDao;
import com.github.danzx.zekke.security.crypto.HashFunction;
import com.github.danzx.zekke.service.ServiceException;
import com.github.danzx.zekke.test.mockito.BaseMockitoValidationTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

@RunWith(JUnitParamsRunner.class)
public class UserServiceImplTest extends BaseMockitoValidationTest {

    private static final String HASHED_PASSWORD = "hashedpassword";
    private static final String HASHED_VAL = "hashedstring";
    private static final String PASSWORD_VAL = "password";

    private @Mock UserDao userDao;
    private @Mock HashFunction hashFunction;

    private @InjectMocks UserServiceImpl service;

    @Before
    public void setup() {
        when(hashFunction.hash(anyString())).thenReturn(HASHED_VAL);
        when(hashFunction.hash(PASSWORD_VAL)).thenReturn(HASHED_PASSWORD);
    }

    @Test
    public void isAdminRegisteredShouldThrowServiceExceptionWhenAdminUserIsNotFound() {
        when(userDao.findById(anyLong())).thenReturn(Optional.empty());

        User admin = newUser(Role.ADMIN, PASSWORD_VAL);
        assertThatThrownBy(() -> service.isAdminRegistered(admin)).isInstanceOf(ServiceException.class);
    }

    @Test
    public void isAdminRegisteredShouldThrowServiceExceptionWhenUserIsNotAdmin() {
        User user = newUser(Role.ANONYMOUS, null);
        assertThatThrownBy(() -> service.isAdminRegistered(user)).isInstanceOf(ServiceException.class);
    }

    @Test
    @Parameters(method = "usersAndResults")
    public void isAdminRegisteredShouldReturnExpectedWhenDifferentUsers(User user, boolean result) {
        User adminInDao = newUser(Role.ADMIN, HASHED_PASSWORD);
        when(userDao.findById(adminInDao.getId())).thenReturn(Optional.of(adminInDao));
        assertThat(service.isAdminRegistered(user)).isEqualTo(result);
    }

    @Test
    @Parameters(method = "invalidUsersAndViolations")
    public void isAdminRegisteredShouldFailValidation(User user, int numberOfViolations) throws Exception {
        Method method = UserServiceImpl.class.getMethod("isAdminRegistered", User.class);
        Object[] parameterValues = { user };
        Set<ConstraintViolation<UserServiceImpl>> violations = validator().forExecutables().validateParameters(
                service,
                method,
                parameterValues
        );
        assertThat(violations).isNotNull().isNotEmpty().hasSize(numberOfViolations);
    }

    private User newUser(Role role, String password) {
        User user = new User();
        if (role != null) {
            user.setId(role.getUserId());
            user.setRole(role);
        }
        if (password != null) user.setPassword(password);
        return user;
    }

    protected Object[][] usersAndResults() {
        return new Object[][] {
            {newUser(Role.ADMIN, PASSWORD_VAL), true},
            {newUser(Role.ADMIN, "anything"), false}
        };
    }

    protected Object[][] invalidUsersAndViolations() {
        return new Object[][] {
            {null, 1},
            {newUser(null, "anything"), 1},
            {newUser(Role.ANONYMOUS, "anything"), 1},
            {newUser(Role.ADMIN, null), 1}
        };
    }
}
