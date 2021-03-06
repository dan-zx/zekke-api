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
package com.github.danzx.zekke.util;

import java.util.Optional;

/**
 * Various throwable utilities.
 *
 * @author Daniel Pedraza-Arcega
 */
public class Throwables {

    private Throwables() {
        throw new AssertionError();
    }

    /**
     * Get the root cause (ie the last non null cause) from a {@link Throwable}.
     * 
     * @param throwable the {@code Throwable} to get root cause from.
     * @return the root cause if any, else {@code null}.
     */
    public static Optional<Throwable> getRootCause(Throwable throwable) {
        if (throwable == null) return Optional.empty();
        if (throwable.getCause() == null) return Optional.of(throwable);
        Throwable cause;
        while ((cause = throwable.getCause()) != null) throwable = cause;
        return Optional.of(throwable);
    }
}
