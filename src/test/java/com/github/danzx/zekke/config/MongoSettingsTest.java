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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Constructor;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.danzx.zekke.test.BaseValidationTest;
import com.github.danzx.zekke.test.paramprovider.BlankStringProvider;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class MongoSettingsTest extends BaseValidationTest {

    @Test
    @Parameters(source = BlankStringProvider.class)
    public void shouldFailValidationWhenDatabaseIsBlank(String blank) throws Exception {
        Constructor<MongoSettings> constructor = MongoSettings.class.getConstructor(String.class, Integer.class, String.class, String.class, String.class);
        Object[] parameterValues = {null, null, blank, null, null};
        Set<ConstraintViolation<MongoSettings>> violations = validator().forExecutables().validateConstructorParameters(constructor, parameterValues);
        assertThat(violations).isNotEmpty().hasSize(1);
    }

    @Test
    public void shouldConstructObject() {
        String anyString = "234redfsc";
        int anyPort = 3532;
        new MongoSettings(anyString, anyPort, anyString, anyString, anyString);
        new MongoSettings(null, null, anyString, null, null);
    }
}
