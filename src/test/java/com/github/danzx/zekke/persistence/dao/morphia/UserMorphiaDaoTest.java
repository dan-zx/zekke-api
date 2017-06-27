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
package com.github.danzx.zekke.persistence.dao.morphia;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.test.mongo.BaseSpringMongoTest;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class UserMorphiaDaoTest extends BaseSpringMongoTest {

    @Inject private UserMorphiaDao userMorphiaDao;

    @Override
    public void before() throws Exception {
        super.before();
        assertThat(userMorphiaDao).isNotNull();
        assertThat(userMorphiaDao.getDatastore()).isNotNull();
    }

    @Test
    @Parameters(method = "idsAndResults")
    public void shouldFindByIdReturnNonNullOptional(long id, Optional<User> result) {
        Optional<User> optUser = userMorphiaDao.findById(id);
        assertThat(optUser).isNotNull();
        assertThat(optUser.isPresent()).isEqualTo(result.isPresent());
        optUser.ifPresent(user -> assertThat(user).isNotNull().isEqualTo(result.get()));
    }

    protected Object[] idsAndResults() {
        User anonymousUser = new User();
        anonymousUser.setId(1L);
        anonymousUser.setRole(User.Role.ANONYMOUS);

        User adminUser = new User();
        adminUser.setId(2L);
        adminUser.setRole(User.Role.ADMIN);
        adminUser.setPassword("n4bQgYhMfWWaL+qgxVrQFaO/TxsrC4Is0V1sFbDwCgg=");

        return new Object[][] {
            {anonymousUser.getId(), Optional.of(anonymousUser)},
            {adminUser.getId(), Optional.of(adminUser)},
            {3L, Optional.empty()}
        };
    }
}
