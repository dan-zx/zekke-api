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
package com.github.danzx.zekke.config;

import java.io.IOException;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.message.LocaleHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.validation.ValidationConfig;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;
import org.springframework.stereotype.Component;

/**
 * Jersey REST configuration.
 * 
 * @author Daniel Pedraza-Arcega
 */
@Component
@ApplicationPath("/api")
public class WebServiceConfig extends ResourceConfig {

    public WebServiceConfig() {
        packages("com.github.danzx.zekke.ws.rest");
        register(ObjectMapperProvider.class);
    }

    @Provider
    private static class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

        @Override
        public ObjectMapper getContext(Class<?> type) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.NON_NULL);
            objectMapper.registerModule(new SimpleModule().addDeserializer(Coordinates.class, new CoordinatesDeserializer()));
            return objectMapper;
        }
    }

    @Provider
    public class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig> {

        @Override
        public ValidationConfig getContext(Class<?> type) {
            return new ValidationConfig().messageInterpolator(new CustomMessageInterpolator());
        }
    }

    private class CustomMessageInterpolator extends ResourceBundleMessageInterpolator {
        private static final String VALIDATION_MESSAGES_BASENAME = "com.github.danzx.zekke.ValidationMessages";

        private CustomMessageInterpolator() {
            super(new PlatformResourceBundleLocator(VALIDATION_MESSAGES_BASENAME));
        }

        @Override
        public String interpolate(String message, Context context) {
            return super.interpolate(message, context, LocaleHolder.get());
        }
    }

    private static class CoordinatesDeserializer extends StdDeserializer<Coordinates> {
        private static final long serialVersionUID = 210982872801797100L;
        private static final String LAT_PROPERTY = "latitude";
        private static final String LNG_PROPERTY = "longitude";

        private CoordinatesDeserializer() { 
            this(null); 
        } 
     
        private CoordinatesDeserializer(Class<?> vc) { 
            super(vc); 
        }

        @Override
        public Coordinates deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = jp.getCodec().readTree(jp);
            Double latitude  = node.has(LAT_PROPERTY) && node.hasNonNull(LAT_PROPERTY) ? node.get(LAT_PROPERTY).asDouble() : null;
            Double longitude = node.has(LNG_PROPERTY) && node.hasNonNull(LNG_PROPERTY) ? node.get(LNG_PROPERTY).asDouble() : null;
            return Coordinates.ofLatLng(latitude, longitude);
        }
    }
}
