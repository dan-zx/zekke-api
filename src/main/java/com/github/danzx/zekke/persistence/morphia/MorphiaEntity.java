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
package com.github.danzx.zekke.persistence.morphia;

import java.io.Serializable;
import java.util.Objects;

import com.github.danzx.zekke.domain.Entity;

import org.mongodb.morphia.annotations.Id;

/**
 * Morphia entity implementaion.
 * 
 * @param <ID> the id type.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class MorphiaEntity<ID extends Serializable> implements Entity<ID> {

    @Id
    private ID id;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        Objects.requireNonNull(id, "id cannot be null");
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
    protected boolean isEntityEqualTo(MorphiaEntity<?> other) {
        return Objects.equals(id, other.id);
    }
}
