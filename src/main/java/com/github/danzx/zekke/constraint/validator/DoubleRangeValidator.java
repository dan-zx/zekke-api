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
package com.github.danzx.zekke.constraint.validator;

import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validate double values are between min and max included.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class DoubleRangeValidator extends BaseFloatingPointRangeValidator<Double> {

    private static final Logger log = LoggerFactory.getLogger(DoubleRangeValidator.class);

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        log.debug("Value: {}", value);
        if (value == null) return true;
        return isInRange(value);
    }
}
