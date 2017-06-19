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

import static java.util.Objects.requireNonNull;

import java.util.Locale;

import com.github.danzx.zekke.message.MessageNotFoundException;
import com.github.danzx.zekke.message.MessageSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Forwards its MessageSource implementation to same method and catches MessageNotFoundExceptions to
 * return a default message.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class DefaultMessageDecorator implements MessageSource {

    public static final String DEFAULT_MISSING_KEY_FORMAT = "???%s???";

    private static final Logger log = LoggerFactory.getLogger(DefaultMessageDecorator.class);

    private final MessageSource messageSource;
    private String missingKeyFormat = DEFAULT_MISSING_KEY_FORMAT;

    public DefaultMessageDecorator(MessageSource messageSource) {
        this.messageSource = requireNonNull(messageSource);
    }

    @Override
    public String getMessage(String messageKey, Object... messageArguments) {
        try {
            return messageSource.getMessage(messageKey, messageArguments);
        } catch (MessageNotFoundException ex) {
            log.warn("Can't find message for key: [{}]", messageKey, ex);
            return String.format(missingKeyFormat, messageKey);
        }
    }

    @Override
    public String getMessage(String messageKey, Locale locale, Object... messageArguments) {
        try {
            return messageSource.getMessage(messageKey, locale, messageArguments);
        } catch (MessageNotFoundException ex) {
            log.warn("Can't find message for key: [{}]", messageKey, ex);
            return String.format(missingKeyFormat, messageKey);
        }
    }

    public void setMissingKeyFormat(String missingKeyFormat) {
        this.missingKeyFormat = requireNonNull(missingKeyFormat);
    }
}
