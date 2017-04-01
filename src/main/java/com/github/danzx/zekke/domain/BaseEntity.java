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
package com.github.danzx.zekke.domain;

import java.util.Objects;

import org.mongodb.morphia.annotations.Id;

/**
 * Base entity class.
 * 
 * @param <ID> the Id type.
 * 
 * @author Daniel Pedraza-Arcega
 */
public abstract class BaseEntity<ID> {

    @Id
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Use this method to complete your equals method.
     * @see {@link #equals(Object)}
     */
    protected boolean isEntityEqualTo(BaseEntity<?> other) {
        return Objects.equals(id, other.id);
    }
}
