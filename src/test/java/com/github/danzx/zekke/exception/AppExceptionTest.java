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

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.danzx.zekke.test.BaseValidationTest;
import com.github.danzx.zekke.test.paramprovider.BlankStringProvider;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class AppExceptionTest extends BaseValidationTest {

    @Test
    public void shouldHaveNoCauseNorMessage() {
        assertThat(new AppException.Builder().build()).isNotNull().hasNoCause().hasMessage(null).extracting("messageKey", "messageArgs").containsExactly(Optional.empty(), null);
    }

    @Test
    public void shouldHaveMessage() {
        Optional<String> messageKey = Optional.of("test.message");
        assertThat(new AppException.Builder()
                .messageKey(messageKey.get())
                .build())
                .isNotNull().hasMessage("Test message").hasNoCause().extracting("messageKey", "messageArgs").containsExactly(messageKey, null);
    }

    @Test
    public void shouldHaveMessageWithArg() {
        Optional<String> messageKey = Optional.of("test.message_with_arg");
        Object[] args = {5};
        assertThat(new AppException.Builder()
                .messageKey(messageKey.get())
                .messageArgs(args[0])
                .build())
                .isNotNull().hasMessage("Test message with 5").hasNoCause().extracting("messageKey", "messageArgs").containsExactly(messageKey, args);
    }

    @Test
    public void shouldHaveHardcodedMessage() {
        String message = "A message";
        assertThat(new AppException.Builder()
                .message(message)
                .build())
                .isNotNull().hasMessage(message).hasNoCause().extracting("messageKey", "messageArgs").containsExactly(Optional.empty(), null);
    }

    @Test
    public void shouldHaveCause() {
        assertThat(new AppException.Builder()
                .cause(new NullPointerException())
                .build())
                .isNotNull().hasMessage(null).hasCauseExactlyInstanceOf(NullPointerException.class).extracting("messageKey", "messageArgs").containsExactly(Optional.empty(), null);
    }

    @Test
    public void shouldHaveMessageWithArgsAndCause() {
        Optional<String> messageKey = Optional.of("test.message_with_args");
        Object[] args = {5, "other"};
        assertThat(new AppException.Builder()
                .cause(new NullPointerException())
                .messageKey(messageKey.get())
                .messageArgs(args[0], args[1])
                .build())
                .isNotNull().hasMessage("Test message with 5 and other").hasCauseExactlyInstanceOf(NullPointerException.class).extracting("messageKey", "messageArgs").containsExactly(messageKey, args);
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldFailValidationWhenMessageKeyIsBlank(String blankMessageKey) throws Exception {
        AppException.Builder object = new AppException.Builder();
        Method method = AppException.Builder.class.getMethod("messageKey", String.class );
        Object[] parameterValues = { blankMessageKey };
        Set<ConstraintViolation<AppException.Builder>> violations = validator().forExecutables().validateParameters(object, method, parameterValues);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldFailValidationWhenMessageIsBlank(String blankMessage) throws Exception {
        AppException.Builder object = new AppException.Builder();
        Method method = AppException.Builder.class.getMethod("message", String.class );
        Object[] parameterValues = { blankMessage };
        Set<ConstraintViolation<AppException.Builder>> violations = validator().forExecutables().validateParameters(object, method, parameterValues);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void shouldFailValidationWhenLocaleIsNull() throws Exception {
        AppException object = new AppException.Builder().build();
        Method method = AppException.class.getMethod("getMessage", Locale.class );
        Object[] parameterValues = { null };
        Set<ConstraintViolation<AppException>> violations = validator().forExecutables().validateParameters(object, method, parameterValues);
        assertThat(violations).isNotEmpty();
    }

    private static class AppException extends BaseAppException {

        private static final long serialVersionUID = 1L;

        private AppException(Builder builder) {
            super(builder);
        }

        private static class Builder extends BaseAppExceptionBuilder<AppException> {

            @Override
            public AppException build() {
                return new AppException(this);
            }
        }
    }
}
