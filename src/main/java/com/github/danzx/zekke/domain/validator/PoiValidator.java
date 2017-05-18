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

import static com.github.danzx.zekke.util.Messages.getMessage;

import com.github.danzx.zekke.domain.Waypoint;

/**
 * Validates POIs.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class PoiValidator extends BaseWaypointValidator {

    public PoiValidator(Waypoint value) {
        super(value);
    }

    private void shouldHaveName() {
        if(!getValue().getName().isPresent()) {
            failValidation();
            addError("name", getMessage("poi.name.null.error"));
        }
    }

    @Override
    protected void performValidations() {
        shouldHaveName();
    }
}
