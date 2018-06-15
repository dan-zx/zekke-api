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
package com.github.danzx.zekke.ws.rest.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.danzx.zekke.base.Buildable;

/**
 * Error response object.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class ErrorMessage {

    public enum Type { PARAM_VALIDATION, NOT_FOUND, SERVER_ERROR, AUTHORIZATION, OTHER }

    private int statusCode;
    private String errorDetail;
    private Type errorType;
    private Map<String, String> paramErrors;

    public ErrorMessage() {}

    private ErrorMessage(Builder builder) {
        statusCode = builder.statusCode;
        errorDetail = builder.errorDetail;
        errorType = builder.errorType;
        paramErrors = builder.paramErrors;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public Type getErrorType() {
        return errorType;
    }

    public void setErrorType(Type errorType) {
        this.errorType = errorType;
    }

    public Map<String, String> getParamErrors() {
        return paramErrors;
    }

    public void setParamErrors(Map<String, String> paramErrors) {
        this.paramErrors = paramErrors;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        return isErrorMessageEqualTo((ErrorMessage) obj);
    }

    /**
     * Use this method to complete your equals method.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is the same as the argument; {@code false} otherwise.
     */
    protected boolean isErrorMessageEqualTo(ErrorMessage other) {
        return Objects.equals(statusCode, other.statusCode) &&
                Objects.equals(errorDetail, other.errorDetail) &&
                Objects.equals(errorType, other.errorType) &&
                Objects.equals(paramErrors, other.paramErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, errorDetail, errorType, paramErrors);
    }

    @Override
    public String toString() {
        return "{ statusCode=" + statusCode + ", errorDetail=" + errorDetail + ", errorType=" + errorType + ", paramErrors=" + paramErrors + " }";
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
