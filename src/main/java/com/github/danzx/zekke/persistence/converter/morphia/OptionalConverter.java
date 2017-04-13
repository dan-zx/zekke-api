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
package com.github.danzx.zekke.persistence.converter.morphia;

import java.util.Optional;

import org.mongodb.morphia.converters.Converters;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

/**
 * Coverter for the optional class.
 * 
 * @author Daniel Pedraza-Arcega
 */
public class OptionalConverter extends TypeConverter implements SimpleValueConverter {

    private final Converters defaultConverters;

    public OptionalConverter(Converters defaultConverters) {
        super(Optional.class);
        this.defaultConverters = defaultConverters;
    }

    @Override
    public Object encode(Object value, MappedField mappedField) {
        Optional<?> optional = (Optional<?>) value;
        return optional.map(defaultConverters::encode).orElse(null);
    }

    @Override
    public Object decode(Class<?> type, Object fromDbObject, MappedField mappedField) {
        return Optional.ofNullable(fromDbObject);
    }
}