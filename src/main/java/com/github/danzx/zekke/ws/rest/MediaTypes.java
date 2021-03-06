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
package com.github.danzx.zekke.ws.rest;

import javax.ws.rs.core.MediaType;

/**
 * Extra media type constants.
 * 
 * @see javax.ws.rs.core.MediaType
 * 
 * @author Daniel Pedraza-Arcega
 */
public class MediaTypes {

    public final static String APPLICATION_JSON_PATCH = "application/json+patch";
    public final static MediaType APPLICATION_JSON_PATCH_TYPE = new MediaType("application", "json+patch");

    private MediaTypes() {
        throw new AssertionError();
    }
}
