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
package com.github.danzx.zekke.data.filter;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.github.danzx.zekke.base.Buildable;

/**
 * Filter options for any data type.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class FilterOptions {

    private final Builder builder;

    protected FilterOptions(Builder builder) {
        this.builder = requireNonNull(builder);
    }

    public Optional<Integer> getLimit() {
        return Optional.ofNullable(builder.limit);
    }

    @Override
    public String toString() {
        return "{ limit: " + getLimit() + " }";
    }

    public static class Builder implements Buildable<FilterOptions> {
        private Integer limit;

        public Builder limitResulsTo(Integer limit) {
            this.limit = limit;
            return this;
        }

        @Override
        public FilterOptions build() {
            return new FilterOptions(this);
        }
    }
}
