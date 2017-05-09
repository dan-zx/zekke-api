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
package com.github.danzx.zekke.ws.rest.paramconverter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.danzx.zekke.domain.BoundingBox;
import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.util.Strings;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class BoundingBoxConverterTest {

    private static final BoundingBoxConverterProvider.Converter CONVERTER = new BoundingBoxConverterProvider.Converter();

    @Test
    @Parameters(method = "convertedValues")
    public void shouldConvertFromString(String strValue, BoundingBox bbox) {
        assertThat(CONVERTER.fromString(strValue)).isEqualTo(bbox);
    }

    @Test
    @Parameters(method = "invalidStringBbox")
    public void shouldConvertFromStringThrowIllegalArgumentException(String vaule) {
        assertThatThrownBy(() -> CONVERTER.fromString(vaule)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Parameters(method = "convertedValues")
    public void shouldConvertToString(String strValue, BoundingBox bbox) {
        assertThat(CONVERTER.toString(bbox)).isEqualTo(strValue);
    }

    protected Object[][] convertedValues() {
        return new Object[][] {
            {"12.24,24.99;78.0,-35.6", new BoundingBox(Coordinates.ofLatLng(12.24, 24.99), Coordinates.ofLatLng(78, -35.6))},
            {null,  null},
        };
    }

    protected Object[][] invalidStringBbox() {
        return new Object[][] {
            {Strings.EMPTY},
            {Strings.BLANK_SPACE},
            {Strings.NEW_LINE},
            {Strings.TAB},
            {"12.24,24.99X78,-35.6"},
            {"12.24,24.9978,-35.6"},
            {"ds;78,-35.6"},
            {"12.24,24.99;ds"},
            {"ds;ds"},
            {"ds"}
        };
    }
}
