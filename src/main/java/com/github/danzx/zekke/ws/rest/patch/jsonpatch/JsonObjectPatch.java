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

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import com.github.danzx.zekke.ws.rest.patch.ObjectPatch;
import com.github.danzx.zekke.ws.rest.patch.ObjectPatchException;

import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

/**
 * Converts the target resource to JSON and then applies a Patch.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class JsonObjectPatch implements ObjectPatch {

    private final ObjectMapper objectMapper;
    private final JsonPatch patch;

    public JsonObjectPatch(ObjectMapper objectMapper, JsonPatch patch) {
        this.objectMapper = requireNonNull(objectMapper);
        this.patch = requireNonNull(patch);
    }

    @Override
    public <T> T apply(T target) throws ObjectPatchException {
        requireNonNull(target);
        JsonNode source = objectMapper.valueToTree(target);
        JsonNode result;
        try {
            result = patch.apply(source);
        } catch (JsonPatchException ex) {
            throw new ObjectPatchException.Builder()
                .messageKey("partial.update.error")
                .messageArgs(ex.getMessage())
                .cause(ex)
                .build();
        }

        ObjectReader reader = objectMapper.readerForUpdating(target);
        try {
            return reader.readValue(result);
        } catch (IOException ex) {
            throw new ObjectPatchException.Builder()
                .messageKey("partial.update.error")
                .messageArgs(ex.getMessage())
                .cause(ex)
                .build();
        }
    }
}
