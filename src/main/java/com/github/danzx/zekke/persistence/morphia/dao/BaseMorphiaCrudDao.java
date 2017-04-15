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
package com.github.danzx.zekke.persistence.morphia.dao;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.github.danzx.zekke.domain.BaseEntity;
import com.github.danzx.zekke.persistence.dao.CrudDao;
import com.github.danzx.zekke.persistence.internal.mongo.Fields;

import org.mongodb.morphia.Datastore;

/**
 * Base Morphia CRUD DAO.
 * 
 * @param <E> the collection type.
 * @param <ID> the id type used by the collection.
 * 
 * @author Daniel Pedraza-Arcega
 */
public abstract class BaseMorphiaCrudDao<E extends BaseEntity<ID>, ID extends Serializable> implements CrudDao<E, ID> {

    private final Datastore datastore;
    private final Class<E> collectionClass;

    /**
     * Constructor.
     * 
     * @param datastore a non null Datastore
     * @param collectionClass a non null class matching the type of this DAO.
     */
    protected BaseMorphiaCrudDao(Datastore datastore, Class<E> collectionClass) {
        requireNonNull(datastore, "Datastore shouldn't be null in order to access collections");
        requireNonNull(collectionClass, "Collection class shouldn't be null in order to access collections");
        this.datastore = datastore;
        this.collectionClass = collectionClass;
    }

    /** {@inheritDoc} */
    @Override
    public E save(E collectionEntity) {
        requireNonNull(collectionEntity, "Entity shouldn't be null in order to be saved");
        datastore.save(collectionEntity);
        return collectionEntity;
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById(ID id) {
        requireNonNull(id, "Id shouldn't be null in order delete an entity");
        datastore.delete(collectionClass, id);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<E> findById(ID id) {
        requireNonNull(id, "Id shouldn't be null in order get an entity");
        return Optional.ofNullable(datastore.createQuery(collectionClass).field(Fields.Common.ID).equal(id).get());
    }

    /** {@inheritDoc} */
    @Override
    public List<E> findAll() {
        return datastore.createQuery(collectionClass).asList();
    }

    protected Datastore getDatastore() {
        return datastore;
    }

    protected Class<E> getCollectionClass() {
        return collectionClass;
    }
}
