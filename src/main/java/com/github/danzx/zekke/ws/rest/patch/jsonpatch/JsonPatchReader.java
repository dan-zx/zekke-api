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
package com.github.danzx.zekke.ws.rest.patch.jsonpatch;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.danzx.zekke.ws.rest.MediaTypes;
import com.github.danzx.zekke.ws.rest.patch.ObjectPatch;

import com.github.fge.jsonpatch.JsonPatch;

/**
 * Creates a JSON Patch ObjectPatch implementation.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Provider
@Consumes(MediaTypes.APPLICATION_JSON_PATCH)
public class JsonPatchReader implements MessageBodyReader<ObjectPatch> {

    private @Context ObjectMapper objectMapper;

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return ObjectPatch.class == type && MediaTypes.APPLICATION_JSON_PATCH_TYPE.isCompatible(mediaType);
    }

    @Override
    public ObjectPatch readFrom(Class<ObjectPatch> type,
                                Type genericType,
                                Annotation[] annotations,
                                MediaType mediaType,
                                MultivaluedMap<String, String> httpHeaders,
                                InputStream entityStream) throws IOException, WebApplicationException {
        JsonPatch patch = objectMapper.readValue(entityStream, JsonPatch.class);
        return new JsonObjectPatch(objectMapper, patch);
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
