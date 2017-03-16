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
package com.github.danzx.zekke.util;

import static com.github.danzx.zekke.util.Strings.allCapsToPascalCase;
import static com.github.danzx.zekke.util.Strings.isBlank;
import static com.github.danzx.zekke.util.Strings.quoted;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.danzx.zekke.test.paramprovider.BlankStringProvider;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class StringsTest {

    @Test
    public void shouldReturnFalseWithNonBlankStrings() {
        assertThat(isBlank("\nnon blank ")).isFalse();
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldReturnTrueWithBlankStrings(String blankString) {
        assertThat(isBlank(blankString)).isTrue();
    }

    @Test
    @Parameters(method = "allCapsStrings")
    public void shouldConvertAllCapsToPascalCase(String allCaps) {
        assertThat(allCapsToPascalCase(allCaps)).isNotBlank().isEqualTo("AllCaps");
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldNotConvertAllCapsToPascalCaseWhenBlankIsGiven(String blankString) {
        assertThat(allCapsToPascalCase(blankString)).isEqualTo(blankString);
    }

    @Test
    public void shouldQuote() {
        assertThat(quoted("lol")).isNotBlank().isEqualTo("\"lol\"");
    }

    @Test
    public void shouldNotQuoteWhenNullIsGiven() {
        assertThat(quoted(null)).isNull();
    }

    protected Object[] allCapsStrings() {
        return new Object[][] {
            {"ALL_CAPS"},
            {"ALL__CAPS"},
            {"_ALL_CAPS"},
            {"ALL_CAPS_"}
        };
    }
}
