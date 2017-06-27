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
package com.github.danzx.zekke.ws.rest.security;

import static java.util.Objects.requireNonNull;

import static com.github.danzx.zekke.util.Strings.hidden;

/**
 * Login credentials object.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class Credentials {

    private String userId;
    private String password;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = requireNonNull(userId);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = requireNonNull(password);
    }

    @Override
    public String toString() {
        return "{ userId:" + userId + ", password=" + hidden(password) + " }";
    }
}
