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

import javax.validation.ConstraintValidator;

import com.github.danzx.zekke.constraint.FloatRange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class to validate floating point values are between min and max included.
 * 
 * @author Daniel Pedraza-Arcega
 */
abstract class BaseFloatingPointRangeValidator<N extends Number> implements ConstraintValidator<FloatRange, N> {

    private static final Logger log = LoggerFactory.getLogger(BaseFloatingPointRangeValidator.class);

    private double min;
    private double max;

    /**
     * @see ConstraintValidator#initialize(java.lang.annotation.Annotation)
     * @param min {@link FloatRange#min()}
     * @param max {@link FloatRange#max()}
     */
    protected void init(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void initialize(FloatRange constraintAnnotation) {
        init(constraintAnnotation.min(), constraintAnnotation.max());
    }

    /**
     * @param value the value to check.
     * @return {@code true} if the value is in range (inclusive); {@code false} otherwise.
     */
    protected boolean isInRange(double value) {
        log.debug("{} <= {} <= {}", min, value, max);
        return min <= value && value <= max;
    }
}
