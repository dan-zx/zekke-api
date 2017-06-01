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
package com.github.danzx.zekke.ws.rest.requestfilter;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

import java.util.Locale;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import com.github.danzx.zekke.message.LocaleHolder;
import com.github.danzx.zekke.test.mockito.BaseMockitoTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;

public class ClientLocaleHolderIntegrationTest extends BaseMockitoTest {

    private @Mock HttpHeaders headers;
    private @Mock ContainerRequestContext requestContext;

    private @InjectMocks ClientLocaleHolderListener listener;

    @Before
    public void setUp() {
        LocaleHolder.unset();
    }

    @After
    public void cleanUp() {
        LocaleHolder.unset();
    }

    @Test
    public void shouldSetLocaleInHolder() throws Exception {
        final Locale french = Locale.FRENCH;
        when(headers.getAcceptableLanguages()).thenReturn(singletonList(french));

        listener.filter(requestContext);
        assertThat(LocaleHolder.get()).isNotNull().isEqualTo(french);
    }

    @Test
    public void shouldNotSetLocaleInHolder() throws Exception {
        when(headers.getAcceptableLanguages()).thenReturn(emptyList());

        listener.filter(requestContext);
        assertThat(LocaleHolder.get()).isNotNull().isEqualTo(Locale.ROOT);
    }
}
