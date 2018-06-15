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

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ObjectAssert;

import static org.assertj.core.api.Assertions.assertThat;

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

    public ResponseAssert doesNotContainEntity() {
        isNotNull();
        assertThat(actual.hasEntity()).overridingErrorMessage("Expecting to not have entity").isFalse();
        return this;
    }

    public <T> ObjectAssert<T> extractingEntityAs(Class<T> entityType) {
        isNotNull();
        assertThat(actual.hasEntity()).overridingErrorMessage("Expecting to have entity").isTrue();
        T entity = null;
        try {
            entity = actual.readEntity(entityType);
        } catch (Exception ex) {
            failWithMessage("Unexpected error %s", ex);
        }
        return new ObjectAssert<>(entity);
    }
}
