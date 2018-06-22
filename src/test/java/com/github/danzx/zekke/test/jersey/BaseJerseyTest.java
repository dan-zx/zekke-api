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
package com.github.danzx.zekke.test.jersey;

import static com.github.danzx.zekke.test.config.TestProfiles.ENDPOINT_TEST;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.github.danzx.zekke.domain.Waypoint;
import com.github.danzx.zekke.security.jwt.JwtFactory;
import com.github.danzx.zekke.security.jwt.JwtSettings;
import com.github.danzx.zekke.security.jwt.JwtVerifier;
import com.github.danzx.zekke.security.jwt.jjwt.JjwtFactory;
import com.github.danzx.zekke.security.jwt.jjwt.JjwtVerifier;
import com.github.danzx.zekke.service.UserService;
import com.github.danzx.zekke.service.WaypointService;
import com.github.danzx.zekke.test.spring.ForEndpointTest;
import com.github.danzx.zekke.transformer.Transformer;
import com.github.danzx.zekke.ws.rest.MediaTypes;
import com.github.danzx.zekke.ws.rest.api.ErrorEndpoint;
import com.github.danzx.zekke.ws.rest.api.JwtAuthenticationEndpoint;
import com.github.danzx.zekke.ws.rest.api.WaypointEndpoint;
import com.github.danzx.zekke.ws.rest.config.ModelTransformerConfig;
import com.github.danzx.zekke.ws.rest.config.ObjectMapperConfig;
import com.github.danzx.zekke.ws.rest.config.WaypointToPoiMapping;
import com.github.danzx.zekke.ws.rest.config.WaypointToTypedWaypointMapping;
import com.github.danzx.zekke.ws.rest.config.WaypointToWalkwayMapping;
import com.github.danzx.zekke.ws.rest.errormapper.AppExceptionMapper;
import com.github.danzx.zekke.ws.rest.errormapper.ConstraintViolationExceptionMapper;
import com.github.danzx.zekke.ws.rest.errormapper.GenericExceptionMapper;
import com.github.danzx.zekke.ws.rest.errormapper.InvalidPathParamExceptionMapper;
import com.github.danzx.zekke.ws.rest.errormapper.JsonMappingExceptionMapper;
import com.github.danzx.zekke.ws.rest.errormapper.ResourceNotFoundExceptionMapper;
import com.github.danzx.zekke.ws.rest.model.Poi;
import com.github.danzx.zekke.ws.rest.model.TypedWaypoint;
import com.github.danzx.zekke.ws.rest.model.Walkway;
import com.github.danzx.zekke.ws.rest.patch.ObjectPatch;
import com.github.danzx.zekke.ws.rest.patch.jsonpatch.JsonObjectPatch;
import com.github.danzx.zekke.ws.rest.patch.jsonpatch.JsonPatchReader;
import com.github.danzx.zekke.ws.rest.security.BasicAuthorizationHeaderExtractor;
import com.github.danzx.zekke.ws.rest.security.BearerAuthorizationHeaderExtractor;
import com.github.danzx.zekke.ws.rest.security.jwt.filter.JwtAuthenticationFilter;

import net.rakugakibox.spring.boot.orika.OrikaAutoConfiguration;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

public class BaseJerseyTest extends JerseyTest {

    private AnnotationConfigApplicationContext context;

    private UserService mockUserService;
    private WaypointService mockWaypointService;
    private JwtVerifier jwtVerifier;
    private JwtFactory jwtFactory;

    private BearerAuthorizationHeaderExtractor bearerAuthHeaderExtractor;
    private BasicAuthorizationHeaderExtractor basicAuthHeaderExtractor;
    private Transformer<Waypoint, Poi> waypointToPoiTransformer;
    private Transformer<Waypoint, Walkway> waypointToWalkwayTransformer;
    private Transformer<Waypoint, TypedWaypoint> waypointToTypedWaypointTransformer;
    private JwtSettings jwtSettings;
    private ObjectMapper objectMapper;

    @Override
    protected Application configure() {
        Locale.setDefault(Locale.ROOT);
        initSpringContextAndFindBeans();
        initMocksAndSpies();
        ResourceConfig resourceConfig = new ResourceConfig();
        bindBeans(resourceConfig);
        customizeDefaultFeatures(resourceConfig);
        registerExceptionMappers(resourceConfig);
        registerBodyHandlers(resourceConfig);
        registerFilters(resourceConfig);
        registerEndpoints(resourceConfig);
        return resourceConfig;
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        context.stop();
    }

    protected void disableSecurity() {
        final String fakeToken = "fakeToken";
        doReturn(fakeToken).when(bearerAuthHeaderExtractor).getToken(any());
        doNothing().when(jwtVerifier).verify(eq(fakeToken), any());
    }

    protected void enableSecurity() {
        doCallRealMethod().when(bearerAuthHeaderExtractor).getToken(any());
        doCallRealMethod().when(jwtVerifier).verify(any(), any());
    }

    @SuppressWarnings("unchecked")
    private void initSpringContextAndFindBeans() {
        context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(ENDPOINT_TEST);
        context.register(InternalConfig.class);
        context.refresh();
        waypointToPoiTransformer = context.getBean("waypointToPoiTransformer", Transformer.class);
        waypointToWalkwayTransformer = context.getBean("waypointToWalkwayTransformer", Transformer.class);
        waypointToTypedWaypointTransformer = context.getBean("waypointToTypedWaypointTransformer", Transformer.class);
        jwtSettings = context.getBean(JwtSettings.class);
        objectMapper = context.getBean(ObjectMapper.class);
    }

    private void initMocksAndSpies() {
        mockUserService = mock(UserService.class);
        mockWaypointService = mock(WaypointService.class);
        bearerAuthHeaderExtractor = spy(new BearerAuthorizationHeaderExtractor());
        basicAuthHeaderExtractor = spy(new BasicAuthorizationHeaderExtractor());
        jwtVerifier = spy(new JjwtVerifier(jwtSettings));
        jwtFactory = spy(new JjwtFactory(jwtSettings));
    }

    private void customizeDefaultFeatures(ResourceConfig resourceConfig) {
        resourceConfig
                .property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true)
                .register(JacksonFeature.class);
    }

    private void registerExceptionMappers(ResourceConfig resourceConfig) {
        resourceConfig
                .register(AppExceptionMapper.class)
                .register(ConstraintViolationExceptionMapper.class)
                .register(GenericExceptionMapper.class)
                .register(InvalidPathParamExceptionMapper.class)
                .register(JsonMappingExceptionMapper.class)
                .register(ResourceNotFoundExceptionMapper.class);
    }

    private void registerBodyHandlers(ResourceConfig resourceConfig) {
        resourceConfig.register(JsonPatchReader.class);
    }

    private void registerFilters(ResourceConfig resourceConfig) {
        resourceConfig.register(JwtAuthenticationFilter.class);
    }

    private void bindBeans(ResourceConfig resourceConfig) {
        resourceConfig.registerInstances(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(bearerAuthHeaderExtractor).to(BearerAuthorizationHeaderExtractor.class);
                bind(jwtVerifier).to(JwtVerifier.class);
                bind(objectMapper).to(ObjectMapper.class);
            }
        });
    }

    private void registerEndpoints(ResourceConfig resourceConfig) {
        resourceConfig
                .register(new ErrorEndpoint())
                .register(new JwtAuthenticationEndpoint(jwtFactory, mockUserService, basicAuthHeaderExtractor))
                .register(new WaypointEndpoint(mockWaypointService, waypointToPoiTransformer, waypointToWalkwayTransformer, waypointToTypedWaypointTransformer));
    }

    @Import({
            OrikaAutoConfiguration.class,
            ModelTransformerConfig.class,
            WaypointToPoiMapping.class,
            WaypointToTypedWaypointMapping.class,
            WaypointToWalkwayMapping.class,
            ObjectMapperConfig.class,
            JwtSettings.class
    })
    @Configuration @ForEndpointTest
    @PropertySource("classpath:test.properties")
    static class InternalConfig { }

    protected UserService getMockUserService() {
        return mockUserService;
    }

    protected WaypointService getMockWaypointService() {
        return mockWaypointService;
    }

    protected JwtVerifier getJwtVerifier() {
        return jwtVerifier;
    }

    protected JwtFactory getJwtFactory() {
        return jwtFactory;
    }

    protected BearerAuthorizationHeaderExtractor getBearerAuthHeaderExtractor() {
        return bearerAuthHeaderExtractor;
    }

    protected BasicAuthorizationHeaderExtractor getBasicAuthHeaderExtractor() {
        return basicAuthHeaderExtractor;
    }

    protected Transformer<Waypoint, Poi> getWaypointToPoiTransformer() {
        return waypointToPoiTransformer;
    }

    protected Transformer<Waypoint, Walkway> getWaypointToWalkwayTransformer() {
        return waypointToWalkwayTransformer;
    }

    protected Transformer<Waypoint, TypedWaypoint> getWaypointToTypedWaypointTransformer() {
        return waypointToTypedWaypointTransformer;
    }

    protected JwtSettings getJwtSettings() {
        return jwtSettings;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
