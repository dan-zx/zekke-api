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
import javax.validation.ConstraintValidatorContext;

import com.github.danzx.zekke.constraint.FloatRange;

/**
 * Validate that the double value is between min and max included.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class DoubleRangeValidator implements ConstraintValidator<FloatRange, Double>{

    private FloatRange meta;

    @Override
    public void initialize(FloatRange constraintAnnotation) {
        meta = constraintAnnotation;
    }


    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) return true;
        double dValue = value.doubleValue();
        return meta.min() <= dValue && dValue <= meta.max();
    }
}
