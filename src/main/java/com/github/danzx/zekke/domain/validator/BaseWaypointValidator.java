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
package com.github.danzx.zekke.domain.validator;

import static java.util.stream.Collectors.joining;

import static com.github.danzx.zekke.util.Messages.getMessage;

import com.github.danzx.zekke.base.ValidatorHelper;
import com.github.danzx.zekke.domain.Waypoint;

/**
 * Base waypoint validator.
 * 
 * @author Daniel Pedraza-Arcega
 */
abstract class BaseWaypointValidator extends ValidatorHelper<Waypoint> {

    protected BaseWaypointValidator(Waypoint value) {
        super(value);
    }

    @Override
    protected void onValidationFailure() {
        String validationErrors = getValidationFails().entrySet().stream()
                .map(entry -> getMessage("validation.error.template", entry.getKey(), entry.getValue()))
                .collect(joining(","));
        String message = getMessage("validation.errors", validationErrors);
        throw new IllegalArgumentException(message);
    }
}
