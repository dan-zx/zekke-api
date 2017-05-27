/*
 * Copyright 2017 Daniel Pedraza-Arcega
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.danzx.zekke.ws.rest.common;

import java.util.HashMap;
import java.util.Map;

import com.github.danzx.zekke.base.Buildable;

/**
 * @author Daniel Pedraza-Arcega
 *
 */
public class ErrorMessage {

    public enum Type { PARAM_VALIDATION, RESOURCE_NOT_FOUND, SERVER_ERROR, OTHER }

    private final int statusCode;
    private final String errorDetail;
    private final Type errorType;
    private final Map<String, String> paramErrors;

    private ErrorMessage(Builder builder) {
        statusCode = builder.statusCode;
        errorDetail = builder.errorDetail;
        errorType = builder.errorType;
        paramErrors = builder.paramErrors;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public Type getErrorType() {
        return errorType;
    }

    public Map<String, String> getParamErrors() {
        return paramErrors;
    }

    public static class Builder implements Buildable<ErrorMessage> {

        private int statusCode = 500;
        private Type errorType = Type.OTHER;
        private String errorDetail;
        private Map<String, String> paramErrors;

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder detailMessage(String errorDetail) {
            this.errorDetail = errorDetail;
            return this;
        }

        public Builder type(Type errorType) {
            this.errorType = errorType;
            return this;
        }

        public Builder addParamError(String property, String error) {
            if (paramErrors == null) paramErrors = new HashMap<>();
            paramErrors.put(property, error);
            return this;
        }

        @Override
        public ErrorMessage build() {
            return new ErrorMessage(this);
        }
    }
}
