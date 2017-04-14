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
package com.github.danzx.zekke.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import com.github.danzx.zekke.test.paramprovider.BlankStringProvider;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AppExceptionTest {

    @Test
    public void shouldHaveNoCauseNorMessage() {
        assertThat(new TestAppException.Builder().build()).isNotNull().hasNoCause().hasMessage(null).extracting("messageKey", "messageArgs").containsExactly(Optional.empty(), null);
    }

    @Test
    public void shouldHaveMessage() {
        Optional<String> messageKey = Optional.of("test.message");
        assertThat(new TestAppException.Builder()
                .messageKey(messageKey.get())
                .build())
                .isNotNull().hasMessage("Test message").hasNoCause().extracting("messageKey", "messageArgs").containsExactly(messageKey, null);
    }

    @Test
    public void shouldHaveMessageWithArg() {
        Optional<String> messageKey = Optional.of("test.message_with_arg");
        Object[] args = {5};
        assertThat(new TestAppException.Builder()
                .messageKey(messageKey.get())
                .messageArgs(args[0])
                .build())
                .isNotNull().hasMessage("Test message with 5").hasNoCause().extracting("messageKey", "messageArgs").containsExactly(messageKey, args);
    }

    @Test
    public void shouldHaveHardcodedMessage() {
        String message = "A message";
        assertThat(new TestAppException.Builder()
                .message(message)
                .build())
                .isNotNull().hasMessage(message).hasNoCause().extracting("messageKey", "messageArgs").containsExactly(Optional.empty(), null);
    }

    @Test
    public void shouldHaveCause() {
        assertThat(new TestAppException.Builder()
                .cause(new NullPointerException())
                .build())
                .isNotNull().hasMessage(null).hasCauseExactlyInstanceOf(NullPointerException.class).extracting("messageKey", "messageArgs").containsExactly(Optional.empty(), null);
    }

    @Test
    public void shouldHaveMessageWithArgsAndCause() {
        Optional<String> messageKey = Optional.of("test.message_with_args");
        Object[] args = {5, "other"};
        assertThat(new TestAppException.Builder()
                .cause(new NullPointerException())
                .messageKey(messageKey.get())
                .messageArgs(args[0], args[1])
                .build())
                .isNotNull().hasMessage("Test message with 5 and other").hasCauseExactlyInstanceOf(NullPointerException.class).extracting("messageKey", "messageArgs").containsExactly(messageKey, args);
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldFailValidationWhenMessageKeyIsBlank(String blankMessageKey) {
        assertThatThrownBy(() -> new TestAppException.Builder().messageKey(blankMessageKey)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenMessageKeyIsNull() {
        assertThatThrownBy(() -> new TestAppException.Builder().messageKey(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldThrowIllegalArgumentExceptionWhenMessageIsBlank(String blankMessage) {
        assertThatThrownBy(() -> new TestAppException.Builder().message(blankMessage)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        assertThatThrownBy(() -> new TestAppException.Builder().message(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenLocaleIsNull() {
        TestAppException ex = new TestAppException.Builder().build();
        assertThatThrownBy(() -> ex.getMessage(null)).isInstanceOf(NullPointerException.class);
    }

    private static class TestAppException extends AppException {

        private static final long serialVersionUID = 1L;

        private TestAppException(Builder builder) {
            super(builder);
        }

        private static class Builder extends BaseAppExceptionBuilder<TestAppException> {

            @Override
            public TestAppException build() {
                return new TestAppException(this);
            }
        }
    }
}
