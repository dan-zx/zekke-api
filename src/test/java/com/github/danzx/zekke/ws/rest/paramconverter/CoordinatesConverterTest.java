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

import com.github.danzx.zekke.domain.Coordinates;
import com.github.danzx.zekke.util.Strings;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class CoordinatesConverterTest {

    private static final CoordinatesConverterProvider.Converter CONVERTER = new CoordinatesConverterProvider.Converter();

    @Test
    @Parameters(method = "convertedValues")
    public void shouldConvertFromString(String strValue, Coordinates latLng) {
        assertThat(CONVERTER.fromString(strValue)).isEqualTo(latLng);
    }

    @Test
    @Parameters(method = "invalidStringCoordinates")
    public void shouldConvertFromStringThrowIllegalArgumentException(String vaule) {
        assertThatThrownBy(() -> CONVERTER.fromString(vaule)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Parameters(method = "convertedValues")
    public void shouldConvertToString(String strValue, Coordinates latLng) {
        assertThat(CONVERTER.toString(latLng)).isEqualTo(strValue);
    }

    protected Object[][] convertedValues() {
        return new Object[][] {
            {"12.24,24.99",  Coordinates.ofLatLng(12.24, 24.99)},
            {null,  null},
        };
    }

    protected Object[][] invalidStringCoordinates() {
        return new Object[][] {
            {Strings.EMPTY},
            {Strings.BLANK_SPACE},
            {Strings.NEW_LINE},
            {Strings.TAB},
            {"12.24;24.99"},
            {"12.24,ds"},
            {"ds,24.99"},
            {"ds,ds"},
            {"ds"},
            {"12.2424.99"}
        };
    }
}
