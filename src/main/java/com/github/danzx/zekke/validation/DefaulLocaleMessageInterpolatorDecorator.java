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

import java.util.Locale;
import java.util.function.Supplier;

import javax.validation.MessageInterpolator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * When {@link #interpolate(String, javax.validation.MessageInterpolator.Context)} is colled a
 * locale supplier will be used in order to complete the message interpolation.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class DefaulLocaleMessageInterpolatorDecorator implements MessageInterpolator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaulLocaleMessageInterpolatorDecorator.class);
    private static final Supplier<Locale> ROOT_LOCALE_SUPPLIER = () -> Locale.ROOT;

    private final MessageInterpolator decorated;
    private Supplier<Locale> localeSupplier;

    public DefaulLocaleMessageInterpolatorDecorator(MessageInterpolator decorated) {
        this.decorated = decorated;
        localeSupplier = ROOT_LOCALE_SUPPLIER;
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        Locale locale = localeSupplier.get();
        LOGGER.debug("interpolating {} with locale {}", messageTemplate, locale);
        return interpolate(messageTemplate, context, locale);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return decorated.interpolate(messageTemplate, context, locale);
    }

    /**
     * Locale supplier used at the method
     * {@link #interpolate(String, javax.validation.MessageInterpolator.Context)}
     * 
     * @param localeSupplier the supplier. If null, it will fallback to the default strategy.
     */
    public void setLocaleResoultionStrategy(Supplier<Locale> localeSupplier) {
        if (localeSupplier != null) this.localeSupplier = localeSupplier;
    }
}
