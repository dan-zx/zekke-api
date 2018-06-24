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
package com.github.danzx.zekke.test.assertion;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectAssert;

public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {

    ResponseAssert(Response actual) {
        super(actual, ResponseAssert.class);
    }

    public ResponseAssert respondedWith(Response.StatusType expectedStatus) {
        isNotNull();
        assertThat(actual.getStatus()).isEqualTo(expectedStatus.getStatusCode());
        return this;
    }

    public ResponseAssert produced(MediaType expectedMediaType) {
        isNotNull();
        assertThat(actual.getMediaType()).isNotNull().isEqualTo(expectedMediaType);
        return this;
    }

    public ResponseAssert hasEntity() {
        isNotNull();
        assertThat(actual.hasEntity()).overridingErrorMessage("Expecting to have entity").isTrue();
        return this;
    }

    public ResponseAssert doesNotContainEntity() {
        isNotNull();
        assertThat(actual.hasEntity()).overridingErrorMessage("Expecting to not have entity").isFalse();
        return this;
    }

    public <T> ObjectAssert<T> extractingEntityAs(Class<T> entityType) {
        hasEntity();
        T entity = null;
        try {
            entity = actual.readEntity(entityType);
        } catch (Exception ex) {
            failWithMessage("Unexpected error %s", ex);
        }
        return new ObjectAssert<>(entity);

    }

    public <T> ListAssert<T> extractingEntityAsListOf(Class<T> entityType) {
        hasEntity();
        List<T> entities = null;
        try {
            entities = actual.readEntity(GenericListType.genericTypeOf(entityType));
        } catch (Exception ex) {
            failWithMessage("Unexpected error %s", ex);
        }
        return new ListAssert<>(entities);

    }

    private static class GenericListType implements ParameterizedType {

        private final Class<?> clazz;

        private static <T> GenericType<List<T>> genericTypeOf(Class<T> clazz) {
            return new GenericType(new GenericListType(clazz)) {};
        }

        private GenericListType(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[] { clazz };
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return List.class;
        }
    }
}
