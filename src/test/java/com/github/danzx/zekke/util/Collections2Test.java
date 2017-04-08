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
package com.github.danzx.zekke.util;

import static com.github.danzx.zekke.util.Collections2.isNullOrEmpty;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class Collections2Test {

    @Test
    @Parameters(method = "collectionsAndResult")
    public void shouldCheckForNullOrEmptyInCollections(Collection<?> collection, boolean expected) {
        assertThat(isNullOrEmpty(collection)).isEqualTo(expected);
    }

    @Test
    @Parameters(method = "mapsAndResult")
    public void shouldCheckForNullOrEmptyInMaps(Map<?, ?> map, boolean expected) {
        assertThat(isNullOrEmpty(map)).isEqualTo(expected);
    }

    protected Object[] collectionsAndResult() {
        return new Object[][] {
            {null, true},
            {new ArrayList<Object>(), true},
            {emptyList(), true},
            {singletonList(1), false}
        };
    }

    protected Object[] mapsAndResult() {
        return new Object[][] {
            {null, true},
            {new HashMap<Object, Object>(), true},
            {emptyMap(), true},
            {singletonMap(1, 1), false}
        };
    }
}
