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
package com.github.danzx.zekke.ws.rest.model.constraint.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.danzx.zekke.domain.Waypoint.Type;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.model.constraint.CheckTypedWaypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TypedWaypoint validator.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class TypedWaypointValidator implements ConstraintValidator<CheckTypedWaypoint, TypedWaypoint> {

    private static final Logger log = LoggerFactory.getLogger(TypedWaypointValidator.class);

    @Override
    public void initialize(CheckTypedWaypoint constraintAnnotation) { }

    @Override
    public boolean isValid(TypedWaypoint value, ConstraintValidatorContext context) {
        log.debug("Waypoint: {}", value);
        if (value == null) return true;
        boolean isValid = true;
        isValid &= Type.POI == value.getType() ? isPoiValid(value, context) : 
                   Type.WALKWAY == value.getType() ? isWalwayValid(value, context) :
                   true;
        if (!isValid) context.disableDefaultConstraintViolation();
        return isValid;
    }

    private boolean isPoiValid(TypedWaypoint value, ConstraintValidatorContext context) {
        if (value.getName() == null) {
            log.trace("POI name is empty");
            context
                .buildConstraintViolationWithTemplate("{com.github.danzx.zekke.constraint.PoiName.message}")
                .addPropertyNode("name")
                .addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean isWalwayValid(TypedWaypoint value, ConstraintValidatorContext context) {
        if (value.getName() != null) {
            log.trace("Walkway name is not null");
            context
                .buildConstraintViolationWithTemplate("{com.github.danzx.zekke.constraint.WalkwayName.message}")
                .addPropertyNode("name")
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
