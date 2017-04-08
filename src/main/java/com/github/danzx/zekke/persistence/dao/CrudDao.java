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
package com.github.danzx.zekke.persistence.dao;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

/**
 * A Data Access Object capable of persist changes in the datastore and read from it.
 * 
 * @param <E> the entity type.
 * @param <ID> the id type used by the entity.
 * 
 * @author Daniel Pedraza-Arcega
 */
public interface CrudDao<E, ID> {

    /**
     * Finds an entity by its id.
     * 
     * @param id an id object.
     * @return the optional entity.
     */
    Optional<E> findById(@NotNull ID id);

    /**
     * Finds all entities.
     * 
     * @return a list of entities or an empty list.
     */
    List<E> findAll();

    /**
     * Saves the given entity.
     *  
     * @param entity an entity.
     * @return the updated entity.
     */
    E save(@NotNull E entity);

    /**
     * Deletes an entity by id.
     *  
     * @param id an id object..
     */
    void deleteById(@NotNull ID id);
}
