/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.danzx.zekke.persistence.dao.morphia;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.mongodb.morphia.Datastore;

import com.github.danzx.zekke.persistence.dao.CrudDao;

/**
 * Base Morphia CRUD DAO.
 * 
 * @param <C> the collection type.
 * @param <ID> the id type used by the collection.
 * 
 * @author Daniel Pedraza-Arcega
 */
public abstract class BaseMorphiaCrudDao<C, ID> implements CrudDao<C, ID> {

    protected static final String ID_FIELD = "_id";

    private final Datastore datastore;
    private final Class<C> collectionClass;

    /**
     * Constructor.
     * 
     * @param datastore a non null {@link Datastore}
     * @param collectionClass a non null class matching the type of this DAO.
     */
    protected BaseMorphiaCrudDao(@NotNull Datastore datastore, @NotNull Class<C> collectionClass) {
        this.datastore = datastore;
        this.collectionClass = collectionClass;
    }

    /** {@inheritDoc} */
    @Override
    public C save(C collectionEntity) {
        datastore.save(collectionEntity);
        return collectionEntity;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById(ID id) {
        datastore.delete(collectionClass, id);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<C> findById(ID id) {
        return Optional.ofNullable(datastore.createQuery(collectionClass).field(ID_FIELD).equal(id).get());
    }

    /** {@inheritDoc} */
    @Override
    public List<C> findAll() {
        return datastore.createQuery(collectionClass).asList();
    }

    protected Datastore getDatastore() {
        return datastore;
    }

    protected Class<C> getCollectionClass() {
        return collectionClass;
    }
}
