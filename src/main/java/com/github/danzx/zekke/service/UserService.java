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
package com.github.danzx.zekke.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.github.danzx.zekke.domain.User;

/**
 * User business logic service.
 * 
 * @author Daniel Pedraza-Arcega
 */
public interface UserService {

    /**
     * Validates user is a registered ADMIN user.
     * 
     * @param user a user. 
     * @return {@code true} if it is an ADMIN user; {@code false} otherwise.
     */
    boolean isAdminRegistered(@NotNull @Valid User user);
}
