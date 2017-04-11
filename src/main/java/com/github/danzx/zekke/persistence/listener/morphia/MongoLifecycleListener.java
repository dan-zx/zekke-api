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
package com.github.danzx.zekke.persistence.listener.morphia;

import com.mongodb.DBObject;

/**
 * MongoDB lifecycle listener. All method implementations are empty. Override the methods you want
 * use and add the corresponding annotation.
 * 
 * @author Daniel Pedraza-Arcega
 */
public abstract class MongoLifecycleListener<E> {

    /** @see {@link org.mongodb.morphia.annotations.PostLoad} */
    protected void postLoad(E entity, DBObject dbObj) {}

    /** @see {@link org.mongodb.morphia.annotations.PostPersist} */
    protected void postPersist(E entity, DBObject dbObj) {}

    /** @see {@link org.mongodb.morphia.annotations.PreLoad} */
    protected void preLoad(E entity, DBObject dbObj) {}

    /** @see {@link org.mongodb.morphia.annotations.PrePersist} */
    protected void prePersist(E entity, DBObject dbObj) {}

    /** @see {@link org.mongodb.morphia.annotations.PreSave} */
    protected void preSave(E entity, DBObject dbObj) {}
}
