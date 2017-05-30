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
package com.github.danzx.zekke.message.impl;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.test.paramprovider.BlankStringProvider;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DefaultMessageDecoratedResourceBundleMessageSourceTest {

    private static final String MISSING_KEY_FORMAT = "XXX<%s>XXX";
    private static final MessageSource TEST_MESSAGES;
    private static final Locale SPANISH = new Locale.Builder().setLanguage("es").build();

    static {
        Set<Locale> supportedLocales = new HashSet<>(Arrays.asList(Locale.ROOT, SPANISH));
        DefaultMessageDecorator messageSource = new DefaultMessageDecorator(new ResourceBundleMessageSource("messages.TestMessages", supportedLocales));
        messageSource.setMissingKeyFormat(MISSING_KEY_FORMAT);
        TEST_MESSAGES = messageSource;
    }

    @Test
    @Parameters(method = "constructorArgs")
    public void shouldBuildResourceBundleMessageSource(String basename, Set<Locale> locales) {
        ResourceBundleMessageSource messageSource = locales == null ? new ResourceBundleMessageSource(basename) : new ResourceBundleMessageSource(basename, locales);
        assertThat(messageSource.getBasename()).isNotNull().isNotBlank().isEqualTo(basename);
        assertThat(messageSource.getSupportedLocales()).isNotNull().isNotEmpty().containsOnly(Locale.ROOT);
    }

    @Test
    public void shouldThrowNullPointExceptionWhenKeyIsNull() {
        assertThatThrownBy(() -> TEST_MESSAGES.getMessage(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldThrowIllegalArgumentExceptionWhenKeyIsBlank(String blankKey) {
        assertThatThrownBy(() -> TEST_MESSAGES.getMessage(blankKey)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenLocaleIsNull() {
        assertThatThrownBy(() -> TEST_MESSAGES.getMessage("whatever", (Locale) null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Parameters(method = "messagesWithoutArgs")
    public void shouldGetExpectedMessage(String messageKey, Locale locale, String expectedMessage) {
        String actualMessage = locale == null ? TEST_MESSAGES.getMessage(messageKey) : TEST_MESSAGES.getMessage(messageKey, locale);
        assertThat(actualMessage).isNotNull().isNotEmpty().isEqualTo(expectedMessage);
    }

    @Test
    @Parameters(method = "messagesWithArgs")
    public void shouldGetMessageUsingArguments(String messageKey, Locale locale, Object[] args, String expectedMessage) {
        String actualMessage = locale == null ? TEST_MESSAGES.getMessage(messageKey, args) : TEST_MESSAGES.getMessage(messageKey, locale, args);
        assertThat(actualMessage).isNotNull().isNotEmpty().isEqualTo(expectedMessage);
    }

    protected Object[] constructorArgs() {
        return new Object[][] {
            {"messages.TestMessages", null},
            {"messages.TestMessages", emptySet()},
            {"messages.TestMessages", singleton(Locale.ROOT)},
        };
    }

    protected Object[] messagesWithoutArgs() {
        String nonExitingMessage = "non_existing.message";
        String expectedMessageWithNonExitingKey = String.format(MISSING_KEY_FORMAT, nonExitingMessage);
        return new Object[][] {
            {"test.message", null, "Test message"},
            {nonExitingMessage, null, expectedMessageWithNonExitingKey},
            {"test.message", Locale.FRENCH, "Test message"},
            {nonExitingMessage, Locale.FRENCH, expectedMessageWithNonExitingKey},
            {"test.message", SPANISH, "Mensage de prueba"},
            {nonExitingMessage, SPANISH, expectedMessageWithNonExitingKey}
        };
    }

    protected Object[] messagesWithArgs() {
        String nonExitingMessage = "non_existing.message";
        String expectedMessageWithNonExitingKey = String.format(MISSING_KEY_FORMAT, nonExitingMessage);
        Object[] args = {"a1arg", "2"};
        Object[] arg = {"lol"};
        return new Object[][] {
            {"test.message_with_arg", null, arg, "Test message with lol"},
            {nonExitingMessage, null, null, expectedMessageWithNonExitingKey},
            {"test.message_with_arg", Locale.FRENCH, arg, "Test message with lol"},
            {nonExitingMessage, Locale.FRENCH, null, expectedMessageWithNonExitingKey},
            {"test.message_with_arg", SPANISH, arg, "Mensage de prueba con lol"},
            {nonExitingMessage, SPANISH, null, expectedMessageWithNonExitingKey},
            {"test.message_with_args", null, args, "Test message with a1arg and 2"},
            {nonExitingMessage, null, null, expectedMessageWithNonExitingKey},
            {"test.message_with_args", Locale.FRENCH, args, "Test message with a1arg and 2"},
            {nonExitingMessage, Locale.FRENCH, null, expectedMessageWithNonExitingKey},
            {"test.message_with_args", SPANISH, args, "Mensage de prueba con a1arg y 2"},
            {nonExitingMessage, SPANISH, null, expectedMessageWithNonExitingKey}
        };
    }
}
