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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    @Parameters(method = "messagesWithoutArgs")
    public void shouldGetExpectedMessageWithNoExceptions(String messageKey, String expectedMessage) {
        String actualMessage = TEST_MESSAGES.getMessage(messageKey);
        assertThat(actualMessage).isNotNull().isNotEmpty().isEqualTo(expectedMessage);
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
    public void shouldGetMessageUsingArguments() {
        List<String> args = Arrays.asList("a1arg", "2");
        String actualMessage = TEST_MESSAGES.getMessage("test.message_with_args", args.get(0), args.get(1));
        assertThat(actualMessage).isNotNull().isNotEmpty().contains(args).isEqualTo("Test message with a1arg and 2");
    }

    @Test
    public void shouldGetLocalizedMessage() {
        String actualMessage = TEST_MESSAGES.getMessage("test.message", SPANISH);
        assertThat(actualMessage).isNotNull().isNotEmpty().isEqualTo("Mensage de prueba");
    }

    @Test
    public void shouldGetLocalizedMessageWithArgs() {
        List<String> args = Arrays.asList("a1arg", "2");
        String actualMessage = TEST_MESSAGES.getMessage("test.message_with_args", SPANISH, args.get(0), args.get(1));
        assertThat(actualMessage).isNotNull().isNotEmpty().contains(args).isEqualTo("Mensage de prueba con a1arg y 2");
    }

    @Test
    public void shouldGetRootLocalizedMessageWhenLocaleIsNotSupported() {
        String actualMessage = TEST_MESSAGES.getMessage("test.message", Locale.FRENCH);
        assertThat(actualMessage).isNotNull().isNotEmpty().isEqualTo("Test message");
    }

    @Test
    public void shouldGetRootLocalizedMessageWithArgsWhenLocaleIsNotSupported() {
        List<String> args = Arrays.asList("a1arg", "2");
        String actualMessage = TEST_MESSAGES.getMessage("test.message_with_args", Locale.FRENCH, args.get(0), args.get(1));
        assertThat(actualMessage).isNotNull().isNotEmpty().contains(args).isEqualTo("Test message with a1arg and 2");
    }

    protected Object[] messagesWithoutArgs() {
        String nonExitingMessage = "non_existing.message";
        return new Object[][]{
                {"test.message", "Test message"},
                {nonExitingMessage, String.format(MISSING_KEY_FORMAT, nonExitingMessage)}
        };
    }
}
