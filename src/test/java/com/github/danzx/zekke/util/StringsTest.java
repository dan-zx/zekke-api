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

import static com.github.danzx.zekke.util.Strings.BLANK_SPACE;
import static com.github.danzx.zekke.util.Strings.EMPTY;
import static com.github.danzx.zekke.util.Strings.NEW_LINE;
import static com.github.danzx.zekke.util.Strings.TAB;
import static com.github.danzx.zekke.util.Strings.allCapsToCamelCase;
import static com.github.danzx.zekke.util.Strings.isNullOrBlank;
import static com.github.danzx.zekke.util.Strings.quoted;
import static com.github.danzx.zekke.util.Strings.requireNonBlank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.danzx.zekke.test.paramprovider.BlankStringProvider;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class StringsTest {

    @Test
    public void shouldReturnFalseWithNonBlankStrings() {
        assertThat(isNullOrBlank("\nnon blank ")).isFalse();
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldReturnTrueWithBlankStrings(String blankString) {
        assertThat(isNullOrBlank(blankString)).isTrue();
    }

    @Test
    public void shouldReturnTrueWithNull() {
        assertThat(isNullOrBlank(null)).isTrue();
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldThrowIllegalArgumentExceptionWhenRequireNonBlank(String blankString) {
        assertThatThrownBy(() -> requireNonBlank(blankString, "message")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenRequireNonNull() {
        assertThatThrownBy(() -> requireNonBlank(null, "message")).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Parameters(method = "allCapsStrings")
    public void shouldConvertAllCapsToPascalCase(String allCaps) {
        assertThat(allCapsToCamelCase(allCaps, true)).isNotBlank().isEqualTo("AllCaps");
    }

    @Test
    @Parameters(method = "allCapsStrings")
    public void shouldConvertAllCapsToCamelCase(String allCaps) {
        assertThat(allCapsToCamelCase(allCaps, false)).isNotBlank().isEqualTo("allCaps");
    }

    @Test
    @Parameters(method = "blankStringsAndCasing")
    public void shouldNotConvertAllCapsToFormatWhenBlankIsGiven(String blankString, boolean isUpperCamelCase) {
        assertThat(allCapsToCamelCase(blankString, isUpperCamelCase)).isEqualTo(blankString);
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

    public static Object[] blankStringsAndCasing() {
        return new Object[][]{
            {EMPTY, false},
            {BLANK_SPACE, false},
            {NEW_LINE, false},
            {TAB, false},
            {EMPTY, true},
            {BLANK_SPACE, true},
            {NEW_LINE, true},
            {TAB, true},
        };
    }
}
