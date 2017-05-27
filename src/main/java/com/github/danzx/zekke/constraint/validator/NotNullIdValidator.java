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

import com.github.danzx.zekke.constraint.NotNullId;

/**
 * Validates that an object has a getId() method visible and its result is not null.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class NotNullIdValidator extends BaseIdValidator<NotNullId> {

    public NotNullIdValidator() {
        super(false);
    }
}
