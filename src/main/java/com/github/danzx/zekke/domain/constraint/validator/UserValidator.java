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
package com.github.danzx.zekke.domain.constraint.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.domain.constraint.CheckUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User validator.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class UserValidator implements ConstraintValidator<CheckUser, User> {

    private static final Logger log = LoggerFactory.getLogger(UserValidator.class);

    @Override
    public void initialize(CheckUser constraintAnnotation) { }

    @Override
    public boolean isValid(User value, ConstraintValidatorContext context) {
        log.debug("User: {}", value);
        if (value == null) return true;
        boolean isValid = true;
        isValid &= User.Role.ADMIN == value.getRole() ? isValidAdmin(value, context) :
                   User.Role.ANONYMOUS == value.getRole() ? isAnonymousUserValid(value, context) :
                   true;

        if (!isValid) context.disableDefaultConstraintViolation();
        return isValid;
    }

    private boolean isValidAdmin(User value, ConstraintValidatorContext context) {
        if (!value.getPassword().isPresent()) {
            log.trace("Admin password is empty");
            context
                .buildConstraintViolationWithTemplate("{com.github.danzx.zekke.constraint.AdminPassword.message}")
                .addPropertyNode("password")
                .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isAnonymousUserValid(User value, ConstraintValidatorContext context) {
        if (value.getPassword().isPresent()) {
            log.trace("Anonymous users don't have password");
            context
                .buildConstraintViolationWithTemplate("{com.github.danzx.zekke.constraint.AnonymousUserPassword.message}")
                .addPropertyNode("password")
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
