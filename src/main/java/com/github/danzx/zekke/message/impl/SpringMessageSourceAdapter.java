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

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * Adapter for Spring's MessageSource objects.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class SpringMessageSourceAdapter implements com.github.danzx.zekke.message.MessageSource {

    private final MessageSource springMessageSource;

    public SpringMessageSourceAdapter(MessageSource springMessageSource) {
        this.springMessageSource = requireNonNull(springMessageSource);
    }

    @Override
    public String getMessage(String messageKey, Object... messageArguments) {
        return getMessage(messageKey, Locale.ROOT, messageArguments);
    }

    @Override
    public String getMessage(String messageKey, Locale locale, Object... messageArguments) {
        try {
            return springMessageSource.getMessage(messageKey, messageArguments, locale);
        } catch (NoSuchMessageException ex) {
            throw new MessageNotFoundException.Builder()
                .withMessage(ex.getMessage())
                .cause(ex)
                .build();
        }
    }
}
