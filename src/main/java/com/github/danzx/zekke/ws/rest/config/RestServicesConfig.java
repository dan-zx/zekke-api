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
package com.github.danzx.zekke.ws.rest.config;

import javax.ws.rs.ApplicationPath;

import com.github.danzx.zekke.ws.rest.api.ErrorEndpoint;
import com.github.danzx.zekke.ws.rest.api.JwtAuthenticationEndpoint;
import com.github.danzx.zekke.ws.rest.api.WaypointEndpoint;
import com.github.danzx.zekke.ws.rest.errormapper.AppExceptionMapper;
import com.github.danzx.zekke.ws.rest.errormapper.ConstraintViolationExceptionMapper;
import com.github.danzx.zekke.ws.rest.errormapper.GenericExceptionMapper;
import com.github.danzx.zekke.ws.rest.errormapper.ResourceNotFoundExceptionMapper;
import com.github.danzx.zekke.ws.rest.patch.jsonpatch.JsonPatchReader;
import com.github.danzx.zekke.ws.rest.security.jwt.filter.JwtAuthenticationFilter;

import org.glassfish.jersey.server.ResourceConfig;

import org.springframework.stereotype.Component;

/**
 * Jersey REST configuration.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
@ApplicationPath("/api")
public class RestServicesConfig extends ResourceConfig {

    public RestServicesConfig() {
        /*
         * There is a limitation in Jersey's classpath scanning that raises a
         * java.io.FileNotFoundException when Spring Boot starts from a fat jar.
         * To solve this issue is best to register each individual resource.
         */
        registerEndpoints();
        registerExceptionMappers();
        registerBodyReaders();
        registerFilters();
    }

    private void registerEndpoints() {
        register(ErrorEndpoint.class);
        register(JwtAuthenticationEndpoint.class);
        register(WaypointEndpoint.class);
    }

    private void registerExceptionMappers() {
        register(AppExceptionMapper.class);
        register(ConstraintViolationExceptionMapper.class);
        register(GenericExceptionMapper.class);
        register(ResourceNotFoundExceptionMapper.class);
    }

    private void registerBodyReaders() {
        register(JsonPatchReader.class);
    }

    private void registerFilters() {
        register(JwtAuthenticationFilter.class);
    }
}
