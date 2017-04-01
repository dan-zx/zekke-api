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

import static com.github.danzx.zekke.test.utils.FilesUtil.readClasspathFileToString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bson.Document;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public enum AppCollection {

    WAYPOINTS("waypoints", "/data/mongo/waypoints.json");

    private static final Cache<String, List<Document>> COLLECTION_CACHE = CacheBuilder.newBuilder().build();

    private final String collectionName;
    private final String jsonClasspathFile;

    private AppCollection(String collectionName, String jsonClasspathFile) {
        this.collectionName = collectionName;
        this.jsonClasspathFile = jsonClasspathFile;
    }

    public String collectionName() {
        return collectionName;
    }

    public List<Document> documents() {
        try {
            return COLLECTION_CACHE.get(collectionName, () -> transformJsonAsDocuments(jsonClasspathFile));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Document> transformJsonAsDocuments(String jsonFile) throws JSONException {
        String json = readClasspathFileToString(jsonFile);
        JSONArray jsonArray = new JSONArray(json);
        int arraySize = jsonArray.length();
        List<Document> documents = new ArrayList<>(arraySize);
        for (int index = 0; index < arraySize; index++) {
            JSONObject jsonObject = jsonArray.getJSONObject(index);
            Document document = Document.parse(jsonObject.toString());
            documents.add(document);
        }
        return documents;
    }
}
