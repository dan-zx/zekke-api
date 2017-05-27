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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates that an object has a getId() method visible and its result is either null or not null.
 * 
 * @author Daniel Pedraza-Arcega
 */
abstract class BaseIdValidator<A extends Annotation> implements ConstraintValidator<A, Object> {

    private static final String ID_GETTER = "getId";
    private static final String ID_PROPERTY = "id";

    private final boolean shouldBeNull;

    BaseIdValidator(boolean shouldBeNull) {
        this.shouldBeNull = shouldBeNull;
    }

    @Override
    public void initialize(A constraintAnnotation) { }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            Method getIdMethod = value.getClass().getMethod(ID_GETTER);
            Object id = getIdMethod.invoke(value);
            boolean isValid = (shouldBeNull && id == null) || (!shouldBeNull && id != null);
            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(ID_PROPERTY)
                    .addConstraintViolation();
            }
            return isValid;
        } catch (Exception e) {
            throw new IllegalArgumentException("Object to validate does not have an accessible getId() method", e);
        }
    }
}
