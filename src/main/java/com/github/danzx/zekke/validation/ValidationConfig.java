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

import javax.validation.Validator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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
        LocalValidatorFactoryBean springValidatorFactory = new LocalValidatorFactoryBean();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(APP_VALIDATION_MESSAGES);
        messageSource.setFallbackToSystemLocale(false);
        springValidatorFactory.setValidationMessageSource(messageSource);
        return springValidatorFactory;
    }
}
