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

import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.danzx.zekke.constraint.CheckId;

/**
 * Validates that an object has a getId() method visible and its result is either null or not null.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class IdValidator  implements ConstraintValidator<CheckId, Object> {

    private boolean shouldBeNull;

    protected void init(boolean shouldBeNull) {
        this.shouldBeNull = shouldBeNull;
    }

    @Override
    public void initialize(CheckId constraintAnnotation) {
        init(constraintAnnotation.shouldBeNull());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            Method getIdMethod = value.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(value);
            return (shouldBeNull && id == null) || (!shouldBeNull && id != null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Object to validate does not have an accessible getId() method", e);
        }
    }
}
