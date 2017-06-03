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
package com.github.danzx.zekke.validation;

import javax.validation.Validation;
import javax.validation.Validator;

import com.github.danzx.zekke.message.LocaleHolder;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean Validation configuration.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Configuration
public class ValidationConfig {

    public static final String APP_VALIDATION_MESSAGES = "com.github.danzx.zekke.ValidationMessages";

    @Bean
    public Validator validator() {
        // TODO: It seems this interpolator is only working with messages that cannot be resolved by
        // the default (unknown) message interpolator. Maybe spring is creating another interpolator
        // that uses to resolve all the known BV messages and, when it fails, it goes to mine to
        // resolve the missing message.
        DefaulLocaleMessageInterpolatorDecorator messageInterpolator = 
                new DefaulLocaleMessageInterpolatorDecorator(
                        new ResourceBundleMessageInterpolator(
                                new PlatformResourceBundleLocator(APP_VALIDATION_MESSAGES)));
        messageInterpolator.setLocaleResoultionStrategy(() -> LocaleHolder.get());
        return Validation.byDefaultProvider().configure()
                .messageInterpolator(messageInterpolator)
                .buildValidatorFactory()
                .getValidator();
    }
}
