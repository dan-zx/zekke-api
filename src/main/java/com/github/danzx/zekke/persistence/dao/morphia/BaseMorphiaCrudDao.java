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
package com.github.danzx.zekke.persistence.dao.morphia;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import com.github.danzx.zekke.domain.BaseEntity;
import com.github.danzx.zekke.persistence.dao.CrudDao;
import com.github.danzx.zekke.persistence.internal.mongo.Fields;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Morphia CRUD DAO.
 * 
 * @param <E> the collection type.
 * @param <ID> the id type used by the collection.
 * 
 * @author Daniel Pedraza-Arcega
 */
abstract class BaseMorphiaCrudDao<E extends BaseEntity<ID>, ID extends Serializable> extends MorphiaDaoSupport implements CrudDao<E, ID> {

    private static final Logger log = LoggerFactory.getLogger(BaseMorphiaCrudDao.class);

    private final Class<E> collectionClass;

    /**
     * Constructor.
     * 
     * @param datastore a non null datastore.
     * @param collectionClass a non null class matching the type of this DAO.
     */
    protected BaseMorphiaCrudDao(Datastore datastore, Class<E> collectionClass) {
        super(datastore);
        this.collectionClass = requireNonNull(collectionClass);
    }

    /** {@inheritDoc} */
    @Override
    public void saveOrUpdate(E collectionEntity) {
        log.debug("Save: {}", collectionEntity);
        requireNonNull(collectionEntity);
        getDatastore().save(collectionEntity);
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteById(ID id) {
        log.debug("Delete {}, id: {}", collectionClass, id);
        requireNonNull(id);
        int documentsAffected = getDatastore().delete(collectionClass, id).getN();
        return documentsAffected > 0;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<E> findById(ID id) {
        log.debug("Find {}, id: {}", collectionClass, id);
        requireNonNull(id);
        return Optional.ofNullable(createQuery().field(Fields.Common.ID).equal(id).get());
    }

    /** {@inheritDoc} */
    @Override
    public List<E> findAll() {
        return createQuery().asList();
    }

    /** @return a new query. */
    protected Query<E> createQuery() {
        return getDatastore().createQuery(collectionClass);
    }

    protected Class<E> getCollectionClass() {
        return collectionClass;
    }
}
