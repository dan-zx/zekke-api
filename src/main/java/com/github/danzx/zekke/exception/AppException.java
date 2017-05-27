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

import static com.github.danzx.zekke.util.Strings.requireNonBlank;
import static java.util.Objects.requireNonNull;

import java.util.Locale;
import java.util.Optional;

import com.github.danzx.zekke.base.Buildable;
import com.github.danzx.zekke.message.MessageSource;
import com.github.danzx.zekke.message.impl.MessageSourceFactory;

/**
 * Base application exception.
 *
 * @author Daniel Pedraza-Arcega
 */
public abstract class AppException extends RuntimeException {

    private static final long serialVersionUID = -298618049284453181L;

    private final Optional<String> messageKey;
    private final Object[] messageArgs;
    private final MessageSource messageSource = MessageSourceFactory.defaultSource();

    /**
     * Buildable constructor.
     *
     * @param builder a builder.
     */
    protected AppException(BaseAppExceptionBuilder<? extends AppException> builder) {
        super(builder.getMessage(), builder.throwable);
        this.messageKey = builder.messageKey;
        this.messageArgs = builder.messageArgs;
    }

    /**
     * @param locale a locale.
     * @return the localized version of the this exception's message.
     */
    public String getMessage(Locale locale) {
        requireNonNull(locale);
        return messageKey
                .map(messageKey -> messageSource.getMessage(messageKey, locale, messageArgs))
                .orElse(getMessage());
    }

    /**
     * Base application exception builder.
     *
     * @param <E> exception type.
     * @author Daniel Pedraza-Arcega
     */
    public static abstract class BaseAppExceptionBuilder<E extends AppException> implements Buildable<E> {

        private final MessageSource messageSource = MessageSourceFactory.defaultSource();
        private String message;
        private Optional<String> messageKey;
        private Object[] messageArgs;
        private Throwable throwable;

        /** Constructor. */
        protected BaseAppExceptionBuilder() {
            messageKey = Optional.empty();
        }

        /** @param messageKey the key for the desired message. */
        public BaseAppExceptionBuilder<E> messageKey(String messageKey) {
            requireNonBlank(messageKey);
            this.messageKey = Optional.of(messageKey);
            message = null;
            return this;
        }

        /** @param message the message of the exception. */
        public BaseAppExceptionBuilder<E> message(String message) {
            this.message = requireNonBlank(message);
            messageKey = Optional.empty();
            return this;
        }

        /**
         * @param arg1 the object to be formatted and substituted in the message.
         * @param moreArgs other objects to be formatted and substituted in the message.
         */
        public BaseAppExceptionBuilder<E> messageArgs(Object arg1, Object... moreArgs) {
            if (moreArgs == null || moreArgs.length == 0) messageArgs = new Object[] {arg1};
            else {
                messageArgs = new Object[moreArgs.length + 1];
                messageArgs[0] = arg1;
                System.arraycopy(moreArgs, 0, messageArgs, 1, moreArgs.length);
            }
            return this;
        }

        /** @param throwable the cause. */
        public BaseAppExceptionBuilder<E> cause(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        private String getMessage() {
            return messageKey.map(messageKey -> messageSource.getMessage(messageKey, messageArgs)).orElse(message);
        }

        /** @return a new exception of type {@code E}. */
        public abstract E build();
    }
}
