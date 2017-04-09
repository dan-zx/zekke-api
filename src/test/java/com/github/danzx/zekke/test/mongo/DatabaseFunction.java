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
package com.github.danzx.zekke.test.mongo;

import static com.github.danzx.zekke.test.util.FilesUtil.readClasspathFileToString;
import static com.github.danzx.zekke.util.Strings.allCapsToCamelCase;

import java.util.concurrent.ExecutionException;

import org.bson.BsonJavaScript;
import org.bson.Document;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public enum DatabaseFunction {

    GET_NEXT_SEQUENCE_VALUE;

    private static final String FUNCTION_FILE_PATH_FORMAT = "/data/mongo/functions/%s.js";
    private static final Cache<String, Document> FUNCTION_CACHE = CacheBuilder.newBuilder().build();

    private final String functionName;
    private final String jsClasspathFile;

    DatabaseFunction() {
        functionName = allCapsToCamelCase(name(), false);
        jsClasspathFile = String.format(FUNCTION_FILE_PATH_FORMAT, functionName);
    }

    public String functionName() {
        return functionName;
    }

    public Document document() {
        try {
            return FUNCTION_CACHE.get(functionName, () -> transformFunctionAsDocument(jsClasspathFile));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private Document transformFunctionAsDocument(String jsClasspathFile) {
        String jsCode = readClasspathFileToString(jsClasspathFile);
        return new Document("value", new BsonJavaScript(jsCode));
    }
}
