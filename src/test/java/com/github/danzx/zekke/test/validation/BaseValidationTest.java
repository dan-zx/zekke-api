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
package com.github.danzx.zekke.test.validation;

import static com.github.danzx.zekke.validation.ValidationConfig.APP_VALIDATION_MESSAGES;

import javax.validation.Validation;
import javax.validation.Validator;

import com.github.danzx.zekke.validation.DefaulLocaleMessageInterpolatorDecorator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import org.junit.BeforeClass;

public abstract class BaseValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void setUpValidator() {
        DefaulLocaleMessageInterpolatorDecorator messageInterpolator = 
                new DefaulLocaleMessageInterpolatorDecorator(
                        new ResourceBundleMessageInterpolator(
                                new PlatformResourceBundleLocator(APP_VALIDATION_MESSAGES)));
        validator = Validation.byDefaultProvider().configure()
                .messageInterpolator(messageInterpolator)
                .buildValidatorFactory()
                .getValidator();
    }

    protected static Validator validator() {
        return validator;
    }
}
