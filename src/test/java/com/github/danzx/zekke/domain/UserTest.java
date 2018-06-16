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

import java.util.Arrays;

import com.github.danzx.zekke.domain.User.Role;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.apache.commons.beanutils.BeanUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class UserTest {

    private static final User TEST_USER = newTestUser();

    @Test
    public void shouldEqualsBeTrueWhenSameReference() {
        assertThat(TEST_USER.equals(TEST_USER)).isTrue();
    }

    @Test
    public void shouldEqualsBeTrueWhenObjectsAreNotTheSameReference() {
        assertThat(TEST_USER.equals(newTestUser())).isTrue();
    }

    @Test
    public void shouldEqualsBeFalseWhenNull() {
        assertThat(TEST_USER.equals(null)).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenComparingWithDifferentObject() {
        assertThat(TEST_USER.equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualsBeFalseWhenAtLeastOnePropertyIsDifferent() {
        User user2 = newTestUser();
        user2.setId(2L);
        assertThat(TEST_USER.equals(user2)).isFalse();

        copy(TEST_USER, user2);
        user2.setRole(Role.ANONYMOUS);
        assertThat(TEST_USER.equals(user2)).isFalse();

        copy(TEST_USER, user2);
        user2.setPassword("otherPassword");
        assertThat(TEST_USER.equals(user2)).isFalse();
    }

    @Test
    public void shouldHashCodeBeEqualWhenSameObjectReference() {
        User user2 = newTestUser();
        assertThat(TEST_USER.hashCode()).isEqualTo(TEST_USER.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @Parameters(method = "equalAuthorityRolesTest")
    public void shouldRoleHasEqualAuthorityThanThanReturnBoolean(Role role1, Role role2, boolean expected) {
        assertThat(role1.hasEqualAuthorityThan(role2)).isEqualTo(expected);
    }

    @Test
    @Parameters(method = "greaterAuthorityRolesTest")
    public void shouldRoleHasMoreAuthorityThanReturnBoolean(Role role1, Role role2, boolean expected) {
        assertThat(role1.hasMoreAuthorityThan(role2)).isEqualTo(expected);
    }

    @Test
    @Parameters(method = "greaterAuthorityRolesTest")
    public void shouldRoleHasLessAuthorityThanReturnBoolean(Role role1, Role role2, boolean expected) {
        assertThat(role1.hasLessAuthorityThan(role2)).isEqualTo(!expected);
    }

    @Test
    @Parameters(method = "greaterOrEqualAuthorityRolesTest")
    public void shouldRoleHasMoreOrEqualAuthorityThanReturnBoolean(Role role1, Role role2, boolean expected) {
        assertThat(role1.hasMoreOrEqualAuthorityThan(role2)).isEqualTo(expected);
    }

    @Test
    @Parameters(method = "lessOrEqualAuthorityRolesTest")
    public void shouldRoleHasLessOrEqualAuthorityThanReturnBoolean(Role role1, Role role2, boolean expected) {
        assertThat(role1.hasLessOrEqualAuthorityThan(role2)).isEqualTo(expected);
    }

    @Test
    @Parameters(method = "rolesAndUsernames")
    public void shouldRoleFromUsernameReturnRole(Role expectedRole, String username) {
        assertThat(Role.fromUsername(username)).isEqualTo(expectedRole);
    }

    @Test
    public void shouldRoleFromUsernameThrowIllegalArgumentException() {
        assertThatThrownBy(() -> Role.fromUsername("unknown")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldRoleFromUsernameThrowNullPointerException() {
        assertThatThrownBy(() -> Role.fromUsername(null)).isInstanceOf(NullPointerException.class);
    }

    public Object[] rolesAndUsernames() {
        return Arrays.stream(Role.values())
                .map(role -> new Object[] {role, role.name().toLowerCase()})
                .toArray();
    }

    public Object[] greaterOrEqualAuthorityRolesTest() {
        return new Object[][] {
            {Role.ANONYMOUS, Role.ANONYMOUS, true},
            {Role.ADMIN, Role.ADMIN, true},
            {Role.ANONYMOUS, Role.ADMIN, false},
            {Role.ADMIN, Role.ANONYMOUS, true},
            {Role.ANONYMOUS, null, true},
            {Role.ADMIN, null, true},
            {Role.ANONYMOUS, Role.ADMIN, false},
        };
    }

    public Object[] lessOrEqualAuthorityRolesTest() {
        return new Object[][] {
            {Role.ANONYMOUS, Role.ANONYMOUS, true},
            {Role.ADMIN, Role.ADMIN, true},
            {Role.ANONYMOUS, Role.ADMIN, true},
            {Role.ADMIN, Role.ANONYMOUS, false},
            {Role.ANONYMOUS, null, false},
            {Role.ADMIN, null, false},
            {Role.ANONYMOUS, Role.ADMIN, true},
        };
    }

    public Object[] equalAuthorityRolesTest() {
        return new Object[][] {
            {Role.ANONYMOUS, Role.ANONYMOUS, true},
            {Role.ADMIN, Role.ADMIN, true},
            {Role.ADMIN, null, false},
            {Role.ANONYMOUS, null, false},
            {Role.ADMIN, Role.ANONYMOUS, false},
            {Role.ANONYMOUS, Role.ADMIN, false}
        };
    }

    public Object[] greaterAuthorityRolesTest() {
        return new Object[][] {
            {Role.ADMIN, Role.ANONYMOUS, true},
            {Role.ANONYMOUS, null, true},
            {Role.ADMIN, null, true},
            {Role.ANONYMOUS, Role.ADMIN, false},
        };
    }

    private static User newTestUser() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setRole(Role.ADMIN);
        testUser.setPassword("password");
        return testUser;
    }

    private void copy(User src, User dest) {
        try {
            BeanUtils.copyProperties(dest, src);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
