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

import java.util.Optional;

import javax.inject.Inject;

import com.github.danzx.zekke.domain.User;
import com.github.danzx.zekke.persistence.dao.UserDao;
import com.github.danzx.zekke.persistence.internal.mongo.Fields;

import org.mongodb.morphia.Datastore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

/**
 * User Morphia DAO.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Repository
public class UserMorphiaDao extends MorphiaDaoSupport implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(UserMorphiaDao.class);

    public @Inject UserMorphiaDao(Datastore datastore) {
        super(datastore);
    }

    @Override
    public Optional<User> findById(long id) {
        log.debug("Find User, id: {}", id);
        return Optional.ofNullable(getDatastore().createQuery(User.class).field(Fields.Common.ID).equal(id).get());
    }
}
