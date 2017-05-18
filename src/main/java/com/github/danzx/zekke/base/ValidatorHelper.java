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
package com.github.danzx.zekke.base;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Validator class to help in validation process.
 * 
 * @author Daniel Pedraza-Arcega
 */
public abstract class ValidatorHelper<T> {

    private final Map<String, String> validationFails;
    private final T value;
    private boolean isValid;

    /**
     * Constructor.
     * 
     * @param value the value to validate
     */
    protected ValidatorHelper(T value) {
        this.value = requireNonNull(value);
        validationFails = new HashMap<>();
        isValid = true;
    }

    /** Sets the the validation has failed. */
    protected void failValidation() {
        isValid = false;
    }

    /**
     * Add error to the validation error messages.
     * 
     * @param property the property that failed.
     * @param message the error message.
     */
    protected void addError(String property, String message) {
        validationFails.put(property, message);
    }

    /** @return a map with its key as the property and its value the validation error. */
    public Map<String, String> getValidationFails() {
        return validationFails;
    }

    /** @return the value to validate. */
    protected T getValue() {
        return value;
    }

    /** @return true if passed the validation; otherwise false. */
    protected boolean isValid() {
        return isValid;
    }

    /** Performs the actual validations. */
    protected abstract void performValidations();

    /** Logic that happen after the a failed validation. */
    protected abstract void onValidationFailure();

    /** Validates its value. */
    public boolean validate() {
        performValidations();
        if (!isValid) onValidationFailure();
        return isValid;
    }
}
