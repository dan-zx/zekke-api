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
package com.github.danzx.zekke.test;

import java.util.Locale;

import javax.validation.Configuration;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.BeforeClass;

public abstract class BaseValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        Configuration<?> configuration = Validation.byDefaultProvider().configure();
        validator = configuration
                .messageInterpolator(new RootMessageInterpolator(configuration.getDefaultMessageInterpolator()))
                .buildValidatorFactory()
                .getValidator();
    }

    protected static Validator validator() {
        return validator;
    }

    private static class RootMessageInterpolator implements MessageInterpolator {
        private final MessageInterpolator delegate;

        private RootMessageInterpolator(MessageInterpolator delegate) { 
            this.delegate = delegate; 
        }

        @Override
        public String interpolate(String message, Context context) {
            return this.delegate.interpolate(message, context, Locale.ROOT);
        }

        @Override
        public String interpolate(String message, Context context, Locale locale) {
            return this.delegate.interpolate(message, context, locale);
        }
    }
}
