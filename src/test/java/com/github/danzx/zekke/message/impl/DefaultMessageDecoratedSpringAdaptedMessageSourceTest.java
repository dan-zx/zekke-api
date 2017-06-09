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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import com.github.danzx.zekke.message.MessageSource;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.context.support.ResourceBundleMessageSource;

@RunWith(JUnitParamsRunner.class)
public class DefaultMessageDecoratedSpringAdaptedMessageSourceTest {

    private static final String MISSING_KEY_FORMAT = "XXX<%s>XXX";
    private static final MessageSource TEST_MESSAGES;
    private static final Locale SPANISH = new Locale.Builder().setLanguage("es").build();

    static {
        ResourceBundleMessageSource springMessageSource = new ResourceBundleMessageSource();
        springMessageSource.setBasename("messages.TestMessages");
        springMessageSource.setFallbackToSystemLocale(false);
        DefaultMessageDecorator messageSource = new DefaultMessageDecorator(new SpringMessageSourceAdapter(springMessageSource));
        messageSource.setMissingKeyFormat(MISSING_KEY_FORMAT);
        TEST_MESSAGES = messageSource;
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
