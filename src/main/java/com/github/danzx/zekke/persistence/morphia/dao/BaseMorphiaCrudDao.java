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
import org.mongodb.morphia.query.Query;

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
        this.datastore = requireNonNull(datastore);
        this.collectionClass = requireNonNull(collectionClass);
    }

    /** {@inheritDoc} */
    @Override
    public void saveOrUpdate(E collectionEntity) {
        datastore.save(requireNonNull(collectionEntity));
    }

    /** {@inheritDoc} */
    @Override
    public void deleteById(ID id) {
        datastore.delete(collectionClass, requireNonNull(id));
    }

    /** {@inheritDoc} */
    @Override
    public Optional<E> findById(ID id) {
        return Optional.ofNullable(createQuery().field(Fields.Common.ID).equal(requireNonNull(id)).get());
    }

    /** {@inheritDoc} */
    @Override
    public List<E> findAll() {
        return createQuery().asList();
    }

    /** @return a new query. */
    protected Query<E> createQuery() {
        return datastore.createQuery(collectionClass);
    }

    protected Datastore getDatastore() {
        return datastore;
    }

    protected Class<E> getCollectionClass() {
        return collectionClass;
    }
}
