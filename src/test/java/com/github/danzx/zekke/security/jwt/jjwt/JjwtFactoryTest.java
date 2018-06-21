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
package com.github.danzx.zekke.security.jwt.jjwt;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.security.jwt.JwtSettings;
import com.github.danzx.zekke.test.spring.BaseSpringTest;

import org.junit.Before;
import org.junit.Test;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@ContextConfiguration(classes = JwtSettings.class)
@TestPropertySource(locations = "classpath:test.properties")
public class JjwtFactoryTest extends BaseSpringTest {

    private static final String JWT_PATTERN = ".+\\..+\\..+";

    private @Inject JwtSettings jwtSettings;

    @Before
    public void setUp() {
        assertThat(jwtSettings).isNotNull();
    }

    @Test
    public void shouldCreateToken() {
        JjwtFactory jwtFactory = new JjwtFactory(jwtSettings);
        String token = jwtFactory.newToken(User.Role.ANONYMOUS);
        assertThat(token).isNotNull().isNotBlank().matches(JWT_PATTERN);
    }
}
